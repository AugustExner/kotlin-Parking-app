package com.map.parkingspotter.ui.screen.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.map.parkingspotter.domain.Email
import com.map.parkingspotter.domain.Password

@Composable
fun SignUp(signUp: (email: Email, password: Password) -> Unit) {
    var email by remember { mutableStateOf("test1@net.dk") }
    var password by remember { mutableStateOf("1234qweQWE!") }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row {
            Text("Email:")
            TextField(
                value = email,
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        }
        Row {
            Text("Password:")
            TextField(
                value = password,
                onValueChange = { password = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
        Button(onClick = {
            if (!Email.validate(email) || !Password.validate(password)) {
                Log.v("SIMPLIFIED", "Error in $email or $password")
            } else {
                signUp(Email(email), Password(password))
            }
        }) {
            Text("Sign Up")
        }
    }
}