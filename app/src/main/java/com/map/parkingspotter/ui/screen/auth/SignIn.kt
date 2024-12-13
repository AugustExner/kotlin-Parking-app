package com.map.parkingspotter.ui.screen.auth

import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.map.parkingspotter.domain.Email
import com.map.parkingspotter.domain.Password

@Composable
fun SignIn(signIn: (email: Email, password: Password) -> Unit) {
    var email by remember { mutableStateOf("test1@net.dk") }
    var password by remember { mutableStateOf("1234qweQWE!") }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 150.dp),
            text = "Parking Spotter", style = MaterialTheme.typography.headlineLarge
        )
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 100.dp)
                .height(250.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Start), text = "Email:"
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.Start), text = "Password:"
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Button(onClick = {
                    if (!Email.validate(email) || !Password.validate(password)) {
                        Log.v("SIMPLIFIED", "Error in $email or $password")
                    } else {
                        signIn(Email(email), Password(password))
                    }
                }) {
                    Text("Sign In")
                }
            }
        }
    }
}