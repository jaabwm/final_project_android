package com.jabwrb.nutridiary.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jabwrb.nutridiary.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFoodFragment extends Fragment implements View.OnClickListener {

    private AddFoodFragmentListener listener;
    private String foodName;
    private TextView textViewFoodName;
    private Button btnAddToDiary;

    public interface AddFoodFragmentListener {
        void onAddToDiaryPressed(String foodName);
    }

    public AddFoodFragment() {
        // Required empty public constructor
    }

    public static AddFoodFragment newInstance(String foodName) {
        Bundle args = new Bundle();
        args.putString("selectedFood", foodName);
        AddFoodFragment fragment = new AddFoodFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foodName = getArguments().getString("selectedFood");
        listener = (AddFoodFragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_food, container, false);

        textViewFoodName = (TextView) view.findViewById(R.id.textViewFoodName);
        textViewFoodName.setText(foodName);

        btnAddToDiary = (Button) view.findViewById(R.id.btnAddToDiary);
        btnAddToDiary.setOnClickListener(this);

        // TODO: get food nutrients from database and set to elements in layout.

        return view;
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
        listener.onAddToDiaryPressed(foodName);

        // TODO: insert food to database
    }
}
