package com.dana.merchantapp.di


import com.dana.merchantapp.data.home.HomeRepositoryImpl
import com.dana.merchantapp.data.login.LoginRepositoryImpl
import com.dana.merchantapp.domain.profile.ProfileRepository
import com.dana.merchantapp.data.profile.ProfileRepositoryImpl
import com.dana.merchantapp.domain.qr.QRRepository
import com.dana.merchantapp.data.qr.QRRepositoryImpl
import com.dana.merchantapp.domain.register.RegisterRepository
import com.dana.merchantapp.data.register.RegisterRepositoryImpl
import com.dana.merchantapp.domain.home.HomeRepository
import com.dana.merchantapp.domain.login.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideRegisterRepository(registerRepositoryImpl: RegisterRepositoryImpl): RegisterRepository

    @Binds
    abstract fun provideQRRepository(qrRepositoryImpl: QRRepositoryImpl): QRRepository

    @Binds
    abstract fun provideProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    abstract fun provideLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun provideHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository
}

