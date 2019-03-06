package com.kurianski.imagedit;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.kurianski.utils.ColorObject;
import com.kurianski.utils.ColorUtil;
import com.kurianski.utils.HistogramUtil;
import com.kurianski.utils.ImageUtil;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
 
public class Main extends Application {
    private static Image originalImage = null;
    private static WritableImage editableImage = null;
    private static Button saveImage = null;
    private static Button histogramsOriginal, histogramsNewImage = null;
    private static ImageView originalImageContainer = null;
    private static ImageView editedImageContainer = null;
    private static Slider slider = null;
    
    private static int bright_bonus = 0;
    
    @Override
    public void start(Stage stage) {
        originalImage = ImageUtil.readImage(stage);
        originalImageContainer = ImageUtil.createImageContainer(originalImage);
        
        editableImage = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
        editedImageContainer = ImageUtil.createImageContainer(originalImage);

        slider = new Slider(-255, 255, 0);
        slider.setMajorTickUnit(35);
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            bright_bonus = newValue.intValue();
            updateImage(originalImage, editableImage, bright_bonus);
        });

        saveImage = new Button();
        saveImage.setText("Save image");
        saveImage.setOnAction(new EventHandler<ActionEvent>() {
        
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = new File(directoryChooser.showDialog(stage).toString()+File.separatorChar+"output.png");

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(editableImage, null), "png", selectedDirectory);
                } catch(IOException e) {
                    System.out.println("salve");
                }
            }
        });
        
        histogramsOriginal = new Button();
        histogramsOriginal.setText("Generate Histograms [Original image]");
        histogramsOriginal.setOnAction(new EventHandler<ActionEvent>() {
        
            @Override
            public void handle(ActionEvent event) {
            	showHistogram(originalImage, "red");
                showHistogram(originalImage, "green");
                showHistogram(originalImage, "blue");
            }
        });
        
        histogramsNewImage = new Button();
        histogramsNewImage.setText("Generate Histograms [Edited image]");
        histogramsNewImage.setOnAction(new EventHandler<ActionEvent>() {
        
            @Override
            public void handle(ActionEvent event) {
            	showHistogram(editableImage, "red");
                showHistogram(editableImage, "green");
                showHistogram(editableImage, "blue");
            }
        });

        HBox imageContainers = new HBox();
        imageContainers.getChildren().addAll(originalImageContainer, editedImageContainer);
        imageContainers.setAlignment(Pos.CENTER);
        imageContainers.setSpacing(150);
        imageContainers.setPadding(new Insets(50));
        
        HBox buttonContainers = new HBox();
        buttonContainers.getChildren().addAll(slider, saveImage, histogramsOriginal, histogramsNewImage);
        buttonContainers.setAlignment(Pos.CENTER);
        buttonContainers.setSpacing(50);
        buttonContainers.setPadding(new Insets(15));
        
        BorderPane borderpane = new BorderPane();
        borderpane.setTop(imageContainers);
        borderpane.setBottom(buttonContainers);
        borderpane.setPrefSize(1000, 600);
    
        Scene scene = new Scene(borderpane);
        
        stage.setTitle("Image Editor");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void showHistogram(Image image, String color) {
    	BarChart<String, Number> histogram = HistogramUtil.genBarChart();
    	ColorObject colorObject = HistogramUtil.generateColorsArray(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
    	
    	XYChart.Series<String, Number> data = null;
    	
    	if(color.equals("red")) {
    		data = HistogramUtil.genHistogram(colorObject.red, "Vermelho");
    	} else if(color.equals("green")) {
    		data = HistogramUtil.genHistogram(colorObject.green, "Verde");
    	} else if(color.equals("blue")) {
    		data = HistogramUtil.genHistogram(colorObject.blue, "Azul");
    	} else {
    		throw new IllegalArgumentException("Cor inv�lida");
    	}
        
        histogram.getData().add(data);
        histogram.setBarGap(17);
        histogram.setCategoryGap(0);
    	
    	Stage histogramWindow = new Stage();
    	Scene histogramGraph = new Scene(histogram, 500, 500);
        histogramWindow.setScene(histogramGraph);
        histogramWindow.setTitle(color);
        histogramWindow.show();
    }

    private static void updateImage(Image originalImage, WritableImage newImage, int constant) {
    	editedImageContainer.setImage(newImage);
        PixelReader reader = originalImage.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();
        
        for(int line = 0; line < originalImage.getWidth(); line++) {
            for(int col = 0; col < originalImage.getHeight(); col++) {
                Color color = reader.getColor(line, col);
                color = ColorUtil.increaseBrightness(color, bright_bonus);
                writer.setColor(line, col, color);
            }
        }
    }
    
     public static void main(String[] args) {
         launch(args);
    }
 }
