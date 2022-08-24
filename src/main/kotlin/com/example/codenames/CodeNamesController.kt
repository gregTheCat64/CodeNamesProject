package com.example.codenames


import javafx.animation.TranslateTransition
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.util.Duration
import java.util.*
import java.util.prefs.Preferences
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;



lateinit var cards: Array<Card?>
class CodeNamesController {

    @FXML
    lateinit var newGameBtn: Button

    @FXML
    lateinit var btn0: Button

    @FXML
    lateinit var btn1: Button

    @FXML
    lateinit var btn2: Button

    @FXML
    lateinit var btn3: Button

    @FXML
    lateinit var btn4: Button

    @FXML
    lateinit var btn5: Button

    @FXML
    lateinit var btn6: Button

    @FXML
    lateinit var btn7: Button

    @FXML
    lateinit var btn8: Button

    @FXML
    lateinit var btn9: Button

    @FXML
    lateinit var btn10: Button

    @FXML
    lateinit var btn11: Button

    @FXML
    lateinit var btn12: Button

    @FXML
    lateinit var btn13: Button

    @FXML
    lateinit var btn14: Button

    @FXML
    lateinit var btn15: Button

    @FXML
    lateinit var btn16: Button

    @FXML
    lateinit var btn17: Button

    @FXML
    lateinit var btn18: Button

    @FXML
    lateinit var btn19: Button

    @FXML
    lateinit var btn20: Button

    @FXML
    lateinit var btn21: Button

    @FXML
    lateinit var btn22: Button

    @FXML
    lateinit var btn23: Button

    @FXML
    lateinit var btn24: Button

    @FXML
    lateinit var redResultField: Label

    @FXML
    lateinit var blueResultField: Label


    @FXML
    lateinit var addNewDeviceBtn: Button

    @FXML
    lateinit var newTokenField: TextField

    @FXML
    lateinit var nameField: TextField

    @FXML
    lateinit var nameLabel: Label

    @FXML
    lateinit var infoField: Label

    var redResult = 9
    var blueResult = 8
    var blackResult = 0
    var isRedTurn = true
    lateinit var token: String
    lateinit var buttons: List<Button>

    val soundClickFile:String = "src/main/resources/sounds/click.mp3"
    val soundErrorFile:String = "src/main/resources/sounds/error.mp3"
    val soundMinionsFile = "src/main/resources/sounds/win-minions.mp3"
    val soundWinFile = "src/main/resources/sounds/win2.mp3"
    val soundClick = Media(File(soundClickFile).toURI().toString())
    val soundBlack = Media(File(soundErrorFile).toURI().toString())
    val soundWin = Media(File(soundWinFile).toURI().toString())
    val soundMinions = Media(File(soundMinionsFile).toURI().toString())


    @FXML
    fun initialize(){
        val prefs = Preferences.userRoot().node(this.javaClass.name)
        val nameOfDevice = prefs.get("NAME","не назначено")
        token = prefs.get("TOKEN", "")
        newGameBtn.style = "-fx-font-size: 12px; -fx-pref-width: 100px;-fx-pref-height: 25px;-fx-font-weight: bold;"
        addNewDeviceBtn.style = "-fx-font-size: 12px; -fx-pref-width: 100px;-fx-pref-height: 25px;-fx-font-weight: bold;"


        nameLabel.text = "Имя устройства: $nameOfDevice"
        buttons = listOf<Button>(
            btn0,
            btn1,
            btn2,
            btn3,
            btn4,
            btn5,
            btn6,
            btn7,
            btn8,
            btn9,
            btn10,
            btn11,
            btn12,
            btn13,
            btn14,
            btn15,
            btn16,
            btn17,
            btn18,
            btn19,
            btn20,
            btn21,
            btn22,
            btn23,
            btn24
        )

        for (b in buttons) {
            b.text = "Code Names"
        }

        newGameBtn.setOnMouseClicked {
            if (nameOfDevice.isNotEmpty() && token.isNotEmpty()){
                infoField.text = "Игра началась!"
                println(token)
                playGame(buttons)
            } else {infoField.text = "Сначала добавьте устройство"}
        }

        addNewDeviceBtn.setOnMouseClicked {
            if (newTokenField.text.isNotEmpty() && nameField.text.isNotEmpty()) {
                prefs.put("TOKEN", newTokenField.text)
                prefs.put("NAME", nameField.text)
                infoField.text = "Устройство добавлено..."
            } else {infoField.text = "Заполните оба поля!"}
        }
    }

    private fun playGame(buttons: kotlin.collections.List<Button>) {
        isRedTurn = !isRedTurn
       if (isRedTurn) {
           cards = getCards(true)
           redResult = 9
           blueResult = 8
           blackResult = 0
       } else {
           cards = getCards(false)
           redResult = 8
           blueResult = 9
           blackResult = 0
       }
        //sendTable(token.toString())
        result()


        for (b in buttons) {
            b.isDisable = false
            b.style = "myStyle.css"
            var tag = 0
            b.text = "Code Names"
            b.setOnMouseClicked {
                tag = b.id.toString().substring(3).toInt()
                openCard(b, tag)
            }
        }

        for (i in buttons.indices) {
            buttons[i].text = cards[i]?.text
        }
    }


    fun openCard(button: Button, tag: Int) {
        MediaPlayer(soundClick).play()
        when (cards[tag]?.color) {
            Color.RED -> {
                button.style = ("-fx-background-color:  #FF7F50; -fx-text-fill: FFE4B5;")
                redResult--

            }
            Color.BLUE -> {
                button.style = ("-fx-background-color:  #4682B4; -fx-text-fill: FFE4B5;")
                blueResult--

            }
            Color.BLACK -> {
                button.style = ("-fx-background-color: Black; -fx-text-fill: FFE4B5;")
                blackResult++
                MediaPlayer(soundBlack).play()
            }
            Color.WHITE -> {
                button.style = ("-fx-background-color: #FFE4B5")


            }

        }


        button.isDisable = true
        button.opacity = 1.0
        animate(button)
        result()
    }

    fun result() {
        redResultField.text = redResult.toString()
        blueResultField.text = blueResult.toString()
        when {
            redResult == 0 -> {
                infoField.text = "Игра окончена! победили Красные"
                MediaPlayer(soundWin).play()
                finish()
            }
            blueResult == 0 -> {
                infoField.text = "Игра окончена! победили Синие"
                MediaPlayer(soundMinions).play()
                finish()
            }
            blackResult == 1 -> {
                infoField.text = "Вы выбрали черную карту! Игра окончена!"

            }
        }

    }

    fun finish(){
        for (b in buttons){
            b.isDisable = true
        }
    }


    fun animate(button: Button){
        //Duration = 2.5 seconds
        //Duration = 2.5 seconds
        val duration = Duration.millis(100.0)
        //Create new translate transition
        //Create new translate transition
        val transition = TranslateTransition(duration, button)
        //Move in X axis by +200
        //Move in X axis by +200
        //transition.byX = 200.0
        //Move in Y axis by +100
        //Move in Y axis by +100
        transition.byY = -10.0
        //Go back to previous position after 2.5 seconds
        //Go back to previous position after 2.5 seconds
        transition.isAutoReverse = true
        //Repeat animation twice
        //Repeat animation twice
        transition.cycleCount = 2
        transition.play()
    }

}