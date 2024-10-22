package com.ismaelgr.domain.manager

import com.ismaelgr.domain.model.Statistic
import com.ismaelgr.domain.roundTo
import java.math.RoundingMode

fun List<Statistic>.SortedByPunctuation() = this.sortedWith(
    compareBy(
        { it.pn },
        { it.pd }
    )
)
    //.filter { it.pn in (-3.5 .. 7.5) }
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


/**
 * Las puntuaciones ganadoras, aparecen en varias ocasiones con nùmeros super parecidos prácticamente iguales.
 * Sería ideal ordenarlo de mayor a menor por el count() de agrupaciones que sean iguales. El número 5 puede tener de pns (2.04, 2.02, 2.06)
 * */