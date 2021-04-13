package dataMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ����ͼƬ
 *
 * @author Naturalpowder
 *
 */
public class ImageDownload implements Runnable {
	/**
	 * �߳�
	 */
	private Thread thread;
	/**
	 * Ŀ���ļ�·�����������URL��ַ
	 */
	String dirPath, url, fileName;

	/**
	 * ��ʼ��
	 *
	 * @param dirPath Ŀ���ļ�·��
	 */
	public ImageDownload(String dirPath, String fileName) {
		this.dirPath = dirPath;
		this.fileName = fileName;
	}

	/**
	 * �趨URL������Ϣ
	 *
	 * @param i
	 * @param j
	 * @param url
	 */
	public void setInfo(String url) {
		this.url = url;
	}

	/**
	 * �������߳�
	 */
	public void join() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		try {
			get(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���� get����
	 *
	 * @throws Exception
	 */
	private void get(String path) throws Exception {
		File file = new File(dirPath + fileName);
		boolean full = checkLastLine(dirPath + fileName);
		if (!file.exists() || file.length() == 0 || !full) {
			CloseableHttpClient httpclient = null;
			CloseableHttpResponse response = null;
			HttpHost proxy = new HttpHost("127.0.0.1", 7890, "http");
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setProxy(proxy)
					.build();
			try {
				httpclient = HttpClients.createDefault();
				// ����httpget.
				HttpGet httpget = new HttpGet(path);
				httpget.setConfig(defaultRequestConfig);
				httpget.setHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.79 Safari/537.1");
				httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				// ִ��get����.
				response = httpclient.execute(httpget);
				// ��ȡ��Ӧʵ��
				HttpEntity entity = response.getEntity();
				// ��ӡ��Ӧ״̬
				// System.out.println(response.getStatusLine().getStatusCode());
				if (entity != null) {
					// ��ӡ��Ӧ����
					// String result = EntityUtils.toString(entity);
					InputStream is = entity.getContent();
					System.out.println(dirPath + fileName);
					download(is, fileName);
				}
			} catch (Exception e) {
				throw e;
			} finally {
				httpclient.close();
				if (response != null)
					response.close();
			}
		}
	}

	/**
	 * �����ļ�������
	 *
	 * @param urlString �����ص��ļ���ַ
	 * @param filename  �����ļ���
	 * @throws Exception �����쳣
	 */
	private void download(InputStream is, String filename) throws Exception {
		File dir = new File(dirPath);
		if (dir == null || !dir.exists()) {
			dir.mkdirs();
		}

		// �ļ���ʵ·��
		String realPath = dirPath.concat(filename);
		File file = new File(realPath);
		if (file == null || !file.exists()) {
			file.createNewFile();
		}

		// 1K�����ݻ���
		byte[] bs = new byte[1024];
		// ��ȡ�������ݳ���
		int len;
		// ������ļ���
		FileOutputStream os = new FileOutputStream(file);
		// ��ʼ��ȡ
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// ��ϣ��ر���������
		os.close();
		is.close();
	}

	/**
	 * �ж�һ���ļ������һ���Ƿ�Ϊĳ�ַ���
	 *
	 * @param src �ļ�·��
	 * @param key �ж��Ƿ����һ�е��ַ��� �� �� ���� true ���� �� ���� false
	 */
	public static boolean checkLastLine(String src) {
		File file = new File(src);
		try {
			ImageIO.read(file);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
