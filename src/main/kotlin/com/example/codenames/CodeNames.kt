package com.example.codenames

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.util.*




class Main : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("code-names-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 1800.0, 920.0)

        stage.scene = scene
        scene.stylesheets.add("/myStyle.css")
        stage.show()
    }


}

fun main() {
    Application.launch(Main::class.java)

}







