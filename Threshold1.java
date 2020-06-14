/* Name: Meecah Cahayon + Eunice Llobet
 * Student ID: 1259825 + 1330233
 */

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Threshold1 {

	//SETTING COLOuR AND CONVERTING TO INT
	public static Color white = new Color(255,255,255);
	public static int rgb_white = white.getRGB();
	public static Color black = new Color(0, 0, 0);
	public static int rgb_black = black.getRGB();
	public static Color pink = new Color(179,126,164);
	public static int rgb_pink = pink.getRGB();

	public static int index = 0;
	public static int[] colorArray;
	
	public static void main(String[] args) {

		try {
			/*
			* FIND THE MEAN OF THE 8 BIT VALUE
			* DIVIDE ORIGINAL IMAGE INTO TWO PORTIONS
			*/

			//READ FILE
			BufferedImage image = ImageIO.read(new File(args[0]));
			BufferedImage img_threshold = image;
			int height = image.getHeight();
			int width = image.getWidth();
			int pixelColor;

			ArrayList<Integer> histogram = getHistogram(image);
			int threshold = 0;
			int allPixels = 0;
			int count = 0;

			for(int i: histogram) {

				allPixels += i;
				
			}
			
			//FOR EVERY PIXEL
			for (int y = 0; y < height; y++){
				for (int x = 0; x < width; x++){  

					count++;
				}
			}

			//Mean value
			threshold = allPixels/100;

			colorArray = image.getRGB(0, 0, width, height, colorArray, 0, width);

			System.out.println("All pixels: " + allPixels);
			System.out.println("Number of pixels: " + count);
			System.out.println("New Threshold: " + threshold);
			System.out.println("");

			// for (int i = 0; i < colorArray.length; i++) {

			// 	System.out.println("Pixel " + i + " : " + colorArray[i]);	
			// }

			// System.out.println("");

			Arrays.sort(colorArray);

			for (int i = 0; i < colorArray.length; i++) {

				System.out.println("Pixel " + i + " : " + colorArray[i]);	
			}

			System.out.println("");

			//double test_threshold = getMedianHistogram(image);

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
					if(red >= threshold || blue >= threshold || green >= threshold){

						//MAKE IT FOREGROUND (WHITE)
						img_threshold = setpixel(image, x, y, rgb_white, "Thresholding");

					}
					else {

						//MAKE IT BACKGROUND (BLACK)
						img_threshold = setpixel(image, x, y, rgb_black, "Thresholding");
					
					}
					
				}
			}

			ImageIO.write(img_threshold, "png", new File("test.jpg"));

		}
		catch(Exception eCounter) {

			System.out.println("Error " + eCounter);
			return;
		}
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

	/*
	*	INITIAL THRESHOLD VALUE
	*/

	//Get histogram
	public static ArrayList<Integer> getHistogram(BufferedImage image) {

		//Set 100 limit to histogram (to exclude outliers)
		ArrayList<Integer> histogram = new ArrayList<Integer>(100);

		int height = image.getHeight();
		int width = image.getWidth();
		int pixelColor;

		//Go through image, and add the first 100 pixels to historgram
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){  

				//GET THE COLOUR OF THE PIXEL
				pixelColor = image.getRGB(x, y);

				//ACCESSING COLOURS
				int red = (pixelColor & 0x00ff0000) >> 16;
				int green = (pixelColor & 0x0000ff00) >> 8;
				int blue  =  pixelColor & 0x000000ff;

				if(histogram.size() < 100) {

					histogram.add(red + green + blue);
				}
			}
		}
		return histogram;
	}

	public static double getMedianHistogram(BufferedImage _image) {

		//DECLARING VARIABLES
		BufferedImage image = _image;
		int height = image.getHeight();
		int width = image.getWidth();
		int pixelColor;

		double median;
		int _index = 0;

		int _frequency = 1;
		int _fdensity = 0;
		int _xWidth = 0;

		double _f2requency = 1;
		double _f2density = 0;
		double _w2idth = 0;
		double _x2totalW = 0;

		while (_index < colorArray.length-1) {
		
			while (colorArray[_index] == colorArray[_index+1]) {
				
				_frequency++;
				_f2requency++;
				_index++;
			}

			//System.out.println("Index " + _index + " Colorpixel " + colorArray[_index] + " : " + _frequency);

			System.out.println("Colorpixel: " + colorArray[_index]);

			System.out.println("-------------------");
			
			System.out.println("Frequency: " + _frequency);
			System.out.println("Width: " + (_index - _xWidth));
			System.out.println("Frequency Density: " + (_frequency*(_index - _xWidth)));

			_fdensity += _frequency*(_index - _xWidth);
			_xWidth += _index - _xWidth;

			System.out.println("Total Frequency Density: " + _fdensity);
			System.out.println("Total Width: " + _xWidth);

			System.out.println("-------------------");

			DecimalFormat df2 = new DecimalFormat("#.##");
			df2.setRoundingMode(RoundingMode.UP);
			_f2requency = 126/(double)_f2requency;
			_w2idth = 126/(double)(_index - _x2totalW);

			System.out.println("126/Frequency: " + df2.format(_f2requency));
			System.out.println("126/Width: " + df2.format(_w2idth));
			System.out.println("126/Frequency Density: " +  df2.format(_f2requency*_w2idth));

			_f2density += _f2requency*_w2idth;
			_x2totalW += 126/(double)(_index - _x2totalW);
			
			System.out.println("Total Another Frequency Density: " +  df2.format(_f2density));
			System.out.println("Total Width: " +  df2.format(_x2totalW));
			System.out.println("");

			_index++;
			_frequency = 1;
			_f2requency = 1;
		}

		median = (int)_f2density*8;
		System.out.println("Median : " + median);

		return median;
	}
}
