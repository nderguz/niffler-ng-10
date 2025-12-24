package guru.qa.niffler.service;


import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.java.net.cookiejar.JavaNetCookieJar;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.CookieManager;
import java.net.CookiePolicy;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

@ParametersAreNonnullByDefault
public abstract class RestClient {

    protected static final Config CFG = Config.getInstance();

    private final OkHttpClient okHttpClient;
    private final Retrofit retrofit;

    public RestClient(String baseUrl) {
        this(baseUrl, false, JacksonConverterFactory.create(),  HttpLoggingInterceptor.Level.HEADERS, null);
    }

    public RestClient(String baseUrl, HttpLoggingInterceptor.Level level) {
        this(baseUrl, false, JacksonConverterFactory.create(), level, null);
    }

    public RestClient(String baseUrl, boolean followRedirects) {
        this(baseUrl, followRedirects, JacksonConverterFactory.create(), HttpLoggingInterceptor.Level.HEADERS, null);
    }

    public RestClient(String baseUrl, boolean followRedirects, HttpLoggingInterceptor.Level level, @Nullable Interceptor... interceptors) {
        this(baseUrl, followRedirects, JacksonConverterFactory.create(), level, interceptors);
    }

    public RestClient(String baseUrl, boolean followRedirects, @Nullable Interceptor... interceptors) {
        this(baseUrl, followRedirects, JacksonConverterFactory.create(), HttpLoggingInterceptor.Level.HEADERS, interceptors);
    }

    public RestClient(String baseUrl, boolean followRedirects, Converter.Factory converterFactory, HttpLoggingInterceptor.Level level, @Nullable Interceptor... interceptors) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .followRedirects(followRedirects)
                .cookieJar(
                        new JavaNetCookieJar(
                                new CookieManager(
                                        ThreadSafeCookieStore.INSTANCE,
                                        CookiePolicy.ACCEPT_ALL
                                )
                        )
                ).addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(level));

        if (isNotEmpty(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                builder.addNetworkInterceptor(interceptor);
            }
        }
        builder.addNetworkInterceptor(new AllureOkHttp3()
                .setRequestTemplate("http-request-attachment.ftl")
                .setResponseTemplate("http-response-attachment.ftl"));
        builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
        this.okHttpClient = builder.build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .client(okHttpClient)
                .build();
    }

    protected <T> T create(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
