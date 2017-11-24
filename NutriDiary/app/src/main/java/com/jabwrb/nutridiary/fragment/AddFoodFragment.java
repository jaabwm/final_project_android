package com.jabwrb.nutridiary.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.activity.MainActivity;
import com.jabwrb.nutridiary.database.DatabaseSingleton;
import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.NutriDiaryDb;
import com.jabwrb.nutridiary.task.AddToDiaryTask;
import com.jabwrb.nutridiary.task.CreateFoodTask;
import com.jabwrb.nutridiary.task.GetDuplicateFoodIdTask;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFoodFragment extends Fragment {

    public static final String KEY_FOOD = "food";
    private NutriDiaryDb db;
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
    private TextView tvBrand;

    public interface AddFoodFragmentListener {
        void onActionAddPressed();
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

        db = DatabaseSingleton.getDatabaseInstance().getDb();

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
        // Toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        etAmount = view.findViewById(R.id.etAmount);
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    setNutrientDetails();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
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

        String brand = food.getBrand();
        if (brand == null || brand.isEmpty()) {
            tvBrand.setVisibility(View.GONE);
        } else {
            tvBrand.setText(food.getBrand());
        }
        tvFoodName.setText(food.getName());
        tvServingSize.setText(food.getServingSizeUnit() + " " + food.getServingSizeMeasurement());
        setNutrientDetails();
    }

    private void setNutrientDetails() {
        float amount = Float.parseFloat(etAmount.getText().toString());
        DecimalFormat formatter = new DecimalFormat("####.##");

        tvCalories.setText(formatter.format(Math.round(food.getCalories() * amount)));
        tvTotalFat.setText(formatter.format(food.getFat() * amount) + " g");
        tvSaturatedFat.setText(formatter.format(food.getSaturatedFat() * amount) + " mg");
        tvChoresterol.setText(formatter.format(food.getCholesterol() * amount) + " mg");
        tvSodium.setText(formatter.format(food.getSodium() * amount) + " g");
        tvTotalCarbohydrates.setText(formatter.format(food.getCarbohydrates() * amount) + " g");
        tvDietaryFiber.setText(formatter.format(food.getDietaryFiber() * amount) + " g");
        tvSugars.setText(formatter.format(food.getSugars() * amount) + " g");
        tvProtein.setText(formatter.format(food.getProtein() * amount) + " g");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_food, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addToDiary();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addToDiary() {
        if (!validate()) {
            return;
        }

        new GetDuplicateFoodIdTask(db, new GetDuplicateFoodIdTask.GetDuplicateFoodIdListener() {
            @Override
            public void OnDuplicateFoodIdGot(int id, Food food) {
                if (id == 0) {
                    // Not duplicate, create new food.
                    createFood(food);
                } else {
                    // Duplicate, normally add food entry.
                    addFoodEntry(food.getId());
                }
            }
        }).execute(food);
    }

    private boolean validate() {
        if (etAmount.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.amount_missed_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createFood(Food food) {
        new CreateFoodTask(db, new CreateFoodTask.OnFoodCreateListener() {
            @Override
            public void onFoodCreated(Long id) {
                addFoodEntry(id.intValue());
            }
        }).execute(food);
    }


    private void addFoodEntry(int foodId) {
        FoodEntry foodEntry = new FoodEntry();
        foodEntry.setFoodId(foodId);
        try {
            foodEntry.setAmount(Float.parseFloat(etAmount.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Invalid number format entered.", Toast.LENGTH_SHORT).show();
            return;
        }
        foodEntry.setMeal(spinner.getSelectedItem().toString());
        foodEntry.setDate(((MainActivity) getActivity()).getCurrentDate());

        new AddToDiaryTask(db, new AddToDiaryTask.OnFoodAddListener() {
            @Override
            public void onFoodAdded() {
                listener.onActionAddPressed();
            }
        }).execute(foodEntry);
    }
}
