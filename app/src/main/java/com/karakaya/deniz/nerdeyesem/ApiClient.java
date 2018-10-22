package com.karakaya.deniz.nerdeyesem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.karakaya.deniz.nerdeyesem.model.Restaurant;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static String Base_Url = "https://developers.zomato.com/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(Restaurant.class, new RestDeserializer<>(Restaurant.class, "restaurant"))
                            .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_Url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(new OkHttpClient())
                    .build();
            return retrofit;
        }
        return retrofit;
    }
}


