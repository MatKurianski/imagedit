package com.kurianski.utils;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class ImageUtil {
	
private ImageUtil() {}

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
}
