package com.jabwrb.nutridiary.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.api.Food;
import com.jabwrb.nutridiary.api.Item;
import com.jabwrb.nutridiary.api.Nutrient;
import com.jabwrb.nutridiary.api.NutrientReport;
import com.jabwrb.nutridiary.api.NutrientReportResponse;
import com.jabwrb.nutridiary.api.SearchResponse;
import com.jabwrb.nutridiary.api.UsdaApi;

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

    private TextView text;
    private OkHttpClient client;
    private Retrofit retrofit;
    private UsdaApi api;

    public ApiFoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_api_food, container, false);

        setup(view);
        api();
        getNutrientReport();

        return view;
    }

    private void setup(View view) {
        // Toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        text = view.findViewById(R.id.text);
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

    private void  configSearchView(Menu menu) {
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
        Call<SearchResponse> call = api.searchFood(UsdaApi.API_KEY, query);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    String str = "";
                    List<Item> items = response.body().getList().getItem();

                    for (Item i : items) {
                        str += String.format("(%s - %s)\n", i.getNdbno(), i.getName());
                    }

                    text.setText(str);
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getNutrientReport() {
        String ndbno = "01009";
        Call<NutrientReportResponse> call = api.getNutrientReport(UsdaApi.API_KEY,
                                                                    ndbno,
                                                                    UsdaApi.ID_CALORIES,
                                                                    UsdaApi.ID_PROTEIN);
        call.enqueue(new Callback<NutrientReportResponse>() {
            @Override
            public void onResponse(Call<NutrientReportResponse> call, Response<NutrientReportResponse> response) {
                if (response.isSuccessful()) {
                    String str = "";
                    NutrientReport nutrientReport = response.body().getReport();
                    List<Food> foods = nutrientReport.getFoods();

                    str += String.format("(%s,%s,%s,%d,%d,%d)\n",
                            nutrientReport.getSr(), nutrientReport.getGroups(), nutrientReport.getSubset(),
                            nutrientReport.getEnd(), nutrientReport.getStart(), nutrientReport.getTotal());

                    for (Food f : foods) {
                        List<Nutrient> nutrients = f.getNutrients();

                        str += String.format("\t(%s,%s,%f,%s)\n",
                                f.getNdbno(), f.getName(), f.getWeight(), f.getMeasure());

                        for (Nutrient n : nutrients) {
                            str += String.format("\t\t(%s,%s,%s,%s,%f)\n",
                                    n.getNutrient_id(), n.getNutrient(), n.getUnit(), n.getValue(), n.getGm());
                        }
                    }

                    text.setText(str);
                }
            }

            @Override
            public void onFailure(Call<NutrientReportResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
