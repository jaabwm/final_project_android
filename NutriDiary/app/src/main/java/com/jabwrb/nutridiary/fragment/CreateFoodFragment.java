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
import android.widget.EditText;
import android.widget.Toast;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.database.DatabaseSingleton;
import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.NutriDiaryDb;
import com.jabwrb.nutridiary.task.CountDuplicateFoodTask;
import com.jabwrb.nutridiary.task.CreateFoodTask;
import com.jabwrb.nutridiary.task.UpdateFoodTask;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFoodFragment extends Fragment {

    public static final String KEY_FOOD = "food";
    public static final String KEY_ACTION = "action";
    public static final int ACTION_INSERT = 1;
    public static final int ACTION_UPDATE = 2;
    private NutriDiaryDb db;
    private CreateFoodFragmentListener listener;
    private Food food;
    private int action;
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
        void onMenuConfirmPressed();

        void onUpdated();
    }

    public CreateFoodFragment() {
        // Required empty public constructor
    }

    public CreateFoodFragment newInstance(Food food, int action) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_FOOD, food);
        args.putInt(KEY_ACTION, action);
        CreateFoodFragment fragment = new CreateFoodFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        food = getArguments().getParcelable(KEY_FOOD);

        action = getArguments().getInt(KEY_ACTION);

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

        if (action == ACTION_INSERT) {
            toolbar.setTitle("Create your Food");
        } else if (action == ACTION_UPDATE) {
            toolbar.setTitle("Update food");
        }

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

        if (action == ACTION_UPDATE) {
            setFoodDetails();
        }
    }

    private void setFoodDetails() {
        DecimalFormat formatter = new DecimalFormat("####.##");

        etName.setText(food.getName());
        etBrand.setText(food.getBrand());
        etServingSizeUnit.setText(formatter.format(food.getServingSizeUnit()));
        etServingSizeMeasurement.setText(food.getServingSizeMeasurement());
        etCalories.setText(formatter.format(food.getCalories()));
        etFat.setText(formatter.format(food.getFat()));
        etCarbohydrates.setText(formatter.format(food.getCarbohydrates()));
        etProtein.setText(formatter.format(food.getProtein()));
        etSaturatedFat.setText(formatter.format(food.getSaturatedFat()));
        etChoresterol.setText(formatter.format(food.getCholesterol()));
        etSodium.setText(formatter.format(food.getSodium()));
        etDietaryFiber.setText(formatter.format(food.getDietaryFiber()));
        etSugars.setText(formatter.format(food.getSugars()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_food, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                onActionConfirm();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onActionConfirm() {
        switch (action) {
            case ACTION_INSERT:
                insertFoodToDb();
                break;

            case ACTION_UPDATE:
                updateFood();
                break;
        }
    }

    private void insertFoodToDb() {
        if (!validate()) {
            return;
        }

        setFoodAttr();

        new CountDuplicateFoodTask(db, new CountDuplicateFoodTask.OnDuplicateFoodCountListener() {
            @Override
            public void onDuplicateFoodCounted(int count, Food food) {
                if (count == 0) {
                    new CreateFoodTask(db, new CreateFoodTask.OnFoodCreateListener() {
                        @Override
                        public void onFoodCreated(Long id) {
                            listener.onMenuConfirmPressed();
                        }
                    }).execute(food);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.duplicate_food_error), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(food);
    }

    private void updateFood() {
        if (!validate()) {
            return;
        }

        setFoodAttr();

        new UpdateFoodTask(db, new UpdateFoodTask.OnFoodUpdateListener() {
            @Override
            public void onFoodUpdated() {
                Toast.makeText(getActivity(), getResources().getString(R.string.updated), Toast.LENGTH_SHORT).show();
                listener.onUpdated();
            }
        }).execute(food);
    }

    private boolean validate() {
        if (etName.getText().toString().isEmpty() ||
                etCalories.getText().toString().isEmpty() ||
                etServingSizeUnit.getText().toString().isEmpty() ||
                etServingSizeMeasurement.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.info_missed_error), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void setFoodAttr() {
        food.setName(etName.getText().toString());
        food.setBrand(etBrand.getText().toString());
        food.setServingSizeMeasurement(etServingSizeMeasurement.getText().toString());
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
    }

    private String emptyToZero(String num) {
        return num.isEmpty() ? "0" : num;
    }
}
