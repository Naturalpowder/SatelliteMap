package dataMap;


import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImagesDownload {
    private final List<PicInfo> picInfos;

    public ImagesDownload(List<PicInfo> picInfos) {
        this.picInfos = picInfos;
    }

    public void downloadImage() {
        ExecutorService service = Executors.newFixedThreadPool(30);
        Vector<ImageDownload> vectors = new Vector<>();
        for (PicInfo picInfo : picInfos) {
            ImageDownload download = new ImageDownload(picInfo.getDirPath(), picInfo.getFileName());
            download.setInfo(picInfo.getUrl());
            vectors.add(download);
        }
        for (ImageDownload o : vectors)
            service.execute(o);
        service.shutdown();
        try {
            while (!service.awaitTermination(1, TimeUnit.SECONDS))
                ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
