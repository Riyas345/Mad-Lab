package com.example.currentlocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    Button getLocationButton;
    Button openMapButton;
    TextView locationText;

    FusedLocationProviderClient fusedLocationClient;

    double latitude = 0.0;
    double longitude = 0.0;

    boolean locationAvailable = false;

    private static final int LOCATION_PERMISSION_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // Connect UI components

        getLocationButton =
                findViewById(R.id.getLocationButton);

        openMapButton =
                findViewById(R.id.openMapButton);

        locationText =
                findViewById(R.id.locationText);


        // Initialize location service

        fusedLocationClient =
                LocationServices
                        .getFusedLocationProviderClient(this);


        // Get Current Location button

        getLocationButton.setOnClickListener(v -> {

            getCurrentLocation();

        });


        // Open in Map button

        openMapButton.setOnClickListener(v -> {

            openMap();

        });
    }


    // =====================================================
    // GET CURRENT LOCATION
    // =====================================================

    private void getCurrentLocation() {

        // Check permission

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {


            // Request permission

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_CODE
            );

            return;
        }


        // Get last known location

        fusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(
                        this,
                        location -> {

                            if (location != null) {

                                latitude =
                                        location.getLatitude();

                                longitude =
                                        location.getLongitude();

                                locationAvailable = true;


                                locationText.setText(
                                        "Latitude: "
                                                + latitude
                                                + "\n\nLongitude: "
                                                + longitude
                                );


                                Toast.makeText(
                                        this,
                                        "Current location obtained",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                locationText.setText(
                                        "Unable to get current location"
                                );

                                Toast.makeText(
                                        this,
                                        "Location not available",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );
    }


    // =====================================================
    // OPEN LOCATION IN MAP
    // =====================================================

    private void openMap() {

        if (!locationAvailable) {

            Toast.makeText(
                    this,
                    "First get the current location",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Create geo URI

        Uri locationUri = Uri.parse(
                "geo:"
                        + latitude
                        + ","
                        + longitude
                        + "?q="
                        + latitude
                        + ","
                        + longitude
        );


        // Create map intent

        Intent mapIntent =
                new Intent(
                        Intent.ACTION_VIEW,
                        locationUri
                );


        // Prefer Google Maps

        mapIntent.setPackage(
                "com.google.android.apps.maps"
        );


        // Check whether Google Maps is installed

        if (mapIntent.resolveActivity(
                getPackageManager()
        ) != null) {

            startActivity(mapIntent);

        } else {

            // Open with any available map application

            Intent genericMapIntent =
                    new Intent(
                            Intent.ACTION_VIEW,
                            locationUri
                    );


            if (genericMapIntent.resolveActivity(
                    getPackageManager()
            ) != null) {

                startActivity(genericMapIntent);

            } else {

                Toast.makeText(
                        this,
                        "No map application found",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }


    // =====================================================
    // PERMISSION RESULT
    // =====================================================

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );


        if (requestCode ==
                LOCATION_PERMISSION_CODE) {

            if (grantResults.length > 0
                    &&
                    (grantResults[0]
                            == PackageManager.PERMISSION_GRANTED
                            ||
                            (grantResults.length > 1
                                    &&
                                    grantResults[1]
                                            == PackageManager.PERMISSION_GRANTED))) {

                getCurrentLocation();

            } else {

                Toast.makeText(
                        this,
                        "Location permission denied",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}