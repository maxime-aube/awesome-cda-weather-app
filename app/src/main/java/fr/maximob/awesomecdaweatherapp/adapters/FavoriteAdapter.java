package fr.maximob.awesomecdaweatherapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awesomecdaweatherapp.R;
import fr.maximob.awesomecdaweatherapp.models.City;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<City> mCities;

    public FavoriteAdapter(Context mContext, ArrayList<City> mCities) {
        this.mContext = mContext;
        this.mCities = mCities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_favorite_city, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = this.mCities.get(position);
        holder.mTextViewCityName.setText(city.mName);
        holder.mTextViewCityDesc.setText(city.mDescription);
        holder.mTextViewCityTemp.setText(city.mTemperature);
        holder.mTextViewCityWeatherIcon.setImageResource(city.mWeatherIcon);
        holder.mCtity = city;
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    // Classe holder qui contient la vue d’un item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        public TextView mTextViewCityName;
        public TextView mTextViewCityDesc;
        public TextView mTextViewCityTemp;
        public ImageView mTextViewCityWeatherIcon;
        public City mCtity;

        public ViewHolder(View view) {
            super(view);
            view.setTag(this);
            mTextViewCityName = (TextView) view.findViewById(R.id.text_view_item_city_name);
            mTextViewCityDesc = (TextView) view.findViewById(R.id.text_view_item_city_desc);
            mTextViewCityTemp = (TextView) view.findViewById(R.id.text_view_item_city_temperature);
            mTextViewCityWeatherIcon = (ImageView) view.findViewById(R.id.image_view_item_city_weather);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("CLICK", "long click sur fav city item");
            ViewHolder holder = (ViewHolder) v.getTag();
            final int position = holder.getBindingAdapterPosition();

    // plus besoin du clic long, on a mis un swipe
//            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//            builder.setMessage("Supprimer " + holder.mTextViewCityName.getText().toString() + " ?");
//            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    mCities.remove(position);
//                    notifyDataSetChanged();
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, mCities.size());
//
//                }
//            });
//
//            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                }
//            });
//
//            builder.create().show();

            return false;
        }
    }
}