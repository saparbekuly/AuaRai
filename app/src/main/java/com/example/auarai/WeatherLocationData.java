package com.example.auarai;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherLocationData {
    ArrayList<Weather> weather;
    Main main;
    Wind wind;
    long dt;
    String name;


    public String getName() {
        return name;
    }
    public ArrayList<Weather> getWeather() {
        return weather;
    }
    public Main getMain() {
        return main;
    }
    public Wind getWind() {
        return wind;
    }
    public String getDt() {
        Date time = new Date(dt*1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMMM dd");
        return dateFormat.format(time);
    }
}

class Weather {
    String description;
    String icon;

    public String getDescription() {
        return description;
    }
    public String getIcon() {
        return icon;
    }
}

class Main {
    double temp;
    double feels_like;
    double temp_min;
    double temp_max;
    int pressure;
    int humidity;

    public String getTemp() {
        return fix(temp);
    }

    public String getTemp_max() {
        return fix(temp_max);
    }

    public String getTemp_min() {
        return fix(temp_min);
    }

    public String getFeels_like() {
        return fix(feels_like);
    }

    public String getPressure() {
        return pressure+"hPa";
    }

    public String getHumidity() {
        return humidity+"%";
    }
    public String fix(double t) {
        int tempInt;
        if (t - Math.floor(t) < 0.5) {
            tempInt = (int) Math.floor(t);
        } else {
            tempInt = (int) Math.ceil(t);
        }
        return tempInt+"°";
    }
}


class Wind {
    double speed;

    public String getSpeed() {
        if (speed - Math.floor(speed) < 0.5) {
            return (int) Math.floor(speed)+"m/s";
        } else {
            return (int) Math.ceil(speed)+"m/s";
        }
    }
}
