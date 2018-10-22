package com.karakaya.deniz.nerdeyesem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.karakaya.deniz.nerdeyesem.model.Search;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    RestInterface restInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restInterface = ApiClient.getClient().create(RestInterface.class);

        Call<Search> call = restInterface.getSearch("3fb383c9094715d5f7d994a43dbbefca", 38.457020, 27.196747, "real_distance", 5);
        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                Search search = new Search();
                search.setResultsFound(response.body().getResultsFound());
                search.setRestaurantList(response.body().getRestaurantList());

                for (int i = 0; i < search.getRestaurantList().size(); i++) {
                    System.out.println("RESTAURANT " + i + " = " + search.getRestaurantList()
                            .get(i).getName() + "  -  " + search.getRestaurantList().get(i).getLocation().getAddress());
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

            }
        });
    }
}
