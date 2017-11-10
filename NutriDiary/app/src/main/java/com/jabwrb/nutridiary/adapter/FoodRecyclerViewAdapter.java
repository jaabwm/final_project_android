package com.jabwrb.nutridiary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jabwrb.nutridiary.R;

public class FoodRecyclerViewAdapter extends RecyclerView.Adapter<FoodRecyclerViewAdapter.ViewHolder> {

    /*- Listener -----------------------------------------------------------------------------*/
    public interface FoodRecyclerViewAdapterListener {
        void onItemClickedListener(String foodName);
    }

    private FoodRecyclerViewAdapterListener listener;

    /*- Attributes ---------------------------------------------------------------------------*/
    // Demo data of user's saved food
    String[] data = {
            "whole egg",
            "raw boneless, skinless chicken breast",
            "Abiyuch, raw",
            "Acerola, (west indian cherry), raw",
            "Alcoholic beverage, beer, light",
            "Alcoholic beverage, beer, light, BUD LIGHT",
            "Alcoholic beverage, beer, light, BUDWEISER SELECT",
            "Alcoholic beverage, beer, light, higher alcohol",
            "Alcoholic beverage, beer, light, low carb",
            "Alcoholic beverage, beer, regular, all",
            "Alcoholic beverage, beer, regular, BUDWEISER",
            "Alcoholic beverage, creme de menthe, 72 proof",
            "Alcoholic beverage, daiquiri, canned",
            "Alcoholic beverage, daiquiri, prepared-from-recipe",
            "Alcoholic beverage, distilled, all (gin, rum, vodka, whiskey) 100 proof"
    };

    private Context context;

    /*- FoodRecyclerViewAdapter --------------------------------------------------------------*/
    public FoodRecyclerViewAdapter(Context context) {
        this.context = context;
        listener = (FoodRecyclerViewAdapterListener) context;

        // TODO: get user's saved food from database and set to "data" attribute.
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
        holder.textViewFoodName.setText(data[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickedListener(data[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    /*- ViewHolder class --------------------------------------------------------------------*/
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewFoodName;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewFoodName = (TextView) itemView.findViewById(R.id.textViewFoodName);
        }
    }
}
