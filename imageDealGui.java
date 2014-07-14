package zht.c2.imagedeal;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class imageDealGui {
	private JPanel jPanel;
	private JTextField filePathField;
	private JButton fileSelectButton;
	private JButton CWRotateButton;
	private JButton ACWRotateButton;
	private JButton horizonallyReverseButton;
	private JButton verticallyReverseButton;
	private JSpinner numberSpinner;
	private JButton saveButton;
	private JButton saveToButton;
	private JLabel wenjianlujing;
	private JToolBar mainToolBar;
	private JLabel fangda;
	private JLabel bei;
	private JToolBar saveToolBar;
	private JLabel tempPictureLabel;
	private JLabel originalPictureLabel;
	private JLabel yuantu;
	private JLabel xianshi;
	private JButton showButton;
	private JButton zoomConfirmButton;
	private Image image, temp;
	private ImageProcesser ip;

	public imageDealGui() {
		fileSelectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"支持的图像文件(*.jpg、 *.png、*.bmp)", "jpg", "png", "bmp"
				));
				int returnVal = fileChooser.showOpenDialog(fileChooser);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File(fileChooser.getSelectedFile().toString());
					filePathField.setText(file.toString());
					image = new Image(file);
					temp = new Image(file);
					ip = new ImageProcesser(temp, file.getParent());
				}
			}
		});
		showButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				String inFilePath = filePathField.getText();
				File inFile = new File(inFilePath);
				if(image == null) {
					image = new Image(inFile);
					temp = new Image(inFile);
					ip = new ImageProcesser(temp, inFile.getParent());
				}
				BufferedImage bi = null;
				try {
					bi = ImageIO.read(inFile);
					AffineTransform transform = AffineTransform.getScaleInstance(
							(double)100/(double)bi.getWidth(),
							(double)100/(double)bi.getHeight());
					AffineTransformOp op = new AffineTransformOp(transform,
							AffineTransformOp.TYPE_BILINEAR);
					bi =  op.filter(bi, null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				originalPictureLabel.setIcon(new ImageIcon(bi));
				tempPictureLabel.setIcon(new ImageIcon(bi));
			}
		});
		CWRotateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				ip.rotate(90);
				Image.saveImage(temp.getRgbArray(), image.getParentPath() + "~temp.bmp");
				BufferedImage biTemp = null;
				try {
					biTemp = ImageIO.read(new File(image.getParentPath() + "~temp.bmp"));
					AffineTransform transform = AffineTransform.getScaleInstance(
							(double)100/(double)biTemp.getWidth(),
							(double)100/(double)biTemp.getHeight());
					AffineTransformOp op = new AffineTransformOp(transform,
							AffineTransformOp.TYPE_BILINEAR);
					biTemp =  op.filter(biTemp, null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				tempPictureLabel.setIcon(new ImageIcon(biTemp));
			}
		});
		ACWRotateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				ip.rotate(-90);
				Image.saveImage(temp.getRgbArray(), image.getParentPath() + "~temp.bmp");
				BufferedImage biTemp = null;
				try {
					biTemp = ImageIO.read(new File(image.getParentPath() + "~temp.bmp"));
					AffineTransform transform = AffineTransform.getScaleInstance(
							(double)100/(double)biTemp.getWidth(),
							(double)100/(double)biTemp.getHeight());
					AffineTransformOp op = new AffineTransformOp(transform,
							AffineTransformOp.TYPE_BILINEAR);
					biTemp =  op.filter(biTemp, null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				tempPictureLabel.setIcon(new ImageIcon(biTemp));
			}
		});
		horizonallyReverseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				ip.reverse(false);
				Image.saveImage(temp.getRgbArray(), image.getParentPath() + "~temp.bmp");
				BufferedImage biTemp = null;
				try {
					biTemp = ImageIO.read(new File(image.getParentPath() + "~temp.bmp"));
					AffineTransform transform = AffineTransform.getScaleInstance(
							(double)100/(double)biTemp.getWidth(),
							(double)100/(double)biTemp.getHeight());
					AffineTransformOp op = new AffineTransformOp(transform,
							AffineTransformOp.TYPE_BILINEAR);
					biTemp =  op.filter(biTemp, null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				tempPictureLabel.setIcon(new ImageIcon(biTemp));
			}
		});
		verticallyReverseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				ip.reverse(true);
				Image.saveImage(temp.getRgbArray(), image.getParentPath() + "~temp.bmp");
				BufferedImage biTemp = null;
				try {
					biTemp = ImageIO.read(new File(image.getParentPath() + "~temp.bmp"));
					AffineTransform transform = AffineTransform.getScaleInstance(
							(double)100/(double)biTemp.getWidth(),
							(double)100/(double)biTemp.getHeight());
					AffineTransformOp op = new AffineTransformOp(transform,
							AffineTransformOp.TYPE_BILINEAR);
					biTemp =  op.filter(biTemp, null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				tempPictureLabel.setIcon(new ImageIcon(biTemp));
			}
		});
		zoomConfirmButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				int mulriple = 1;
				try {
					mulriple = Integer.parseInt(numberSpinner.getValue().toString());
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "请输入有效的缩放倍数！", "缩放倍数错误", JOptionPane.ERROR_MESSAGE);
				}
				if(mulriple >= 0) {
					ip.magnify(mulriple);
					JOptionPane.showMessageDialog(null, "放大成功！", "恭喜", JOptionPane.INFORMATION_MESSAGE);
				} else {
					ip.shrink(Math.abs(mulriple));
					JOptionPane.showMessageDialog(null, "缩小成功！", "恭喜", JOptionPane.INFORMATION_MESSAGE);
				}
				Image.saveImage(temp.getRgbArray(), image.getParentPath() + "~temp.bmp");
			}
		});
		saveToButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("保存为(*.bmp)", "bmp"));
				int returnVal = fileChooser.showSaveDialog(fileChooser);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File saveFile = new File(fileChooser.getSelectedFile().toString());
					if(saveFile.toString().endsWith(".bmp")) {
						Image.saveImage(temp.getRgbArray(), saveFile.getAbsolutePath());
					} else {
						Image.saveImage(temp.getRgbArray(), saveFile.getAbsolutePath() + ".bmp");
					}
				}
			}
		});
		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				temp.saveImage(image.getParentPath() + "result.bmp");
			}
		});

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("图像处理程序");
		frame.setContentPane(new imageDealGui().jPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
