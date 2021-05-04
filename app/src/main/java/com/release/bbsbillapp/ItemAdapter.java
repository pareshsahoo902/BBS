package com.release.bbsbillapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemVewHolder>{


    private  List<ItemsModel > itemsModelList;
    private  Context mCOntext;

    public ItemAdapter(List<ItemsModel> itemsModelList, Context mCOntext) {
        this.itemsModelList = itemsModelList;
        this.mCOntext = mCOntext;
    }

    public void updateList(List<ItemsModel> modelList){
        this.itemsModelList = modelList;
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public ItemVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemVewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemVewHolder holder, int position) {

        ItemsModel model = itemsModelList.get(position);
        holder.slno.setText(String.valueOf(position+1)+".");
        holder.price.setText("₹"+String.valueOf(model.price));
        holder.itemName.setText(String.valueOf(model.itemName));
        holder.weight.setText(String.valueOf(model.weight)+"kg");
        holder.quantity.setText(String.valueOf(model.quantity));

        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsModelList.remove(model);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsModelList.size();
    }

    public class ItemVewHolder extends RecyclerView.ViewHolder{

        public TextView itemName , weight , quantity , price ,slno;
        public ImageView deleteImage;
        public ItemVewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            weight = itemView.findViewById(R.id.weigthtext);
            quantity = itemView.findViewById(R.id.quantitiyText);
            price = itemView.findViewById(R.id.price);
            slno = itemView.findViewById(R.id.slNo);
            deleteImage = itemView.findViewById(R.id.deleteRow);
        }
    }
}
