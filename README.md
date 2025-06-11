# 🕵️ Hide-and-Go-Sneak App

A JavaFX-based desktop application that lets you **hide and extract secret messages** inside images using **password-based steganography**. Built with Java 21 and JavaFX 21.0.7.

---

## 📦 Features

* Embed text messages into PNG images using password-based encryption.
* Secure password protection using various **hashing algorithms**
* Extract hidden messages with correct password match.
* User-friendly JavaFX GUI interface with Light/Dark Mode Capabilities.
* Drag-and-drop image support.
* Fully portable: builds a runnable `.jar` with no external dependencies

---

## ✅ Prerequisites

To run this application, ensure you have the following installed:

* **Java 21+ JDK**
* **Maven 3.6+ (Build)**

---

## 🚀 Getting Started

### 0. Download the .jar Release V1.1 

---

### 1. 📂 Clone this Repository

```bash
git clone https://github.com/yourusername/SteganographyApp.git
cd SteganographyApp/steganography
```

---

### 2. 🔨 Build the Application

```bash
mvn clean package
```

This will generate an executable JAR file at:

```
target/Hide-and-Go-Sneak.jar
```

---

### 3. ▶️ Run the Application

```bash
java -jar target/Hide-and-Go-Sneak.jar
```

If you encounter issues about JavaFX components missing:

```bash
java --module-path "C:\Program Files\Java\javafx-sdk-21.0.7\lib" --add-modules javafx.controls,javafx.fxml,javafx.swing -jar target/steganography-app.jar
```

Make sure to replace the path if your JavaFX SDK is installed elsewhere.

---

## Usage

## 🔐 Hash Algorithm Selection (Password Encryption)

When embedding or extracting a secret, you must provide a password. You will also select a **hashing algorithm** that will encode the password. This must be the **same during both embed and extract**.

### 📘 Options Available:

| Algorithm      | Strength Level                | Notes                                |
|----------------|-------------------------------|--------------------------------------|
| SHA-256        | 🔐 Strong - Recommended        | Secure and standard (256-bit digest) |
| SHA-512        | 🔐 Very Strong                 | High security (512-bit digest)       |
| SHA-384        | 🔐 Strong                      | Slightly faster than SHA-512         |
| SHA-224        | 🟡 Moderate                    | Rarely used                          |
| SHA-1          | ⚠️ Weak (Legacy)               | Deprecated - avoid use               |
| MD5            | ⚠️ Weak (Legacy)               | Deprecated - vulnerable to attacks   |

> 📝 You **must choose the same algorithm** when embedding and later extracting the secret. Otherwise, password matching will fail.

### 💡 Example Use Case

1. When embedding:
   - Password: `mysecret`
   - Algorithm: `SHA-256 (Strong - Recommended)`
2. When extracting:
   - Use the same password and same algorithm selection.

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
* **App Won't Launch**: Ensure you're using Java 21+ and Maven 3.6+ (to build package).

---

## 🛠 Technologies Used

- Java 21
- JavaFX 21.0.7
- Maven

---

## Contributing

Pull requests are welcome! Feel free to open issues or suggest improvements.

---

## 📝 License

This project is licensed under the MIT License.

---

## Credits

* Developed by Jorge and Jessica Aponte.
* Icons and UI components: Icons8