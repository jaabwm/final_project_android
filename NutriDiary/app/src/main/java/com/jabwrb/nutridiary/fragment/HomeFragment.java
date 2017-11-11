package com.jabwrb.nutridiary.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jabwrb.nutridiary.InstanceData;
import com.jabwrb.nutridiary.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "HomeFragment";
    private HomeFragmentListener listener;
    private Button btnAddBreakfast;
    private TableLayout tableLayout;

    public interface HomeFragmentListener {
        void onBtnAddBreakfastPressed();
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listener = (HomeFragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        btnAddBreakfast = (Button) view.findViewById(R.id.btnAddBreakfast);
        btnAddBreakfast.setOnClickListener(this);

        tableLayout = (TableLayout) view.findViewById(R.id.tableLayoutBreakfastItems);
        updateDiary();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddBreakfast:
                listener.onBtnAddBreakfastPressed();
                break;
        }
    }

    public void updateDiary() {
        for (String foodName : InstanceData.history) {
            TableRow tableRow1 = new TableRow(getActivity());
            tableRow1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TableRow tableRow2 = new TableRow(getActivity());
            tableRow2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            // Text params

            // Table row: TextView Name
            TextView textViewName = new TextView(getActivity()); // Add textview
            textViewName.setText(foodName);
            textViewName.setTextSize(18);
            textViewName.setTextColor(Color.DKGRAY);
            textViewName.setGravity(Gravity.CENTER_VERTICAL);
            TableRow.LayoutParams paramsName = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            paramsName.setMargins(4, 8, 0, 0); // Left, top, right, bottom
            tableRow1.addView(textViewName, paramsName);

            // Table row: TextView Energy
            TextView textViewEnergy = new TextView(getActivity()); // Add textview
            textViewEnergy.setText("300");
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
            textViewSubLine.setText("100 g");
            tableRow2.addView(textViewSubLine, paramsSubLine);

            // Add row to table
            tableLayout.addView(tableRow1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)); /* Add row to TableLayout. */
            tableLayout.addView(tableRow2, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)); /* Add row to TableLayout. */
        }

        // TODO: get diary data from database and update
    }

    public void addToDiary(String foodName) {
        InstanceData.history.add(foodName);
    }
}
