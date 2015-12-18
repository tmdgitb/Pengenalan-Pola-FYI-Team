package com.ocr.thinning;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yusfia Hafid A on 12/18/2015.
 */
public class CDFEqualization {
    private static final Logger log = LoggerFactory.getLogger(CDFEqualization.class);

    public LookupTable createCDFLookup(Histogram hist) {
        LookupTable lp = new LookupTable();
        lp.setLookuplv();
        int r = 0;
        int g = 0;
        int b = 0;
        int gray = 0;
        for (int i = 0; i < 256; i++) {
            r = r + hist.lv[0][i];
            g = g + hist.lv[1][i];
            b = b + hist.lv[2][i];
            gray = gray + hist.lv[3][i];
            log.info("value {} hist0 {} ", ((hist.lv[0].length - 1) * b) / (float) (hist.totalPix), b);
            lp.setLookuplvValue(0, i, Math.round(((hist.lv[0].length - 1) * b) / (float) (hist.totalPix)));
            lp.setLookuplvValue(1, i, Math.round(((hist.lv[1].length - 1) * g) / (float) (hist.totalPix)));
            lp.setLookuplvValue(2, i, Math.round(((hist.lv[2].length - 1) * r) / (float) (hist.totalPix)));
            lp.setLookuplvValue(3, i, Math.round(((hist.lv[3].length - 1) * gray) / (float) (hist.totalPix)));
        }
        return lp;
    }

    public Mat getEditedImageRGB(Mat image, LookupTable lp) {
        Mat outputImage = image.clone();
        for (int i = 0; i < image.rows(); i++) {
            Mat scanLine = image.row(i);
            Mat scanOutput = outputImage.row(i);
            for (int j = 0; j < image.cols(); j++) {
                double[] data = new double[3];
                data = scanLine.get(0, j);
                data[0] = lp.getLookuplv()[0][(int)Math.round(data[0])];
                data[1] = lp.getLookuplv()[1][(int)Math.round(data[1])];
                data[2] = lp.getLookuplv()[2][(int)Math.round(data[2])];
                scanOutput.put(0, j, data);
            }
        }
        return outputImage;
    }
}
