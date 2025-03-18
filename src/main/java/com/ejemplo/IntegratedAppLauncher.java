package com.ejemplo;


import javafx.application.Platform;

/**
 * Clase principal para lanzar la aplicación integrada
 * Implementa una mejor integración entre JME y JavaFX
 */
public class IntegratedAppLauncher {

    private static JMECubeApplication jmeApp;

    public static void main(String[] args) {
        // Inicializar JavaFX primero
        Platform.startup(() -> {});

        // Iniciar JME en el hilo principal
        new Thread(() -> {
            jmeApp = new JMECubeApplication();
            // Configurar la propiedad para que JME mantenga el bucle de renderizado activo
            jmeApp.setPauseOnLostFocus(false);
            jmeApp.start();
        }).start();

        // Dar tiempo para que JME inicialice
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Iniciar la aplicación JavaFX en el hilo de JavaFX
        Platform.runLater(() -> {
            JavaFXSliderApp sliderApp = new JavaFXSliderApp();
            sliderApp.initialize(jmeApp);
        });
    }

    public static JMECubeApplication getJMEApp() {
        return jmeApp;
    }
}