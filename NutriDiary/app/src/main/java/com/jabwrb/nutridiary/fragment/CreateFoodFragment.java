package com.jabwrb.nutridiary.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jabwrb.nutridiary.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFoodFragment extends Fragment implements View.OnClickListener {

    private Button btnAdd;
    private EditText etName;
    private EditText etBrand;
    private EditText etServingSize;
    private EditText etCal;
    private EditText etFat;
    private EditText etCarb;
    private EditText etProtein;
    private EditText etSaturatedFat;
    private EditText etChoresterol;
    private EditText etSodium;
    private EditText etDietaryFiber;
    private EditText etSugars;
    private CreateFoodFragmentListener listener;

    public interface CreateFoodFragmentListener {
        void onBtnAddPressed();
    }

    public CreateFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        etName = (EditText) view.findViewById(R.id.etName);
        etBrand = (EditText) view.findViewById(R.id.etBrand);
        etServingSize = (EditText) view.findViewById(R.id.etServingSize);
        etCal = (EditText) view.findViewById(R.id.etCal);
        etFat = (EditText) view.findViewById(R.id.etFat);
        etCarb = (EditText) view.findViewById(R.id.etCarb);
        etProtein = (EditText) view.findViewById(R.id.etProtein);
        etSaturatedFat = (EditText) view.findViewById(R.id.etSaturatedFat);
        etChoresterol = (EditText) view.findViewById(R.id.etCholesterol);
        etSodium = (EditText) view.findViewById(R.id.etSodium);
        etDietaryFiber = (EditText) view.findViewById(R.id.etDietaryFiber);
        etSugars = (EditText) view.findViewById(R.id.etSugars);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                insertFoodToDb();
                break;
        }
    }

    private void insertFoodToDb() {
        // TODO
        Toast.makeText(getActivity(), "Created.", Toast.LENGTH_LONG).show();
        listener.onBtnAddPressed();
    }
}
