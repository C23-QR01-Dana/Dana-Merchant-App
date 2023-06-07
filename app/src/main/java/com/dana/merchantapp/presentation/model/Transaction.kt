package com.dana.merchantapp.presentation.model

data class Transaction(
    val amount: Int?,
    val id: String?,
    val merchantId: String?,
    val payerId: String?,
    val timestamp: Long?,
    val trxType: String?
) {
    constructor() : this(null, null, null, null, null, null)
}