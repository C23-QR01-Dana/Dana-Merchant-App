package com.dana.merchantapp.data.home

import com.dana.merchantapp.data.model.Merchant
import com.google.firebase.firestore.DocumentSnapshot

object MerchantMapper {
    fun mapToMerchant(documentSnapshot: DocumentSnapshot): Merchant? {
        return if (documentSnapshot.exists()) {
            val balance = documentSnapshot.getLong("balance")?.toInt() ?: 0
            val email = documentSnapshot.getString("email") ?: ""
            val merchantAddress = documentSnapshot.getString("merchantAddress") ?: ""
            val merchantLogo = documentSnapshot.getString("merchantLogo") ?: ""
            val merchantName = documentSnapshot.getString("merchantName") ?: ""

            Merchant(balance, email, merchantAddress, merchantLogo, merchantName)
        } else {
            null
        }
    }
}
