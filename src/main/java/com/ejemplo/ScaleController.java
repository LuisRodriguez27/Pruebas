package com.ejemplo;


import com.jme3.scene.Geometry;
import com.jme3.math.Vector3f;



/**
 * Controlador para manejar la escala del cubo
 * Implementa el patrón Singleton para acceso global
 * Versión mejorada para integración JME-JavaFX
 */
public class ScaleController {
    private static ScaleController instance;
    private Geometry geometry;
    private float currentScale = 1.0f;
    private final float MIN_SCALE = 0.1f;
    private final float MAX_SCALE = 5.0f;

    // Constructor
    public ScaleController(Geometry geometry) {
        this.geometry = geometry;
        instance = this;
    }

    // Método estático para obtener la instancia
    public static synchronized ScaleController getInstance() {
        return instance;
    }

    // Métodos para modificar la escala
    public void increaseScale(float amount) {
        setScale(currentScale + amount);
    }

    public void decreaseScale(float amount) {
        setScale(currentScale - amount);
    }

    public void setScale(float scale) {
        // Limitar escala a los valores mínimos y máximos
        currentScale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));

        // Aplicar escala al objeto
        geometry.setLocalScale(new Vector3f(currentScale, currentScale, currentScale));

        // Notificar a los observadores (implementación del patrón Observer)
        ScaleObservable.getInstance().notifyObservers(currentScale);
    }

    public float getCurrentScale() {
        return currentScale;
    }
}