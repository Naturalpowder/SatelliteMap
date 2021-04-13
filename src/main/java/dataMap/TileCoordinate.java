package dataMap;

/**
 * �ɾ�γ�ȼ�����Ƭ����
 * 
 * @author Naturalpowder
 *
 */
public class TileCoordinate {
	/**
	 * ��γ��
	 */
	double lat, lon;
	/**
	 * ��Ƭ���꼰���Ų㼶
	 */
	int tileX, tileY, level, pixelX, pixelY;

	/**
	 * ����ʾ������
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
	 * ��ʼ��
	 */
	public TileCoordinate() {

	}

	/**
	 * ��ʼ��
	 * 
	 * @param lat   γ��
	 * @param lon   ����
	 * @param level ��Ƭ���Ų㼶
	 */
	public TileCoordinate(double lat, double lon, int level) {
		this.lat = lat;
		this.lon = lon;
		this.level = level;
		set();
	}

	/**
	 * ���趨
	 */
	private void set() {
		setTileX();
		setTileY();
		setPixelX();
		setPixelY();
	}

	/**
	 * ��ȡ��Ƭ����ĺ�����
	 * 
	 * @return
	 */
	public int getTileX() {
		return tileX;
	}

	/**
	 * ��ȡ��Ƭ�����������
	 * 
	 * @return
	 */
	public int getTileY() {
		return tileY;
	}

	/**
	 * ��ȡ���غ�����
	 * 
	 * @return
	 */
	public int getPixelX() {
		return pixelX;
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return
	 */
	public int getPixelY() {
		return pixelY;
	}

	/**
	 * �������غ�����
	 */
	private void setPixelX() {
		double temp = (lon + 180) / 360 * Math.pow(2, level);
		pixelX = ((int) (temp * 256)) % 256;
	}

	/**
	 * ��������������
	 */
	private void setPixelY() {
		double angle = lat * Math.PI / 180;
		double temp = Math.log(Math.tan(angle) + 1 / Math.cos(angle));
		double temp_2 = 1 - temp / (Math.PI * 2);
		pixelY = ((int) (temp_2 * Math.pow(2, level) * 256)) % 256;
	}

	/**
	 * ������Ƭ������
	 */
	private void setTileX() {
		tileX = (int) ((lon + 180) / 360 * Math.pow(2, level));
	}

	/**
	 * ������Ƭ������
	 */
	private void setTileY() {
		double angle = lat * Math.PI / 180;
		double numerator = Math.log(Math.tan(angle) + 1 / Math.cos(angle));
		tileY = (int) ((0.5 - numerator / (2 * Math.PI)) * Math.pow(2, level));
	}
}
