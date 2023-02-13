package com.udacity.asteroidradar.utils

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.ui.main.NasaApiStatus
import com.udacity.asteroidradar.data.models.PictureOfDay

@BindingAdapter(value = ["bind:asteroidName","bind:asteroidStatus"], requireAll = false)
fun bindTextViewToAsteroidsName(textView: TextView, name: String, isHazardous: Boolean) {
    textView.text = name
    if (isHazardous){
        textView.setTextColor(Color.RED)
    }else{
        textView.setTextColor(Color.WHITE)
    }
}

@BindingAdapter("pictureOfTheDay")
fun bindPictureOfTheDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    val context = imageView.context
    if (pictureOfDay != null && pictureOfDay.url.isNotBlank()) {
        Picasso.with(context)
            .load(pictureOfDay.url)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .error(R.drawable.ic_baseline_photo_24)
            .fit()
            .centerCrop()
            .into(imageView)

        imageView.contentDescription =
            String.format(context.getString(R.string.nasa_picture_of_day_content_description_format), pictureOfDay.title)
    } else {
        imageView.setImageResource(R.drawable.ic_baseline_photo_24)
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imageView.contentDescription =
            context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    }
}

@BindingAdapter("nasaApiStatus")
fun bindStatusProgressBar(statusProgressBar: ProgressBar, status: NasaApiStatus) {
    when (status) {
        NasaApiStatus.LOADING -> {
            statusProgressBar.visibility = View.VISIBLE
        }
        NasaApiStatus.ERROR -> {
            statusProgressBar.visibility = View.GONE
        }
        NasaApiStatus.DONE -> {
            statusProgressBar.visibility = View.GONE
        }
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription =
            context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription =
            context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
