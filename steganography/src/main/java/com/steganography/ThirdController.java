package com.steganography;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

public class ThirdController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button saveButton;

    @FXML
    private Button nextButton;

    @FXML
    private void onSaveClicked() {
        String password = passwordField.getText();
        System.out.println("Password entered: " + password);
        // Later: Store password securely, validate, etc.
    }

    @FXML
    private void onNextClicked() throws IOException{
        System.out.println("Next button clicked - proceed to the next step.");
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
    private void switchToFourth() throws IOException {
        App.setRoot("fourth");
    }
}