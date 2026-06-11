package com.rtkcamera.di

import com.rtkcamera.nativebridge.AlgorithmProcessor
import com.rtkcamera.nativebridge.NativeBridge
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for Native components.
 * Configures the dependency injection for algorithm processing.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class NativeModule {

    @Binds
    @Singleton
    abstract fun bindAlgorithmProcessor(nativeBridge: NativeBridge): AlgorithmProcessor
}
