package com.steganography;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ExtractMessageController {

    @FXML
    private Label messageLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Button returnButton;

    @FXML
    private Text messageText;

    private static String extractedMessage = "";

    public static void setExtractedMessage(String message) {
        extractedMessage = message;
    }

    @FXML
    private void initialize() {
        if (errorLabel != null) {
            errorLabel.setVisible(false);
            errorLabel.setText("");
        }

        if (extractedMessage == null || extractedMessage.trim().isEmpty()) {
            showError("No message could be extracted. Please try again with a valid image and password.");
            messageText.setText("");
        } else {
            hideError();
            messageText.setText(extractedMessage);
        }
    }

    @FXML
    private void onReturnClicked() throws IOException {
        App.setRoot("PaneMain");
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }

    private void hideError() {
        if (errorLabel != null) {
            errorLabel.setVisible(false);
            errorLabel.setText("");
        }
    }
}