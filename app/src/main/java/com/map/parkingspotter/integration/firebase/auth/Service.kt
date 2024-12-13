package com.map.parkingspotter.integration.firebase.auth

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.map.parkingspotter.domain.AuthResult
import com.map.parkingspotter.domain.Email
import com.map.parkingspotter.domain.Password
import com.map.parkingspotter.domain.Status
import com.map.parkingspotter.domain.User
import kotlinx.coroutines.tasks.await

class Service {
    private val auth: FirebaseAuth = Firebase.auth

    companion object {
        const val TAG = "SIGNUP"
    }

    suspend fun signup(email: Email, password: Password): AuthResult {
        return try {

            val result =
                auth.createUserWithEmailAndPassword(email.value, password.value).await().user
                    ?: return AuthResult(null, Status.ERROR)

            val email = result.email?.let { Email(it) } ?: return AuthResult(null, Status.ERROR)

            //result.sendEmailVerification()

            val user = User(result.uid, email)


            val db = FirebaseFirestore.getInstance()
            // Default Settings for the data base
            val defaultSettings = mapOf(
                "filter" to "Americano",
                "theme" to "Dark Mode",
                "displayName" to "",
                "homeAddress" to ""
                )
            db.collection("users")
                .document(result.uid)
                .set(defaultSettings)
                .await()  // Use await to make this a suspend function


            Log.v(TAG, "SignIn should work")
            AuthResult(user, Status.OK)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            AuthResult(null, Status.ERROR)
        }
    }

    suspend fun signIn(email: Email, password: Password): AuthResult {
        return try {
            val result =
                auth.signInWithEmailAndPassword(email.value, password.value).await().user
                    ?: return AuthResult(null, Status.ERROR)
            val email = result.email?.let { Email(it) } ?: return AuthResult(null, Status.ERROR)
            val user = User(result.uid, email)
            AuthResult(user, Status.OK)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            AuthResult(null, Status.ERROR)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}