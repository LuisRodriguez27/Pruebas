package com.ejemplo;

/**
 * Clase principal para lanzar la aplicación
 * Implementa el patrón Facade para simplificar el inicio
 */
public class ApplicationLauncher {

    public static void main(String[] args) {
        // Iniciar la aplicación JME en un hilo separado
        new Thread(() -> {
            JMECubeApplication app = new JMECubeApplication();
            app.start();
        }).start();
    }
}