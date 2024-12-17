package com.ismaelgr.domain.manager

import com.ismaelgr.domain.model.NumberData
import com.ismaelgr.domain.model.Statistic
import com.ismaelgr.domain.roundTo
import java.math.RoundingMode

fun List<Statistic>.SortedByPunctuation() = this.sortedWith(
    compareBy(
        { it.pn },
        { it.pd }
    )
)
    .let { sortedList ->
        val numbers = sortedList.groupBy { it.number }
            .mapValues { values ->
                values.value.map { value ->
                    value.pn.roundTo(
                        decimals = 0,
                        roundingMode = RoundingMode.UP
                    )
                }
            }
            .filter { item ->
                val distinctNumbers = item.value.distinct().size
                item.value.size > distinctNumbers && distinctNumbers < 2
            }

        sortedList.sortedByDescending { statistic -> statistic.number in numbers.map { it.key } }
    }


fun List<Statistic>.statiticWithUsuals(usualPNs: List<Int>) =
    this.sortedBy { usualPNs.indexOf(it.pn.toInt()) }

fun List<NumberData>.numberDataWithUsuals(usualPNs: List<Int>) =
    this.sortedBy { usualPNs.indexOf(it.punctuation.toInt()) }
/**
 * Las puntuaciones ganadoras, aparecen en varias ocasiones con nùmeros super parecidos prácticamente iguales.
 * Sería ideal ordenarlo de mayor a menor por el count() de agrupaciones que sean iguales. El número 5 puede tener de pns (2.04, 2.02, 2.06)
 * */