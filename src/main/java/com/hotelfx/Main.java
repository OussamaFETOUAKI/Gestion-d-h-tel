package com.hotelfx;

import com.hotelfx.util.JpaUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize JPA on start to check connection early
        try {
            JpaUtil.getEntityManagerFactory();
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }

        scene = new Scene(loadFXML("main_view"), 800, 600);
        stage.setScene(scene);
        stage.setTitle("HotelFX Manager");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void stop() {
        JpaUtil.shutdown();
    }
}
