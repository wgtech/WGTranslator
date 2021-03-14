package project.wgtech.wgtranslator.repository

import android.content.Context
import android.graphics.drawable.Drawable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import project.wgtech.wgtranslator.model.Language
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    private fun getDrawable(context: Context, fileName: String) : Drawable {
        val assetManager = context.resources.assets
        return Drawable.createFromStream(assetManager.open(fileName), null)
    }

    @Provides
    @Singleton
    fun provideSpinnerData(@ApplicationContext context: Context) : ArrayList<Language> {

        return ArrayList<Language>().apply {
            add(Language("EN", "영어", getDrawable(context, "united_states.png")))
            add(Language("KO", "한국어", getDrawable(context, "republic_of_korea.png")))
        }
    }
}