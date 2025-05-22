package com.steganography;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class FourthController {

    @FXML
    private Button returnToStartButton;

    @FXML
    private void onReturnToStartClicked() throws IOException {
        System.out.println("Returning to start (primary view)...");
        App.setRoot("PaneOne");
    }

    // OLD BUTTONS
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void switchToThird() throws IOException {
        App.setRoot("third");
    }
}