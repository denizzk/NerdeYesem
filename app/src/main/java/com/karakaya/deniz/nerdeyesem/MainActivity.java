package com.karakaya.deniz.nerdeyesem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.karakaya.deniz.nerdeyesem.model.Search;
import com.karakaya.deniz.nerdeyesem.retrofit.ApiClient;
import com.karakaya.deniz.nerdeyesem.retrofit.RestInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    RestInterface restInterface;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restInterface = ApiClient.getClient().create(RestInterface.class);
        fetchData();
    }

    private void fetchData() {
        compositeDisposable.add(restInterface.getSearch("3fb383c9094715d5f7d994a43dbbefca", 38.457020, 27.196747, "real_distance", 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Search>() {
                    @Override
                    public void accept(Search search) throws Exception {
                        for (int i = 0; i < search.getRestaurantList().size(); i++) {
                            System.out.println("RESTAURANT " + i + " = " + search.getRestaurantList()
                                    .get(i).getName() + "  -  " + search.getRestaurantList().get(i).getLocation().getAddress());
                        }
                    }
                })
        );
    }
}
