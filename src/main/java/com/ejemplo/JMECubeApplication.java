package com.ejemplo;


import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;


import java.util.concurrent.CountDownLatch;

/**
 * Aplicación principal JMonkeyEngine que muestra un cubo escalable
 */
public class JMECubeApplication extends SimpleApplication {

    private static final float SCALE_FACTOR = 0.1f;
    private Geometry cubeGeometry;
    private ScaleController scaleController;
    private CountDownLatch initLatch = new CountDownLatch(1);

    @Override
    public void simpleInitApp() {
        // Crear un cubo
        Box box = new Box(1, 1, 1);
        cubeGeometry = new Geometry("Cubo", box);

        // Crear y asignar material
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        cubeGeometry.setMaterial(mat);

        // Añadir el cubo a la escena
        rootNode.attachChild(cubeGeometry);

        // Configurar la cámara
        cam.setLocation(new Vector3f(3, 3, 3));
        cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);

        // Configurar inputs
        setupKeys();

        // Inicializar controlador de escala
        scaleController = new ScaleController(cubeGeometry);

        // Notificar que la inicialización está completa
        initLatch.countDown();
    }

    private void setupKeys() {
        // Mapear teclas
        inputManager.addMapping("IncreaseScale", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("DecreaseScale", new KeyTrigger(KeyInput.KEY_D));

        // Añadir listener para las teclas
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    if ("IncreaseScale".equals(name)) {
                        scaleController.increaseScale(SCALE_FACTOR);
                    } else if ("DecreaseScale".equals(name)) {
                        scaleController.decreaseScale(SCALE_FACTOR);
                    }
                }
            }
        }, "IncreaseScale", "DecreaseScale");
    }

    public void waitForInit() {
        try {
            initLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ScaleController getScaleController() {
        return scaleController;
    }

    // Método para actualizar escala desde fuentes externas (como JavaFX)
    public void updateScale(float scale) {
        if (scaleController != null) {
            // Asegurar que la actualización se realiza en el hilo de JME
            enqueue(() -> {
                scaleController.setScale(scale);
                return null;
            });
        }
    }
}