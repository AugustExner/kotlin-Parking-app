package com.map.parkingspotter.integration.firebase.auth

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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