package project.wgtech.wgtranslator

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.lang.Exception
import java.util.concurrent.*
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
        if (isDebug) Log.d("wgtech", any.toString())
    }

    fun logw(any: Any) {
        Log.w("wgtech", any.toString())
    }

    fun loge(any: Any) {
        Log.e("wgtech", any.toString())
    }

    fun toastShort(any: Any) {
        Toast.makeText(context, any.toString(), Toast.LENGTH_SHORT).show()
    }

    fun toastLong(any: Any) {
        Toast.makeText(context, any.toString(), Toast.LENGTH_LONG).show()
    }

    fun getString(resourceId: Int) = context.getString(resourceId)

    fun executeTask(runnable: Runnable) = TaskRunner().execute<Any>(runnable)

    fun executeReturnable(callable: Callable<R>) = TaskRunner().executeReturnable(callable)

    fun executeAsync(callable: Callable<R>, callback: TaskRunner.Callback<R>) = TaskRunner().executeAsync(callable, callback)

    class TaskRunner() {
        private val executor: Executor = Executors.newSingleThreadExecutor()
        private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        private val singleExecutor: Executor = ThreadPoolExecutor(5, 128,
            1, TimeUnit.SECONDS,
            LinkedBlockingQueue<Runnable>()
        )
        private val handler: Handler = Handler(Looper.getMainLooper())

        interface Callback<R> {
            fun onComplete(result: R)
        }

        fun <R> execute(runnable: Runnable) {
            singleExecutor.execute(runnable)
        }

        @Throws(ExecutionException::class, InterruptedException::class, TimeoutException::class)
        fun <R> executeReturnable(callable: Callable<R>) : R {
            return executorService.submit(callable).get(5000, TimeUnit.MILLISECONDS)
        }

        fun <R> executeAsync(callable: Callable<R>, callback: TaskRunner.Callback<R>) {
            executor.execute {
                try {
                    val result: R = callable.call()
                    handler.post {
                        callback.onComplete(result)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}