package com.ismaelgr.data.retrofit.dao

data class SimpleBonoloto(
    val date: String,
    val combination: String
) {

    fun getListNumbers(): List<Int> {
        return if (combination.contains("C")) {
            combination
                .split("C")[0]
                .split("-")
                .mapNotNull { it.trim().toIntOrNull() }
        } else {
            combination.split("-")
                .mapNotNull { it.trim().toIntOrNull() }
        }
    }

    fun containsNumber(number: Int): Boolean {
        return getListNumbers().contains(number)
    }
}