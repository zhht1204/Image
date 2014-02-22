import java.io.*;
import java.awt.image.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
import javax.imageio.*;

/**
 *
 * @author Ren Yuan
 * @modifier zhht1204
 */
public class Image {
	private int width,height;
	private int[][][] rgbArray;
	private int[][] grayArray;

	public Image(){}
	//创建图片对象时，读取其rgb及灰度两类信息，存储为成员变量
	public Image(String filepath){
		this.rgbArray = readRGBImage(filepath);
		this.grayArray = readGrayImage(filepath);
	}

	private int[][][] readRGBImage(String filePath) { //get the 3D array of an RGB image
		//filePath: the path where to load the image
		File file = new File(filePath);
		int[][][] result = null;
		if (!file.exists()) {
			return result;
		}
		try {
			BufferedImage bufImg = ImageIO.read(file);
			//Get the picture's width and height as member variables.
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
		}
		return result;
	}

	private int[][] readGrayImage(String filePath) { //get the 2D array of an graylevel image
		//filePath: the path where to load the image
		int tempResult[][][] = readRGBImage(filePath);
		int height = tempResult.length;
		int width = tempResult[0].length;
		int result[][] = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				result[i][j] = (tempResult[i][j][0] + tempResult[i][j][1] + tempResult[i][j][2]) / 3;
				//System.out.print(result[i][j] + "\t");
			}
			//System.out.println();
		}
		return result;
	}

	public void saveImage(int[][][] rgbArray,String filePath) { //save a 3D matrix as a RGB image
		int height = rgbArray.length;
		int width = rgbArray[0].length;
		//filePath: the path where to save the file
		//int red[][] = new int[height][width];
		//int green[][] = new int[height][width];
		//int blue[][] = new int[height][width];
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
		}
	}

	public void saveImage(int grayArray[][], String filePath) { //save a 2D matrix as a RGB image
		//Overlated saveImage
		//grayArray [][]: the graylevel Matrix of the image to be saved
		//filePath: the path where to save the file
		int height = grayArray.length;
		int width = grayArray[0].length;
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

	public void generateRandomGrayImage(int m, int n) { //m: number of columns;n:number of rows
		int randomImg[][] = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				randomImg[i][j] = (int) (Math.random() * 256);
			}
		}
		saveImage(randomImg, "rimg.bmp");
	}

	/*添加功能：以下*/
	/*①旋转（默认绕中心）（angle为正时顺时针）*/
	public void rotate(int angle){
		if(angle%90 != 0){
			System.out.print("请指定角度为90的倍数。");
		}else{
			if(angle % 360 == 90 || angle % 360 == -270){
				int[][][] result = new int[this.width][this.height][3];
				for(int i=0;i<this.width;i++){
					for(int j=0;j<this.height;j++){
						result[i][j] = this.rgbArray[this.height-j-1][i];
					}
				}
				saveImage(result, "roimg.bmp");
			}else if(angle % 360 == 180 || angle % 360 == -180){
				int[][][] result = new int[this.height][this.width][3];
				for(int i=0;i<this.height;i++){
					for(int j=0;j<this.width;j++){
						result[i][j] = this.rgbArray[this.height - i -1][this.width - j -1];
					}
				}
				saveImage(result, "roimg.bmp");
			}else if(angle % 360 == 270 || angle % 360 == -90){
				int[][][] result = new int[this.width][this.height][3];
				for(int i=0;i<this.width;i++){
					for(int j=0;j<this.height;j++){
						result[i][j] = this.rgbArray[j][this.width - i -1];
					}
				}
				saveImage(result, "roimg.bmp");
			}else{
				saveImage(this.rgbArray, "roimg.bmp");
			}
		}
	}

	/*②翻转（若参数指定为0，则为水平翻转；为1，则为垂直翻转）*/
	public void reverse(int type){
		int[][][] result = new int[this.height][this.width][3];
		if(type == 0){
			for(int i=0;i<this.height;i++){
				for(int j=0;j<this.width;j++){
					result[i][j] = this.rgbArray[i][this.width - j -1];
				}
			}
			this.saveImage(result, "reimg.bmp");
		}else if(type == 1){
			for(int i=0;i<this.width;i++){
				for(int j=0;j<this.height;j++){
					result[j][i] = this.rgbArray[height -j -1][i];
				}
			}
			this.saveImage(result, "reimg.bmp");
		}else{
			System.out.println("请输入有效类型参数（0或1）。");
		}
	}

	/*③分割图片*/
	public void divide(int m, int n, int p, int q){
		/**
		 * @param m 子图宽
		 * @param n 子图高
		 * @param p 水平重叠像素数
		 * @param q 纵向重叠像素数
		 */
		if(m<=0 || n<=0 || p<0 || q<0){
			System.out.println("参数不正确，请输入有效参数！");
		}else if(m >= this.width && n >= this.height){
			saveImage("div.bmp");
		}else{
			if(m >= this.width){
				m = this.width;
				p = 0;
			}if(n >= this.height){
				n = this.height;
				q = 0;
			}if(p >= m || q >=n){
				System.out.println("重叠像素太大，请重新输入");
			}
			int[][][] dividedImg = new int[n][m][3];
			int divNumW = ((this.width - m) / (m - p)) + 1;
			int divNumH = ((this.height - n) / (n - q)) + 1;
			int remainderW = ((this.width - m) % (m - p));
			int remainderH = ((this.height - n) % (n - q));
			for(int i=0;i<=divNumH;i++){
				for(int j=0;j<=divNumW;j++){
					if(i < divNumH && j < divNumW){
						//无多余情况
						for(int k=0;k<n;k++){
							for(int l=0;l<m;l++){
								dividedImg[k][l] = this.rgbArray[i * (n - q) + k][j * (m - p) + l];
							}
						}
						this.saveImage(dividedImg, "div" + (i + 1) + "-" + (j + 1) + ".bmp");
					}else if(i < divNumH && j == divNumW && remainderW > 0 && remainderH == 0 ){
						//高正常，有多余宽情况（即此情况下一行中的最后一列）
						int[][][] lastImgW = new int[n][remainderW + p][3];
						for(int k=0;k<n;k++){
							for(int l=0;l<(remainderW+p);l++){
								lastImgW[k][l] = this.rgbArray[j * (n - q) + k][m - remainderW - p - 1 + l];
							}
						}
						this.saveImage(lastImgW, "div" + (i + 1) + "-" + (j + 1) + ".bmp");
					}else{
					/*
					  对于有多余的处理的想法：尽可能完成指定任务，即，即使宽或高有多余也导出，重叠也尽可能完成。因此
				      可能出现一行的最后几张或是最后一行的宽高与前些张不同。此时：
					  1.一行的最后一张：指定的横向重叠像素+横向多余像素。
					  2.最后一行：指定的纵向重叠像素+纵向多余像素。
					  3.最后一行最后一张：（指定的横向重叠像素+横向多余像素）x（指定的纵向重叠像素+纵向多余像素）
					*/
					//有多余高情况
						if(i < divNumH && j == divNumW && remainderW > 0){
						//宽高均有多余情况下最后一列中高正常宽多余的（即最后一列除最后一张）
							int[][][] lastImgW = new int[n][remainderW + p][3];
							for(int k=0;k<n;k++){
								for(int l=0;l<(remainderW+p);l++){
									lastImgW[k][l] = this.rgbArray[i * (n - q) + k][this.width - remainderW - p - 1 + l];
								}
							}
							this.saveImage(lastImgW, "div" + (i + 1) + "-" + (j + 1) + ".bmp");
						}if(i == divNumH){
						//有多余高情况的最后一行
							if(j < divNumW){
							//有多余高，最后一行子图高度与指定不同，但宽度相同时情况
								int[][][] lastImgH = new int[remainderH + q][m][3];
								for(int k=0;k<(remainderH + q);k++){
									for(int l=0;l<m;l++){
										lastImgH[k][l] = this.rgbArray[this.height - remainderH - q - 1 + k][j * (m - p) + l];
									}
								}
								this.saveImage(lastImgH, "div" + ( i + 1 ) + "-" + ( j + 1 ) + ".bmp");
							}else if(j == divNumW && remainderW > 0){
							//有多余高，最后一行子图高度与指定不同，宽度也不同（即最后一张）
								int[][][] lastImgW = new int[remainderH + q][remainderW + p][3];
								for(int k=0;k<(remainderH + p);k++){
									for(int l=0;l<(remainderW + q);l++){
										lastImgW[k][l] = this.rgbArray[this.height - n - q - 1 + k][this.width - m - p - 1 + l];
									}
								}
								this.saveImage(lastImgW, "div" + ( i + 1 ) + "-" + ( j + 1 ) + ".bmp");
							}
						}
					}
				}
			}
		}
	}
}

class test{
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		System.out.println("此程序会将生成指定图片旋转90度的图片，生成其灰度图，翻转图，并将其分成多块子图，\n" +
				"同时生成一幅1024x768的随机黑白图片，" +
				"请输入图片完整路径参数（注意如果在IDE中路径中的“\\”需要写成“\\\\”：");
		Scanner scanner = new Scanner(System.in);
		String filepath = scanner.next();
		Image im = new Image(filepath);
		//int imArray[][][] = im.readRGBImage("E:\\My Documents\\SDJU\\Teaching\\2013-2014\\软件开发基础\\My courseware\\theory_ppt\\1st_semester\\校历.jpg");
		//System.out.println(imArray.length);
		//System.out.println(imArray[0].length);
		//im.saveImage(imArray, "test.bmp");
		//生成随机灰度图
		im.generateRandomGrayImage(1024, 768);
		//旋转
		im.rotate(-90);
		//生成灰度图
		im.saveGrayImage("gray.bmp");
		//翻转
		im.reverse(1);
		//分割子图
		im.divide(50, 50, 10, 10);
	}
}
