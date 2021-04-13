package test;

import dataMap.SatelliteMap;

public class Test_SatelliteMapDownload {
    public static void main(String[] args) {
        SatelliteMap satelliteMap = new SatelliteMap(
                new double[]{32.042463, 118.808986},
                new double[]{32.036942, 118.848693},
                18,
                "src/main/data/"
        );
        satelliteMap.setType(SatelliteMap.GAODE_MAP);
        satelliteMap.download();
        satelliteMap.merge();
    }
}