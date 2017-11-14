package com.jabwrb.nutridiary.fragment;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.adapter.FoodEntryRecyclerViewAdapter;
import com.jabwrb.nutridiary.database.DatabaseSingleton;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.FoodEntryWithFood;
import com.jabwrb.nutridiary.database.NutriDiaryDb;
import com.jabwrb.nutridiary.task.DeleteFoodEntryTask;
import com.jabwrb.nutridiary.task.LoadFoodEntriesWithFoodTask;

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
    private static final String KEY_CURRENT_DATE = "currentDate";
    private NutriDiaryDb db;
    private HomeFragmentListener listener;
    private Button btnDatePicker;
    private Button btnAddBreakfast;
    private RecyclerView listBreakfast;
    private RecyclerView listLunch;
    private RecyclerView listDinner;
    private RecyclerView listSnack;
    private FoodEntryRecyclerViewAdapter adapterBreakfast;
    private FoodEntryRecyclerViewAdapter adapterLunch;
    private FoodEntryRecyclerViewAdapter adapterDinner;
    private FoodEntryRecyclerViewAdapter adapterSnack;
    private Date currentDate;

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

        db = DatabaseSingleton.getDatabaseInstance().getDb();

        listener = (HomeFragmentListener) getActivity();

        adapterBreakfast = new FoodEntryRecyclerViewAdapter(getActivity());
        adapterLunch = new FoodEntryRecyclerViewAdapter(getActivity());
        adapterDinner = new FoodEntryRecyclerViewAdapter(getActivity());
        adapterSnack = new FoodEntryRecyclerViewAdapter(getActivity());

        if (savedInstanceState == null) {
            currentDate = new Date();
        } else {
            currentDate = new Date(savedInstanceState.getLong(KEY_CURRENT_DATE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setup(view);

        queryFoodEntries(currentDate);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(KEY_CURRENT_DATE, currentDate.getTime());
    }

    private void setup(View view) {
        btnDatePicker = view.findViewById(R.id.btnDatePicker);
        btnDatePicker.setOnClickListener(this);
        setBtnDatePickerInfo(currentDate);

        btnAddBreakfast = view.findViewById(R.id.btnAddBreakfast);
        btnAddBreakfast.setOnClickListener(this);

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

        for (FoodEntryWithFood f : foodEntryWithFoodList) {
            switch (f.getFoodEntry().getMeal()) {
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
        }

        adapterBreakfast.setData(dataBreakfast);
        adapterLunch.setData(dataLunch);
        adapterDinner.setData(dataDinner);
        adapterSnack.setData(dataSnack);

        adapterBreakfast.notifyDataSetChanged();
        adapterLunch.notifyDataSetChanged();
        adapterDinner.notifyDataSetChanged();
        adapterSnack.notifyDataSetChanged();
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
