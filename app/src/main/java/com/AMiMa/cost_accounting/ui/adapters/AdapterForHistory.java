package com.AMiMa.cost_accounting.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AMiMa.cost_accounting.R;

import java.util.List;

public class AdapterForHistory extends RecyclerView.Adapter<AdapterForHistory.ViewHolder> {

    private LayoutInflater inflater;
    private List<String> listProducts;
    private List<String> listPrices;

    public AdapterForHistory(List<String> products, List<String> prices, Context context){
        this.inflater = LayoutInflater.from(context);
        listProducts = products;
        listPrices = prices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_for_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.product.setText(listProducts.get(position).toString());
        holder.product.setId(position);
        holder.price.setText(listPrices.get(position));
        holder.price.setId(position);
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView product, price;

        public ViewHolder(View itemView) {
            super(itemView);
            product = itemView.findViewById(R.id.productInHistory);
            price = itemView.findViewById(R.id.priceProductInHistory);
        }
    }
}
