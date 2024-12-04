package com.map.parkingspotter.domain

data class Name(val value: String) {

    init {
        if (!validate(value)) {
            throw IllegalArgumentException("ILLEGAL_NAME")
        }
    }

    companion object {
        fun validate(name: String): Boolean {
            return name.length in (2..30) && name.matches(Regex("^[a-zA-Z]+(\\s[a-zA-Z]+)*$"))
        }
    }
}