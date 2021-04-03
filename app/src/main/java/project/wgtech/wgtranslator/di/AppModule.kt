package project.wgtech.wgtranslator.di

import android.content.ClipboardManager
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import project.wgtech.wgtranslator.R
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    fun provideInputMethodManager(@ApplicationContext context: Context): InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    @Provides
    fun provideRemoteModelManager() : RemoteModelManager =
        RemoteModelManager.getInstance()

    @Provides
    fun provideDownloadConditions() : DownloadConditions =
        DownloadConditions.Builder().build()

    @Provides
    internal fun provideRetrofitClient() : Retrofit =
        Retrofit.Builder().build()
}