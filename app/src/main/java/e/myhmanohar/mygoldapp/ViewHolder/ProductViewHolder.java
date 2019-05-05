package e.myhmanohar.mygoldapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import e.myhmanohar.mygoldapp.Interface.ItemClickListner;
import e.myhmanohar.mygoldapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
public TextView textproductName, textProductQuality,textProductWeight;
public ImageView imageView;
public ItemClickListner listner;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView=(ImageView)itemView.findViewById(R.id.product_image);
        textproductName=(TextView) itemView.findViewById(R.id.product_name);
        textProductQuality=(TextView) itemView.findViewById(R.id.product_quality);
        textProductWeight=(TextView) itemView.findViewById(R.id.product_weight);
    }


    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(),false);
    }
}


