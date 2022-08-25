package fr.maximob.awesomecdaweatherapp.models;

import android.annotation.SuppressLint;

import fr.maximob.awesomecdaweatherapp.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class City {

    public int mIdDataBase;

    public String mName;
    public String mDescription;
    public String mTemperature;
    public int mWeatherIcon;

    // api only
    public String mStringJson;
    public int mIdCity;
    public double mLatitude;
    public double mLongitude;
    public int mWeatherResIconWhite;
    public int mWeatherResIconGrey;

    public City() {
    }

    public City (String mName, String mDescription, String mTemperature, int mWeatherIcon) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mTemperature = mTemperature;
        this.mWeatherIcon = mWeatherIcon;
    }

    @SuppressLint("DefaultLocale")
    public City(String stringJson) throws JSONException {
        this.mStringJson = stringJson;
        JSONObject json = new JSONObject(stringJson);
        this.mName = json.getString("name");
        this.mDescription = json.getJSONArray("weather").getJSONObject(0).getString("description");
//        this.mTemperature = String.format("%.0f", json.getJSONObject("main").getDouble("temp") - 273.15) + " ℃";
        this.mTemperature = String.format("%.0f", json.getJSONObject("main").getDouble("temp")) + " ℃";
        this.mIdCity = json.getInt("id");
        this.mLatitude = json.getJSONObject("coord").getDouble("lat");
        this.mLongitude = json.getJSONObject("coord").getDouble("lon");
        this.mWeatherResIconWhite = Util.setWeatherIcon(
                json.getJSONArray("weather").getJSONObject(0).getInt("id"),
                json.getJSONObject("sys").getLong("sunrise") * 1000,
                json.getJSONObject("sys").getLong("sunset") * 1000
        );
        this.mWeatherResIconGrey = Util.setWeatherIcon(json.getJSONArray("weather").getJSONObject(0).getInt("id"));
    }
}

