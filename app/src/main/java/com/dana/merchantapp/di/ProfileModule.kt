package com.dana.merchantapp.di

import com.dana.merchantapp.data.profile.ProfileRepository
import com.dana.merchantapp.data.profile.ProfileRepositoryImpl
import com.dana.merchantapp.domain.profile.ProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ProfileModule {
    @Provides
    fun provideProfileUseCase(profileRepository: ProfileRepository): ProfileUseCase {
        return ProfileUseCase(profileRepository)
    }

    @Provides
    fun provideProfileRepository(): ProfileRepository {
        return ProfileRepositoryImpl()
    }
}