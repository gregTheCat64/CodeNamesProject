package com.example.codenames


import javafx.animation.TranslateTransition
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import java.io.*


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

    @FXML
    lateinit var deviceMenu: ComboBox<String>

    var redResult = 9
    var blueResult = 8
    var blackResult = 0
    var isRedTurn = true

    lateinit var buttons: List<Button>

    private var currentNameOfDevice:String = ""
    lateinit var currentToken: String

    val soundClickFile:String = "src/main/resources/sounds/click.mp3"
    val soundErrorFile:String = "src/main/resources/sounds/error.mp3"
    val soundMinionsFile = "src/main/resources/sounds/win-minions.mp3"
    val soundWinFile = "src/main/resources/sounds/win2.mp3"
    val soundClick = Media(File(soundClickFile).toURI().toString())
    val soundBlack = Media(File(soundErrorFile).toURI().toString())
    val soundWin = Media(File(soundWinFile).toURI().toString())
    val soundMinions = Media(File(soundMinionsFile).toURI().toString())

    var filename = "tokens.dat"
    lateinit var devices:ArrayList<Device>


    @FXML
    fun initialize(){

        newGameBtn.style = "-fx-font-size: 12px; -fx-pref-width: 100px;-fx-pref-height: 25px;-fx-font-weight: bold;"
        addNewDeviceBtn.style = "-fx-font-size: 12px; -fx-pref-width: 100px;-fx-pref-height: 25px;-fx-font-weight: bold;"

        firebaseInit()

        devices = getDevices()
        var currentDevice: Device? = null

        if (devices.isNotEmpty()){
            updateDeviceMenu()
        }


        if (devices.isNotEmpty()){
            currentDevice = devices.lastOrNull()
            nameLabel.text = "Имя устройства: "
            currentToken = currentDevice?.token.toString()
            currentNameOfDevice = currentDevice?.name.toString()


        } else infoField.text = "Добавьте устройство"

        var choosenDeviceName: String = ""

        deviceMenu.setOnAction { event ->
                choosenDeviceName = deviceMenu.value
                println(choosenDeviceName)
                currentDevice = devices.firstOrNull{it.name == choosenDeviceName}
                currentToken = currentDevice?.token.toString()
                currentNameOfDevice = currentDevice?.name.toString()
            }




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
            if (currentDevice != null){
                infoField.text = "Игра началась!"
                println(currentNameOfDevice)
                playGame(buttons)
            } else {infoField.text = "Сначала добавьте устройство"}
        }

        addNewDeviceBtn.setOnMouseClicked {

            if (newTokenField.text.isNotEmpty() && nameField.text.isNotEmpty()) {
                if (devices.firstOrNull{it.name == nameField.text.toString().trim()}==null){
                    devices.add(Device(nameField.text, newTokenField.text))
                } else {
                    infoField.text = "Выберите другое название"
                    nameField.clear()
                    newTokenField.clear()
                    return@setOnMouseClicked
                }

                try {
                    ObjectOutputStream(FileOutputStream(filename)).use { oos ->
                        oos.writeObject(devices)
                        println("File has been written")
                        for (d in devices){
                            println("name: ${d.name}, token: ${d.token}")
                        }
                    }
                } catch (ex: java.lang.Exception) {
                    println(ex.message)
                }
                infoField.text = "Устройство добавлено..."
                nameField.clear()
                newTokenField.clear()
                updateDeviceMenu()
            } else {infoField.text = "Заполните оба поля!"}
        }
    }

    private fun updateDeviceMenu(){
        //поправить обновление меню
        val deviceNames = ArrayList<String>()
        for (d in devices){
            deviceNames.add(d.name)
        }
        val observableDeviceNames = FXCollections.observableArrayList<String>(deviceNames)
        deviceMenu.items = observableDeviceNames
        deviceMenu.value = observableDeviceNames[observableDeviceNames.size-1]
    }

    @JvmName("getDevices1")
    fun getDevices(): ArrayList<Device>{
        var newDevices = ArrayList<Device>()
        try {
            ObjectInputStream(FileInputStream(filename)).use { ois ->
                newDevices = ois.readObject() as ArrayList<Device>
            }
        } catch (ex: java.lang.Exception) {
            println(ex.message)
        }

        for (d in newDevices){
            println("name: ${d.name}, token: ${d.token}")
        }
        return newDevices
    }

    private fun playGame(buttons: List<Button>) {
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
        sendTable(currentToken)
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

    private fun openCard(button: Button, tag: Int) {
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

    fun saveToken(){
        try {
            ObjectOutputStream(FileOutputStream("devices.dat")).use { oos ->
                val d = Device("Sam", "33")
                oos.writeObject(d)
            }
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    fun getTokens(){
        try {
            ObjectInputStream(FileInputStream("devices.dat")).use { ois ->
                val d: Device = ois.readObject() as Device
                System.out.printf("Name: %s \t Token: %d \n", d.name, d.token)
            }
        } catch (ex: java.lang.Exception) {
            println(ex.message)
        }
    }



}