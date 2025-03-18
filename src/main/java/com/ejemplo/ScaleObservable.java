package com.ejemplo;


import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del patrón Observer para notificar cambios en la escala
 */
public class ScaleObservable {
    private static ScaleObservable instance;
    private List<ScaleObserver> observers = new ArrayList<>();

    // Constructor privado (patrón Singleton)
    private ScaleObservable() {}

    // Método para obtener instancia
    public static synchronized ScaleObservable getInstance() {
        if (instance == null) {
            instance = new ScaleObservable();
        }
        return instance;
    }

    // Añadir observador
    public void addObserver(ScaleObserver observer) {
        observers.add(observer);
    }

    // Eliminar observador
    public void removeObserver(ScaleObserver observer) {
        observers.remove(observer);
    }

    // Notificar a todos los observadores
    public void notifyObservers(float scale) {
        for (ScaleObserver observer : observers) {
            observer.onScaleChanged(scale);
        }
    }
}