package e.myhmanohar.mygoldapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import e.myhmanohar.mygoldapp.Interface.ItemClickListner;
import e.myhmanohar.mygoldapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
public TextView txtProductName,txtProductQuantity,txtProductWeight;
private ItemClickListner itemClickListner;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtProductWeight = itemView.findViewById(R.id.cart_product_weight);

    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
