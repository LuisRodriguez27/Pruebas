package com.ejemplo;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;

public class App extends Application {

    private SimpleApplication jmeApp;
    private Geometry cubeGeometry; // Cubo azul

    @Override
    public void start(Stage primaryStage) {
        // Crear el layout principal (división horizontal: 70% jME, 30% controles)
        HBox root = new HBox();

        // Crear un Canvas para jME (70% de la pantalla)
        Canvas canvas = new Canvas(800, 600);
        setupJme(canvas);
        root.getChildren().add(canvas);

        // Crear un panel para los controles (30% de la pantalla)
        VBox controls = new VBox(10); // Espaciado de 10px entre elementos
        controls.setPrefWidth(300); // Ancho del panel de controles
        controls.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        // Sliders para controlar la rotación, traslación y escala
        Slider rotationSlider = createSlider("Rotación", -360, 360, 0, (obs, oldVal, newVal) -> {
            cubeGeometry.rotate(0, (float) Math.toRadians(newVal.doubleValue()), 0);
        });

        Slider translationSlider = createSlider("Traslación X", -5, 5, 0, (obs, oldVal, newVal) -> {
            cubeGeometry.move(newVal.floatValue(), 0f, 0f);
        });

        Slider scaleSlider = createSlider("Escala", 0.1, 3, 1, (obs, oldVal, newVal) -> {
            cubeGeometry.setLocalScale(newVal.floatValue());
        });

        // Añadir los sliders al panel de controles
        controls.getChildren().addAll(
                new Label("Controles de Transformación"),
                rotationSlider,
                translationSlider,
                scaleSlider
        );

        // Añadir el panel de controles al layout principal
        root.getChildren().add(controls);

        // Configurar la escena y mostrar la ventana
        Scene scene = new Scene(root, 1100, 600);
        primaryStage.setTitle("JavaFX + jMonkeyEngine");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Detener jME cuando se cierre la ventana
        primaryStage.setOnCloseRequest(event -> {
            if (jmeApp != null) {
                jmeApp.stop(); // Detener la aplicación jME
            }
        });
    }

    private void setupJme(Canvas canvas) {
        // Crear una instancia de la aplicación jME
        jmeApp = new SimpleApplication() {
            @Override
            public void simpleInitApp() {
                // Configurar el fondo gris oscuro
                viewPort.setBackgroundColor(ColorRGBA.DarkGray);

                // Crear un cubo azul
                Box box = new Box(1, 1, 1);
                cubeGeometry = new Geometry("Cubo Azul", box);

                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                mat.setColor("Color", ColorRGBA.Blue);
                cubeGeometry.setMaterial(mat);

                // Mover la cámara
                cam.setLocation(new com.jme3.math.Vector3f(0, 2, 5));
                cam.lookAt(new com.jme3.math.Vector3f(0, 0, 0), com.jme3.math.Vector3f.UNIT_Y);

                // Añadir el cubo a la escena
                rootNode.attachChild(cubeGeometry);

                // Agregar ejes de coordenadas para debugging visual
                addDebugAxes();
            }

            private void addDebugAxes() {
                // Crear flechas para los ejes X, Y y Z
                Geometry xAxis = new Geometry("Eje X", new com.jme3.scene.debug.Arrow(com.jme3.math.Vector3f.UNIT_X));
                Geometry yAxis = new Geometry("Eje Y", new com.jme3.scene.debug.Arrow(com.jme3.math.Vector3f.UNIT_Y));
                Geometry zAxis = new Geometry("Eje Z", new com.jme3.scene.debug.Arrow(com.jme3.math.Vector3f.UNIT_Z));

                // Asignar materiales de colores diferentes a cada eje
                Material redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                redMat.setColor("Color", ColorRGBA.Red);
                xAxis.setMaterial(redMat);

                Material greenMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                greenMat.setColor("Color", ColorRGBA.Green);
                yAxis.setMaterial(greenMat);

                Material blueMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                blueMat.setColor("Color", ColorRGBA.Blue);
                zAxis.setMaterial(blueMat);

                // Añadir los ejes a la escena
                rootNode.attachChild(xAxis);
                rootNode.attachChild(yAxis);
                rootNode.attachChild(zAxis);
            }
        };

        // Configurar el canvas de jME
        AppSettings settings = new AppSettings(true);
        settings.setWidth((int) canvas.getWidth());
        settings.setHeight((int) canvas.getHeight());
        jmeApp.setSettings(settings);

        // Crear un contexto de canvas para jME
        jmeApp.createCanvas();
        JmeCanvasContext context = (JmeCanvasContext) jmeApp.getContext();
        context.setSystemListener(jmeApp);

        // Obtener el canvas de AWT y redirigir su renderizado al Canvas de JavaFX
        java.awt.Canvas awtCanvas = context.getCanvas();
        awtCanvas.setSize((int) canvas.getWidth(), (int) canvas.getHeight());

        // Iniciar el renderizado de jME
        jmeApp.startCanvas();

        // Sincronizar el tamaño del canvas cuando cambia
        canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
            awtCanvas.setSize(newVal.intValue(), (int) canvas.getHeight());
        });
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            awtCanvas.setSize((int) canvas.getWidth(), newVal.intValue());
        });
    }

    private Slider createSlider(String label, double min, double max, double initialValue, javafx.beans.value.ChangeListener<? super Number> listener) {
        Slider slider = new Slider(min, max, initialValue);
        slider.setOrientation(Orientation.HORIZONTAL);

        // Listener para manejar el cambio de valor del slider
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Convertir el nuevo valor a float antes de usarlo
            float value = newVal.floatValue();
            listener.changed(obs, oldVal, value);
        });
        //
        return slider;
    }

    public static void main(String[] args) {
        launch(args);
    }
}