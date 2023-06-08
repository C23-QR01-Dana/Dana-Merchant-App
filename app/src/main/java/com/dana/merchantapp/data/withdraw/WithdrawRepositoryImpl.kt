package com.dana.merchantapp.data.withdraw

import android.util.Log
import com.dana.merchantapp.data.model.BankAccount
import com.dana.merchantapp.domain.withdraw.WithdrawRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class WithdrawRepositoryImpl @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) : WithdrawRepository {
    override fun getMerchantBank(callback: (Boolean, ArrayList<BankAccount>) -> Unit) {
        val merchantId = auth.currentUser?.uid
        if (merchantId != null) {
            val merchantDocRef = firestore.collection("merchant").document(merchantId)
            merchantDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val bankListRef = documentSnapshot.get("bankList") as? ArrayList<DocumentReference>
                        if (bankListRef != null) {
                            val bankList = ArrayList<BankAccount>()
                            val pendingTasks = bankListRef.map { it.get() }
                            Tasks.whenAllComplete(pendingTasks)
                                .addOnSuccessListener {
                                    for (task in pendingTasks) {
                                        if (task.isSuccessful) {
                                            val bankListDocumentSnapshot = task.result as? DocumentSnapshot
                                            if (bankListDocumentSnapshot != null) {
                                                val bankAccount = BankMapper.mapToBank(bankListDocumentSnapshot)
                                                bankList.add(bankAccount)
                                            }
                                        }
                                    }
                                    callback(true, bankList)
                                }
                        } else {
                            callback(true, ArrayList())
                        }
                    } else {
                        callback(false, ArrayList())
                    }
                }
                .addOnFailureListener {
                    callback(false, ArrayList())
                }
        } else {
            callback(false, ArrayList())
        }
    }

    override fun validateBank(bankName: String, accountNo: String, callback: (Boolean, BankAccount) -> Unit) {
        val documentReference = firestore.collection("validbank").document(accountNo)
        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    Log.v("repo input", "$bankName - $accountNo")
                    Log.v("repo hasil", "validateBank: ${documentSnapshot.getString("bankInst")}")
                    if (documentSnapshot.getString("bankInst") == bankName) {
                        val bankAccount = BankMapper.mapToBank(documentSnapshot)
                        callback(true, bankAccount)
                    } else {
                        callback(false, BankAccount("", "", ""))
                    }
                } else {
                    callback(false, BankAccount("", "", ""))
                }
            }
            .addOnFailureListener {
                callback(false, BankAccount("", "", ""))
            }
    }

    override fun addBank(bankAccount: BankAccount, callback: (Boolean) -> Unit) {
        val merchantId = auth.currentUser?.uid

        if (merchantId != null) {
            val db = firestore.collection("bankaccount").document(bankAccount.bankAccountNo)

            db.set(bankAccount)
                .addOnSuccessListener {
                    Log.v("repo", "addBankData: success")

                    val merchantDocRef = firestore.collection("merchant").document(merchantId)
                    merchantDocRef.update("bankList", FieldValue.arrayUnion(db))
                        .addOnSuccessListener {
                            Log.v("repo", "addReferenceData: success")
                            callback(true)
                        }
                        .addOnFailureListener {
                            Log.v("repo", "addReferenceData: failed")
                            callback(false)
                        }
                }
                .addOnFailureListener {
                    Log.v("repo", "addBankData: failed")
                    callback(false)
                }
        } else {
            callback(false)
        }
    }

    override fun withdraw(amount: Long, timestamp: Long, bankAccount: BankAccount, callback: (Boolean) -> Unit) {
        val user = auth.currentUser
        val merchantId = user?.uid

        if (merchantId != null) {
            val db = firestore.collection("transaction").document()
            val transactionData = hashMapOf(
                "amount" to amount,
                "id" to db.id,
                "bankAccountNo" to bankAccount.bankAccountNo,
                "bankInst" to bankAccount.bankInst,
                "merchantId" to merchantId,
                "timestamp" to timestamp,
                "trxType" to "MERCHANT_WITHDRAW",
            )
            db.set(transactionData)
                .addOnSuccessListener {
                    Log.v("withdraw", "Data successfully saved to Firestore")
                    val merchantDocRef = firestore.collection("merchant").document(merchantId)
                    merchantDocRef.update("balance", FieldValue.increment(-amount))
                        .addOnSuccessListener {
                            Log.v("withdraw", "Balance successfully updated")
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("withdraw", "Error updating balance", e)
                            callback(false)
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("withdraw", "Error saving data to Firestore", e)
                    callback(false)
                }
        } else {
            Log.e("withdraw", "Merchant ID is null")
            callback(false)
        }
    }


}
