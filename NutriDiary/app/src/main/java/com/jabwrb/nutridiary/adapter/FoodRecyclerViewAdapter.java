package com.jabwrb.nutridiary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.database.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodRecyclerViewAdapter extends RecyclerView.Adapter<FoodRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private FoodRecyclerViewAdapterListener listener;
    private List<Food> data;

    public interface FoodRecyclerViewAdapterListener {
        void onItemClickedListener(String foodName);
    }

    public FoodRecyclerViewAdapter(Context context) {
        this.context = context;
        listener = (FoodRecyclerViewAdapterListener) context;
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.food_item, null, false);

        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // set each food name in data to TextView
        holder.textViewFoodName.setText(data.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickedListener(data.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewFoodName;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewFoodName = (TextView) itemView.findViewById(R.id.textViewFoodName);
        }
    }

    public void setData(List<Food> data) {
        this.data = data;
    }
}
