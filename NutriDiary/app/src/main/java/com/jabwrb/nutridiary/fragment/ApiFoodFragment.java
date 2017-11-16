package com.jabwrb.nutridiary.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.adapter.FoodRecyclerViewAdapter;
import com.jabwrb.nutridiary.api.Food;
import com.jabwrb.nutridiary.api.Item;
import com.jabwrb.nutridiary.api.Nutrient;
import com.jabwrb.nutridiary.api.NutrientReportResponse;
import com.jabwrb.nutridiary.api.SearchResponse;
import com.jabwrb.nutridiary.api.SearchResult;
import com.jabwrb.nutridiary.api.UsdaApi;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApiFoodFragment extends Fragment {

    public static final String TAG = ApiFoodFragment.class.getSimpleName();
    private OkHttpClient client;
    private Retrofit retrofit;
    private UsdaApi api;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FoodRecyclerViewAdapter adapter;
    private List<com.jabwrb.nutridiary.database.Food> data;

    public ApiFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new FoodRecyclerViewAdapter(getActivity());
        data = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_api_food, container, false);

        setup(view);
        api();

        return view;
    }

    private void setup(View view) {
        // Toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        progressBar = view.findViewById(R.id.progressBar);

        recyclerView = view.findViewById(R.id.listFood);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void api() {
        client = new OkHttpClient
                .Builder()
                .build();

        retrofit = new Retrofit
                .Builder()
                .baseUrl(UsdaApi.BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(UsdaApi.class);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_api_food, menu);
        configSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void configSearchView(Menu menu) {
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSearchResult(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getSearchResult(String query) {
        progressBar.setVisibility(View.VISIBLE);

        Call<SearchResponse> call = api.searchFood(query);
        System.out.println("search url: " + call.request().url().toString());
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    SearchResult searchResult = response.body().getList();

                    if (searchResult == null) {
                        Toast.makeText(getActivity(), "Food not found.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    data.clear();
                    List<Item> items = searchResult.getItem();
                    int rows = items.size();

                    for (Item i : items) {
                        getNutrientReport(i.getNdbno(), rows);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getNutrientReport(String ndbno, final int rows) {
        Call<NutrientReportResponse> call = api.getNutrientReport(ndbno);
        System.out.println("nutrient url: " + call.request().url().toString());
        call.enqueue(new Callback<NutrientReportResponse>() {
            @Override
            public void onResponse(Call<NutrientReportResponse> call, Response<NutrientReportResponse> response) {
                if (response.isSuccessful()) {
                    Food foodApi = response.body().getReport().getFoods().get(0);
                    List<Nutrient> nutrients = foodApi.getNutrients();

                    com.jabwrb.nutridiary.database.Food foodDb = new com.jabwrb.nutridiary.database.Food();
                    foodDb.setName(foodApi.getName());
                    foodDb.setServingSizeUnit(100);
                    foodDb.setServingSizeMeasurement("g");

                    for (Nutrient n : nutrients) {
                        float gram;
                        if (n.getGm().equals("--")) {
                            gram = 0;
                        } else {
                            gram = Float.parseFloat(n.getGm());
                        }

                        switch (n.getNutrient_id()) {
                            case UsdaApi.ID_CALORIES:
                                foodDb.setCalories((int) gram);
                                break;

                            case UsdaApi.ID_FAT:
                                foodDb.setFat(gram);
                                break;

                            case UsdaApi.ID_CARBOHYDRATES:
                                foodDb.setCarbohydrates(gram);
                                break;

                            case UsdaApi.ID_PROTEIN:
                                foodDb.setProtein(gram);
                                break;

                            case UsdaApi.ID_SATURATED_FAT:
                                foodDb.setSaturatedFat(gram);
                                break;

                            case UsdaApi.ID_CHOLESTEROL:
                                foodDb.setCholesterol(gram);
                                break;

                            case UsdaApi.ID_SODIUM:
                                foodDb.setSodium(gram);
                                break;

                            case UsdaApi.ID_DIETARY_FIBER:
                                foodDb.setDietaryFiber(gram);
                                break;

                            case UsdaApi.ID_SUGARS:
                                foodDb.setSugars(gram);
                                break;
                        }
                    }

                    data.add(foodDb);
                    adapter.setData(data);
                    adapter.notifyDataSetChanged();

                    if (data.size() == rows) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<NutrientReportResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
