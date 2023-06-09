package com.dana.merchantapp.data.register

import android.util.Log
import com.dana.merchantapp.domain.register.RegisterRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject


class RegisterRepositoryImpl @Inject constructor (private val auth: FirebaseAuth, private val firestore: FirebaseFirestore, private val messaging: FirebaseMessaging) :
    RegisterRepository {
    override fun registerUser(name: String, address: String,email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid

                    messaging.token.addOnCompleteListener(OnCompleteListener { messagingTask ->
                        if (messagingTask.isSuccessful) {
                            val token = messagingTask.result
                            val merchantData = hashMapOf(
                                "balance" to 0,
                                "email" to email,
                                "merchantAddress" to address,
                                "merchantLogo" to "https://media.discordapp.net/attachments/1105099627042193490/1109750540268744704/image.png?width=640&height=640",
                                "merchantName" to name,
                                "tokenId" to token
                            )

                            if (uid != null) {
                                firestore.collection("merchant").document(uid)
                                    .set(merchantData)
                                    .addOnSuccessListener {
                                        Log.v("register", "Data successfully saved to Firestore")
                                        callback(true, "Registration Success")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("register", "Error saving data to Firestore", e)
                                        callback(false, "Registration error")
                                    }
                            } else {
                                Log.e("register", "User UID is null")
                                callback(false, "Registration error")
                            }
                        }
                    })
                } else {
                    val errorMsg = task.exception?.message ?: "Registration failed"
                    Log.e("register", errorMsg)
                    callback(false, errorMsg)
                }
            }
    }
}
