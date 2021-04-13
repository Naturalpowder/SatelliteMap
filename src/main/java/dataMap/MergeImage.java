package dataMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * �ϳ�ͼƬ
 * 
 * @author Naturalpowder
 *
 */
public class MergeImage implements Runnable {
	/**
	 * �߳�
	 */
	Thread thread;
	/**
	 * ���ϳ�ͼƬ�ĺ�������
	 */
	int x;
	/**
	 * ���ϳ�ͼƬ����������
	 */
	int y;
	/**
	 * ÿ��ͼƬ��ȷ����������Ŀ
	 */
	int unit_Width;
	/**
	 * ÿ��ͼƬ�߶ȷ����������Ŀ
	 */
	int unit_Height;
	/**
	 * ͼƬ�Ĵ洢��ַ����
	 */
	String[] pics;
	/**
	 * ͼƬ���ࣨjpg , png��
	 */
	String type;
	/**
	 * �ϳ���ͼ��Ĵ洢��ַ
	 */
	String dst_pic;

	/**
	 * ��ʼ��
	 */
	public MergeImage() {

	}

	/**
	 * ��ʼ��
	 * 
	 * @param x ���ϳ�ͼƬ�ĺ�������
	 * @param y ���ϳ�ͼƬ����������
	 */
	public MergeImage(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * ���г�ʼ������
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
	 * ƴ��ͼƬ
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
			ImageArrays[i] = new int[width * height];// ��ͼƬ�ж�ȡRGB
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

		// ������ͼƬ
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
			ImageIO.write(ImageNew, type, outFile);// дͼƬ
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * ��ʼ�߳�
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
