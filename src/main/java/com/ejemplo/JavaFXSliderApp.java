package com.ejemplo;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;




/**
 * Aplicación JavaFX que contiene un slider para controlar la escala del cubo
 * Diseñada para una mejor integración con JME
 */
public class JavaFXSliderApp implements ScaleObserver {

    private Slider scaleSlider;
    private boolean updating = false;
    private JMECubeApplication jmeApp;
    private Stage stage;

    public void initialize(JMECubeApplication jmeApp) {
        this.jmeApp = jmeApp;

        // Esperar a que JME inicialice
        jmeApp.waitForInit();

        // Registrarse como observador
        ScaleObservable.getInstance().addObserver(this);

        // Crear y mostrar la ventana JavaFX
        createAndShowStage();
    }

    private void createAndShowStage() {
        stage = new Stage();

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Control de Escala del Cubo");

        // Crear slider
        scaleSlider = new Slider(0.1, 5.0, 1.0);
        scaleSlider.setShowTickLabels(true);
        scaleSlider.setShowTickMarks(true);
        scaleSlider.setMajorTickUnit(1.0);
        scaleSlider.setMinorTickCount(4);
        scaleSlider.setBlockIncrement(0.1);

        Label valueLabel = new Label("Escala: 1.0");

        // Añadir listener al slider
        scaleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!updating) {
                float scale = newVal.floatValue();
                valueLabel.setText(String.format("Escala: %.2f", scale));

                // Actualizar el cubo en JME de forma segura
                if (jmeApp != null) {
                    jmeApp.updateScale(scale);
                }
            }
        });

        // Añadir componentes al layout
        root.getChildren().addAll(titleLabel, scaleSlider, valueLabel);

        // Crear y mostrar la escena
        Scene scene = new Scene(root, 300, 150);
        stage.setTitle("Control de Escala");
        stage.setScene(scene);

        // Mostrar en el lado derecho de la pantalla para no superponerse con JME
        stage.setX(800);
        stage.setY(200);

        stage.show();

        // Configurar cierre de la aplicación
        stage.setOnCloseRequest(event -> {
            ScaleObservable.getInstance().removeObserver(this);
            // No cerrar JME cuando se cierra el slider
            event.consume();
            stage.hide();
        });
    }

    @Override
    public void onScaleChanged(float scale) {
        // Actualizar el slider cuando la escala cambia desde JME
        Platform.runLater(() -> {
            updating = true;
            scaleSlider.setValue(scale);
            updating = false;
        });
    }

    public void show() {
        if (stage != null) {
            Platform.runLater(() -> stage.show());
        }
    }

    public void hide() {
        if (stage != null) {
            Platform.runLater(() -> stage.hide());
        }
    }
}