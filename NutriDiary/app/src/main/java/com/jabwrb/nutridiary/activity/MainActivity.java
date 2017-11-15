package com.jabwrb.nutridiary.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.adapter.FoodEntryRecyclerViewAdapter;
import com.jabwrb.nutridiary.adapter.FoodRecyclerViewAdapter;
import com.jabwrb.nutridiary.database.DatabaseSingleton;
import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.fragment.AddFoodFragment;
import com.jabwrb.nutridiary.fragment.ApiFoodFragment;
import com.jabwrb.nutridiary.fragment.CreateFoodFragment;
import com.jabwrb.nutridiary.fragment.DatePickerFragment;
import com.jabwrb.nutridiary.fragment.HomeFragment;
import com.jabwrb.nutridiary.fragment.SelectFoodFragment;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener,
                                                            SelectFoodFragment.SelectFoodFragmentListener,
                                                            CreateFoodFragment.CreateFoodFragmentListener,
                                                            AddFoodFragment.AddFoodFragmentListener,
                                                            FoodEntryRecyclerViewAdapter.OnFoodEntryClickListener,
                                                            FoodRecyclerViewAdapter.OnFoodClickListener {

    private static final String KEY_CURRENT_DATE = "currentDate";
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            DatabaseSingleton.getDatabaseInstance().initDb(getApplicationContext());

            currentDate = new Date();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, new HomeFragment(), HomeFragment.TAG)
                    .commit();
        } else {
            currentDate = new Date(savedInstanceState.getLong(KEY_CURRENT_DATE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(KEY_CURRENT_DATE, currentDate.getTime());
    }

    @Override
    public void onBtnAddBreakfastPressed() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new SelectFoodFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBtnDatePickerPressed() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG), 0);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onMenuSearchPressed() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new ApiFoodFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFoodClicked(Food food) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new AddFoodFragment().newInstance(food))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAddToDiaryPressed() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBtnCreatePressed() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new CreateFoodFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBtnAddPressed() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onFoodEntryLongClicked(FoodEntry foodEntry) {
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        fragment.showDialogDelete(foodEntry);
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
