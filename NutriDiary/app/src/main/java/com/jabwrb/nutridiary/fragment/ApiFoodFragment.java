package com.jabwrb.nutridiary.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.api.Food;
import com.jabwrb.nutridiary.api.Nutrient;
import com.jabwrb.nutridiary.api.NutrientReport;
import com.jabwrb.nutridiary.api.NutrientReportResponse;
import com.jabwrb.nutridiary.api.UsdaApi;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
                            nutrientReport.getSr(),nutrientReport.getGroups(),nutrientReport.getSubset(),
                            nutrientReport.getEnd(),nutrientReport.getStart(),nutrientReport.getTotal());

                    for (Food f : foods) {
                        List<Nutrient> nutrients = f.getNutrients();

                        str += String.format("\t(%s,%s,%f,%s)\n",
                                f.getNdbno(),f.getName(),f.getWeight(),f.getMeasure());

                        for (Nutrient n : nutrients) {
                            str += String.format("\t\t(%s,%s,%s,%s,%f)\n",
                                    n.getNutrient_id(),n.getNutrient(),n.getUnit(),n.getValue(),n.getGm());
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
