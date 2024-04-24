package zad1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientUI{


    public void ui(Stage stage) throws Exception {

        stage.setTitle("Prosty UI w JavaFX");

        Client client = new Client();



        TextField textField1 = new TextField();
        TextField textField2 = new TextField();

        Button updateButton = new Button("Aktualizuj");

        Label translationLabel = new Label();

        updateButton.setOnAction(event -> {
            String text1 = textField1.getText();
            String text2 = textField2.getText();

            String abc = client.clientLogic(text1, text2);

            translationLabel.setText(abc);

            System.out.println("Pole 1: " + text1);
            System.out.println("Pole 2: " + text2);
        });

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(textField1, 0, 0);
        gridPane.add(textField2, 0, 1);
        gridPane.add(updateButton, 1, 0, 1, 2);
        gridPane.add(translationLabel, 0, 2);

        Scene scene = new Scene(gridPane, 300, 200);

        stage.setScene(scene);

        stage.show();
    }

}
