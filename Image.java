package zht.c2.imagedeal;

import java.io.*;
import java.awt.image.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*;

/**
 * @author Ren Yuan
 * @modifier zhht1204
 */
public class Image {
	private int width, height;
	private int[][][] rgbArray;
	private int[][] grayArray;
	File originalFile;

	public Image(){}
	//创建图片对象时，读取其rgb及灰度两类信息，存储为成员变量
	public Image(String filePath){
		this.originalFile = new File(filePath);
		this.rgbArray = readRGBImage(originalFile);
		this.grayArray = readGrayImage(originalFile);
	}
	public Image(File originalFile){
		this(originalFile.toString());
	}

	int getWidth() {
		return this.width;
	}
	int getHeight() {
		return this.height;
	}
	int[][][] getRgbArray() {
		return rgbArray;
	}
	int[][] getGrayArray() {
		return grayArray;
	}
	public String getParentPath() {
		return originalFile.getParent();
	}
	public File getFile() {
		return originalFile;
	}

	private int[][][] readRGBImage(File inFile) { //get the 3D array of an RGB image
		//filePath: the path where to load the image
		int[][][] result = null;
		if (!inFile.exists()) {
			System.err.println("File doesn't exist.Please check.");
			return result;
		}
		try {
			BufferedImage bufImg = ImageIO.read(inFile);
			this.height = bufImg.getHeight();
			this.width = bufImg.getWidth();
			result = new int[this.height][this.width][3];
			for (int i = 0; i < this.height; i++) {
				for (int j = 0; j < this.width; j++) {
					//System.out.println(bufImg.getRGB(i, j) & 0xFFFFFF);
					int rgb = bufImg.getRGB(j, i);//visiting sequence for image: column, row
					result[i][j][0] = (rgb & 0xff0000) >> 16;
					result[i][j][1] = (rgb & 0xff00) >> 8;
					result[i][j][2] = (rgb & 0xff);
					//System.out.print(result[i][j][0] + " " + result[i][j][1] + " " + result[i][j][2] + "\t");
				}
				//System.out.println();
			}
		} catch (IOException e) {
			System.out.println("Image reading error!");
			e.printStackTrace();
		} catch (Exception e) {	}
		return result;
	}

	private int[][] readGrayImage(File inFile) { //get the 2D array of an graylevel image
		//filePath: the path where to load the image
		int tempResult[][][] = readRGBImage(inFile);
		int result[][] = new int[this.height][this.width];
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				result[i][j] = (tempResult[i][j][0] + tempResult[i][j][1] + tempResult[i][j][2]) / 3;
				//System.out.print(result[i][j] + "\t");
			}
			//System.out.println();
		}
		return result;
	}

	void setArray(int[][][] array) {
		this.rgbArray = array;
		int[][] grayArray = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				grayArray[i][j] = (array[i][j][0] + array[i][j][1] + array[i][j][2]) / 3;
			}
		}
		this.grayArray = grayArray;
		this.width = array[0].length;
		this.height = array.length;
	}
	void setFile(File file) {
		this.originalFile = file;
	}

	public static void saveImage(int[][][] rgbArray,String filePath) { //save a 3D matrix as a RGB image
		//filePath: the path where to save the file
		//int red[][] = new int[height][width];
		//int green[][] = new int[height][width];
		//int blue[][] = new int[height][width];
		int width = rgbArray[0].length;
		int height = rgbArray.length;
		/////////////////set this matrices

		BufferedImage image = new BufferedImage(width/*Width*/, height/*height*/, BufferedImage.TYPE_INT_RGB);
		//TYPE_INT_RGB or TYPE_INT_ARGB
		//TYPE_INT_ARGB does not work for BMP

		Color c;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				c = new Color(rgbArray[i][j][0], rgbArray[i][j][1], rgbArray[i][j][2]);
				image.setRGB(j, i, c.getRGB()); //column, row!!
			}
		}

		try {
			ImageIO.write(image, "bmp", new File(filePath));
		} catch (IOException ex) {
			System.out.println("Image saving error!");
			Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveImage(int[][] grayArray, String filePath) { //save a 2D matrix as a RGB image
		//Overlated saveImage
		//grayArray [][]: the graylevel Matrix of the image to be saved
		//filePath: the path where to save the file
		int width = grayArray[0].length;
		int height = grayArray.length;
		int rgbArray[][][] = new int[height][width][3];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				rgbArray[i][j][0] = grayArray[i][j];
				rgbArray[i][j][1] = grayArray[i][j];
				rgbArray[i][j][2] = grayArray[i][j];
			}
		}
		saveImage(rgbArray, filePath);
	}

	//重载方法，默认其存储为rgb型图片
	public void saveImage(String filepath){
		this.saveImage(rgbArray, filepath);
	}
	public void saveGrayImage(String filepath){
		this.saveImage(grayArray, filepath);
	}
	public void saveRGBImage(String filepath){
		this.saveImage(rgbArray, filepath);
	}

	public static void main(String[] args) {
		Image image = new Image("F:\\fengchentu.jpg");
		ImageProcesser ip = new ImageProcesser(image, "F:\\");
		ip.rotate(90);
	}

}