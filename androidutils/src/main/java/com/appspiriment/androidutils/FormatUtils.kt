package com.appspiriment.androidutils

import android.text.format.DateUtils
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/********************************************
 * Class for methods related actionTo Logging*
 *******************************************/
object FormatUtils {

    /*********************************************
     * Format Time in milliseconds to Time format
     *********************************************/
    fun Long.formatMillisToTime(): String {
        return try {
            String.format(
                Locale.ENGLISH, "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(this) -
                        TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(this)
                        ),
                TimeUnit.MILLISECONDS.toSeconds(this) -
                        TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(this)
                        )
            )
        } catch (e: java.lang.Exception) {
            return this.toString()
        }
    }


    /**********************************
     * Format an Integer to Kilo,
     * Mega, Giga format
     **********************************/
    fun Int.toKmgFormat(): String {
        return formatToKmgFormat(this)
    }

    fun Long.toKmgFormat(): String {
        return formatToKmgFormat(this)
    }

    private fun formatToKmgFormat(num: Number): String {
        val formatter = NumberFormat.getNumberInstance()
        formatter.maximumFractionDigits = 1
        val kmgMap = mapOf<Long, String>(1000000000L to "G", 1000000L to "M", 1000L to "K")
        kmgMap.keys.forEach {
            if (num.toLong() >= it) {
                val numQnt = num.toFloat() / it
                return formatter.format(numQnt.toDouble()) + kmgMap[it]
            }
        }

        return num.toString()
    }


    /**********************************
     * Format Date To RelativeTimeSpan
     **********************************/
    fun String.toRelativeDateString(
        span: Long = DateUtils.DAY_IN_MILLIS,
        maxSpan: Int = 1,
        inputFormat: String = "yyyy-MM-dd",
        outputFormat: String = "dd MMM yyyy"
    ): String {
        return try {
            val date1 =
                SimpleDateFormat(inputFormat, Locale.ENGLISH).parse(this)
            val now = System.currentTimeMillis()
            val diffInMillisec: Long = now - date1.getTime()

            val diffInDays: Long = TimeUnit.MILLISECONDS.toDays(diffInMillisec)
            if (diffInDays > maxSpan)
                SimpleDateFormat(outputFormat, Locale.ENGLISH).format(date1)
            else
                DateUtils.getRelativeTimeSpanString(date1.getTime(), now, span).toString();
        } catch (e: Exception) {
            this
        }
    }
}