package com.example.auarai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    CircleIndicator3 indicator;
    ImageButton add;
    RecyclerView cities;
    WeatherCityAdapter cityAdapter;
    WeatherFragmentAdapter viewPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewPager2);
        indicator = findViewById(R.id.indicator);
        add = findViewById(R.id.add);

        viewPagerAdapter = new WeatherFragmentAdapter(this);

        viewPager2.setAdapter(viewPagerAdapter);
        indicator.setViewPager(viewPager2);

        // optional
        viewPagerAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            viewPagerAdapter.addFragment(new LocationFragment());
        }

        add.setOnClickListener(v -> {
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.add_city, null);

            ImageButton back = dialogView.findViewById(R.id.back);
            EditText search = dialogView.findViewById(R.id.search);
            Button addCity = dialogView.findViewById(R.id.confirm);
            cities = dialogView.findViewById(R.id.cities);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(dialogView);

            AlertDialog dialog = builder.create();
            dialog.show();

            cityAdapter = new WeatherCityAdapter(new ClickListener() {
                @Override
                public void onClickItem(CityItemData city) {
                    dialog.dismiss();
                    viewPagerAdapter.addFragment(new CityFragment(city.getName()));
                    viewPager2.setCurrentItem(viewPagerAdapter.getLastItemIndex());
                }
            });
            cities.setAdapter(cityAdapter);

            back.setOnClickListener(v1 -> dialog.dismiss());

            addCity.setOnClickListener(v2 ->
                    searchByCity(search.getText().toString(), cities));
        });
    }

    private void searchByCity(String cityName, RecyclerView cities) {
        String url = "https://api.openweathermap.org/";
        String key = "af302ceb9625a4d5d1703fd2143fe0f8";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherSearchAPI weatherSearchApi = retrofit.create(WeatherSearchAPI.class);

        Call<ArrayList<CityItemData>> call = weatherSearchApi.getData(cityName, 5, key);

        call.enqueue(new Callback<ArrayList<CityItemData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CityItemData>> call, @NonNull Response<ArrayList<CityItemData>> response) {
                ArrayList<CityItemData> arr = response.body();
                System.out.println("Haahalalaa");
                cityAdapter.updateArrayList(arr);
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<CityItemData>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to get weather data city", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class WeatherCityAdapter extends RecyclerView.Adapter<WeatherCityAdapter.CityViewHolder> {
    private ArrayList<CityItemData> cityList = new ArrayList<>();
    private final ClickListener clickListener;

    public WeatherCityAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateArrayList(ArrayList<CityItemData> cityList) {
        this.cityList = cityList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        CityItemData city = cityList.get(position);
        holder.bind(city);
        holder.itemView.setOnClickListener(v -> {
            clickListener.onClickItem(city);
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameTextView;
        TextView stateTextView;
        TextView countryTextView;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityNameTextView = itemView.findViewById(R.id.name);
            stateTextView = itemView.findViewById(R.id.state);
            countryTextView = itemView.findViewById(R.id.country);
        }

        public void bind(CityItemData city) {
            cityNameTextView.setText(city.getName());
            countryTextView.setText(city.getCountry());
            stateTextView.setText(city.getState());
        }
    }
}