package com.example.wms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private DatabaseHelper dbHelper;

    public ProductAdapter(List<Product> productList, DatabaseHelper dbHelper) {
        this.productList = productList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameEditText.setText(product.getName());
        holder.quantityEditText.setText(String.valueOf(product.getQuantity()));

        holder.updateButton.setOnClickListener(v -> {
            product.setName(holder.nameEditText.getText().toString());
            product.setQuantity(Integer.parseInt(holder.quantityEditText.getText().toString()));
            dbHelper.updateProduct(product);
            notifyDataSetChanged();
        });

        holder.deleteButton.setOnClickListener(v -> {
            dbHelper.deleteProduct(product.getId());
            productList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, productList.size());
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText nameEditText;
        EditText quantityEditText;
        Button updateButton;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameEditText = itemView.findViewById(R.id.editTextName);
            quantityEditText = itemView.findViewById(R.id.editTextQuantity);
            updateButton = itemView.findViewById(R.id.button_update);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
}
