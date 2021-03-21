package project.wgtech.wgtranslator

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.util.Log
import android.widget.Toast
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class Util @Inject constructor(@ApplicationContext var context: Context) {

    val isDebug = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    val isSupportKitkat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    val isSupportLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    val isSupportMarshmallow = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    val isSupportAndroid11 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    fun logd(any: Any) {
        Log.d("wgtech", any.toString())
    }

    fun logw(any: Any) {
        Log.w("wgtech", any.toString())
    }

    fun toastShort(any: Any) {
        Toast.makeText(context, any.toString(), Toast.LENGTH_SHORT).show()
    }

    fun toastLong(any: Any) {
        Toast.makeText(context, any.toString(), Toast.LENGTH_LONG).show()
    }
}