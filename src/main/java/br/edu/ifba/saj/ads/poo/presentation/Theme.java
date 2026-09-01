package br.edu.ifba.saj.ads.poo.presentation;

import javafx.scene.Scene;

// aplica o stylesheet visual único da aplicação a uma cena recém-criada
public final class Theme {

    private static final String CAMINHO_CSS = "/br/edu/ifba/saj/ads/poo/presentation/view/theme.css";

    private Theme() {
    }

    public static void aplicar(Scene scene) {
        scene.getStylesheets().add(Theme.class.getResource(CAMINHO_CSS).toExternalForm());
    }
}
