/* Name: Meecah Cahayon + Eunice Llobet
 * Student ID: 1259825 + 1330233
 */

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Counter {

	//SETTING COLOuR AND CONVERTING TO INT
	public static Color white = new Color(255,255,255);
	public static int rgb_white = white.getRGB();
	public static Color black = new Color(0, 0, 0);
	public static int rgb_black = black.getRGB();
	public static Color pink = new Color(179,126,164);
	public static int rgb_pink = pink.getRGB();

	public static int index = 0;

	
	public static void main(String[] args) {

		try {

			//READ FILE
			BufferedImage image = ImageIO.read(new File(args[0]));

			//DECLARING VARIABLES
			BufferedImage img_threshold;
			BufferedImage img_erosion;
			BufferedImage img_dilution;
			BufferedImage img_regionLabel;

			//THRESHOLDING THE IMAGE
			img_threshold = Thresholding(image);
			ImageIO.write(img_threshold, "png", new File("step_1_tresholding.jpg"));

			//EROSION THE IMAGE
			img_erosion = img_threshold;
			for (index = 0; index < 2; index++) {

				img_erosion = Erosion(img_erosion);
				ImageIO.write(img_erosion, "png", new File("step_2_erosion_"+index+".jpg"));
			}

			//DILUTION THE IMAGE
			img_dilution = img_erosion;
			for (index = 0; index < 2; index++) {

				img_dilution = Dilution(img_dilution);
				ImageIO.write(img_dilution, "png", new File("step_3_dilution_"+index+".jpg"));
			}

			//REGION LABELING THE IMAGE
			img_regionLabel = RegionLabeling(img_dilution);
			ImageIO.write(img_threshold, "png", new File("step_4_regionLabel.jpg"));
		}
		catch(Exception eCounter) {

			System.out.println("Error " + eCounter);
			return;
		}
	}

	public static BufferedImage Thresholding(BufferedImage _image) {

		//DECLARING VARIABLES
		BufferedImage image = _image;

		final int THRESHOLD = 25;
		int height = image.getHeight();
		int width = image.getWidth();
		int pixelColor;

		//FOR EVERY PIXEL
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){  

				//GET THE COLOUR OF THE PIXEL
				pixelColor = image.getRGB(x, y);

				//ACCESSING COLOURS
				int red = (pixelColor & 0x00ff0000) >> 16;
				int green = (pixelColor & 0x0000ff00) >> 8;
				int blue  =  pixelColor & 0x000000ff;

				//IF PIXEL COLOUR IS GREATER OR EQUEAL TO THE TRESHOLD
				if(red >= THRESHOLD || blue >= THRESHOLD || green >= THRESHOLD){

					//MAKE IT FOREGROUND (WHITE)
					image = setpixel(image, x, y, rgb_white, "Thresholding");
				}
				else {

					//MAKE IT BACKGROUDN (BLACK)
					image = setpixel(image, x, y, rgb_black, "Thresholding");
				}
			}
		}

		return image;
	}

	public static BufferedImage Erosion(BufferedImage _image) {

		//DECLARING VARIABLES
		BufferedImage image = _image;

		int height = image.getHeight();
		int width = image.getWidth();
		int pixelColor;

		//FOR EVERY PIXEL (WHITE TO PINK)
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){ 

				//GET THE COLOUR OF THE PIXEL
				pixelColor = image.getRGB(x, y);

				//CHECK IF ITS A BACKGROUND (BLACK)
				if (pixelColor == rgb_black) {

					//CHECK IF UP IS WITHIN THE IMAGE BOUNDERY
					if (!(y-1 < 0)) {

						image = setpixel(image, x, y-1, rgb_pink, "Erosion");
					}

					//CHECK IF LEFT IS WITHIN THE IMAGE BOUNDERY
					if (!(x-1 < 0)) {

						image = setpixel(image, x-1, y, rgb_pink, "Erosion");
					}

					//CHECK IF RIGHT IS WITHIN THE IMAGE BOUNDERY
					if (!(x+1 >= width)) {

						image = setpixel(image, x+1, y, rgb_pink, "Erosion");
					}

					//CHECK IF DOWN IS WITHIN THE IMAGE BOUNDERY
					if (!(y+1 >= height)) {

						image = setpixel(image, x, y+1, rgb_pink, "Erosion");
					}
				}
			}
		}

		//FOR EVERY PIXEL (PINK TO BLACK)
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){ 

				//GET THE COLOUR OF THE PIXEL
				pixelColor = image.getRGB(x, y);

				//CHECK IF ITS A PIXEL TO SHRINK (PINK)
				if (pixelColor == rgb_pink) {

					image = setpixel(image, x, y, rgb_black, "ColorChange");
				}
			}
		}

		return image;
	}

	public static BufferedImage Dilution(BufferedImage _image) {

		//DECLARING VARIABLES
		BufferedImage image = _image;

		int height = image.getHeight();
		int width = image.getWidth();
		int pixelColor;

		//FOR EVERY PIXEL (WHITE TO PINK)
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){ 

				//GET THE COLOUR OF THE PIXEL
				pixelColor = image.getRGB(x, y);

				//CHECK IF ITS A FOREGROUND (WHITE)
				if (pixelColor == rgb_white) {

					//CHECK IF UP IS WITHIN THE IMAGE BOUNDERY
					if (!(y-1 < 0)) {

						image = setpixel(image, x, y-1, rgb_pink, "Dilution");
					}

					//CHECK IF LEFT TOP CORNER IS WITHIN THE IMAGE BOUNDERY
					if (!((x-1 < 0) || (y-1 < 0))) {

						image = setpixel(image, x-1, y-1, rgb_pink, "Dilution");
					}

					//CHECK IF LEFT IS WITHIN THE IMAGE BOUNDERY
					if (!(x-1 < 0)) {

						image = setpixel(image, x-1, y, rgb_pink, "Dilution");
					}

					//CHECK IF LEFT BOTTOM CORNER IS WITHIN THE IMAGE BOUNDERY
					if (!((x-1 < 0) || (y+1 >= height))) {

						image = setpixel(image, x-1, y+1, rgb_pink, "Dilution");
					}

					//CHECK IF DOWN IS WITHIN THE IMAGE BOUNDERY
					if (!(y+1 >= height)) {

						image = setpixel(image, x, y+1, rgb_pink, "Dilution");
					}

					//CHECK IF RIGHT BOTTOM CORNER IS WITHIN THE IMAGE BOUNDERY
					if (!((x+1 >= width) || (y+1 >= height))) {

						image = setpixel(image, x+1, y+1, rgb_pink, "Dilution");
					}

					//CHECK IF RIGHT IS WITHIN THE IMAGE BOUNDERY
					if (!(x+1 >= width)) {

						image = setpixel(image, x+1, y, rgb_pink, "Dilution");
					}

					//CHECK IF RIGHT TOP CORNER IS WITHIN THE IMAGE BOUNDERY
					if (!((x+1 >= width) || (y-1 < 0))) {

						image = setpixel(image, x+1, y-1, rgb_pink, "Dilution");
					}
				}
			}
		}

		//FOR EVERY PIXEL (PINK TO BLACK)
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){ 

				//GET THE COLOUR OF THE PIXEL
				pixelColor = image.getRGB(x, y);

				//CHECK IF ITS A PIXEL TO SHRINK (PINK)
				if (pixelColor == rgb_pink) {

					image = setpixel(image, x, y, rgb_white, "ColorChange");
				}
			}
		}

		return image;
	}

	public static BufferedImage RegionLabeling(BufferedImage _image) {

		//DECLARING VARIABLES
		BufferedImage image = _image;

		int height = image.getHeight();
		int width = image.getWidth();
		int pixelColor;

		int label = rgb_pink;
		int count = 0;

		//FOR EVERY PIXEL
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){

				System.out.println("Label: " + count + " Colour: " + label); 

				//GET THE COLOUR OF THE PIXEL
				pixelColor = image.getRGB(x, y);

				//CHECK IF ITS A FOREGROUND (WHITE)
				if (pixelColor == rgb_white) {

					image = FloodFill(image, x, y, label);

					count++;
					label += 10000;
				}
			}
		}

		return image;
	}

	public static BufferedImage FloodFill(BufferedImage _image, int _x, int _y, int _color) {

		//DECLARING VARIABLES
		BufferedImage image = _image;

		Stack<Integer> xCoord = new Stack<Integer>();
		Stack<Integer> yCoord = new Stack<Integer>();
		int height = image.getHeight();
		int width = image.getWidth();
		int pixelColor;
		int x = _x;
		int y = _y;

		xCoord.push(x); yCoord.push(y);

		while (!xCoord.empty()) {

			x = xCoord.pop();
			y = yCoord.pop();

			//GET THE COLOUR OF THE PIXEL
			pixelColor = image.getRGB(x, y);

			//CHECK IF ITS A FOREGROUND (WHITE)
			if (pixelColor == rgb_white) {

				//FILL THE PIXEL
				image = setpixel(image, x, y, _color, "ColorChange");
				
				//CHECK IF UP IS WITHIN THE IMAGE BOUNDERY
				if (!(y-1 < 0)) {

					xCoord.push(x); yCoord.push(y-1);
				}

				//CHECK IF LEFT IS WITHIN THE IMAGE BOUNDERY
				if (!(x-1 < 0)) {

					xCoord.push(x-1); yCoord.push(y);
				}

				//CHECK IF RIGHT IS WITHIN THE IMAGE BOUNDERY
				if (!(x+1 >= width)) {

					xCoord.push(x+1); yCoord.push(y);
				}

				//CHECK IF DOWN IS WITHIN THE IMAGE BOUNDERY
				if (!(y+1 >= height)) {

					xCoord.push(x); yCoord.push(y+1);
				}	
			}
		}

		return image;
	}

	public static BufferedImage setpixel(BufferedImage _image, int x, int y, int _color, String method) {

		//DECLARING VARIABLES
		BufferedImage image = _image;
		int pixelColor;

		if ((method.compareTo("Thresholding") == 0) || (method.compareTo("ColorChange") == 0)) {

			image.setRGB(x, y, _color);
		}
		else if (method.compareTo("Erosion") == 0) {

			//GET THE COLOUR OF THE PIXEL
			pixelColor = image.getRGB(x, y);

			//IF ITS WHITE
			if (pixelColor == rgb_white) {

				// CHANGE TO PINK
				image.setRGB(x, y, _color);
			}
		}
		else if (method.compareTo("Dilution") == 0) {

			//GET THE COLOUR OF THE PIXEL
			pixelColor = image.getRGB(x, y);

			//IF ITS WHITE
			if (pixelColor == rgb_black) {

				// CHANGE TO PINK
				image.setRGB(x, y, _color);
			}
		}

		return image;
	}
}