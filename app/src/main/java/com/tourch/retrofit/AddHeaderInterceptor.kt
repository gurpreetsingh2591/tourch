
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

class AddHeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //Basic Auth
        val builder = chain.request().newBuilder()
        builder.addHeader("Authorization", "Bearer" + " " + "pref.apiToken")
        return chain.proceed(builder.build())
    }
}