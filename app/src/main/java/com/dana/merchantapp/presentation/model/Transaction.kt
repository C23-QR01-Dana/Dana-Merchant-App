package com.dana.merchantapp.presentation.model

open class Transaction(
    open val amount: Int?,
    open val id: String?,
    open val merchantId: String?,
    open val timestamp: Long?,
    open val trxType: String?
)

data class PaymentTransaction(
    override val amount: Int?,
    override val id: String?,
    override val merchantId: String?,
    override val timestamp: Long?,
    override val trxType: String?,
    val payerId: String?
) : Transaction(amount, id, merchantId, timestamp, trxType)

data class MerchantWithdrawTransaction(
    override val amount: Int?,
    override val id: String?,
    override val merchantId: String?,
    override val timestamp: Long?,
    override val trxType: String?,
    val bankAccountNo: String?,
    val bankInst: String?
) : Transaction(amount, id, merchantId, timestamp, trxType)

//{
//    constructor() : this(null, null, null, null, null, null)
//}