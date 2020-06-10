/* Name: Meecah Cahayon + Eunice Llobet
 * Student ID: 1259825 + 1330233
 */

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Counter {
	
	public static void main(String[] args) {

		try {

        	BufferedImage image = ImageIO.read(new File(args[0]));
        	final int THRESHOLD = 25;
        	int rgb;
	        int height = image.getHeight();
	        int width = image.getWidth();

	        Color white = new Color(255,255,255);
	        int wh = white.getRGB();

	        Color black = new Color(0, 0, 0);
	        int bl = black.getRGB();

	        for (int v = 0; v < height; v++){
	            for (int u = 0; u < width; u++){  

	                rgb = image.getRGB(u, v);

	                int red = (rgb & 0x00ff0000) >> 16;
	                int green = (rgb & 0x0000ff00) >> 8;
	                int blue  =  rgb & 0x000000ff;

	                if(red >= THRESHOLD || blue >= THRESHOLD || green >= THRESHOLD){
	                     image.setRGB(u, v, wh);
	                }
	                else {

	                	image.setRGB(u, v, bl);
	                }
	            }
	        }

        	ImageIO.write(image, "png", new File("test.jpg"));

		}
		catch(Exception eCounter) {

			System.out.println("Error " + eCounter);
			return;
		}
	}
}