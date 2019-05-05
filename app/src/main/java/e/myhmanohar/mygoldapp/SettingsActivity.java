package e.myhmanohar.mygoldapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import e.myhmanohar.mygoldapp.Prevalent.Prevalent;

public class SettingsActivity extends AppCompatActivity {
private CircleImageView profileImageView;
private EditText fullName,userPhone,addressText;
private TextView profileChange, closeText, saveText;
private Uri imageUri;
private String myUrl="";
private StorageReference storageProfilePricutureRef;
private String checkr="";
private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        profileImageView=(CircleImageView)findViewById(R.id.settings_profile_image);
        fullName=(EditText)findViewById(R.id.settings_fullname);
        userPhone=(EditText)findViewById(R.id.settings_phn_no);
        addressText=(EditText)findViewById(R.id.settings_address);
        profileChange=(TextView)findViewById(R.id.profile_image_change_btn);
        closeText=(TextView)findViewById(R.id.close_settings);
        saveText=(TextView)findViewById(R.id.update_acc_settings);
        userInfoDisplay(profileImageView,fullName, userPhone,addressText);
        storageProfilePricutureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkr.equals("Clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });
        profileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkr= "Clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
        HashMap<String, Object> userMap =new HashMap<>();
        userMap.put("name",fullName.getText().toString());
        userMap.put("address",addressText.getText().toString());
        userMap.put("phoneOrder",userPhone.getText().toString());
        ref.child(Prevalent.currentonLineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Info Update Successfully..", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error: Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved()
{
        if(TextUtils.isEmpty(fullName.getText().toString()))
        {
            Toast.makeText(this, "Name is Mandatory", Toast.LENGTH_SHORT).show();
        }
  else   if(TextUtils.isEmpty(addressText.getText().toString()))
    {
        Toast.makeText(this, "Address is Mandatory", Toast.LENGTH_SHORT).show();
    }
        else   if(TextUtils.isEmpty(userPhone.getText().toString()))
        {
            Toast.makeText(this, "Phone Number is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(checkr.equals("Clicked"))
        {
            uploadImage();
        }


}

    private void uploadImage() {
        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your accountinformation.. ");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if(imageUri!=null)
        {
            final  StorageReference filrRef = storageProfilePricutureRef
                    .child(Prevalent.currentonLineUser.getPhone()+".jpg");
            uploadTask = filrRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return filrRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {
                                Uri download = task.getResult();
                                myUrl = download.toString();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
                                HashMap<String, Object> userMap =new HashMap<>();
                                userMap.put("name",fullName.getText().toString());
                                userMap.put("address",addressText.getText().toString());
                                userMap.put("phoneOrder",userPhone.getText().toString());
                                userMap.put("image",myUrl);
                                ref.child(Prevalent.currentonLineUser.getPhone()).updateChildren(userMap);
                                progressDialog.dismiss();
                                startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                                Toast.makeText(SettingsActivity.this, "Profile Info Update Successfully..", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Error: ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        else
        {
            Toast.makeText(this, "Image is not selected:", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullName, final EditText userPhone, EditText address) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User").child(Prevalent.currentonLineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image= dataSnapshot.child("image").getValue().toString();
                        String name= dataSnapshot.child("name").getValue().toString();
                        String phone= dataSnapshot.child("phone").getValue().toString();
                        String address= dataSnapshot.child("address").getValue().toString();
                        Picasso.get().load(image).into(profileImageView);
                        fullName.setText(name);
                        userPhone.setText(phone);
                        addressText.setText(address);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
