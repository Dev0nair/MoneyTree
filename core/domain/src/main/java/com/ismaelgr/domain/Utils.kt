package com.ismaelgr.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.getDateFormated(): String {
    return SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(this)
}

fun String.getFormatedToDate(): Date {
    return SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(this)
        ?: Date()
}

fun String.getNextDay(n: Int): String {
    return Calendar.getInstance().apply { time = getFormatedToDate(); addDays(n) }.time.getDateFormated()
}

fun getDateString(year: String, month: String, day: String): String = "$year$month$day"

fun getDay0(year: String = "2020") = "${year}0101"

fun getTodayString(): String = Date().getDateFormated()

fun Calendar.addDays(numDays: Int) {
    add(Calendar.DAY_OF_YEAR, numDays)
}

// Para el futuro. Desde this hasta until + 1
suspend fun String.iterateUntilDay(until: String, untilExtra: Int = 1, action: suspend (Date) -> Unit) {
    val currentDate = this.getFormatedToDate()
    val currentCal = Calendar.getInstance().apply { time = currentDate }
    val untilCal = Calendar.getInstance().apply { time = until.getFormatedToDate() }
    untilCal.addDays(untilExtra)
    
    while (currentCal.time.before(untilCal.time)) {
        action(currentCal.time)
        currentCal.addDays(1)
    }
}

fun String.iterateUntilDay(until: String, untilExtra: Int = 1): List<Date> {
    val currentDate = this.getFormatedToDate()
    val currentCal = Calendar.getInstance().apply { time = currentDate }
    val untilCal = Calendar.getInstance().apply { time = until.getFormatedToDate() }
    untilCal.addDays(untilExtra)
    val result = mutableListOf<Date>()
    
    while (currentCal.time.before(untilCal.time)) {
        result.add(currentCal.time)
        currentCal.addDays(1)
    }
    
    return result
}

fun String.before(date: String): Boolean {
    return this.getFormatedToDate().before(date.getFormatedToDate())
}

// Desde el pasado. Desde from hasta this + untilExtra
suspend fun String.iterateFromDay(from: String, untilExtra: Int = 1, action: suspend (Date) -> Unit) {
    val currentCal = Calendar.getInstance().apply { time = from.getFormatedToDate() }
    val untilCal = Calendar.getInstance().apply { time = this@iterateFromDay.getFormatedToDate() }
    untilCal.addDays(untilExtra)
    
    while (currentCal.time.before(untilCal.time)) {
        action(currentCal.time)
        currentCal.addDays(1)
    }
}

fun median(numbers: List<Int>): Double {
    val sorted = numbers.sorted()
    val result = if (sorted.size > 2) {
        sorted.slice(1..<sorted.size).average()
    } else {
        sorted.average()
    }
    
    return if (result.isNaN()) {
        0.0
    } else {
        result
    }
}

fun Double.roundTo(decimals: Int = 2, roundingMode: RoundingMode = RoundingMode.DOWN): Double {
    return BigDecimal(this).setScale(decimals, roundingMode).toDouble()
}

fun Int.toPrize(): Int = when (this) {
    3 -> 4
    4 -> 24
    5 -> 1000
    6 -> 100000
    else -> 0
}