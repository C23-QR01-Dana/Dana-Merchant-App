package com.dana.merchantapp.data.register

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterRepositoryImpl : RegisterRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun registerUser(name: String, address: String,email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid

                    val merchantData = hashMapOf(
                        "balance" to 0,
                        "email" to email,
                        "merchantAddress" to address,
                        "merchantName" to name
                    )

                    if (uid != null) {
                        firestore.collection("merchant").document(uid)
                            .set(merchantData)
                            .addOnSuccessListener {
                                Log.v("register", "Data successfully saved to Firestore")
                            }
                            .addOnFailureListener { e ->
                                Log.e("register", "Error saving data to Firestore", e)
                            }
                    } else {
                        Log.e("register", "User UID is null")
                    }
                } else {
                    val errorMsg = task.exception?.message ?: "Registration failed"
                    Log.e("register", errorMsg)
                }
            }
    }
}
