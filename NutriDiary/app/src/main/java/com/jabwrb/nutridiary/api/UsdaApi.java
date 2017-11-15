package com.jabwrb.nutridiary.api;

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

    @GET("nutrients/?format=JSON")
    Call<NutrientReportResponse> getNutrientReport(@Query("api_key") String api_key,
                                                   @Query("ndbno") String ndbno,
                                                   @Query("nutrients") String nutrient1,
                                                   @Query("nutrients") String nutrient2);

    @GET("search/?format=JSON")
    Call<SearchResponse> searchFood(@Query("api_key") String api_key,
                                    @Query("q") String q);
}
