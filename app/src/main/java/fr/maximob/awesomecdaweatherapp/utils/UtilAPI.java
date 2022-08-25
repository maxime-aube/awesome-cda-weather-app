package fr.maximob.awesomecdaweatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import fr.maximob.awesomecdaweatherapp.models.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UtilAPI {

    public static final String OPEN_WEATHER_MAP_API_TOKEN = "a77a7daa70162f8c5201726bc4dc622b";
    public static final String OPEN_WEATHER_MAP_API_COORDINATES = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&lang=fr&appid=" + OPEN_WEATHER_MAP_API_TOKEN;
    public static final String OPEN_WEATHER_MAP_API_CITY_NAME = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=fr&appid=" + OPEN_WEATHER_MAP_API_TOKEN;
    public static final String OPEN_WEATHER_MAP_API_FAVOURITE = "https://api.openweathermap.org/data/2.5/group?id=%s&units=metric&lang=fr&appid=" + OPEN_WEATHER_MAP_API_TOKEN;
    public static final String OPEN_WEATHER_MAP_API_FORECAST = "https://api.openweathermap.org/data/2.5/forecast/daily?id=%s&units=metric&cnt=5&lang=fr&appid=" + OPEN_WEATHER_MAP_API_TOKEN;

    public static boolean isSuccessful(String stringJson) {

        try {
            JSONObject json = new JSONObject(stringJson);

            int cod = json.getInt("cod");
            if (cod != 200)
                return false;

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean isSuccessForecast(String stringJson) {

        try {
            JSONObject json = new JSONObject(stringJson);

            int cod = json.getInt("cnt");
            if (cod == 0)
                return false;

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    // sauvegarde les villes en favori dans les sharedPreferences
    public static void saveFavouriteCities(Context context, ArrayList<City> cities) {
        JSONArray jsonArrayCities = new JSONArray();
        for (int i = 0; i < cities.size(); i++) {
            jsonArrayCities.put(cities.get(i).mStringJson);
        }
        SharedPreferences preferences = context.getSharedPreferences(Util.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Util.PREFS_FAVORITE_CITIES, jsonArrayCities.toString());
        editor.apply();
    }

    // génère un ArrayList<City> à partir des villes sauvagardées (format json string) en tant que favoris dans les SharedPreferences
    public static ArrayList<City> initFavoriteCities(Context context) {
        ArrayList<City> cities = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences(Util.PREFS_NAME, Context.MODE_PRIVATE);

        try {
            JSONArray jsonArray = new JSONArray(preferences.getString(Util.PREFS_FAVORITE_CITIES, ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCity = new JSONObject(jsonArray.getString(i));
                City city = new City(jsonObjectCity.toString());
                cities.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cities;
    }
}
