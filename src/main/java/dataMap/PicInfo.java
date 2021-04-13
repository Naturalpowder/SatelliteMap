package dataMap;

public class PicInfo {
    private final String  fileName, dirPath, url;

    public PicInfo(String fileName, String dirPath, String url) {
        this.fileName = fileName;
        this.dirPath = dirPath;
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDirPath() {
        return dirPath;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "PicInfo{" +
                "fileName='" + fileName + '\'' +
                ", dirPath='" + dirPath + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}