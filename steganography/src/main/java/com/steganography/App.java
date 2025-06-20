package com.steganography;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    private static boolean isDarkTheme = false;

    public void run() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("PaneMain"), 1280, 800);

        stage.getIcons().add(new Image(App.class.getResourceAsStream("/com/steganography/hide_and_sneak_logo.png")));
        scene.getStylesheets().add(getClass().getResource("/com/steganography/light-theme.css").toExternalForm());
        stage.setTitle("Hide-and-Go-Sneak");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        stage.setOnCloseRequest(t -> {
            cleanExit();
        });
    }

    static void cleanExit(){
        Platform.exit();
        System.exit(0);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static void toggleTheme() {
        scene.getStylesheets().clear();
        if (isDarkTheme) {
            scene.getStylesheets().add(App.class.getResource("/com/steganography/light-theme.css").toExternalForm());
        } else {
            scene.getStylesheets().add(App.class.getResource("/com/steganography/dark-theme.css").toExternalForm());
        }
        isDarkTheme = !isDarkTheme;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    static boolean isDarkTheme() {
        return isDarkTheme;
    }
}