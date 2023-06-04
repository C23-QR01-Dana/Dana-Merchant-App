package com.dana.merchantapp.di


import com.dana.merchantapp.data.register.RegisterRepository
import com.dana.merchantapp.data.register.RegisterRepositoryImpl
import com.dana.merchantapp.domain.register.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RegisterModule {

    @Provides
    fun provideRegisterUseCase(registerRepository: RegisterRepository): RegisterUseCase {
        return RegisterUseCase(registerRepository)
    }

    @Provides
    fun provideRegisterRepository(): RegisterRepository {
        return RegisterRepositoryImpl()
    }

}