package com.karakaya.deniz.nerdeyesem;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.karakaya.deniz.nerdeyesem.model.Search;
import com.karakaya.deniz.nerdeyesem.retrofit.ApiClient;
import com.karakaya.deniz.nerdeyesem.retrofit.RestInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RestInterface restInterface;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private LocationManager locationManager;
    private LocationListener locationListener;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restInterface = ApiClient.getClient().create(RestInterface.class);

        swipeRefreshLayout = findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color
                .refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        if (isOnline()) {
                            if (isGpsEnabled()) {
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                                        PackageManager.PERMISSION_GRANTED) {
                                    locationManager.requestLocationUpdates(LocationManager
                                            .GPS_PROVIDER, 0, 0, locationListener);
                                    fetchData(locationManager.getLastKnownLocation
                                            (LocationManager.GPS_PROVIDER));
                                }
                            }
                        }
                    }
                }, 3000);
            }
        });

        //get location
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .ACCESS_FINE_LOCATION}, 1);
        } else {
            if (isOnline())
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                        locationListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        // Make sure that internet connection is enabled on the device
//        if (isOnline()) {
//            // after that make sure that GPS is enabled on the device
//            isGpsEnabled();
//        }
    }

    public boolean isGpsEnabled() {
        final LocationManager locationManager = (LocationManager) getSystemService(Context
                .LOCATION_SERVICE);
        boolean isEnabled;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            String message = "GPS";
            buildAlertMessage(message);
            return false;

        }
        return true;
    }

    public boolean isOnline() {
        boolean isOnline;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        String message = "internet connection";
        buildAlertMessage(message);
        return false;
    }

    private void buildAlertMessage(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your " + message + " seems to be disabled, You have to enable it to " +
                "continue to use application.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        if (message.equals("internet connection")) {
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        } else {
                            startActivity(new Intent(android.provider.Settings
                                    .ACTION_LOCATION_SOURCE_SETTINGS));
                        }

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                        locationListener);
            }
        } else {
            moveTaskToBack(true);
            finish();
        }

    }

    private void fetchData(Location location) {
        compositeDisposable.add(restInterface.getSearch("3fb383c9094715d5f7d994a43dbbefca",
                location.getLatitude(), location.getLongitude(), "real_distance", 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Search>() {
                    @Override
                    public void accept(Search search) throws Exception {
                        for (int i = 0; i < search.getRestaurantList().size(); i++) {
                            System.out.println("RESTAURANT " + i + " = " + search
                                    .getRestaurantList()
                                    .get(i).getName() + "  -  " + search.getRestaurantList().get
                                    (i).getLocation().getAddress());
                        }
                    }
                })
        );
    }

    @Override
    public void onClick(View v) {

    }
}
