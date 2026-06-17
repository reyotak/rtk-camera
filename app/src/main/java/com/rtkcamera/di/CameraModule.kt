package com.rtkcamera.di

import com.rtkcamera.camera.CapturePipeline
import com.rtkcamera.camera.CapturePipelineImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CameraModule {

    @Binds
    @Singleton
    abstract fun bindCapturePipeline(
        impl: CapturePipelineImpl
    ): CapturePipeline
}
