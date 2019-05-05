package e.myhmanohar.mygoldapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductActivity extends AppCompatActivity {
private Button applyChangesBtn,deleteBtn;
private EditText name,weight,quality;
private ImageView imageView;
    private String productId="";
    private DatabaseReference productRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);

        applyChangesBtn=findViewById(R.id.apply_chang_btn);
        name = findViewById(R.id.product_name_maintain);
        weight = findViewById(R.id.product_weight_maintain);
        quality = findViewById(R.id.product_quality_maintain);
        imageView = findViewById(R.id.product_image_maintain);

        deleteBtn=findViewById(R.id.delete_product_btn);

        productId = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);


        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apllyChanges();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteThisProduct();
            }
        });


    }

    private void deleteThisProduct() {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                Intent intent=new Intent(AdminMaintainProductActivity.this,AdminCategoryActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMaintainProductActivity.this, "The Product is deleted successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void apllyChanges()
    {
        String pName = name.getText().toString();
        String pQuality = quality.getText().toString();
        String pWeight = weight.getText().toString();
        if(pName.equals(""))
        {
            Toast.makeText(this,"Write down Product Name :",Toast.LENGTH_SHORT).show();
        }
        else if(pQuality.equals(""))
        {
            Toast.makeText(this,"Write down Product Quality :",Toast.LENGTH_SHORT).show();
        }
       else if(pWeight.equals(""))
        {
            Toast.makeText(this,"Write down Product Weight :",Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap=new HashMap<>();
            productMap.put("pid",productId);
            productMap.put("weight",pWeight);
            productMap.put("quality",pQuality);
            productMap.put("pname",pName);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful())
                 {
                     Toast.makeText(AdminMaintainProductActivity.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();

                     Intent intent=new Intent(AdminMaintainProductActivity.this,AdminCategoryActivity.class);
                     startActivity(intent);
                     finish();

                 }
                }
            });
        }
    }

    private void displaySpecificProductInfo()
    {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String pName = dataSnapshot.child("pname").getValue().toString();
                    String pQuality = dataSnapshot.child("quality").getValue().toString();
                    String pWeight = dataSnapshot.child("weight").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();

                    name.setText(pName);
                    quality.setText(pQuality);
                    weight.setText(pWeight);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
