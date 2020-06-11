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
	public static Color pink = new Color(179,128,139);
	public static int rgb_pink = pink.getRGB();

	public static int index = 0;

	
	public static void main(String[] args) {

		try {

			//READ FILE
			BufferedImage image = ImageIO.read(new File(args[0]));

			//THRESHOLDING THE IMAGE
			BufferedImage img_threshold = Thresholding(image);
			ImageIO.write(img_threshold, "png", new File("step_1_tresholding.jpg"));

			//EROSION THE IMAGE
			BufferedImage img_erosion = img_threshold;
			for (index = 0; index < 2; index++) {

				img_erosion = Erosion(img_erosion);
				ImageIO.write(img_erosion, "png", new File("step_2_erosion_"+index+".jpg"));
			}

			//BufferedImage img_erosion = Erosion(img_threshold);
			//ImageIO.write(img_erosion, "png", new File("step_2_erosion.jpg"));
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
		int color;

		//FOR EVERY PIXEL
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){  

				//GET THE COLOUR OF THE PIXEL
				color = image.getRGB(x, y);

				//ACCESSING COLOURS
				int red = (color & 0x00ff0000) >> 16;
				int green = (color & 0x0000ff00) >> 8;
				int blue  =  color & 0x000000ff;

				if(red >= THRESHOLD || blue >= THRESHOLD || green >= THRESHOLD){

					image = setpixel(image, x, y, rgb_white, "Thresholding");
				}
				else {

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
		int color;

		//FOR EVERY PIXEL (WHITE TO PINK)
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){ 

				//GET THE COLOUR OF THE PIXEL
				color = image.getRGB(x, y);

				//CHECK IF ITS A BACKGROUND (BLACK)
				if (color == rgb_black) {

					//CHECK IF UP A PHOTO
					if (!(y-1 < 0)) {

						image = setpixel(image, x, y-1, rgb_pink, "Erosion");
					}

					//CHECK IF LEFT IS A PHOTO
					if (!(x-1 < 0)) {

						image = setpixel(image, x-1, y, rgb_pink, "Erosion");
					}

					//CHECK IF RIGHT IS A PHOTO
					if (!(x+1 >= width)) {

						image = setpixel(image, x+1, y, rgb_pink, "Erosion");
					}

					//CHECK IF DOWN IS A PHOTO
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
				color = image.getRGB(x, y);

				//CHECK IF ITS A PIXEL TO SHRINK (PINK)
				if (color == rgb_pink) {

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
		int color;

		//FOR EVERY PIXEL (WHITE TO PINK)
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){ 

				//GET THE COLOUR OF THE PIXEL
				color = image.getRGB(x, y);

				//CHECK IF ITS A CELL (WHITE)
				if (color == rgb_white) {

					//CHECK IF UP A PHOTO
					if (!(y-1 < 0)) {

						image = setpixel(image, x, y-1, rgb_pink, "Erosion");
					}

					//CHECK IF LEFT IS A PHOTO
					if (!(x-1 < 0)) {

						image = setpixel(image, x-1, y, rgb_pink, "Erosion");
					}

					//CHECK IF RIGHT IS A PHOTO
					if (!(x+1 >= width)) {

						image = setpixel(image, x+1, y, rgb_pink, "Erosion");
					}

					//CHECK IF DOWN IS A PHOTO
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
				color = image.getRGB(x, y);

				//CHECK IF ITS A PIXEL TO SHRINK (PINK)
				if (color == rgb_pink) {

					image = setpixel(image, x, y, rgb_black, "ColorChange");
				}
			}
		}

		return image;
	}

	public static BufferedImage setpixel(BufferedImage _image, int x, int y, int color, String method) {

		//DECLARING VARIABLES
		BufferedImage image = _image;

		if ((method.compareTo("Thresholding") == 0) || (method.compareTo("ColorChange") == 0)) {

			image.setRGB(x, y, color);
		}
		else if (method.compareTo("Erosion") == 0) {

			//GET THE COLOUR OF THE PIXEL
			color = image.getRGB(x, y);

			//IF ITS WHITE
			if (color == rgb_white) {

				// CHANGE TO PINK
				image.setRGB(x, y, rgb_pink);
			}
		}

		return image;
	}
}