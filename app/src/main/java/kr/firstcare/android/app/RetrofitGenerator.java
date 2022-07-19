package kr.firstcare.android.app;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import kr.firstcare.android.app.api.APIService;
import kr.firstcare.android.app.data.Constant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ClassName            RetrofitGenerator
 * Created by JSky on   2020-06-03
 *
 * Description          서버 통신 설정
 */

public class RetrofitGenerator {

    private static RetrofitGenerator instance = new RetrofitGenerator();
    public static RetrofitGenerator getInstance() {
        return instance;
    }
    public RetrofitGenerator(){}

    private static OkHttpClient createOkHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        /*로그*/
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        /*타임아웃 시간*/
        builder.connectTimeout(5, TimeUnit.MINUTES);
        builder.readTimeout(5, TimeUnit.MINUTES);
        builder.writeTimeout(5, TimeUnit.MINUTES);
        return builder.build();
    }

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    // FirstCare
    Retrofit retrofitFC = new Retrofit.Builder()
            .baseUrl(Constant.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(createOkHttpClient())
            .build();

    APIService apiService = retrofitFC.create(APIService.class);

    public APIService getApiService() {
        return apiService;
    }



}
