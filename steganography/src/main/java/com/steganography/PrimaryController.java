package com.steganography;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PrimaryController {

    @FXML
    private Button selectEmbedImageButton;

    @FXML
    private Button embedNextButton;

    @FXML
    private Button selectExtractImageButton;

    @FXML
    private Button extractNextButton;

    @FXML
    private void onSelectEmbedImage() {
        System.out.println("Embed: Select Image clicked");
    }

    @FXML
    private void onEmbedNext() {
        System.out.println("Embed: Next clicked");
    }

    @FXML
    private void onSelectExtractImage() {
        System.out.println("Extract: Select Image clicked");
    }

    @FXML
    private void onExtractNext() {
        System.out.println("Extract: Next clicked");
    }

    // These are from your old buttons
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
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