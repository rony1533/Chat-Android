package com.rony1533.ronyzap.`interface`

import com.rony1533.ronyzap.constants.Constants
import com.rony1533.ronyzap.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=${Constants.SERVER_KEY}","Content-type:${Constants.CONTENT_TYPE}")
    @POST("fmc/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}