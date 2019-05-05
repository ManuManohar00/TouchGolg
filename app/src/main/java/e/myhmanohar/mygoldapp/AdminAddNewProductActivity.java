package e.myhmanohar.mygoldapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
private String categoryName,weight,quality,pname,saveCurrentDate, saveCurrentTime;
private Button addnewProductBtn;
private ImageView inputProductImage;
private static final int GalleryPick=1;
private Uri ImageUri;
private String productRandomKey,downloadImageUrl;
private StorageReference ProductImageRef;
private EditText inputProductName,inputProductWeight,inputProductQuality;
private DatabaseReference ProductRef;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        loadingBar=new ProgressDialog(this);
        categoryName=getIntent().getExtras().get("category").toString();
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef=FirebaseDatabase.getInstance().getReference().child("Products");
        addnewProductBtn=(Button)findViewById(R.id.add_product);
        inputProductImage=(ImageView)findViewById(R.id.select_product_image);
        inputProductName=(EditText)findViewById(R.id.product_name);
        inputProductWeight=(EditText)findViewById(R.id.product_weight);
        inputProductQuality=(EditText)findViewById(R.id.product_quality);
        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        addnewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ValidateProductData();

            }
        });
    }

    private void ValidateProductData() {
        weight=inputProductWeight.getText().toString();
        quality=inputProductQuality.getText().toString();
        pname=inputProductName.getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "Product Image is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(weight))
        {
            Toast.makeText(this, "Product Weight is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(quality))
        {
            Toast.makeText(this, "Product Quality is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pname))
        {
            Toast.makeText(this, "Product Name is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else
        {
         StoreProductInformation();
        }

    }


    private void StoreProductInformation() {

        loadingBar.setTitle("Add new Product");
        loadingBar.setMessage("Please wait, While we are adding the new Product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate= currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate + saveCurrentTime;
      final StorageReference filePath= ProductImageRef.child(ImageUri.getLastPathSegment()+productRandomKey+".jpg");
      final UploadTask uploadTask=filePath.putFile(ImageUri);
      uploadTask.addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
                String message= e.toString();
              Toast.makeText(AdminAddNewProductActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
              loadingBar.dismiss();
          }
      }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
          @Override
          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              Toast.makeText(AdminAddNewProductActivity.this, "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
              Task<Uri>uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                  @Override
                  public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                      if(!task.isSuccessful())
                      {
                          throw task.getException();

                      }
                      downloadImageUrl=filePath.getDownloadUrl().toString();
                      return filePath.getDownloadUrl();
                  }
              }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                  @Override
                  public void onComplete(@NonNull Task<Uri> task) {
                      if(task.isSuccessful())
                      {
                          downloadImageUrl = task.getResult().toString();
                          Toast.makeText(AdminAddNewProductActivity.this, "Get the Product Image Url Successfully...", Toast.LENGTH_SHORT).show();

                          SaveProductInfoToDataBase();
                      }
                  }
              });
          }
      });

    }

    private void SaveProductInfoToDataBase() {
        HashMap<String, Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("weight",weight);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("quality",quality);
        productMap.put("pname",pname);

        ProductRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product is Added Successfully", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void openGallery() {
        Intent galleryIntent= new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null )
        {
            ImageUri= data.getData();
            inputProductImage.setImageURI(ImageUri);
        }
    }
}
