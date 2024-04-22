package com.example.auarai;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationFragment extends Fragment {
    TextView name, description, dt, temp, temp_min_max, feels_like, pressure, wind, humidity;
    ImageView weather_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);
        weather_image = view.findViewById(R.id.weather_image);
        dt = view.findViewById(R.id.dt);
        temp = view.findViewById(R.id.temp);
        temp_min_max = view.findViewById(R.id.temp_min_max);
        feels_like = view.findViewById(R.id.feels_like);
        pressure = view.findViewById(R.id.pressure);
        wind = view.findViewById(R.id.wind);
        humidity = view.findViewById(R.id.humidity);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            Toast.makeText(requireContext(), location.getLatitude() +" "+ location.getLongitude(), Toast.LENGTH_SHORT).show();
                            getWeatherData(location.getLatitude(), location.getLongitude());

                        }
                    });
        }
        return view;
    }


    private void getWeatherData(double latitude, double longitude) {
        String url = "https://api.openweathermap.org/";
        String key = "af302ceb9625a4d5d1703fd2143fe0f8";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherLocationAPI weatherLocationApi = retrofit.create(WeatherLocationAPI.class);

        Call<WeatherLocationData> call = weatherLocationApi.getData(latitude, longitude, "metric", key);

        call.enqueue(new Callback<WeatherLocationData>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<WeatherLocationData> call, @NonNull Response<WeatherLocationData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherLocationData weather = response.body();

                    String icon = weather.getWeather().get(0).getIcon();
                    if (icon.charAt(2) == 'd') {
                        switch (icon) {
                            case "01d":
                                weather_image.setImageResource(R.drawable.sun);
                                break;
                            case "02d":
                                weather_image.setImageResource(R.drawable.sun_cloud);
                                break;
                            case "03d":
                                weather_image.setImageResource(R.drawable.sun_clouds);
                                break;
                            case "04d":
                                weather_image.setImageResource(R.drawable.cloud);
                                break;
                            case "09d":
                                weather_image.setImageResource(R.drawable.rain1);
                                break;
                            case "10d":
                                weather_image.setImageResource(R.drawable.rain2);
                                break;
                            case "11d":
                                weather_image.setImageResource(R.drawable.lightning);
                                break;
                            case "13d":
                                weather_image.setImageResource(R.drawable.sun_snow);
                                break;
                            case "50d":
                                weather_image.setImageResource(R.drawable.mist);
                                break;
                        }
                    } else {
                        switch (icon) {
                            case "01n":
                                weather_image.setImageResource(R.drawable.moon);
                                break;
                            case "02n":
                                weather_image.setImageResource(R.drawable.moon_cloud);
                                break;
                            case "03n":
                                weather_image.setImageResource(R.drawable.moon_clouds);
                                break;
                            case "04n":
                                weather_image.setImageResource(R.drawable.cloud);
                                break;
                            case "09n":
                                weather_image.setImageResource(R.drawable.rain1);
                                break;
                            case "10n":
                                weather_image.setImageResource(R.drawable.rain_moon);
                                break;
                            case "11n":
                                weather_image.setImageResource(R.drawable.lightning);
                                break;
                            case "13n":
                                weather_image.setImageResource(R.drawable.moon_snow);
                                break;
                            case "50n":
                                weather_image.setImageResource(R.drawable.mist);
                                break;
                        }
                    }


                    name.setText(weather.getName());
                    description.setText(weather.getWeather().get(0).getDescription());
                    dt.setText(weather.getDt());
                    temp.setText(" "+weather.getMain().getTemp());
                    temp_min_max.setText("mx:" + weather.getMain().getTemp_max() + ", mn:" + weather.getMain().getTemp_min());
                    feels_like.setText(weather.getMain().getFeels_like());
                    pressure.setText(weather.getMain().getPressure());
                    wind.setText(weather.getWind().getSpeed());
                    humidity.setText(weather.getMain().getHumidity());
                } else {
                    Toast.makeText(requireContext(), "Failed to get weather data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherLocationData> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Failed to get weather data location", Toast.LENGTH_SHORT).show();
            }
        });
    }


}