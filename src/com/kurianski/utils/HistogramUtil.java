package com.kurianski.utils;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class HistogramUtil {
	
	private HistogramUtil() {}
	
	public static BarChart<String, Number> genBarChart() {
    	final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Cores RGB");       
        yAxis.setLabel("Frequência");
        
        return new BarChart<>(xAxis,yAxis);
    }
    
    public static ColorObject generateColorsArray(PixelReader pr, int width, int height) {
    	ArrayList<Integer> redPixels = new ArrayList<>(Collections.nCopies(256, 0));
    	ArrayList<Integer> greenPixels = new ArrayList<>(Collections.nCopies(256, 0));
    	ArrayList<Integer> bluePixels = new ArrayList<>(Collections.nCopies(256, 0));
    	
    	for(int i = 0; i < width; i++) {
    		for(int j = 0; j < height; j++) {
    			Color color = pr.getColor(i, j);
    			int red = ColorUtil.doubleToRGB(color.getRed());
    			int green = ColorUtil.doubleToRGB(color.getGreen());
    			int blue = ColorUtil.doubleToRGB(color.getBlue());
    			
    			int count = redPixels.get(red);
    			redPixels.set(red, count+1);
    			
    			count = greenPixels.get(green);
    			greenPixels.set(green, count+1);
    			
    			count = bluePixels.get(blue);
    			bluePixels.set(blue, count+1);
    		}
    	}
    	return new ColorObject(redPixels, greenPixels, bluePixels);
    }
    
    public static XYChart.Series<String, Number> genHistogram(ArrayList<Integer> pixels, String cor) {
    	XYChart.Series<String, Number> coresRGB = new XYChart.Series<>();
    	coresRGB.setName(cor);
    	
    	for(Integer i = 0; i < pixels.size(); i++)
    		coresRGB.getData().add(new XYChart.Data<String, Number>(i.toString(), pixels.get(i)));
    	
    	return coresRGB;
    }
}
