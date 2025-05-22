package com.steganography;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SecondaryController {

    @FXML
    private Button nextButton;

    @FXML
    private Button saveButton;

    @FXML
    private void onNextClicked() throws IOException {
        System.out.println("Next button clicked - proceed to next step.");
        App.setRoot("PaneThree");
    }

    @FXML
    private void onSaveClicked() {
        System.out.println("Save button clicked - message would be saved.");
    }

    // OLD BUTTONS
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void switchToThird() throws IOException {
        App.setRoot("third");
    }

    @FXML
    private void switchToFourth() throws IOException {
        App.setRoot("fourth");
    }
}