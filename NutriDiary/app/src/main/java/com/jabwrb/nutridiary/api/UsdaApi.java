package com.jabwrb.nutridiary.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsdaApi {

    String BASE = "https://api.nal.usda.gov/ndb/";
    String API_KEY = "ktIgvwf5tYCjIL7sdYWiQ43In5R2o8CTUOK4LJIP";
    String ID_CALORIES = "208";
    String ID_FAT = "204";
    String ID_CARBOHYDRATES = "205";
    String ID_PROTEIN = "203";
    String ID_SATURATED_FAT = "606";
    String ID_CHOLESTEROL = "601";
    String ID_SODIUM = "307";
    String ID_DIETARY_FIBER = "291";
    String ID_SUGARS = "269";

    @GET("nutrients/?format=JSON" +
            "&api_key=" + API_KEY +
            "&nutrients=" + ID_CALORIES +
            "&nutrients=" + ID_FAT +
            "&nutrients=" + ID_CARBOHYDRATES +
            "&nutrients=" + ID_PROTEIN +
            "&nutrients=" + ID_SATURATED_FAT +
            "&nutrients=" + ID_CHOLESTEROL +
            "&nutrients=" + ID_SODIUM +
            "&nutrients=" + ID_DIETARY_FIBER +
            "&nutrients=" + ID_SUGARS)
    Call<NutrientReportResponse> getNutrientReport(@Query("ndbno") String ndbno);

    @GET("search/?format=JSON" +
            "&api_key=" + API_KEY +
            "&ds=Standard Reference" +
            "&sort=r" +
            "&max=50")
    Call<SearchResponse> searchFood(@Query("q") String q);
}
