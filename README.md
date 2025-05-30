# Steganography App

A JavaFX-based desktop application that allows you to hide secret messages inside image files (using steganography) and extract hidden messages from them.

## Features

* Embed text messages into PNG images using password-based encryption.
* Extract hidden messages using the correct password.
* User-friendly JavaFX GUI interface.
* Drag-and-drop image support.
* Built with Java 21 and JavaFX 21.0.7.

---

## Prerequisites

To run this application, ensure you have the following installed:

* **Java 21+ JDK**
* **JavaFX SDK 21.0.7** (must match build version)
* Git (optional, for cloning the repo)

---

## Getting Started

### 1. Clone or Download the Repository

```bash
git clone https://github.com/yourusername/steganography-app.git
cd steganography-app
```

Or download the ZIP and extract it.

---

### 2. Build the Application

Ensure Java and Maven are installed, then:

```bash
mvn clean package
```

This will generate an executable JAR file in the `target/` folder.

---

### 3. Run the Application

```bash
java -jar target/steganography-app.jar
```

If you encounter issues about JavaFX components missing:

```bash
java --module-path "C:\Program Files\Java\javafx-sdk-21.0.7\lib" --add-modules javafx.controls,javafx.fxml,javafx.swing -jar target/steganography-app.jar
```

Make sure to replace the path if your JavaFX SDK is installed elsewhere.

---

## Usage

### Embedding a Message

1. Launch the app.
2. Choose an image file (PNG recommended).
3. Enter the secret message.
4. Set a password.
5. Click **Embed**.
6. Save the resulting image.

### Extracting a Message

1. Launch the app.
2. Choose an image with an embedded message.
3. Enter the correct password.
4. Click **Extract** to reveal the hidden message.

---

## Troubleshooting

* **JavaFX API Version Warning**: This is expected if you use classpath-based launching (not a bug).
* **App Won't Launch**: Ensure you're using Java 21+ and JavaFX 21.0.7.

---

## Contributing

Pull requests are welcome! Feel free to open issues or suggest improvements.

---

## License

MIT License. See `LICENSE` file for details.

---

## Credits

* Developed using JavaFX and Maven.
* Icons and UI components: \[credits if any].