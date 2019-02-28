package com.kurianski.imagedit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
 
public class Main extends Application {
    private static Image originalImage = null;
    private static WritableImage editableImage = null;
    private static Button saveImage = null;
    private static ImageView originalImageContainer = null;
    private static ImageView editedImageContainer = null;
    private static Slider slider = null;
    
    private static int bright_bonus = 0;
    
    @Override
    public void start(Stage stage) {
        originalImage = readImage(stage);
        
        originalImageContainer = createImageContainer(originalImage);
        editedImageContainer = createImageContainer(originalImage); //temp

        slider = new Slider(0, 255, 0);
        slider.setMajorTickUnit(35);
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            bright_bonus = newValue.intValue();
            updateImage();
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

        HBox imageContainers = new HBox();
        imageContainers.getChildren().addAll(originalImageContainer, editedImageContainer);
        imageContainers.setAlignment(Pos.CENTER);
        imageContainers.setSpacing(150);
        imageContainers.setPadding(new Insets(50));
        
        HBox buttonContainers = new HBox();
        buttonContainers.getChildren().addAll(slider, saveImage);
        buttonContainers.setAlignment(Pos.CENTER);
        buttonContainers.setSpacing(50);
        buttonContainers.setPadding(new Insets(15));
        
        BorderPane borderpane = new BorderPane();
        borderpane.setTop(imageContainers);
        borderpane.setBottom(buttonContainers);
        borderpane.setPrefSize(800, 600);
    
        Scene scene = new Scene(borderpane);
        
        stage.setTitle("Image Editor");
        stage.setScene(scene);
        stage.show();
    }

    public static void updateImage() {
        if(editableImage == null) {
            editableImage = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
            editedImageContainer.setImage(editableImage);
        }
        
        PixelReader reader = originalImage.getPixelReader();
        PixelWriter writer = editableImage.getPixelWriter();
        
        for(int line = 0; line < originalImage.getWidth(); line++) {
            for(int col = 0; col < originalImage.getHeight(); col++) {
                Color color = reader.getColor(line, col);
                color = increaseBrightness(color, bright_bonus);
                writer.setColor(line, col, color);
            }
        }
    }

    public static Image readImage(Stage stage) {
        FileChooser fileSelection = new FileChooser();
        fileSelection.getExtensionFilters().add(new ExtensionFilter("Image file", "*.jpeg", "*.png", "*.jpg"));
        File file = fileSelection.showOpenDialog(stage);
        return new Image(file.toURI().toString());
    }
    
    public static ImageView createImageContainer(Image image) {
        ImageView imageContainer = new ImageView();
        imageContainer.setImage(image);
        imageContainer.setFitWidth(250);
        imageContainer.setPreserveRatio(true);
        imageContainer.setSmooth(true);
        imageContainer.setCache(true);
        
        return imageContainer;
    }
    
    public static Color increaseBrightness(Color color, int c) {
        ArrayList<Integer> colors_rgb = new ArrayList<>();
        double[] colors = {color.getRed(), color.getGreen(), color.getBlue()};
        
        for(double double_color : colors) {
            int rgb_color = doubleToRGB(double_color) + c;
            if(rgb_color > 255) rgb_color = 255;
            colors_rgb.add(rgb_color);
        }
        return Color.rgb(colors_rgb.get(0), colors_rgb.get(1), colors_rgb.get(2), color.getOpacity());
    }
    
    public static int doubleToRGB(double value) {
        return (int) (value*255);
    }
    
     public static void main(String[] args) {
         launch(args);
    }
 }
