package com.steganography;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private ImageView embedImageView; // reference to ImageView in Embed tab

    @FXML
    private TextField embedImagePathField;

    private static File selectedEmbedImageFile;

    @FXML
    private void onSelectEmbedImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image for Embedding");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            selectedEmbedImageFile = file;
            embedImagePathField.setText(file.getAbsolutePath());
            Image image = new Image(file.toURI().toString());
            embedImageView.setImage(image);
            System.out.println("Embed: Image selected - " + file.getAbsolutePath());
        } else {
            System.out.println("Embed: Image selection canceled.");
        }
    }

    @FXML
    private void onEmbedNext() throws IOException {
        if (selectedEmbedImageFile == null) {
            System.out.println("Please select an image before proceeding.");
            return;
        }
        // In the future, pass this file to PaneTwo
        System.out.println("Embed: Next clicked, moving to PaneTwo...");
        App.setRoot("PaneTwo");
    }

    @FXML
    private void onSelectExtractImage() {
        System.out.println("Extract: Select Image clicked");
    }

    @FXML
    private void onExtractNext() throws IOException {
        System.out.println("Extract: Next clicked");
        App.setRoot("PaneExtractOne");
    }

    public static File getSelectedEmbedImageFile() {
        return selectedEmbedImageFile;
    }

    // Legacy navigation (optional cleanup)
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