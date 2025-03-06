package se233.project2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primarystage) throws Exception {
        mainMenu mainMenu = new mainMenu();
        Scene scene = new Scene(mainMenu,Platform.WIDTH,Platform.HEIGHT);

        primarystage.setTitle("Space Invender Ver.Copy");
        primarystage.setScene(scene);
        primarystage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
