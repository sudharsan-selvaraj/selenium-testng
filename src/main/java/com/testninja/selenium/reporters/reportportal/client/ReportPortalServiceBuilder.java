package com.testninja.selenium.reporters.reportportal.client;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.utils.properties.PropertiesLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ReportPortalServiceBuilder {

    private ListenerParameters listenerParameters;

    public ReportPortalServiceBuilder withParameter(ListenerParameters listenerParameters) {
        this.listenerParameters = listenerParameters;
        return this;
    }

    public ReportPortalService build() {
        if (listenerParameters == null) {
            listenerParameters = new ListenerParameters(PropertiesLoader.load());
        }
        return getReportPortalService();
    }

    private ReportPortalService getReportPortalService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
        OkHttpClient client =  new OkHttpClient().newBuilder()
                .addInterceptor(new ReportPortalInterceptor(listenerParameters.getApiKey()))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(getBaseUrl())
                .build();

        return retrofit.create(ReportPortalService.class);
    }

    private String getBaseUrl() {
        return listenerParameters.getBaseUrl() + "/api/v1/" + listenerParameters.getProjectName() +"/";
    }

}

class ReportPortalInterceptor implements Interceptor {

    private String apiKey;

    public ReportPortalInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + apiKey).build();
        return chain.proceed(request);
    }
}
