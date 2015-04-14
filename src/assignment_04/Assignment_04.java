/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment_04;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Barry Speller
 */
public class Assignment_04 extends Application {

    private static GroceryLineSimulation simulation;
    private static TextArea taSimulationLog;
    
    private static NumberAxis xAxis;
    private static NumberAxis yAxis;
    private static LineChart<Number, Number> lineChart;
    
    private static Series queueSizeSeries;
    private static Series customerCountSeries;
    
    private static TextField tfArrivalInterval;
    private static TextField tfServiceInterval;
    private static TextField tfSimulationInterval;

    @Override
    public void start(Stage primaryStage) {
        xAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis = new NumberAxis();
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setMinWidth(900);
        lineChart.setCreateSymbols(false);
        
        taSimulationLog = new TextArea();
        taSimulationLog.setStyle("-fx-font-family:'Lucida Sans Typewriter'; -fx-font-size:12;");
        taSimulationLog.setMinWidth(900);

        tfArrivalInterval = new TextField("4");
        tfServiceInterval = new TextField("4");
        tfSimulationInterval  = new TextField("720");
        Button btnRunSim = new Button();
        btnRunSim.setText("Run Simulation");
        btnRunSim.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                handleRunClick();
                } catch(NumberFormatException e) {
                    System.out.println("Please enter a valid integer for each interval");
                }
                
            }
        });
        Button btnClearSim = new Button();
        btnClearSim.setText("Clear Simulation");
        btnClearSim.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                handleClearClick();
            }
        });
        
        Separator separator1 = new Separator();

        GridPane controlGrid = new GridPane();
        controlGrid = new GridPane();
        controlGrid.setMinWidth(900);
        controlGrid.setPadding(new Insets(-10,0,0,0));
        controlGrid.setHgap(10);
        controlGrid.setVgap(2);
        
        controlGrid.add(new Label("Arrival Interval"), 0, 0);
        controlGrid.add(new Label("Service Interval"), 1, 0);
        controlGrid.add(new Label("Simulation Interval"), 2, 0);
        controlGrid.add(tfArrivalInterval, 0, 1);
        controlGrid.add(tfServiceInterval, 1, 1);
        controlGrid.add(tfSimulationInterval, 2, 1);
        controlGrid.add(btnRunSim, 3, 1);
        controlGrid.add(btnClearSim, 4, 1);
        controlGrid.setAlignment(Pos.CENTER);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setSpacing(20);
        root.getChildren().addAll(lineChart, taSimulationLog, separator1, controlGrid);

        Scene scene = new Scene(root, 940, 600);
        scene.getStylesheets().add("assignment_04/assignment_04.css");

        primaryStage.setTitle("Grocery Line Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void handleRunClick() {
        int arrivalInterval = Integer.parseInt(tfArrivalInterval.getText());
        int serviceInterval = Integer.parseInt(tfServiceInterval.getText());
        int simulationInterval = Integer.parseInt(tfSimulationInterval.getText());
        simulation = new GroceryLineSimulation(arrivalInterval, serviceInterval, simulationInterval);
        simulation.runSimulation();
        taSimulationLog.setText(simulation.getSimulationLog());
        
        queueSizeSeries = new XYChart.Series();
        customerCountSeries = new XYChart.Series();
        
        queueSizeSeries.setName("Simulation: " 
                + Integer.toString(arrivalInterval) + "/" 
                + Integer.toString(serviceInterval) + "/" 
                + Integer.toString(simulationInterval));

        int[] sizeLog = simulation.getQueueSizeLog();
        for (int i = 0; i < sizeLog.length; i++) {
            queueSizeSeries.getData().add(new XYChart.Data(i, sizeLog[i]));
        }
        xAxis.setUpperBound(sizeLog.length);
        yAxis.setUpperBound(simulation.getMaxNumberOfCustomersInQueue());
        
        lineChart.getData().add(queueSizeSeries);
    }
    
    private static void handleClearClick() {
        lineChart.getData().clear();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
