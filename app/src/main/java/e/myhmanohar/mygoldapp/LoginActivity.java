package e.myhmanohar.mygoldapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.service.autofill.UserData;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import e.myhmanohar.mygoldapp.Model.User;
import e.myhmanohar.mygoldapp.Prevalent.Prevalent;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
private Button login_bt;
private EditText input_phn,input_password;
private ProgressDialog loadingBar;
private CheckBox checkBoxRememberMe;
private  String parentDbName="User";
private TextView AdminLink,NotAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_bt=(Button)findViewById(R.id.login_btn);
        input_phn=(EditText)findViewById(R.id.login_phn_no);
        input_password=(EditText)findViewById(R.id.login_password);
        loadingBar=new ProgressDialog(this);
        AdminLink=(TextView)findViewById(R.id.admin_link);
        NotAdmin=(TextView)findViewById(R.id.not_admin_link);

        checkBoxRememberMe=(CheckBox)findViewById(R.id.remember_me);
        Paper.init(this);

        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_bt.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdmin.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });
        NotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_bt.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdmin.setVisibility(View.INVISIBLE);
                parentDbName="User";
            }
        });
    }

    private void loginUser() {
        String phone=input_phn.getText().toString();
        String password= input_password.getText().toString();
         if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Give Your Phone_Number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Give Your Password", Toast.LENGTH_SHORT).show();
        }
        else
         {
             loadingBar.setTitle("Login Account");
             loadingBar.setMessage("Please wait, While we are checking the credentials.");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();
             AllowAccessToAccount(phone,password);
         }
    }

    private void AllowAccessToAccount(final String phone, final String password) {
        if(checkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }
        
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    User userdata=dataSnapshot.child(parentDbName).child(phone).getValue(User.class);
                    if(userdata.getPhone().equals(phone))
                    {
                        if(userdata.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Admins"))
                            {
                            Toast.makeText(LoginActivity.this, "Welcome Admin ! you are Logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent=new Intent(LoginActivity.this,AdminCategoryActivity.class);
                            startActivity(intent);
                             }
                           else   if(parentDbName.equals("User"))
                              {
                            Toast.makeText(LoginActivity.this, "Login Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                            Prevalent.currentonLineUser = userdata;
                            startActivity(intent);
                              }


                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();

                        }
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this "+phone+"number do not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "You need to create a new Account", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
