package com.dobrimajstori.kucnimajstor.APINotification;

import com.dobrimajstori.kucnimajstor.Notifications.MyResponse;
import com.dobrimajstori.kucnimajstor.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService
{
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAPrFXGNs:APA91bGiYAH5v4ySV0L5LE8WXXf5n0Ncv53rB4yi1AW1N22Gv-xEdK3ZIYC9clthjtTQzy59PqkJbPR0XDUGRdsMvlm0nUJu5hxiQcPgiQNlzFgJRBJxNM3BnOntgifmfEd9H5W5dpZmBd-1hx_rYYwI05HxXCHgtw"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
