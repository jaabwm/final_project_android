package com.jabwrb.nutridiary.fragment;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.activity.MainActivity;
import com.jabwrb.nutridiary.adapter.FoodEntryRecyclerViewAdapter;
import com.jabwrb.nutridiary.database.DatabaseSingleton;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.database.FoodEntryWithFood;
import com.jabwrb.nutridiary.database.NutriDiaryDb;
import com.jabwrb.nutridiary.model.Meal;
import com.jabwrb.nutridiary.task.DeleteFoodEntryTask;
import com.jabwrb.nutridiary.task.LoadFoodEntriesWithFoodTask;

import java.io.File;
import java.io.FileOutputStream;
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
    private NutriDiaryDb db;
    private HomeFragmentListener listener;
    private Button btnDatePicker;
    private ImageButton imgBtnDateDec;
    private ImageButton imgBtnDateInc;
    private FloatingActionButton fabAddEntry;
    private TextView tvCalEaten;
    private TextView tvFatEaten;
    private TextView tvCarbsEaten;
    private TextView tvProteinEaten;
    private TextView tvCalBreakfast;
    private TextView tvCalLunch;
    private TextView tvCalDinner;
    private TextView tvCalSnack;
    private LinearLayout linearLayoutAll;
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
        void onFabAddEntryPressed();

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

        linearLayoutAll = view.findViewById(R.id.linearLayoutAll);

        btnDatePicker = view.findViewById(R.id.btnDatePicker);
        btnDatePicker.setOnClickListener(this);
        setBtnDatePickerInfo(currentDate);

        imgBtnDateDec = view.findViewById(R.id.imgBtnDateDec);
        imgBtnDateInc = view.findViewById(R.id.imgBtnDateInc);

        imgBtnDateDec.setOnClickListener(this);
        imgBtnDateInc.setOnClickListener(this);

        fabAddEntry = view.findViewById(R.id.fabAddEntry);
        fabAddEntry.setOnClickListener(this);

        tvCalEaten = view.findViewById(R.id.tvCalEaten);
        tvFatEaten = view.findViewById(R.id.tvFatEaten);
        tvCarbsEaten = view.findViewById(R.id.tvCarbsEaten);
        tvProteinEaten = view.findViewById(R.id.tvProteinEaten);
        tvCalBreakfast = view.findViewById(R.id.tvCalBreakfast);
        tvCalLunch = view.findViewById(R.id.tvCalLunch);
        tvCalDinner = view.findViewById(R.id.tvCalDinner);
        tvCalSnack = view.findViewById(R.id.tvCalSnack);

        linearLayoutBreakfast = view.findViewById(R.id.linearLayoutBreakfast);
        linearLayoutLunch = view.findViewById(R.id.linearLayoutLunch);
        linearLayoutDinner = view.findViewById(R.id.linearLayoutDinner);
        linearLayoutSnack = view.findViewById(R.id.linearLayoutSnack);

        listBreakfast = view.findViewById(R.id.listBreakfast);
        listLunch = view.findViewById(R.id.listLunch);
        listDinner = view.findViewById(R.id.listDinner);
        listSnack = view.findViewById(R.id.listSnack);

        listBreakfast.setNestedScrollingEnabled(false);
        listLunch.setNestedScrollingEnabled(false);
        listDinner.setNestedScrollingEnabled(false);
        listSnack.setNestedScrollingEnabled(false);

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
        Meal breakfast = new Meal();
        Meal lunch = new Meal();
        Meal dinner = new Meal();
        Meal snack = new Meal();

        for (FoodEntryWithFood f : foodEntryWithFoodList) {
            switch (f.getFoodEntry().getMeal()) {
                case "Breakfast":
                    breakfast.add(f);
                    break;

                case "Lunch":
                    lunch.add(f);
                    break;

                case "Dinner":
                    dinner.add(f);
                    break;

                case "Snack":
                    snack.add(f);
                    break;
            }
        }

        adapterBreakfast.setData(breakfast.getMeal());
        adapterLunch.setData(lunch.getMeal());
        adapterDinner.setData(dinner.getMeal());
        adapterSnack.setData(snack.getMeal());

        adapterBreakfast.notifyDataSetChanged();
        adapterLunch.notifyDataSetChanged();
        adapterDinner.notifyDataSetChanged();
        adapterSnack.notifyDataSetChanged();

        if (breakfast.size() > 0) {
            linearLayoutBreakfast.setVisibility(View.VISIBLE);
        } else {
            linearLayoutBreakfast.setVisibility(View.GONE);
        }

        if (lunch.size() > 0) {
            linearLayoutLunch.setVisibility(View.VISIBLE);
        } else {
            linearLayoutLunch.setVisibility(View.GONE);
        }

        if (dinner.size() > 0) {
            linearLayoutDinner.setVisibility(View.VISIBLE);
        } else {
            linearLayoutDinner.setVisibility(View.GONE);
        }

        if (snack.size() > 0) {
            linearLayoutSnack.setVisibility(View.VISIBLE);
        } else {
            linearLayoutSnack.setVisibility(View.GONE);
        }

        DecimalFormat formatter = new DecimalFormat("####.##");
        String textCalories = getResources().getString(R.string.calories_lowercase);
        int totalCalEaten = breakfast.getTotalCalories() + lunch.getTotalCalories() +
                dinner.getTotalCalories() + snack.getTotalCalories();
        float totalFatEaten = breakfast.getTotalFat() + lunch.getTotalFat() +
                dinner.getTotalFat() + snack.getTotalFat();
        float totalCarbsEaten = breakfast.getTotalCarbohydrates() + lunch.getTotalCarbohydrates() +
                dinner.getTotalCarbohydrates() + snack.getTotalCarbohydrates();
        float totalProteinEaten = breakfast.getTotalProtein() + lunch.getTotalProtein() +
                dinner.getTotalProtein() + snack.getTotalProtein();

        tvCalEaten.setText(formatter.format(totalCalEaten) + " " + textCalories);
        tvFatEaten.setText(formatter.format(totalFatEaten) + " g");
        tvCarbsEaten.setText(formatter.format(totalCarbsEaten) + " g");
        tvProteinEaten.setText(formatter.format(totalProteinEaten) + " g");
        tvCalBreakfast.setText(formatter.format(breakfast.getTotalCalories()) + " " + textCalories);
        tvCalLunch.setText(formatter.format(lunch.getTotalCalories()) + " " + textCalories);
        tvCalDinner.setText(formatter.format(dinner.getTotalCalories()) + " " + textCalories);
        tvCalSnack.setText(formatter.format(snack.getTotalCalories()) + " " + textCalories);
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

            case R.id.action_share:
                onActionShare();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onActionShare() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

        shareImage();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void shareImage() {
        String date = new SimpleDateFormat("dMMMyyyy").format(new Date());
        final String DIR_PATH = Environment.getExternalStorageDirectory() + "/NutriDiary";
        final String FILE_NAME = date + ".png";

        storeImage(getScreenshot(linearLayoutAll), DIR_PATH, FILE_NAME);
        sendImage(new File(DIR_PATH, FILE_NAME));
    }

    private Bitmap getScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight() - dp2px(72), Bitmap.Config.ARGB_8888);
        view.setDrawingCacheEnabled(false);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(getResources().getColor(R.color.windowBackground));
        view.draw(canvas);

        return bitmap;
    }

    private void storeImage(Bitmap bitmap, String dirPath, String fileName) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendImage(File file) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        startActivity(Intent.createChooser(intent, "Share to"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDatePicker:
                listener.onBtnDatePickerPressed();
                break;

            case R.id.imgBtnDateDec:
                addDaysToCurrentDate(-1);
                break;

            case R.id.imgBtnDateInc:
                addDaysToCurrentDate(1);
                break;

            case R.id.fabAddEntry:
                listener.onFabAddEntryPressed();
                break;
        }
    }

    private void addDaysToCurrentDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, days);

        currentDate = calendar.getTime();

        invalidateDate();
    }

    private void invalidateDate() {
        ((MainActivity) getActivity()).setCurrentDate(currentDate);
        setBtnDatePickerInfo(currentDate);
        queryFoodEntries(currentDate);
    }

    /**
     * DatePickerFragment
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        currentDate = calendar.getTime();

        invalidateDate();
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
                Toast.makeText(getActivity(), getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
            }
        }).execute(foodEntry);
    }
}
