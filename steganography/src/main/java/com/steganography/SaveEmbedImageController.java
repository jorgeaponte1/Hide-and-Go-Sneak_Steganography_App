package com.steganography;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class SaveEmbedImageController {

    @FXML
    private Button returnToStartButton;

    @FXML
    private ImageView resultImageView;

    @FXML
    private AnchorPane rootPane;

    private static Image finalEmbeddedImage;

    @FXML
    private void initialize() {
        loadHeader();
        if (finalEmbeddedImage != null) {
            resultImageView.setImage(finalEmbeddedImage);
        } else {
            System.out.println("No embedded image provided. Displaying nothing.");
        }

        // Add context menu on right-click
        ContextMenu contextMenu = new ContextMenu();
        MenuItem saveItem = new MenuItem("Save Image As...");
        saveItem.setStyle("-fx-font-size: 18; -fx-text-fill: #000000;");
        saveItem.setOnAction(e -> saveImageToFile());
        contextMenu.getItems().add(saveItem);

        resultImageView.setOnContextMenuRequested((ContextMenuEvent event) -> {
            if (event.getPickResult().getIntersectedNode() == resultImageView) {
                contextMenu.show(resultImageView, event.getScreenX(), event.getScreenY());
            }
        });

        resultImageView.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.hide();
            }
        });
    }

    private void loadHeader() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/steganography/PaneHeader.fxml"));
            Node header = loader.load();

            // Anchor to top of rootPane
            AnchorPane.setTopAnchor(header, 0.0);
            AnchorPane.setLeftAnchor(header, 0.0);
            AnchorPane.setRightAnchor(header, 0.0);

            rootPane.getChildren().add(header);
        } catch (IOException e) {
            System.err.println("Failed to load header: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveImageToFile() {
        Image image = resultImageView.getImage();
        if (image == null) {
            System.out.println("No image to save.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PNG Image", "*.png")
        );
        fileChooser.setInitialFileName("steganography_output.png");

        File file = fileChooser.showSaveDialog(resultImageView.getScene().getWindow());
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                System.out.println("Image saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Failed to save image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onReturnToStartClicked() throws IOException {
        if(SteganographyUtil.confirmReturnToStart()){
            System.out.println("Returning to start (primary view)...");
            resultImageView.setImage(null);
            finalEmbeddedImage = null;
            App.setRoot("PaneMain");
        } else {
            System.out.println("Return to start canceled.");
        }
    }

    static void setFinalImage(Image image) {
        finalEmbeddedImage = image;
    }
}