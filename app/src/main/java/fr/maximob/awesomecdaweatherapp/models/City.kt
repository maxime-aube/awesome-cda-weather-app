package fr.maximob.awesomecdaweatherapp.models

/**
 * this will generate class
 * If the primary constructor does not have any annotations or visibility modifiers, the constructor keyword can be omitted
 */
class City (
        val mIdDataBase: String?,
        val mName: String,
        val mDescription: String,
        val mTemperature: String,
        val mWeatherIcon: Int,

        // api only
        val mStringJson: String,
        val mIdCity: Int,
        val mLatitude: Double,
        val mLongitude: Double,
        val mWeatherResIconWhite: Int,
        val mWeatherResIconGrey: Int
) {
}