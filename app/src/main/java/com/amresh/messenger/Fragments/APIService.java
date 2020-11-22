package com.amresh.messenger.Fragments;

import com.amresh.messenger.Notifications.MyResponse;
import com.amresh.messenger.Notifications.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService
{
    @Headers({
            "Content-Type:application/jason",
            "Authorization:key=AAAALPXnlt4:APA91bHAGZXyZjGbWotpGbhsd59sIdIVEtYbQdlzNBtpnpi9yk2BkAtxyPSw6md9iB1TRFtE3JfcghAs3BoBmO8q0jQsAV16UmuJcr1nkvB3Yp4_NSmF92WDJKngw-yuac1zUDo7lXiM"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
