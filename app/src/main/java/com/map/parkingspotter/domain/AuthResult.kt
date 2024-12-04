package com.map.parkingspotter.domain

enum class Status {
    ERROR,
    OK
}

data class AuthResult(val user: User?, val status: Status) {
    fun isOk() = status == Status.OK
}