package dataMap;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SatelliteMap {
    private final double[] min, max;
    private final int level;
    private final String dirPath;
    private final List<PicInfo> picInfos;
    private int x, y;
    private final static String GOOGLE = "https://www.google.com/maps/vt?lyrs=s@";//level:0-21
    private final static String GAODE = "https://webst02.is.autonavi.com/appmaptile?style=6&";//level:1-19
    public final static int GOOGLE_MAP = 0, GAODE_MAP = 1;
    public int type = GOOGLE_MAP;

    public SatelliteMap(double[] a, double[] b, int level, String dirPath) {
        Pair pair = toBoundaryArray(a, b);
        this.min = pair.left;
        this.max = pair.right;
        this.level = level;
        this.dirPath = dirPath;
        this.picInfos = new ArrayList<>();
    }

    public void setType(int type) {
        this.type = type;
    }

    private Pair toBoundaryArray(double[] a, double[] b) {
        double x1 = Math.min(a[0], b[0]);
        double x2 = Math.max(a[0], b[0]);
        double y1 = Math.min(a[1], b[1]);
        double y2 = Math.max(a[1], b[1]);
        return new Pair(new double[]{x1, y1}, new double[]{x2, y2});
    }

    public void download() {
        setUrls();
        ImagesDownload imagesDownload = new ImagesDownload(picInfos);
        imagesDownload.downloadImage();
    }

    public void merge() {
        MergeImage merge = new MergeImage(x, y);
        System.out.printf("x = %d ,y = %d\n", x, y);
        String mergePath = dirPath + " Merge.jpg";
        String[] pics = picInfos.stream().map(e -> e.getDirPath() + e.getFileName()).toArray(String[]::new);
        merge.initialize(pics, "jpg", mergePath);
        Thread thread = new Thread(merge);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //É¾³ýÐ¡Í¼
        for (String e : pics) {
            File file = new File(e);
            if (!file.delete())
                System.out.println("Deletion failed!");
        }
    }

    private void setUrls() {
        TileCoordinate tile = new TileCoordinate(min[0], min[1], level);
        int x1 = tile.getTileX();
        int y1 = tile.getTileY();
        tile = new TileCoordinate(max[0], max[1], level);
        int x2 = tile.getTileX();
        int y2 = tile.getTileY();
        x = x2 - x1 + 1;
        y = y1 - y2 + 1;
        System.out.printf("( %d , %d , %d , %d )\n", x1, x2, y1, y2);
        int index = 0;
        for (int j = y2; j <= y1; j++) {
            for (int i = x1; i <= x2; i++) {
                String url = "";
                if (type == GOOGLE_MAP)
                    url = GOOGLE + index++ + "&gl=com&x=" + i + "&y=" + j + "&z=" + level;
                else if (type == GAODE_MAP)
                    url = GAODE + "x=" + i + "&y=" + j + "&z=" + level;
//                System.out.println(url);
                String fileName = i + " " + j + ".jpg";
                picInfos.add(new PicInfo(fileName, dirPath, url));
            }
        }
    }

    private static class Pair {
        private final double[] left, right;

        public Pair(double[] left, double[] right) {
            this.left = left;
            this.right = right;
        }
    }
}