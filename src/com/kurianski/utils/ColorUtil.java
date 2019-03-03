package com.kurianski.utils;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class ColorUtil {
	private ColorUtil() {}

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
}
