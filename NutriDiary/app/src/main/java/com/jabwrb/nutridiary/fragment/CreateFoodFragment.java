package com.jabwrb.nutridiary.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.database.DatabaseSingleton;
import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.NutriDiaryDb;
import com.jabwrb.nutridiary.task.CountDuplicateFoodTask;
import com.jabwrb.nutridiary.task.CreateFoodTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFoodFragment extends Fragment {

    private NutriDiaryDb db;
    private CreateFoodFragmentListener listener;
    private EditText etName;
    private EditText etBrand;
    private EditText etServingSizeUnit;
    private EditText etServingSizeMeasurement;
    private EditText etCalories;
    private EditText etFat;
    private EditText etCarbohydrates;
    private EditText etProtein;
    private EditText etSaturatedFat;
    private EditText etChoresterol;
    private EditText etSodium;
    private EditText etDietaryFiber;
    private EditText etSugars;

    public interface CreateFoodFragmentListener {
        void onBtnAddPressed();
    }

    public CreateFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseSingleton.getDatabaseInstance().getDb();

        listener = (CreateFoodFragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_food, container, false);

        setup(view);

        return view;
    }

    private void setup(View view) {
        // Toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        etName = view.findViewById(R.id.etName);
        etBrand = view.findViewById(R.id.etBrand);
        etServingSizeUnit = view.findViewById(R.id.etServingSizeUnit);
        etServingSizeMeasurement = view.findViewById(R.id.etServingSizeMeasurement);
        etCalories = view.findViewById(R.id.etCalories);
        etFat = view.findViewById(R.id.etFat);
        etCarbohydrates = view.findViewById(R.id.etCarbohydrates);
        etProtein = view.findViewById(R.id.etProtein);
        etSaturatedFat = view.findViewById(R.id.etSaturatedFat);
        etChoresterol = view.findViewById(R.id.etCholesterol);
        etSodium = view.findViewById(R.id.etSodium);
        etDietaryFiber = view.findViewById(R.id.etDietaryFiber);
        etSugars = view.findViewById(R.id.etSugars);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_food, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                insertFoodToDb();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void insertFoodToDb() {
        if (!validate()) {
            return;
        }

        Food food = new Food();
        food.setName(etName.getText().toString());
        food.setBrand(etBrand.getText().toString());
        food.setServingSizeMeasurement(etServingSizeMeasurement.getText().toString());
        try {
            food.setCalories(Integer.parseInt(emptyToZero(etCalories.getText().toString())));
            food.setServingSizeUnit(Integer.parseInt(emptyToZero(etServingSizeUnit.getText().toString())));
            food.setCalories(Integer.parseInt(emptyToZero(etCalories.getText().toString())));
            food.setFat(Float.parseFloat(emptyToZero(etFat.getText().toString())));
            food.setCarbohydrates(Float.parseFloat(emptyToZero(etCarbohydrates.getText().toString())));
            food.setProtein(Float.parseFloat(emptyToZero(etProtein.getText().toString())));
            food.setSaturatedFat(Float.parseFloat(emptyToZero(etSaturatedFat.getText().toString())));
            food.setCholesterol(Float.parseFloat(emptyToZero(etChoresterol.getText().toString())));
            food.setSodium(Float.parseFloat(emptyToZero(etSodium.getText().toString())));
            food.setDietaryFiber(Float.parseFloat(emptyToZero(etDietaryFiber.getText().toString())));
            food.setSugars(Float.parseFloat(emptyToZero(etSugars.getText().toString())));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Invalid number format entered.", Toast.LENGTH_SHORT).show();
            return;
        }

        new CountDuplicateFoodTask(db, new CountDuplicateFoodTask.OnDuplicateFoodCountListener() {
            @Override
            public void onDuplicateFoodCounted(int count, Food food) {
                if (count == 0) {
                    new CreateFoodTask(db, new CreateFoodTask.OnFoodCreateListener() {
                        @Override
                        public void onFoodCreated(Long id) {
                            listener.onBtnAddPressed();
                        }
                    }).execute(food);
                } else {
                    Toast.makeText(getActivity(), "Fail to create.\nAlready has this food.", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(food);
    }

    private boolean validate() {
        if (etName.getText().toString().isEmpty() ||
                etCalories.getText().toString().isEmpty() ||
                etServingSizeUnit.getText().toString().isEmpty() ||
                etServingSizeMeasurement.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter the required information.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private String emptyToZero(String num) {
        return num.isEmpty() ? "0" : num;
    }
}
