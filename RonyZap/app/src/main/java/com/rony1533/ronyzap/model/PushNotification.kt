package com.rony1533.ronyzap.model

data class PushNotification(
    var data: NotificationData,
    val to: String
)