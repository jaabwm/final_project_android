package com.jabwrb.nutridiary.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.adapter.FoodRecyclerViewAdapter;
import com.jabwrb.nutridiary.database.DatabaseSingleton;
import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.NutriDiaryDb;
import com.jabwrb.nutridiary.task.LoadMyFoodTask;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFoodFragment extends Fragment implements View.OnClickListener {

    private NutriDiaryDb db;
    private SelectFoodFragmentListener listener;
    private Button btnCreate;
    private RecyclerView recyclerView;
    private FoodRecyclerViewAdapter foodRecyclerViewAdapter;

    public interface SelectFoodFragmentListener {
        void onBtnCreatePressed();
    }

    public SelectFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseSingleton.getDatabaseInstance().getDb();

        listener = (SelectFoodFragmentListener) getActivity();

        foodRecyclerViewAdapter = new FoodRecyclerViewAdapter(getActivity());

        queryFoods();
    }

    private void queryFoods() {
        new LoadMyFoodTask(db, new LoadMyFoodTask.OnFoodLoadListener() {
            @Override
            public void onFoodLoaded(List<Food> foods) {
                foodRecyclerViewAdapter.setData(foods);
                foodRecyclerViewAdapter.notifyDataSetChanged();
            }
        }).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_select_food, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_select_food, container, false);

        setup(view);

        return view;
    }

    private void setup(View view) {
        // Toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.listFood);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.setAdapter(foodRecyclerViewAdapter);

        btnCreate = view.findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreate:
                listener.onBtnCreatePressed();
                break;
        }
    }
}
