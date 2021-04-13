package dataMap;

/**
 * 由经纬度计算瓦片坐标
 * 
 * @author Naturalpowder
 *
 */
public class TileCoordinate {
	/**
	 * 经纬度
	 */
	double lat, lon;
	/**
	 * 瓦片坐标及缩放层级
	 */
	int tileX, tileY, level, pixelX, pixelY;

	/**
	 * 测试示例程序
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TileCoordinate tile = new TileCoordinate(43.877177, 11.018495, 22);
		int tileX = tile.getTileX();
		int tileY = tile.getTileY();
		System.out.println("( " + tileX + " , " + tileY + " )");
	}

	/**
	 * 初始化
	 */
	public TileCoordinate() {

	}

	/**
	 * 初始化
	 * 
	 * @param lat   纬度
	 * @param lon   经度
	 * @param level 瓦片缩放层级
	 */
	public TileCoordinate(double lat, double lon, int level) {
		this.lat = lat;
		this.lon = lon;
		this.level = level;
		set();
	}

	/**
	 * 总设定
	 */
	private void set() {
		setTileX();
		setTileY();
		setPixelX();
		setPixelY();
	}

	/**
	 * 获取瓦片坐标的横坐标
	 * 
	 * @return
	 */
	public int getTileX() {
		return tileX;
	}

	/**
	 * 获取瓦片坐标的纵坐标
	 * 
	 * @return
	 */
	public int getTileY() {
		return tileY;
	}

	/**
	 * 获取像素横坐标
	 * 
	 * @return
	 */
	public int getPixelX() {
		return pixelX;
	}

	/**
	 * 获取像素纵坐标
	 * 
	 * @return
	 */
	public int getPixelY() {
		return pixelY;
	}

	/**
	 * 计算像素横坐标
	 */
	private void setPixelX() {
		double temp = (lon + 180) / 360 * Math.pow(2, level);
		pixelX = ((int) (temp * 256)) % 256;
	}

	/**
	 * 计算像素纵坐标
	 */
	private void setPixelY() {
		double angle = lat * Math.PI / 180;
		double temp = Math.log(Math.tan(angle) + 1 / Math.cos(angle));
		double temp_2 = 1 - temp / (Math.PI * 2);
		pixelY = ((int) (temp_2 * Math.pow(2, level) * 256)) % 256;
	}

	/**
	 * 计算瓦片横坐标
	 */
	private void setTileX() {
		tileX = (int) ((lon + 180) / 360 * Math.pow(2, level));
	}

	/**
	 * 计算瓦片纵坐标
	 */
	private void setTileY() {
		double angle = lat * Math.PI / 180;
		double numerator = Math.log(Math.tan(angle) + 1 / Math.cos(angle));
		tileY = (int) ((0.5 - numerator / (2 * Math.PI)) * Math.pow(2, level));
	}
}
