package com.steganography;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HeaderPaneController {

    @FXML
    private Button themeToggleButton;

    @FXML
    private Label appTitleLabel;

    @FXML
    private void initialize() {
        themeToggleButton.setText(App.isDarkTheme() ? "☀" : "🌙");
    }

    @FXML
    private void handleToggleTheme() {
        App.toggleTheme();
        themeToggleButton.setText(App.isDarkTheme() ? "☀" : "🌙");    
    }
}