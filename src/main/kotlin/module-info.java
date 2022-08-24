module com.example.codenames {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires java.prefs;
    requires javafx.media;


    opens com.example.codenames to javafx.fxml;
    exports com.example.codenames;
}