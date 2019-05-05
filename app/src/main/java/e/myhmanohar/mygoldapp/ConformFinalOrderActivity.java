package e.myhmanohar.mygoldapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import e.myhmanohar.mygoldapp.Prevalent.Prevalent;

public class ConformFinalOrderActivity extends AppCompatActivity {
private EditText nameEditText,phnEditText,addressEditText,cityEditText;
private Button conFirmOrderBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conform_final_order);

        conFirmOrderBtn = (Button)findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText)findViewById(R.id.shipment_name);
        phnEditText = (EditText)findViewById(R.id.shipment_phn);
        addressEditText = (EditText)findViewById(R.id.shipment_address);
        cityEditText = (EditText)findViewById(R.id.shipment_city);

        conFirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

    }

    private void check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Provide your Full Name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(phnEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Provide your Phone Number", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Provide your Address", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Provide City Name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }

    }

    private void ConfirmOrder() {
       final String saveCurrentDate,saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference oredrRef = FirebaseDatabase.getInstance()
                .getReference().child("Orders")
                .child(Prevalent.currentonLineUser.getPhone());
        HashMap<String , Object> orderMap= new HashMap<>();
        orderMap.put("city",cityEditText.getText().toString());
        orderMap.put("name",nameEditText.getText().toString());
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("address",addressEditText.getText().toString());
        orderMap.put("phone",phnEditText.getText().toString());
        orderMap.put("state","not shipped");


        oredrRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentonLineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ConformFinalOrderActivity.this, "Your final order has been place successfully..", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(ConformFinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}
