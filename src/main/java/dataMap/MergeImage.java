package dataMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 合成图片
 * 
 * @author Naturalpowder
 *
 */
public class MergeImage implements Runnable {
	/**
	 * 线程
	 */
	Thread thread;
	/**
	 * 待合成图片的横向数量
	 */
	int x;
	/**
	 * 待合成图片的纵向数量
	 */
	int y;
	/**
	 * 每张图片宽度方向的像素数目
	 */
	int unit_Width;
	/**
	 * 每张图片高度方向的像素数目
	 */
	int unit_Height;
	/**
	 * 图片的存储地址集合
	 */
	String[] pics;
	/**
	 * 图片种类（jpg , png）
	 */
	String type;
	/**
	 * 合成新图像的存储地址
	 */
	String dst_pic;

	/**
	 * 初始化
	 */
	public MergeImage() {

	}

	/**
	 * 初始化
	 * 
	 * @param x 待合成图片的横向数量
	 * @param y 待合成图片的纵向数量
	 */
	public MergeImage(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 进行初始化计算
	 * 
	 * @param pics
	 * @param type
	 * @param dst_pic
	 */
	public void initialize(String[] pics, String type, String dst_pic) {
		this.pics = pics;
		this.type = type;
		this.dst_pic = dst_pic;
	}

	/**
	 * 拼接图片
	 * 
	 * @return
	 */
	public boolean merge() {
		int len = pics.length;
		if (len < 1) {
			System.out.println("pics len < 1");
			return false;
		}
		File[] src = new File[len];
		BufferedImage[] images = new BufferedImage[len];
		int[][] ImageArrays = new int[len][];
		for (int i = 0; i < len; i++) {
			try {
				src[i] = new File(pics[i]);
//				 System.out.println(src[i]);
				images[i] = ImageIO.read(src[i]);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			int width = images[i].getWidth();
			int height = images[i].getHeight();
			ImageArrays[i] = new int[width * height];// 从图片中读取RGB
			ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
		}
		unit_Width = images[0].getWidth();
		unit_Height = images[0].getHeight();

		int dst_height = unit_Height * y;
		int dst_width = unit_Width * x;
		if (dst_height < 1) {
			System.out.println("dst_height < 1");
			return false;
		}

		// 生成新图片
		try {
			BufferedImage ImageNew = new BufferedImage(dst_width, dst_height, BufferedImage.TYPE_INT_RGB);
			int height_i = 0;
			int width_i = 0;
			for (int i = 0; i < images.length; i++) {
				ImageNew.setRGB(width_i % dst_width, height_i / dst_width * unit_Height, unit_Width, unit_Height,
						ImageArrays[i], 0, unit_Width);
				width_i += unit_Width;
				height_i += unit_Height;
			}

			File outFile = new File(dst_pic);
			ImageIO.write(ImageNew, type, outFile);// 写图片
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 开始线程
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		merge();
	}
}
