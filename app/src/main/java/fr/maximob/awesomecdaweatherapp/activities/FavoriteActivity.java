package fr.maximob.awesomecdaweatherapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awesomecdaweatherapp.R;
import fr.maximob.awesomecdaweatherapp.adapters.FavoriteAdapter;
import com.example.awesomecdaweatherapp.databinding.ActivityFavoriteBinding;
import fr.maximob.awesomecdaweatherapp.models.City;
import fr.maximob.awesomecdaweatherapp.utils.Util;
import fr.maximob.awesomecdaweatherapp.utils.UtilAPI;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FavoriteActivity extends AppCompatActivity {

    private ActivityFavoriteBinding binding;
    private ArrayList<City> mCities;
    protected Context mContext;
    protected RecyclerView mRecyclerViewFavorite;
    protected FavoriteAdapter mAdapter;
    protected FloatingActionButton mFAB;
    protected OkHttpClient mOkHttpClient;
    protected Handler mHandler;
    //swipe
    protected int mPositionCityRemoved;
    protected City mCityRemoved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        mContext = this;
        mHandler = new Handler();

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mCities = UtilAPI.initFavoriteCities(mContext);

        mRecyclerViewFavorite = findViewById(R.id.recycler_view_favorite);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewFavorite.setLayoutManager(layoutManager);

        mAdapter = new FavoriteAdapter(mContext, mCities);
        mRecyclerViewFavorite.setAdapter(mAdapter);

        // swipe
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mPositionCityRemoved = viewHolder.getBindingAdapterPosition();
                mCityRemoved = mCities.remove(mPositionCityRemoved);
                UtilAPI.saveFavouriteCities(mContext, mCities);
                mAdapter.notifyDataSetChanged();
                Snackbar
                        .make(findViewById(R.id.coordinator_layout_favorite), mCityRemoved.mName + " est supprimé", Snackbar.LENGTH_LONG)
                        .setAction("Annuler", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mCities.add(mPositionCityRemoved, mCityRemoved);
                                UtilAPI.saveFavouriteCities(mContext, mCities);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerViewFavorite);


        mFAB = findViewById(R.id.fab);
        mFAB.setOnClickListener(view -> onClickFAB());
    }

    // callback de click sur le floating button. Crée un AlertDialog
    public void onClickFAB() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_favorite, null);
        final EditText editTextCity = (EditText) v.findViewById(R.id.edit_text_dialog_city);
        builder.setView(v);
        builder.setMessage(R.string.fab);
        builder.setPositiveButton(R.string.fabPositive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (editTextCity.getText().toString().length() > 0) {
                    updateWeatherDataCityName(editTextCity.getText().toString());
                }
            }
        });
        builder.setNegativeButton(R.string.fabNegative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();
    }

    public void updateWeatherDataCityName(final String cityName) {
        // call API
        if (Util.isActiveNetwork(mContext)) {
            mOkHttpClient = new OkHttpClient();
            String requestUrl = String.format(UtilAPI.OPEN_WEATHER_MAP_API_CITY_NAME, cityName.trim());
            Request request = new Request.Builder().url(requestUrl).build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, IOException e) {
                    Log.d("REQUEST ERR", "Failed to get OpenAPI test URL (" + requestUrl + ")");
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String stringJson = Objects.requireNonNull(response.body()).string();
                    if (response.isSuccessful() && UtilAPI.isSuccessful(stringJson)) {
                        Log.d("REQUEST SUCCESS", stringJson);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    City city = new City(stringJson);
                                    mCities.add(city);
                                    UtilAPI.saveFavouriteCities(mContext, mCities);
                                    mAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    Log.d("JSONException", e.toString());
//                                    Toast.makeText(mContext, "Erreur lors du chargement des données", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Log.d("REQUEST FAILED", stringJson);
//                        Toast.makeText(mContext, "Echec de la requête", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    protected void onStart() {
        super.onStart();
        Log.d("START", "FavoriteActivity: onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("RESTART", "FavoriteActivity: onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("RESUME", "FavoriteActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("PAUSE", "FavoriteActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("STOP", "FavoriteActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DESTROY", "FavoriteActivity: onDestroy()");
    }
}