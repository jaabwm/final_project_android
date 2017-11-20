package com.jabwrb.nutridiary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.database.Food;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FoodRecyclerViewAdapter extends RecyclerView.Adapter<FoodRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private OnFoodClickListener listener;
    private List<Food> data;

    public interface OnFoodClickListener {
        void onFoodClicked(Food food);

        void onFoodInfoClicked(Food food);
    }

    public FoodRecyclerViewAdapter(Context context) {
        this.context = context;
        this.listener = (OnFoodClickListener) context;
        this.data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_food, parent, false);

        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Food food = data.get(position);

        DecimalFormat formatter = new DecimalFormat("####.##");
        // 100.77 g, 300 cal
        String subline = formatter.format(food.getServingSizeUnit()) + " " +
                food.getServingSizeMeasurement() + ", " +
                food.getCalories() + " cal";

        holder.food = food;
        holder.tvFoodName.setText(food.getName());
        holder.tvSubline.setText(subline);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Food> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Food food;
        TextView tvFoodName;
        TextView tvSubline;
        ImageButton imgBtnInfo;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvSubline = itemView.findViewById(R.id.tvSubline);
            imgBtnInfo = itemView.findViewById(R.id.imgBtnInfo);
            imgBtnInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgBtnInfo:
                    listener.onFoodInfoClicked(food);
                    break;

                default:
                    listener.onFoodClicked(food);
            }
        }
    }
}
