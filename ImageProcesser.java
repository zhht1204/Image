package zht.c2.imagedeal;

import java.io.File;

public class ImageProcesser {
	File inFile, tempDirectory;
	Image image;

	public ImageProcesser(Image originalImage, String tempDirectoryPath) {
		this.image = originalImage;
		try {
			inFile = image.getFile();
			if(!inFile.exists()) {
				System.err.println("File doesn't exist.Please check.");
				return;
			}
			if(new File(tempDirectoryPath).isDirectory()){
				tempDirectory = new File(tempDirectoryPath);
			} else {
				tempDirectory = new File(image.getParentPath());
			}
			Image.saveImage(image.getRgbArray(), tempDirectory.toString() + "~temp.bmp");
			this.image.setFile(new File(tempDirectory.toString() + "~temp.bmp"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateRandomGrayImage(int m, int n) { //m: number of columns;n:number of rows
		generateRandomGrayImage(m, n, "randomGenerate.bmp");
	}
	public void generateRandomGrayImage(int m, int n, String fileName) {
		int randomImg[][] = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				randomImg[i][j] = (int) (Math.random() * 256);
			}
		}
		Image.saveImage(randomImg, this.tempDirectory + fileName);
	}

	/*①旋转（默认绕中心）（angle为正时顺时针）*/
	public void rotate(int angle) {
		int width = image.getWidth();
		int height = image.getHeight();
		if (angle % 90 != 0) {
			System.err.print("请指定角度为90的倍数。");
			return;
		} else {
			int[][][] result = new int[width][height][3];
			if (angle % 360 == 90 || angle % 360 == -270) {
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						result[i][j] = image.getRgbArray()[height - j - 1][i];
					}
				}
			} else if (angle % 360 == 180 || angle % 360 == -180) {
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						result[i][j] = image.getRgbArray()[height - i - 1][width - j - 1];
					}
				}
			} else if (angle % 360 == 270 || angle % 360 == -90) {
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						result[i][j] = image.getRgbArray()[j][width - i - 1];
					}
				}
			}
			image.setArray(result);
			Image.saveImage(image.getRgbArray(), tempDirectory.toString() + "~temp.bmp");
		}
	}

	/*②翻转（若参数指定为true，则为垂直翻转；为false，则为水平翻转）*/
	public void reverse(boolean reverserVertically) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][][] result = new int[height][width][3];
		if (reverserVertically) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					result[j][i] = image.getRgbArray()[height - j - 1][i];
				}
			}
		} else {

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					result[i][j] = image.getRgbArray()[i][width - j - 1];
				}
			}
		}
		image.setArray(result);
		Image.saveImage(image.getRgbArray(), tempDirectory.toString() + "~temp.bmp");
	}

	/*③分割图片*/
	public void divide(int m, int n, int p, int q) {
		/**
		 * @param m 子图宽
		 * @param n 子图高
		 * @param p 水平重叠像素数
		 * @param q 纵向重叠像素数
		 */
		int width = image.getWidth();
		int height = image.getHeight();
		if (m <= 0 || n <= 0 || p < 0 || q < 0) {
			System.out.println("参数不正确，请输入有效参数！");
		} else if (m >= width && n >= height) {
			Image.saveImage(image.getRgbArray(), "div.bmp");
		} else {
			if (m >= width) {
				m = width;
				p = 0;
			}
			if (n >= height) {
				n = height;
				q = 0;
			}
			if (p >= m || q >= n) {
				System.out.println("重叠像素太大，请重新输入");
			}
			int[][][] dividedImg = new int[n][m][3];
			int divNumW = ((width - m) / (m - p)) + 1;
			int divNumH = ((height - n) / (n - q)) + 1;
			int remainderW = ((width - m) % (m - p));
			int remainderH = ((height - n) % (n - q));
			for (int i = 0; i <= divNumH; i++) {
				for (int j = 0; j <= divNumW; j++) {
					if (i < divNumH && j < divNumW) {
						//无多余情况
						for (int k = 0; k < n; k++) {
							for (int l = 0; l < m; l++) {
								dividedImg[k][l] = image.getRgbArray()[i * (n - q) + k][j * (m - p) + l];
							}
						}
						Image.saveImage(dividedImg, tempDirectory + "div" + (i + 1) + "-" + (j + 1) + ".bmp");
					} else if (i < divNumH && j == divNumW && remainderW > 0 && remainderH == 0) {
						//高正常，有多余宽情况（即此情况下一行中的最后一列）
						int[][][] lastImgW = new int[n][remainderW + p][3];
						for (int k = 0; k < n; k++) {
							for (int l = 0; l < (remainderW + p); l++) {
								lastImgW[k][l] = image.getRgbArray()[j * (n - q) + k][m - remainderW - p - 1 + l];
							}
						}
						Image.saveImage(lastImgW, tempDirectory + "div" + (i + 1) + "-" + (j + 1) + ".bmp");
					} else {
					/*
					  对于有多余的处理的想法：尽可能完成指定任务，即，即使宽或高有多余也导出，重叠也尽可能完成。因此
				      可能出现一行的最后几张或是最后一行的宽高与前些张不同。此时：
					  1.一行的最后一张：指定的横向重叠像素+横向多余像素。
					  2.最后一行：指定的纵向重叠像素+纵向多余像素。
					  3.最后一行最后一张：（指定的横向重叠像素+横向多余像素）x（指定的纵向重叠像素+纵向多余像素）
					*/
						//有多余高情况
						if (i < divNumH && j == divNumW && remainderW > 0) {
							//宽高均有多余情况下最后一列中高正常宽多余的（即最后一列除最后一张）
							int[][][] lastImgW = new int[n][remainderW + p][3];
							for (int k = 0; k < n; k++) {
								for (int l = 0; l < (remainderW + p); l++) {
									lastImgW[k][l] = image.getRgbArray()[i * (n - q) + k][width - remainderW - p - 1 + l];
								}
							}
							Image.saveImage(lastImgW, tempDirectory + "div" + (i + 1) + "-" + (j + 1) + ".bmp");
						}
						if (i == divNumH) {
							//有多余高情况的最后一行
							if (j < divNumW) {
								//有多余高，最后一行子图高度与指定不同，但宽度相同时情况
								int[][][] lastImgH = new int[remainderH + q][m][3];
								for (int k = 0; k < (remainderH + q); k++) {
									for (int l = 0; l < m; l++) {
										lastImgH[k][l] = image.getRgbArray()[height - remainderH - q - 1 + k][j * (m - p) + l];
									}
								}
								Image.saveImage(lastImgH, tempDirectory.getAbsolutePath() + "div" + (i + 1) + "-" + (j + 1) + ".bmp");
							} else if (j == divNumW && remainderW > 0) {
								//有多余高，最后一行子图高度与指定不同，宽度也不同（即最后一张）
								int[][][] lastImgW = new int[remainderH + q][remainderW + p][3];
								for (int k = 0; k < (remainderH + p); k++) {
									for (int l = 0; l < (remainderW + q); l++) {
										lastImgW[k][l] = image.getRgbArray()[height - n - q - 1 + k][width - m - p - 1 + l];
									}
								}
								Image.saveImage(lastImgW, tempDirectory + "div" + (i + 1) + "-" + (j + 1) + ".bmp");
							}
						}
					}
				}
			}
		}
	}

	/*④放大图片*/
	public void magnify(int mulriple) {
		int width = image.getWidth();
		int height = image.getHeight();
		if(mulriple == 0) { mulriple = 1; }
		int[][][] result = new int[width*mulriple][height*mulriple][3];
		int[][][] origin = image.getRgbArray();
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				for(int m=0; m<mulriple; m++) {
					for(int n=0; n<mulriple; n++) {
						result[mulriple * i + m][mulriple * j + n] = origin[i][j];
					}
				}
			}
		}
		image.setArray(result);
		Image.saveImage(image.getRgbArray(), tempDirectory.toString() + "~temp.bmp");
	}
	public void magnify() {
		magnify(2);
	}

	/*⑤缩小图片*/
	public void shrink(int mulriple) {
		int width = image.getWidth();
		int height = image.getHeight();
		if(mulriple == 0) { return; }
		int[][][] result = new int[height/mulriple][width/mulriple][3];
		int[][][] origin = image.getRgbArray();
		for(int i=0; i<result.length; i++) {
			for(int j=0; j<result[0].length; j++) {
				result[i][j] = origin[i*mulriple][j*mulriple];
			}
		}
		image.setArray(result);
		Image.saveImage(image.getRgbArray(), tempDirectory.toString() + "~temp.bmp");
	}
	public void shrink() {
		shrink(2);
	}

}