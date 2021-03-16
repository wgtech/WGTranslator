package project.wgtech.wgtranslator.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    internal fun provideRetrofitClient() : Retrofit =
        Retrofit.Builder().build()
}