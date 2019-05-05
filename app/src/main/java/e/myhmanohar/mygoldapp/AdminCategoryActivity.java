package e.myhmanohar.mygoldapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {
private ImageView neckless,chain,rings,engegmentrings,nosering,bangle,braclets,diamound,earring;
private Button logoutBtn,checkOrderbtn,maintainProductsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        neckless=(ImageView)findViewById(R.id.neckless);
        chain=(ImageView)findViewById(R.id.chain);
        rings=(ImageView)findViewById(R.id.ring);
        engegmentrings=(ImageView)findViewById(R.id.engegmentring);
        nosering=(ImageView)findViewById(R.id.nose_ring);
        bangle=(ImageView)findViewById(R.id.bangle);
        braclets=(ImageView)findViewById(R.id.braclat);
        diamound=(ImageView)findViewById(R.id.diamound);
        earring=(ImageView)findViewById(R.id.ear_ring);

        logoutBtn=(Button)findViewById(R.id.admin_logout_btn);
        checkOrderbtn=(Button)findViewById(R.id.check_orders_btn);
        maintainProductsBtn=(Button)findViewById(R.id.maintain_products);


        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        checkOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });


        neckless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Neckless");
                startActivity(intent);
            }
        });

        chain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Chains");
                startActivity(intent);
            }
        });

        rings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Rings");
                startActivity(intent);
            }
        });
        engegmentrings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Engagement Rings");
                startActivity(intent);
            }
        });

        nosering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Nose Rings");
                startActivity(intent);
            }
        });

        braclets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Bracelets");
                startActivity(intent);
            }
        });

        diamound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Diamonds Stones");
                startActivity(intent);
            }
        });


        earring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Ear_Rings");
                startActivity(intent);
            }
        });


        bangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Bangles");
                startActivity(intent);
            }
        });
    }
}
