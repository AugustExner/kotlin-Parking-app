package com.map.parkingspotter.domain

data class UserData(val filter: String = "",
                    val theme: String = "",
                    val displayName: String = "",
                    val homeAddress: String = "")