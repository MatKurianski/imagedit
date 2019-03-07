package com.kurianski.utils;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class ColorUtil {
	private ColorUtil() {}

	public static Color increaseBrightness(Color color, int c) {
	    ArrayList<Integer> colors_rgb = new ArrayList<>(3);
	    double[] colors = {color.getRed(), color.getGreen(), color.getBlue()};
		// As cores por padrão em Java não são RGB, e sim entre 0 e 1
		// Abaixo faço a conversão pra RGB e incremento uma constante solicitado no exercício
	    
	    for(double double_color : colors) {
	        int rgb_color = doubleToRGB(double_color) + c;
	        if(rgb_color > 255) rgb_color = 255;
	        else if(rgb_color < 0) rgb_color = 0;
	        colors_rgb.add(rgb_color);
	    }

	    return Color.rgb(
			colors_rgb.get(0),
			colors_rgb.get(1),
			colors_rgb.get(2),
			color.getOpacity()
		);
	}

	public static int doubleToRGB(double value) {
	    return (int) (value*255);
	}
}
