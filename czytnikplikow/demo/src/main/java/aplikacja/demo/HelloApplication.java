package aplikacja.demo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HelloApplication extends Application {

    private TextArea polezawartoscipliku;
    private TextArea poleedycji;
    private TextField sciezkadopliku;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Odczyt plików");

        sciezkadopliku = new TextField();
        sciezkadopliku.setPromptText("Ścieżkę do pliku");

        Button loadButton = new Button("Wczytaj");
        loadButton.setOnAction(e -> loadFile());

        polezawartoscipliku = new TextArea();
        polezawartoscipliku.setEditable(false);

        poleedycji = new TextArea();

        Button zapisz = new Button("Zapisz");
        zapisz.setOnAction(e -> saveFile());

        Button zamknij = new Button("Zamknij");
        zamknij.setOnAction(e -> primaryStage.close());

        VBox topLayout = new VBox(sciezkadopliku, loadButton);
        VBox bottomLayout = new VBox(polezawartoscipliku, poleedycji, zapisz, zamknij);
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topLayout);
        mainLayout.setCenter(bottomLayout);

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadFile() {
        String filePath = sciezkadopliku.getText();
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            try {
                List<String> fileContent = readFile(filePath);
                polezawartoscipliku.setText(String.join("\n", fileContent));
                poleedycji.setText(String.join("\n", fileContent));
            } catch (IOException e) {
                showAlert("Błąd", "Nie można odczytać pliku: " + e.getMessage());
            }
        } else {
            showAlert("Błąd", "Coś poszło nie tak.");
        }
    }

    private void saveFile() {
        String filePath = sciezkadopliku.getText();
        List<String> contentToSave = List.of(poleedycji.getText().split("\n"));
        try {
            writeFile(filePath, contentToSave);
            polezawartoscipliku.setText(poleedycji.getText());
            showAlert("Sukces", "Plik został zapisany.");
        } catch (IOException e) {
            showAlert("Błąd", "Nie można zapisać pliku: " + e.getMessage());
        }
    }

    private List<String> readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path);
    }

    private void writeFile(String filePath, List<String> lines) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, lines);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}