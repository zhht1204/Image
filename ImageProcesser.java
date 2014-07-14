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

	/*����ת��Ĭ�������ģ���angleΪ��ʱ˳ʱ�룩*/
	public void rotate(int angle) {
		int width = image.getWidth();
		int height = image.getHeight();
		if (angle % 90 != 0) {
			System.err.print("��ָ���Ƕ�Ϊ90�ı�����");
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

	/*�ڷ�ת��������ָ��Ϊtrue����Ϊ��ֱ��ת��Ϊfalse����Ϊˮƽ��ת��*/
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

	/*�۷ָ�ͼƬ*/
	public void divide(int m, int n, int p, int q) {
		/**
		 * @param m ��ͼ��
		 * @param n ��ͼ��
		 * @param p ˮƽ�ص�������
		 * @param q �����ص�������
		 */
		int width = image.getWidth();
		int height = image.getHeight();
		if (m <= 0 || n <= 0 || p < 0 || q < 0) {
			System.out.println("��������ȷ����������Ч������");
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
				System.out.println("�ص�����̫������������");
			}
			int[][][] dividedImg = new int[n][m][3];
			int divNumW = ((width - m) / (m - p)) + 1;
			int divNumH = ((height - n) / (n - q)) + 1;
			int remainderW = ((width - m) % (m - p));
			int remainderH = ((height - n) % (n - q));
			for (int i = 0; i <= divNumH; i++) {
				for (int j = 0; j <= divNumW; j++) {
					if (i < divNumH && j < divNumW) {
						//�޶������
						for (int k = 0; k < n; k++) {
							for (int l = 0; l < m; l++) {
								dividedImg[k][l] = image.getRgbArray()[i * (n - q) + k][j * (m - p) + l];
							}
						}
						Image.saveImage(dividedImg, tempDirectory + "div" + (i + 1) + "-" + (j + 1) + ".bmp");
					} else if (i < divNumH && j == divNumW && remainderW > 0 && remainderH == 0) {
						//���������ж������������������һ���е����һ�У�
						int[][][] lastImgW = new int[n][remainderW + p][3];
						for (int k = 0; k < n; k++) {
							for (int l = 0; l < (remainderW + p); l++) {
								lastImgW[k][l] = image.getRgbArray()[j * (n - q) + k][m - remainderW - p - 1 + l];
							}
						}
						Image.saveImage(lastImgW, tempDirectory + "div" + (i + 1) + "-" + (j + 1) + ".bmp");
					} else {
					/*
					  �����ж���Ĵ�����뷨�����������ָ�����񣬼�����ʹ�����ж���Ҳ�������ص�Ҳ��������ɡ����
				      ���ܳ���һ�е�����Ż������һ�еĿ����ǰЩ�Ų�ͬ����ʱ��
					  1.һ�е����һ�ţ�ָ���ĺ����ص�����+����������ء�
					  2.���һ�У�ָ���������ص�����+����������ء�
					  3.���һ�����һ�ţ���ָ���ĺ����ص�����+����������أ�x��ָ���������ص�����+����������أ�
					*/
						//�ж�������
						if (i < divNumH && j == divNumW && remainderW > 0) {
							//��߾��ж�����������һ���и����������ģ������һ�г����һ�ţ�
							int[][][] lastImgW = new int[n][remainderW + p][3];
							for (int k = 0; k < n; k++) {
								for (int l = 0; l < (remainderW + p); l++) {
									lastImgW[k][l] = image.getRgbArray()[i * (n - q) + k][width - remainderW - p - 1 + l];
								}
							}
							Image.saveImage(lastImgW, tempDirectory + "div" + (i + 1) + "-" + (j + 1) + ".bmp");
						}
						if (i == divNumH) {
							//�ж������������һ��
							if (j < divNumW) {
								//�ж���ߣ����һ����ͼ�߶���ָ����ͬ���������ͬʱ���
								int[][][] lastImgH = new int[remainderH + q][m][3];
								for (int k = 0; k < (remainderH + q); k++) {
									for (int l = 0; l < m; l++) {
										lastImgH[k][l] = image.getRgbArray()[height - remainderH - q - 1 + k][j * (m - p) + l];
									}
								}
								Image.saveImage(lastImgH, tempDirectory.getAbsolutePath() + "div" + (i + 1) + "-" + (j + 1) + ".bmp");
							} else if (j == divNumW && remainderW > 0) {
								//�ж���ߣ����һ����ͼ�߶���ָ����ͬ�����Ҳ��ͬ�������һ�ţ�
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

	/*�ܷŴ�ͼƬ*/
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

	/*����СͼƬ*/
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