package fr.maximob.awesomecdaweatherapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Button
import android.os.Bundle
import android.net.ConnectivityManager
import com.example.awesomecdaweatherapp.R
import java.io.IOException
import android.util.Log
import org.json.JSONException
import android.view.View
import android.widget.EditText
import android.content.Intent
import android.os.Handler
import fr.maximob.awesomecdaweatherapp.models.City
import okhttp3.*
import java.util.*

class MainActivity : AppCompatActivity() {

    protected var mTextViewCityName: TextView? = null
    protected var mTextViewCityDesc: TextView? = null
    protected var mTextViewCityTemp: TextView? = null
    protected var mImageViewCity: ImageView? = null
    protected var mLinearLayoutCity: LinearLayout? = null
    protected var mButtonFavorite: Button? = null
    protected var mTextViewNoNetwork: TextView? = null
    protected var mOkHttpClient: OkHttpClient? = null
    protected var openAPItestUrl = "https://api.openweathermap.org/data/2.5/weather?q=Tours&units=metric&lang=fr&appid=a77a7daa70162f8c5201726bc4dc622b"
    protected var mHandler: Handler? = null
    protected var mCurrentCity: City? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val connMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        setContentView(R.layout.activity_main)
        mHandler = Handler()

        // si connecté à internet
        if (networkInfo != null && networkInfo.isConnected) {
            mOkHttpClient = OkHttpClient()
            val request: Request = Request.Builder().url(openAPItestUrl).build()
            mOkHttpClient!!.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("REQUEST ERR", "Failed to get OpenAPI test URL ($openAPItestUrl)")
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val stringJson = Objects.requireNonNull(response.body)?.string()
                    if (response.isSuccessful) {
                        stringJson?.let { Log.d("REQUEST SUCCESS", it) }
                        mHandler!!.post {
                            try {
                                renderCurrentWeather(stringJson)
                            } catch (e: JSONException) {
                                Log.d("JSONException", e.toString())
                            }
                        }
                    } else {
                        stringJson?.let { Log.d("REQUEST FAILED", it) }
                    }
                }
            })
            mLinearLayoutCity = findViewById<View>(R.id.linear_layout_city) as LinearLayout
            mLinearLayoutCity!!.visibility = View.VISIBLE
            mTextViewCityName = findViewById<View>(R.id.text_view_city_name) as TextView
            mTextViewCityName!!.setText(R.string.city_name)
            mTextViewCityDesc = findViewById<View>(R.id.text_view_city_desc) as TextView
            mTextViewCityTemp = findViewById<View>(R.id.text_view_city_temperature) as TextView
            mImageViewCity = findViewById<View>(R.id.image_view_city_weather) as ImageView
            val mEditTextSearch = findViewById<EditText>(R.id.edit_text_search)
            //            mEditTextSearch.()
            // todo => plus tard si refus de position GPS
            mButtonFavorite = findViewById<View>(R.id.button1) as Button
            mButtonFavorite!!.visibility = View.VISIBLE
            mButtonFavorite!!.setOnClickListener { view: View? -> onClickButtonFavorite() }
        } else {
            // pas connecté à internet
            Log.d("NETWORK", "pas de connexion internet")
            mTextViewNoNetwork = findViewById<View>(R.id.text_view_no_network) as TextView
            mTextViewNoNetwork!!.visibility = View.VISIBLE
        }
    }

    // exécuté dans le thread principal après la réponse de l'API
    @Throws(JSONException::class)
    fun renderCurrentWeather(json: String?) {
        mCurrentCity = City(json)
        mTextViewCityName?.setText(mCurrentCity!!.mName)
        mTextViewCityDesc?.setText(mCurrentCity!!.mDescription)
        mTextViewCityTemp?.setText(mCurrentCity!!.mTemperature)
        mImageViewCity?.setImageResource(mCurrentCity!!.mWeatherResIconWhite)
    }

    // callback de l'event listener sur le bouton favori
    fun onClickButtonFavorite() {
        val intent = Intent(this, FavoriteActivity::class.java)
        val mEditTextSearch = findViewById<EditText>(R.id.edit_text_search)
        intent.putExtra("search", mEditTextSearch.text.toString())
        Log.d("+++++( Main )+++++=", mEditTextSearch.text.toString())
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        Log.d("START", "MainActivity: onStart()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("RESTART", "MainActivity: onRestart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("RESUME", "MainActivity: onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("PAUSE", "MainActivity: onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("STOP", "MainActivity: onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DESTROY", "MainActivity: onDestroy()")
    }
}