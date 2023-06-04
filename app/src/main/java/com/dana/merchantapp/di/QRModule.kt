package com.dana.merchantapp.di

import com.dana.merchantapp.data.qr.QRRepository
import com.dana.merchantapp.data.qr.QRRepositoryImpl
import com.dana.merchantapp.domain.qr.QRUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object QRModule {

    @Provides
    fun provideQRUseCase(qrRepository: QRRepository): QRUseCase {
        return QRUseCase(qrRepository)
    }

    @Provides
    fun provideQRRepository(): QRRepository {
        return QRRepositoryImpl()
    }

}