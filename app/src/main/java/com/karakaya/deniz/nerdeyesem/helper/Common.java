package com.karakaya.deniz.nerdeyesem.helper;

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
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import static android.content.Context.LOCATION_SERVICE;

public class Common {

    private LocationManager locationManager;
    private LocationListener locationListener;

    public Location getLocation(Context context) {
        //get location
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
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

        if (ActivityCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            if (isOnline(context)) {
                if (isGpsEnabled(context)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                            locationListener);

                    return locationManager.getLastKnownLocation
                            (LocationManager.GPS_PROVIDER);
                }
            }
        }
        return null;

    }

    private boolean isGpsEnabled(Context context) {
        final LocationManager locationManager = (LocationManager) context.getSystemService
                (LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            String message = "GPS";
            buildAlertMessage(message, context);
            return false;

        }
        return true;
    }

    private boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
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
        buildAlertMessage(message, context);
        return false;
    }

    private void buildAlertMessage(String message, Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your " + message + " seems to be disabled, You have to enable it to " +
                "continue to use application.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                    if (message.equals("internet connection")) {
                        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    } else {
                        context.startActivity(new Intent(Settings
                                .ACTION_LOCATION_SOURCE_SETTINGS));
                    }

                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
