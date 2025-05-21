module com.steganography {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.steganography to javafx.fxml;
    exports com.steganography;
}
