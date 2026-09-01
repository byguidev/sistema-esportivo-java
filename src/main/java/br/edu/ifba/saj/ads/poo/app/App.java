package br.edu.ifba.saj.ads.poo.app;

// classe de inicialização da aplicação javafx

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// ponto de entrada do javafx: carrega a tela principal a partir do fxml
public class App extends Application {
    // monta o stage inicial lendo o fxml principal
    @Override
    public void start(Stage stage) throws Exception {
        // a aplicação sempre começa pela tela de login; ela troca a cena pra Main.fxml quando autentica
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/br/edu/ifba/saj/ads/poo/presentation/view/Login.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    // método main chamado pelo javafx para iniciar a aplicação
    public static void main(String[] args) {
        launch(args);
    }
}
