package com.jabwrb.nutridiary.fragment;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.NutriDiaryDb;
import com.jabwrb.nutridiary.task.AddToDiaryTask;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFoodFragment extends Fragment implements View.OnClickListener {

    public static final String KEY_FOOD = "food";
    private NutriDiaryDb nutriDiaryDb;
    private AddFoodFragmentListener listener;
    private Food food;
    private EditText etAmount;
    private Spinner spinner;
    private TextView tvFoodName;
    private TextView tvServingSize;
    private TextView tvCalories;
    private TextView tvTotalFat;
    private TextView tvSaturatedFat;
    private TextView tvChoresterol;
    private TextView tvSodium;
    private TextView tvTotalCarbohydrates;
    private TextView tvDietaryFiber;
    private TextView tvSugars;
    private TextView tvProtein;
    private Button btnAddToDiary;
    private TextView tvBrand;

    public interface AddFoodFragmentListener {
        void onAddToDiaryPressed(String foodName);
    }

    public AddFoodFragment() {
        // Required empty public constructor
    }

    public static AddFoodFragment newInstance(Food food) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_FOOD, food);
        AddFoodFragment fragment = new AddFoodFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        food = getArguments().getParcelable(KEY_FOOD);

        nutriDiaryDb = Room.databaseBuilder(getActivity(),
                NutriDiaryDb.class, "NutriDiary.db")
                .fallbackToDestructiveMigration()
                .build();

        listener = (AddFoodFragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_food, container, false);

        setup(view);

        return view;
    }

    private void setup(View view) {
        etAmount = view.findViewById(R.id.etAmount);
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    setNutrientDetails();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.meal));
        spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);

        tvBrand = view.findViewById(R.id.tvBrand);
        tvFoodName = view.findViewById(R.id.tvName);
        tvServingSize = view.findViewById(R.id.tvServingSize);
        tvCalories = view.findViewById(R.id.tvCalories);
        tvTotalFat = view.findViewById(R.id.tvTotalFat);
        tvSaturatedFat = view.findViewById(R.id.tvSaturatedFat);
        tvChoresterol = view.findViewById(R.id.tvCholesterol);
        tvSodium = view.findViewById(R.id.tvSodium);
        tvTotalCarbohydrates = view.findViewById(R.id.tvTotalCarbohydrates);
        tvDietaryFiber = view.findViewById(R.id.tvDietaryFiber);
        tvSugars = view.findViewById(R.id.tvSugars);
        tvProtein = view.findViewById(R.id.tvProtein);

        tvBrand.setText(food.getBrand());
        tvFoodName.setText(food.getName());
        tvServingSize.setText(food.getServingSizeUnit() + " " + food.getServingSizeMeasurement());
        setNutrientDetails();

        btnAddToDiary =  view.findViewById(R.id.btnAddToDiary);
        btnAddToDiary.setOnClickListener(this);
    }

    private void setNutrientDetails() {
        float amount = Float.parseFloat(etAmount.getText().toString());
        DecimalFormat formatter = new DecimalFormat("####.##");

        tvCalories.setText(formatter.format(Math.round(food.getCalories() * amount)));
        tvTotalFat.setText(formatter.format(food.getFat() * amount) + " g");
        tvSaturatedFat.setText(formatter.format(food.getSaturatedFat() * amount)  + " mg");
        tvChoresterol.setText(formatter.format(food.getCholesterol() * amount)  + " mg");
        tvSodium.setText(formatter.format(food.getSodium() * amount) + " g");
        tvTotalCarbohydrates.setText(formatter.format(food.getCarbohydrates() * amount) + " g");
        tvDietaryFiber.setText(formatter.format(food.getDietaryFiber() * amount) + " g");
        tvSugars.setText(formatter.format(food.getSugars() * amount) + " g");
        tvProtein.setText(formatter.format(food.getProtein() * amount) + " g");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddToDiary:
                onAddToDiary();
                break;
        }
    }

    private void onAddToDiary() {
        FoodEntry foodEntry = new FoodEntry();
        foodEntry.setFoodId(food.getId());
        try {
            foodEntry.setAmount(Float.parseFloat(etAmount.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Please enter amount.", Toast.LENGTH_SHORT).show();
        }
        foodEntry.setMeal(spinner.getSelectedItem().toString());
        Date date = new Date();
        foodEntry.setDate(date);

        new AddToDiaryTask(nutriDiaryDb, new AddToDiaryTask.OnFoodAddListener() {
            @Override
            public void onFoodAdded() {
                listener.onAddToDiaryPressed(food.getName());
            }
        }).execute(foodEntry);
    }
}
