package com.testninja.selenium.reporters.reportportal.client;

import com.epam.ta.reportportal.ws.model.*;
import com.epam.ta.reportportal.ws.model.item.ItemCreatedRS;
import com.epam.ta.reportportal.ws.model.launch.LaunchResource;
import com.epam.ta.reportportal.ws.model.launch.MergeLaunchesRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRS;
import com.testninja.selenium.reporters.reportportal.client.models.LaunchResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ReportPortalService {
    @POST("launch")
    Call<StartLaunchRS> startLaunch(@Body StartLaunchRQ rq);

    @POST("launch/merge")
    Call<LaunchResource> mergeLaunches(@Body MergeLaunchesRQ rq);

    @PUT("launch/{launchId}/finish")
    Call<OperationCompletionRS> finishLaunch(@Path("launchId") String launch, @Body FinishExecutionRQ rq);

    @POST("item/")
    Call<ItemCreatedRS> startTestItem(@Body StartTestItemRQ rq);

    @POST("item/{parent}")
    Call<ItemCreatedRS> startTestItem(@Path("parent") String parent, @Body StartTestItemRQ rq);

    @PUT("item/{itemId}")
    Call<OperationCompletionRS> finishTestItem(@Path("itemId") String itemId, @Body FinishTestItemRQ rq);

    @Multipart
    @POST("log/")
    Call<EntryCreatedAsyncRS> log(@Part MultipartBody.Part attachment, @Part MultipartBody.Part jsonFile);

    @POST("log/")
    Call<EntryCreatedAsyncRS> log(@Body SaveLogRQ rq);

    @GET("launch/uuid/{launchUuid}")
    Call<LaunchResponse> getLaunchByUuid(@Path("launchUuid") String launchUuid);
}
