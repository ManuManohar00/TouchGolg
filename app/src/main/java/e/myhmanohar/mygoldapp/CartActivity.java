package e.myhmanohar.mygoldapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import e.myhmanohar.mygoldapp.Model.Cart;
import e.myhmanohar.mygoldapp.Prevalent.Prevalent;
import e.myhmanohar.mygoldapp.ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private RecyclerView.LayoutManager layoutManager;
private Button nxtBtn;
private TextView txtTotal,txtmsg1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nxtBtn = (Button) findViewById(R.id.nxt_process_btn);
        txtTotal=(TextView)findViewById(R.id.total);
        txtmsg1=(TextView)findViewById(R.id.msg_1);
        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CartActivity.this,ConformFinalOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options= new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                .child(Prevalent.currentonLineUser.getPhone()).child("Products"),Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart,CartViewHolder>adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductQuantity.setText("Quantity :"+model.getQuantity());
                holder.txtProductWeight.setText("Weight :"+model.getWeight());
                holder.txtProductName.setText(model.getPname());



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                 "Edit",
                                 "Remove"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                if(position == 0)
                                {
                                    Intent intent=new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if(position==1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentonLineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Item removed Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent=new Intent(CartActivity.this,HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout,viewGroup,false);
                CartViewHolder holder=new CartViewHolder(view);
                return  holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState()
    {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonLineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String shippingState= dataSnapshot.child("state").getValue().toString();
                    String userName= dataSnapshot.child("name").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                            txtTotal.setText("Dear "+ userName + "\n order is shipped successfully..");
                            recyclerView.setVisibility(View.GONE);
                            txtmsg1.setVisibility(View.VISIBLE);
                            txtmsg1.setText("Congratulations, Your final order has been placed successfully..");
                            nxtBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can purchase more products, once you received your first order", Toast.LENGTH_SHORT).show();

                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        txtTotal.setText("Shipping State = not shipped");
                        recyclerView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        nxtBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can purchase more products, once you received your first order. Soon it will be verified..", Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
