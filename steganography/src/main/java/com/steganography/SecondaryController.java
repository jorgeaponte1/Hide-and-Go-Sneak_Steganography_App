package com.steganography;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class SecondaryController {

    @FXML
    private Button nextButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextArea messageTextArea;

    private static String secretMessage = "";

    @FXML
    private void onSaveClicked() {
        secretMessage = messageTextArea.getText().trim();

        if (secretMessage.isEmpty()) {
            System.out.println("Message is empty. Please write a message before saving.");
        } else {
            System.out.println("Message saved: " + secretMessage);
        }
    }

    @FXML
    private void onNextClicked() throws IOException {
        if (secretMessage.isEmpty()) {
            System.out.println("No message saved. Please save a message before proceeding.");
        } else {
            System.out.println("Proceeding to PaneThree...");
            App.setRoot("PaneThree");
        }
    }

    // Legacy buttons (optional for future navigation)
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

    public static String getSecretMessage() {
        return secretMessage;
    }
}