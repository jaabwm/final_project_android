package com.jabwrb.nutridiary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.FoodEntryWithFood;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FoodEntryRecyclerViewAdapter extends RecyclerView.Adapter<FoodEntryRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private OnFoodEntryClickListener listener;
    private List<FoodEntryWithFood> data;

    public interface OnFoodEntryClickListener {
        void onFoodEntryLongClicked(FoodEntry foodEntry);
    }

    public FoodEntryRecyclerViewAdapter(Context context) {
        this.context = context;
        this.listener = (OnFoodEntryClickListener) context;
        this.data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_food_entry, parent, false);

        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodEntry foodEntry = data.get(position).getFoodEntry();
        Food food = data.get(position).getFood();

        DecimalFormat formatter = new DecimalFormat("####.##");

        float amount = foodEntry.getAmount();
        String name = food.getName();
        String totalCalories = formatter.format(amount * food.getCalories());
        String totalServingSize = formatter.format(amount * food.getServingSizeUnit()) + " " + food.getServingSizeMeasurement();

        holder.foodEntry = foodEntry;
        holder.tvFoodName.setText(name);
        holder.tvServingSize.setText(totalServingSize);
        holder.tvCalories.setText(totalCalories);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<FoodEntryWithFood> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        FoodEntry foodEntry;
        TextView tvFoodName;
        TextView tvServingSize;
        TextView tvCalories;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnLongClickListener(this);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvServingSize = itemView.findViewById(R.id.tvServingSize);
            tvCalories = itemView.findViewById(R.id.tvCalories);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onFoodEntryLongClicked(foodEntry);

            return true;
        }
    }
}
