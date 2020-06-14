/* Name: Meecah Cahayon + Eunice Llobet
 * Student ID: 1259825 + 1330233
 */

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.awt.*;
import javax.swing.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;

class Counter extends Canvas {

	//SETTING COLOuR AND CONVERTING TO INT
	public static Color white = new Color(255,255,255);
	public static int rgb_white = white.getRGB();
	public static Color black = new Color(0, 0, 0);
	public static int rgb_black = black.getRGB();
	public static Color pink = new Color(179,126,164);
	public static int rgb_pink = pink.getRGB();

	public static int count = 1;
	public static int threshold = 0;
	public static int[] colorArray;
	public static int img_height;
	public static int img_width;
	public static int dis_img_height;
	public static int dis_img_width;
	
	public static void main(String[] args) {

		try {

			//READ FILE
			BufferedImage image = ImageIO.read(new File(args[0]));

			//DECLARING VARIABLES
			BufferedImage img_cell;
			int index = 0;
			img_height = image.getHeight();
			img_width = image.getWidth();
			dis_img_height = (img_height/3) + (img_height/3);
			dis_img_width = (img_width/3) + (img_width/3);

			//ORIGINAL IMAGE
			ImageIO.write(image, "png", new File("step_0_original.jpg"));

			//THRESHOLDING THE IMAGE
			img_cell = Thresholding(image);
			ImageIO.write(img_cell, "png", new File("step_1_tresholding.jpg"));

			//EROSION THE IMAGE
			img_cell = img_cell;
			for (index = 0; index < 3; index++) {

				img_cell = Erosion(img_cell);
				ImageIO.write(img_cell, "png", new File("step_2_erosion_"+index+".jpg"));
			}

			//DILUTION THE IMAGE
			img_cell = img_cell;
			for (index = 0; index < 3; index++) {

				img_cell = Dilution(img_cell);
				ImageIO.write(img_cell, "png", new File("step_3_dilution_"+index+".jpg"));
			}

			//REGION LABELING THE IMAGE
			img_cell = RegionLabeling(img_cell);
			ImageIO.write(img_cell, "png", new File("step_4_regionLabel.jpg"));

			//CREATING GUI
			Counter gui = new Counter();
			JFrame frame = new JFrame("Assignment 4: 1259825 + 1330233");

			//SETTING GUI
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setSize(800,800);

			frame.add(gui);
			frame.setVisible(true);
		}
		catch(Exception eCounter) {

			System.out.println("Error " + eCounter);
			return;
		}
	}

	public void paint(Graphics g) {

		//SETTING COLOUR
		Color greyishpink = new Color(179,143,149);
		Color blue = new Color(125,163,179);
		Color brownpink = new Color(127,102,106);

		//SETTING IMAGE
		Image img_original = new ImageIcon("step_0_original.jpg").getImage();
		Image img_threshold = new ImageIcon("step_1_tresholding.jpg").getImage();
		Image img_erosion_0 = new ImageIcon("step_2_erosion_0.jpg").getImage();
		Image img_erosion_1 = new ImageIcon("step_2_erosion_1.jpg").getImage();
		Image img_erosion_2 = new ImageIcon("step_2_erosion_2.jpg").getImage();
		Image img_dilution_0 = new ImageIcon("step_3_dilution_0.jpg").getImage();
		Image img_dilution_1 = new ImageIcon("step_3_dilution_1.jpg").getImage();
		Image img_dilution_2 = new ImageIcon("step_3_dilution_2.jpg").getImage();
		Image img_regionLabel = new ImageIcon("step_4_regionLabel.jpg").getImage();
		
		//SETTING UP VARIABLES
		Canvas canvas = this;
		int xCoord = 200;
		int yCoord = 50;

		//DISPLAY CELL COUNT
		String cellCount = "Cell Count: " + count;
		g.setColor(greyishpink);
		g.setFont(new Font("helvetica", Font.BOLD, 30));
		g.drawString(cellCount, (((dis_img_width*4)/2)+(xCoord)), 70);

		//DISPLAYING IMAGES
		DisplayImage_pl(g, canvas, img_original, brownpink, xCoord, yCoord, "Step 0: Original");

		xCoord = 200;
		yCoord = 50 + dis_img_height + 28 + 40;
		DisplayImage_pl(g, canvas, img_threshold, greyishpink, xCoord, yCoord, ("Step 1: Thresholding ( Value: " + threshold + " )"));

		xCoord = xCoord + dis_img_width + 28 + 15;
		yCoord = yCoord;
		DisplayImage_pl(g, canvas, img_erosion_0, blue, xCoord, yCoord, "Step 2.1: Erosion");

		xCoord = xCoord + dis_img_width + 28 + 15;
		yCoord = yCoord;
		DisplayImage_pl(g, canvas, img_erosion_1, greyishpink, xCoord, yCoord, "Step 2.2: Erosion");

		xCoord = xCoord + dis_img_width + 28 + 15;
		yCoord = yCoord;
		DisplayImage_pl(g, canvas, img_erosion_2, blue, xCoord, yCoord, "Step 2.3: Erosion");

		xCoord = 200;
		yCoord = 50 + (dis_img_height*2) + (28*2) + 80;
		DisplayImage_pl(g, canvas, img_dilution_0, greyishpink, xCoord, yCoord, "Step 3.1: Dilution");

		xCoord = xCoord + dis_img_width + 28 + 15;
		yCoord = yCoord;
		DisplayImage_pl(g, canvas, img_dilution_1, blue, xCoord, yCoord, "Step 3.2: Dilution");

		xCoord = xCoord + dis_img_width + 28 + 15;
		yCoord = yCoord;
		DisplayImage_pl(g, canvas, img_dilution_2, greyishpink, xCoord, yCoord, "Step 3.3: Dilution");

		xCoord = xCoord + dis_img_width + 28 + 15;
		yCoord = yCoord;
		DisplayImage_pl(g, canvas, img_regionLabel, blue, xCoord, yCoord, "Step 4: Region Counting (Fillflood)");
	}

	public static void DisplayImage_pl(Graphics g, Canvas canvas, Image display_image, Color color, 
		int xCoord, int yCoord, String method) {

		//DECLARING VARIABLES
		int block_size = 12;
		int block_space = 4;

		int block_wide;
		int block_height;
		int block_xCoord;
		int block_yCoord;

		// SETTING UP BORDER //
		
		//(TOP BORDER)		
		block_wide = dis_img_width + (block_space*2) + (block_size*2);
		block_height = block_size;
		block_xCoord = xCoord;
		block_yCoord = yCoord;
		drawBorder(g, color, block_xCoord, block_yCoord, block_wide, block_height, method);

		//(BOTTOM BORDER)
		block_wide = dis_img_width + (block_space*2) + (block_size*2);
		block_height = block_size;
		block_xCoord = xCoord;
		block_yCoord = yCoord + block_size + (block_space*2) + dis_img_height;
		drawBorder(g, color, block_xCoord, block_yCoord, block_wide, block_height, null);

		//(LEFT BORDER)
		block_wide = block_size;
		block_height = dis_img_height;
		block_xCoord = xCoord;
		block_yCoord = yCoord + block_size + block_space;
		drawBorder(g, color, block_xCoord, block_yCoord, block_wide, block_height, null);

		//(RIGHT BORDER)
		block_wide = block_size;
		block_height = dis_img_height;
		block_xCoord = xCoord + block_size + (block_space*2) + dis_img_width;
		block_yCoord = yCoord + block_size + block_space;
		drawBorder(g, color, block_xCoord, block_yCoord, block_wide, block_height, null);

		//DISPLAYING THE IMAGE
		g.drawImage(display_image, (xCoord+block_size+block_space), (yCoord+block_size+block_space), dis_img_width, dis_img_height, canvas);
	}

	public static void drawBorder(Graphics g, Color color, int xCoord, int yCoord, int block_wide, int block_height, 
		String method) {

		g.setColor(color);
		g.drawRect(xCoord, yCoord, block_wide, block_height);
		g.fillRect(xCoord+2,yCoord+2, block_wide-4, block_height-4);

		if (method != null) {

			Color dark_blue = new Color(61,90,102);
			int middle = block_wide/3;
			int string_w = middle/method.length();
			
			g.setColor(dark_blue);
			g.setFont(new Font("helvetica", Font.PLAIN, 12));
			g.drawString(method, (xCoord+middle+string_w), yCoord-10);
		}	
	}

	public static BufferedImage Thresholding(BufferedImage _image) {

		//DECLARING VARIABLES
		BufferedImage image = _image;

		ArrayList<Integer> histogram = new ArrayList<Integer>();
		int pixelColor;
		int totalRGB = 0;
		int meanValue = 0;
		threshold = 0;

		//FOR EVERY PIXEL
		for (int y = 0; y < img_height; y++){
			for (int x = 0; x < img_width; x++){  

				//GET THE COLOUR OF THE PIXEL
				pixelColor = image.getRGB(x, y);

				//ACCESSING COLOURS
				int red = (pixelColor & 0x00ff0000) >> 16;
				int green = (pixelColor & 0x0000ff00) >> 8;
				int blue  =  pixelColor & 0x000000ff;

				//Get all the RGB values of all pixels
				histogram.add(red + green + blue);
			}
		}

		//Total all RGB values
		for(int i: histogram) {

			totalRGB += i;
		}

		//Get the mean value
		meanValue = totalRGB/histogram.size();
		//Divide mean value to get a good threshold value
		threshold = meanValue/2;

		//FOR EVERY PIXEL
		for (int y = 0; y < img_height; y++){
			for (int x = 0; x < img_width; x++){  

				//GET THE COLOUR OF THE PIXEL
				pixelColor = image.getRGB(x, y);

				//ACCESSING COLOURS
				int red = (pixelColor & 0x00ff0000) >> 16;
				int green = (pixelColor & 0x0000ff00) >> 8;
				int blue  =  pixelColor & 0x000000ff;

				//IF PIXEL COLOUR IS GREATER OR EQUEAL TO THE TRESHOLD
				if(red >= threshold || blue >= threshold || green >= threshold){

					//MAKE IT FOREGROUND (WHITE)
					image = setpixel(image, x, y, rgb_white, "Thresholding");
				}
				else {

					//MAKE IT BACKGROUND (BLACK)
					image = setpixel(image, x, y, rgb_black, "Thresholding");
				}
			}
		}

		return image;
	}

	public static BufferedImage Erosion(BufferedImage _image) {

		//DECLARING VARIABLES
		BufferedImage image = _image;
		int pixelColor;

		//FOR EVERY PIXEL (WHITE TO PINK)
		for (int y = 0; y < img_height; y++){
			for (int x = 0; x < img_width; x++){ 

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
					if (!(x+1 >= img_width)) {

						image = setpixel(image, x+1, y, rgb_pink, "Erosion");
					}

					//CHECK IF DOWN IS WITHIN THE IMAGE BOUNDERY
					if (!(y+1 >= img_height)) {

						image = setpixel(image, x, y+1, rgb_pink, "Erosion");
					}
				}
			}
		}

		//FOR EVERY PIXEL (PINK TO BLACK)
		for (int y = 0; y < img_height; y++){
			for (int x = 0; x < img_width; x++){ 

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
		int pixelColor;

		//FOR EVERY PIXEL (WHITE TO PINK)
		for (int y = 0; y < img_height; y++){
			for (int x = 0; x < img_width; x++){ 

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
					if (!((x-1 < 0) || (y+1 >= img_height))) {

						image = setpixel(image, x-1, y+1, rgb_pink, "Dilution");
					}

					//CHECK IF DOWN IS WITHIN THE IMAGE BOUNDERY
					if (!(y+1 >= img_height)) {

						image = setpixel(image, x, y+1, rgb_pink, "Dilution");
					}

					//CHECK IF RIGHT BOTTOM CORNER IS WITHIN THE IMAGE BOUNDERY
					if (!((x+1 >= img_width) || (y+1 >= img_height))) {

						image = setpixel(image, x+1, y+1, rgb_pink, "Dilution");
					}

					//CHECK IF RIGHT IS WITHIN THE IMAGE BOUNDERY
					if (!(x+1 >= img_width)) {

						image = setpixel(image, x+1, y, rgb_pink, "Dilution");
					}

					//CHECK IF RIGHT TOP CORNER IS WITHIN THE IMAGE BOUNDERY
					if (!((x+1 >= img_width) || (y-1 < 0))) {

						image = setpixel(image, x+1, y-1, rgb_pink, "Dilution");
					}
				}
			}
		}

		//FOR EVERY PIXEL (PINK TO BLACK)
		for (int y = 0; y < img_height; y++){
			for (int x = 0; x < img_width; x++){ 

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
		int pixelColor;
		int label = rgb_pink;

		//FOR EVERY PIXEL
		for (int y = 0; y < img_height; y++){
			for (int x = 0; x < img_width; x++){

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
				if (!(x+1 >= img_width)) {

					xCoord.push(x+1); yCoord.push(y);
				}

				//CHECK IF DOWN IS WITHIN THE IMAGE BOUNDERY
				if (!(y+1 >= img_height)) {

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

		//IF THRESHOLDING METHOD OR SIMPLY CHANGING COLOUR
		if ((method.compareTo("Thresholding") == 0) || (method.compareTo("ColorChange") == 0)) {

			image.setRGB(x, y, _color);
		}
		//IF EROSION METHOD
		else if (method.compareTo("Erosion") == 0) {

			//GET THE COLOUR OF THE PIXEL
			pixelColor = image.getRGB(x, y);

			//IF ITS WHITE
			if (pixelColor == rgb_white) {

				// CHANGE TO PINK
				image.setRGB(x, y, _color);
			}
		}
		//IF DILUTION METHOD
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