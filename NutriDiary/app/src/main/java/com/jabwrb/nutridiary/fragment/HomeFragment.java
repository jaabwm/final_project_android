package com.jabwrb.nutridiary.fragment;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.activity.MainActivity;
import com.jabwrb.nutridiary.adapter.FoodEntryRecyclerViewAdapter;
import com.jabwrb.nutridiary.database.DatabaseSingleton;
import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.FoodEntryWithFood;
import com.jabwrb.nutridiary.database.NutriDiaryDb;
import com.jabwrb.nutridiary.task.DeleteFoodEntryTask;
import com.jabwrb.nutridiary.task.LoadFoodEntriesWithFoodTask;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final String TAG = HomeFragment.class.getSimpleName();
    private NutriDiaryDb db;
    private HomeFragmentListener listener;
    private Button btnDatePicker;
    private Button btnAddBreakfast;
    private TextView tvCalEaten;
    private TextView tvFatEaten;
    private TextView tvCarbsEaten;
    private TextView tvProteinEaten;
    private LinearLayout linearLayoutBreakfast;
    private LinearLayout linearLayoutLunch;
    private LinearLayout linearLayoutDinner;
    private LinearLayout linearLayoutSnack;
    private RecyclerView listBreakfast;
    private RecyclerView listLunch;
    private RecyclerView listDinner;
    private RecyclerView listSnack;
    private FoodEntryRecyclerViewAdapter adapterBreakfast;
    private FoodEntryRecyclerViewAdapter adapterLunch;
    private FoodEntryRecyclerViewAdapter adapterDinner;
    private FoodEntryRecyclerViewAdapter adapterSnack;
    public Date currentDate;

    public interface HomeFragmentListener {
        void onBtnAddBreakfastPressed();

        void onBtnDatePickerPressed();

        void onMenuSearchPressed();
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseSingleton.getDatabaseInstance().getDb();

        listener = (HomeFragmentListener) getActivity();

        adapterBreakfast = new FoodEntryRecyclerViewAdapter(getActivity());
        adapterLunch = new FoodEntryRecyclerViewAdapter(getActivity());
        adapterDinner = new FoodEntryRecyclerViewAdapter(getActivity());
        adapterSnack = new FoodEntryRecyclerViewAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        currentDate = ((MainActivity) getActivity()).getCurrentDate();

        setup(view);

        queryFoodEntries(currentDate);

        return view;
    }

    private void setup(View view) {
        // Toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        btnDatePicker = view.findViewById(R.id.btnDatePicker);
        btnDatePicker.setOnClickListener(this);
        setBtnDatePickerInfo(currentDate);

        btnAddBreakfast = view.findViewById(R.id.btnAddBreakfast);
        btnAddBreakfast.setOnClickListener(this);

        tvCalEaten = view.findViewById(R.id.tvCalEaten);
        tvFatEaten = view.findViewById(R.id.tvFatEaten);
        tvCarbsEaten = view.findViewById(R.id.tvCarbsEaten);
        tvProteinEaten = view.findViewById(R.id.tvProteinEaten);

        linearLayoutBreakfast = view.findViewById(R.id.linearLayoutBreakfast);
        linearLayoutLunch = view.findViewById(R.id.linearLayoutLunch);
        linearLayoutDinner = view.findViewById(R.id.linearLayoutDinner);
        linearLayoutSnack = view.findViewById(R.id.linearLayoutSnack);

        listBreakfast = view.findViewById(R.id.listBreakfast);
        listLunch = view.findViewById(R.id.listLunch);
        listDinner = view.findViewById(R.id.listDinner);
        listSnack = view.findViewById(R.id.listSnack);

        listBreakfast.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        listLunch.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        listDinner.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        listSnack.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        listBreakfast.setAdapter(adapterBreakfast);
        listLunch.setAdapter(adapterLunch);
        listDinner.setAdapter(adapterDinner);
        listSnack.setAdapter(adapterSnack);
    }

    private void queryFoodEntries(Date date) {
        new LoadFoodEntriesWithFoodTask(db, new LoadFoodEntriesWithFoodTask.OnFoodLoadListener() {
            @Override
            public void onFoodLoaded(List<FoodEntryWithFood> foodEntryWithFoodList) {
                updateDiary(foodEntryWithFoodList);
            }
        }).execute(date);
    }

    public void updateDiary(List<FoodEntryWithFood> foodEntryWithFoodList) {
        List<FoodEntryWithFood> dataBreakfast = new ArrayList<>();
        List<FoodEntryWithFood> dataLunch = new ArrayList<>();
        List<FoodEntryWithFood> dataDinner = new ArrayList<>();
        List<FoodEntryWithFood> dataSnack = new ArrayList<>();
        int totalCalEaten = 0;
        float totalFatEaten = 0;
        float totalCarbsEaten = 0;
        float totalProteinEaten = 0;

        for (FoodEntryWithFood f : foodEntryWithFoodList) {
            FoodEntry foodEntry = f.getFoodEntry();
            Food food = f.getFood();

            switch (foodEntry.getMeal()) {
                case "Breakfast":
                    dataBreakfast.add(f);
                    break;

                case "Lunch":
                    dataLunch.add(f);
                    break;

                case "Dinner":
                    dataDinner.add(f);
                    break;

                case "Snack":
                    dataSnack.add(f);
                    break;
            }

            float amount = foodEntry.getAmount();
            totalCalEaten += amount * food.getCalories();
            totalFatEaten += amount * food.getFat();
            totalCarbsEaten += amount * food.getCarbohydrates();
            totalProteinEaten += amount * food.getProtein();
        }

        adapterBreakfast.setData(dataBreakfast);
        adapterLunch.setData(dataLunch);
        adapterDinner.setData(dataDinner);
        adapterSnack.setData(dataSnack);

        adapterBreakfast.notifyDataSetChanged();
        adapterLunch.notifyDataSetChanged();
        adapterDinner.notifyDataSetChanged();
        adapterSnack.notifyDataSetChanged();

        if (dataBreakfast.size() > 0) {
            linearLayoutBreakfast.setVisibility(View.VISIBLE);
        } else {
            linearLayoutBreakfast.setVisibility(View.GONE);
        }

        if (dataLunch.size() > 0) {
            linearLayoutLunch.setVisibility(View.VISIBLE);
        } else {
            linearLayoutLunch.setVisibility(View.GONE);
        }

        if (dataDinner.size() > 0) {
            linearLayoutDinner.setVisibility(View.VISIBLE);
        } else {
            linearLayoutDinner.setVisibility(View.GONE);
        }

        if (dataSnack.size() > 0) {
            linearLayoutSnack.setVisibility(View.VISIBLE);
        } else {
            linearLayoutSnack.setVisibility(View.GONE);
        }

        DecimalFormat formatter = new DecimalFormat("####.##");

        tvCalEaten.setText(formatter.format(totalCalEaten));
        tvFatEaten.setText(formatter.format(totalFatEaten) + " g");
        tvCarbsEaten.setText(formatter.format(totalCarbsEaten) + " g");
        tvProteinEaten.setText(formatter.format(totalProteinEaten) + " g");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                listener.onMenuSearchPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

        currentDate = calendar.getTime();

        ((MainActivity) getActivity()).setCurrentDate(currentDate);
        setBtnDatePickerInfo(currentDate);
        queryFoodEntries(currentDate);
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

    public void showDialogDelete(final FoodEntry foodEntry) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete this entry?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteFoodEntry(foodEntry);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteFoodEntry(FoodEntry foodEntry) {
        new DeleteFoodEntryTask(db, new DeleteFoodEntryTask.OnFoodEntryDeleteListener() {
            @Override
            public void onFoodEntryDeleted() {
                queryFoodEntries(currentDate);
                Toast.makeText(getActivity(), "Deleted.", Toast.LENGTH_SHORT).show();
            }
        }).execute(foodEntry);
    }
}
