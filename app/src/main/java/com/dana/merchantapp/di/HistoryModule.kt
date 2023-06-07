package com.dana.merchantapp.di

import com.dana.merchantapp.data.history.HistoryRepository
import com.dana.merchantapp.data.history.HistoryRepositoryImpl
import com.dana.merchantapp.domain.history.HistoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HistoryModule {

    @Provides
    fun provideHistoryUseCase(historyRepository: HistoryRepository): HistoryUseCase {
        return HistoryUseCase(historyRepository)
    }

    @Provides
    fun provideHistoryRepository(): HistoryRepository {
        return HistoryRepositoryImpl()
    }
}



//import com.dana.merchantapp.data.qr.QRRepository
//import com.dana.merchantapp.data.qr.QRRepositoryImpl
//import com.dana.merchantapp.domain.qr.QRUseCase
//import dagger.Module
//import dagger.Provides



//@Module
//@InstallIn(SingletonComponent::class)
//object QRModule {
//
//    @Provides
//    fun provideQRUseCase(qrRepository: QRRepository): QRUseCase {
//        return QRUseCase(qrRepository)
//    }
//
//    @Provides
//    fun provideQRRepository(): QRRepository {
//        return QRRepositoryImpl()
//    }
//
//}