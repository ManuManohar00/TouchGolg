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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import e.myhmanohar.mygoldapp.Model.AdminOrders;

public class AdminNewOrderActivity extends AppCompatActivity {
private RecyclerView orderlist;
private DatabaseReference orderRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderlist = findViewById(R.id.order_list);
        orderlist.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef,AdminOrders.class)
                .build();
        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                holder.useName.setText("Name : " +model.getName());
                holder.userPhnNumber.setText("Phone : " +model.getPhnoe());
                holder.UserDateTime.setText("Order at : " +model.getDate() +"" + model.getTime());
                holder.UserShippingAddress.setText("Name : " +model.getAddress() + "" + model.getCity());
                holder.showOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uID= getRef(position).getKey();
                        Intent intent = new  Intent(AdminNewOrderActivity.this,AdminUserProductActivity.class);
                        intent.putExtra("uid",uID);
                        startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderActivity.this);
                        builder.setTitle("Have you Shipped this order products ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0)
                                {
                                    String uID= getRef(position).getKey();
                                    RemoveOrder(uID);
                                }
                                else
                                {
                                        finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orders_layout,viewGroup,false);
                return new AdminOrdersViewHolder(view);
            }
        };
        orderlist.setAdapter(adapter);
        adapter.startListening();

    }



    public static class  AdminOrdersViewHolder extends RecyclerView.ViewHolder {

        public  TextView useName, userPhnNumber, UserDateTime, UserShippingAddress;
        public Button showOrderBtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            useName = itemView.findViewById(R.id.user_name);
            userPhnNumber = itemView.findViewById(R.id.phone_number);
            UserDateTime = itemView.findViewById(R.id.order_date_time);
            UserShippingAddress = itemView.findViewById(R.id.address_city);
            showOrderBtn = itemView.findViewById(R.id.sahow_all_products_btn);



        }
    }

    private void RemoveOrder(String uID)
    {
        orderRef.child(uID).removeValue();
    }

}
