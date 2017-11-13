package com.jabwrb.nutridiary.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.adapter.FoodRecyclerViewAdapter;
import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.fragment.AddFoodFragment;
import com.jabwrb.nutridiary.fragment.CreateFoodFragment;
import com.jabwrb.nutridiary.fragment.DatePickerFragment;
import com.jabwrb.nutridiary.fragment.HomeFragment;
import com.jabwrb.nutridiary.fragment.SelectFoodFragment;
import com.jabwrb.nutridiary.task.LoadFoodEntriesWithFoodTask;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener,
                                                               FoodRecyclerViewAdapter.FoodRecyclerViewAdapterListener,
                                                               AddFoodFragment.AddFoodFragmentListener,
                                                               SelectFoodFragment.SelectFoodFragmentListener,
                                                               CreateFoodFragment.CreateFoodFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, new HomeFragment(), HomeFragment.TAG)
                    .commit();
        }
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
    public void onItemClickedListener(Food food) {
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
}
