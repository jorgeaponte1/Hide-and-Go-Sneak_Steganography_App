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
    private Button returnButton;

    @FXML
    private Text messageText;

    private static String extractedMessage = "";

    public static void setExtractedMessage(String message) {
        extractedMessage = message;
    }

    @FXML
    private void initialize() {
        messageText.setText(extractedMessage != null ? extractedMessage : "No message extracted.");
    }

    @FXML
    private void onReturnClicked() throws IOException {
        App.setRoot("PaneMain");
    }
}