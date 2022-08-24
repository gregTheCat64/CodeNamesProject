package com.example.codenames

import java.io.FileInputStream
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message


fun sendTable(token: String) {
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(FileInputStream("src/fcm.json")))
        .build()

    val message = java.lang.StringBuilder()
    val table: Array<Card?> = cards
    for (card in table) {
        message.append("${card?.color} ")

    }
    message.append("/")
    for (card in table) {
        message.append(card?.text)
    }
    val sams_token =
        "cTovH_MKRDSVVUQqaSezWK:APA91bED9RMgqPUz35gxnoMWV1rhPw_9fVb0BV9uQEsPqkGaXnmMx5bmU_UamLlm7g1UM2m-g5sQIFx5ew4PuuyXANTy4H7aVAVj2A3J8KQmyji4ON8ifBfFTmqisynWE44irl9m8pWi"


    FirebaseApp.initializeApp(options)

        val newPostMessage = Message.builder()
            .putData("action", "NEW_GAME")
            .putData(
                "content", """
         "$message"
          """.trimIndent()
            )
            .setToken(token)
            .build()
        FirebaseMessaging.getInstance().send(newPostMessage)
        println("Отправлено на устройство $token")

}






