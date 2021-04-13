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
 * 下载图片
 *
 * @author Naturalpowder
 *
 */
public class ImageDownload implements Runnable {
	/**
	 * 线程
	 */
	private Thread thread;
	/**
	 * 目标文件路径及待请求的URL地址
	 */
	String dirPath, url, fileName;

	/**
	 * 初始化
	 *
	 * @param dirPath 目的文件路径
	 */
	public ImageDownload(String dirPath, String fileName) {
		this.dirPath = dirPath;
		this.fileName = fileName;
	}

	/**
	 * 设定URL输入信息
	 *
	 * @param i
	 * @param j
	 * @param url
	 */
	public void setInfo(String url) {
		this.url = url;
	}

	/**
	 * 加入主线程
	 */
	public void join() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		try {
			get(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送 get请求
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
				// 创建httpget.
				HttpGet httpget = new HttpGet(path);
				httpget.setConfig(defaultRequestConfig);
				httpget.setHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.79 Safari/537.1");
				httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				// 执行get请求.
				response = httpclient.execute(httpget);
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				// System.out.println(response.getStatusLine().getStatusCode());
				if (entity != null) {
					// 打印响应内容
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
	 * 下载文件到本地
	 *
	 * @param urlString 被下载的文件地址
	 * @param filename  本地文件名
	 * @throws Exception 各种异常
	 */
	private void download(InputStream is, String filename) throws Exception {
		File dir = new File(dirPath);
		if (dir == null || !dir.exists()) {
			dir.mkdirs();
		}

		// 文件真实路径
		String realPath = dirPath.concat(filename);
		File file = new File(realPath);
		if (file == null || !file.exists()) {
			file.createNewFile();
		}

		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		FileOutputStream os = new FileOutputStream(file);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}

	/**
	 * 判断一个文件的最后一行是否为某字符串
	 *
	 * @param src 文件路径
	 * @param key 判断是否最后一行的字符串 是 则 返回 true 不是 则 返回 false
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
