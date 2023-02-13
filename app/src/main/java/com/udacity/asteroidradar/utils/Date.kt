package com.udacity.asteroidradar.utils

import java.text.SimpleDateFormat
import java.util.*

fun getStartDate(): String {
    return formatDate(Calendar.getInstance().time)
}

fun getEndDate(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 7)
    return formatDate(calendar.time)
}

private fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(date)
}