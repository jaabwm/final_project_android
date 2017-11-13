package com.jabwrb.nutridiary.fragment;


import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.FoodEntryWithFood;
import com.jabwrb.nutridiary.database.NutriDiaryDb;
import com.jabwrb.nutridiary.task.LoadFoodEntriesWithFoodTask;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final String TAG = HomeFragment.class.getSimpleName();
    private NutriDiaryDb nutriDiaryDb;
    private HomeFragmentListener listener;
    private Button btnDatePicker;
    private Button btnAddBreakfast;
    private TableLayout tableLayoutBreakfastItems;
    private TableLayout tableLayoutLunchItems;
    private TableLayout tableLayoutDinnerItems;
    private TableLayout tableLayoutSnackItems;

    public interface HomeFragmentListener {
        void onBtnAddBreakfastPressed();
        void onBtnDatePickerPressed();
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nutriDiaryDb = Room.databaseBuilder(getActivity(),
                NutriDiaryDb.class, "NutriDiary.db")
                .fallbackToDestructiveMigration()
                .build();

        listener = (HomeFragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setup(view);

        queryFoodEntries(new Date());

        return view;
    }

    private void setup(View view) {
        btnDatePicker = view.findViewById(R.id.btnDatePicker);
        btnDatePicker.setOnClickListener(this);
        setBtnDatePickerInfo(new Date());

        btnAddBreakfast = view.findViewById(R.id.btnAddBreakfast);
        btnAddBreakfast.setOnClickListener(this);

        tableLayoutBreakfastItems = view.findViewById(R.id.tableLayoutBreakfastItems);
        tableLayoutLunchItems = view.findViewById(R.id.tableLayoutLunchItems);
        tableLayoutDinnerItems = view.findViewById(R.id.tableLayoutDinnerItems);
        tableLayoutSnackItems = view.findViewById(R.id.tableLayoutSnackItems);
    }

    private void queryFoodEntries(Date date) {
        new LoadFoodEntriesWithFoodTask(nutriDiaryDb, new LoadFoodEntriesWithFoodTask.OnFoodLoadListener() {
            @Override
            public void onFoodLoaded(List<FoodEntryWithFood> foodEntryWithFoodList) {
                clearAllTableItems();
                updateDiary(foodEntryWithFoodList);
            }
        }).execute(date);
    }

    private void clearAllTableItems() {
        tableLayoutBreakfastItems.removeAllViews();
        tableLayoutLunchItems.removeAllViews();
        tableLayoutDinnerItems.removeAllViews();
        tableLayoutSnackItems.removeAllViews();
    }

    public void updateDiary(List<FoodEntryWithFood> foodEntryWithFoodList) {
        for (FoodEntryWithFood f : foodEntryWithFoodList) {
            FoodEntry foodEntry = f.getFoodEntry();
            Food food = f.getFoods().get(0);

            switch (foodEntry.getMeal()) {
                case "Breakfast":
                    addTableRow(tableLayoutBreakfastItems, foodEntry, food);
                    break;

                case "Lunch":
                    addTableRow(tableLayoutLunchItems, foodEntry, food);
                    break;

                case "Dinner":
                    addTableRow(tableLayoutDinnerItems, foodEntry, food);
                    break;

                case "Snack":
                    addTableRow(tableLayoutSnackItems, foodEntry, food);
                    break;
            }
        }
    }

    public void addTableRow(TableLayout tableLayout, FoodEntry foodEntry, Food food) {
        DecimalFormat formatter = new DecimalFormat("####.##");

        TableRow tableRow1 = new TableRow(getActivity());
        tableRow1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TableRow tableRow2 = new TableRow(getActivity());
        tableRow2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        // Text params
        // Table row: TextView Name
        TextView textViewName = new TextView(getActivity()); // Add textview
        textViewName.setText(food.getName());
        textViewName.setTextSize(18);
        textViewName.setTextColor(Color.DKGRAY);
        textViewName.setGravity(Gravity.CENTER_VERTICAL);
        TableRow.LayoutParams paramsName = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        paramsName.setMargins(4, 8, 0, 0); // Left, top, right, bottom
        tableRow1.addView(textViewName, paramsName);

        // Table row: TextView Energy
        TextView textViewEnergy = new TextView(getActivity()); // Add textview
        textViewEnergy.setText(String.valueOf(food.getCalories()));
        textViewEnergy.setTextSize(18);
        textViewEnergy.setTextColor(Color.DKGRAY);
        textViewEnergy.setGravity(Gravity.CENTER_VERTICAL);
        TableRow.LayoutParams paramsEnergy = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        paramsEnergy.setMargins(0, 8, 10, 0); // Left, top, right, bottom
        tableRow1.addView(textViewEnergy, paramsEnergy);

        // Table row: TextView subLine
        TableRow.LayoutParams paramsSubLine = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        paramsSubLine.setMargins(4, 0, 0, 12); // Left, top, right, bottom

        TextView textViewSubLine = new TextView(getActivity()); // Add textview
        textViewSubLine.setText(formatter.format(foodEntry.getAmount() * food.getServingSizeUnit()) + " " + food.getServingSizeMeasurement());
        tableRow2.addView(textViewSubLine, paramsSubLine);

        // Add row to table
        tableLayout.addView(tableRow1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)); /* Add row to TableLayout. */
        tableLayout.addView(tableRow2, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)); /* Add row to TableLayout. */
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddBreakfast:
                listener.onBtnAddBreakfastPressed();
                break;

            case R.id.btnDatePicker:
                listener.onBtnDatePickerPressed();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();

        setBtnDatePickerInfo(date);
        queryFoodEntries(date);
    }

    private void setBtnDatePickerInfo(Date date) {
        switch (date.getDay()) {
            case 0:
                btnDatePicker.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calendar_sunday, 0, 0, 0);
                break;

            case 1:
                btnDatePicker.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calendar_monday, 0, 0, 0);
                break;

            case 2:
                btnDatePicker.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calendar_tuesday, 0, 0, 0);
                break;

            case 3:
                btnDatePicker.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calendar_wednesday, 0, 0, 0);
                break;

            case 4:
                btnDatePicker.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calendar_thursday, 0, 0, 0);
                break;

            case 5:
                btnDatePicker.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calendar_friday, 0, 0, 0);
                break;

            case 6:
                btnDatePicker.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calendar_saturday, 0, 0, 0);
                break;
        }

        btnDatePicker.setText(new SimpleDateFormat("d MMM yyyy").format(date));
    }
}
