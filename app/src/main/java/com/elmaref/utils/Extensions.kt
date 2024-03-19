package com.elmaref.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.elmaref.ui.quran.paged.functions.toArabicNumber
import java.text.SimpleDateFormat
import java.util.Locale

fun String.changeFormat(): String {
    // just change to arabic
    return this.replace("left until", "حَتَّى").replace("Hour", "سَاعَةٌ و").replace("Minute", "دَقِيقَةٌ").toArabicNumber().toString()
}

fun Any?.removeZeroFromLeft(): String? {
    return this.toString().replaceFirst("^٠+(?!$)".toRegex(), "")
}
fun String?.convertTo12HourFormat(): String {
    val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val date = inputFormat.parse(this ?: "00:00")
    // remove am and pm
    return outputFormat.format(date).replace("am", "").replace("pm", "")
}
@BindingAdapter("app:setImageResource")
fun setImageResource(image: ImageView, imageResource: Int) {
    image.setImageResource(imageResource)
}
