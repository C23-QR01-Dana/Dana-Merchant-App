package com.dana.merchantapp.data.login

import android.util.Log
import com.dana.merchantapp.domain.login.LoginRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor (private val auth: FirebaseAuth, private val firestore: FirebaseFirestore, private val messaging: FirebaseMessaging): LoginRepository {
    override fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    messaging.token.addOnCompleteListener(OnCompleteListener { messagingTask ->
                        if (messagingTask.isSuccessful) {
                            val token = messagingTask.result
                            val merchantId = auth.currentUser?.uid
                            if (merchantId != null) {
                                val merchantDocRef = firestore.collection("merchant").document(merchantId)
                                merchantDocRef.update("tokenId", token)
                                    .addOnSuccessListener {
                                        callback(true, "Login Success")
                                    }
                                    .addOnFailureListener {
                                        callback(false, "Unable to retrieve device token")
                                    }
                            } else {
                                callback(false, "Merchant Not Found")
                            }
                        } else {
                            callback(false, "FCM Error")
                        }
                    })
                } else {
                    Log.e("login", "wrong email or password")
                    val errorMsg = task.exception?.message ?: "Login failed"
                    callback(false, errorMsg)
                }
            }
    }
}