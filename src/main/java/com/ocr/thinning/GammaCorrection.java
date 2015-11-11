package com.ocr.thinning;

import org.opencv.core.Mat;

/**
 * Created by Yusfia Hafid A on 9/18/2015.
 */
public class GammaCorrection {
    private float gamma = 2;
    public float getGamma() {
        return gamma;
    }

    public void setGamma(float gamma) {
        this.gamma = gamma;
    }

    public LookupTable createGammaCorrectionLookup(){
        LookupTable lp = new LookupTable();
        lp.setLookuplv();
        float gammaCorrection = 1/gamma;
        for (int i = 0; i < 256; i++){
            lp.setLookuplvValue(0,i,(int)(255 * (Math.pow(((float) i / 255), (float) (gammaCorrection)))));
            lp.setLookuplvValue(1,i,(int)(255 * (Math.pow(((float) i / 255), (float) (gammaCorrection)))));
            lp.setLookuplvValue(2,i,(int)(255 * (Math.pow(((float) i / 255), (float) (gammaCorrection)))));
            lp.setLookuplvValue(3,i,(int)(255 * (Math.pow(((float) i / 255), (float) (gammaCorrection)))));
        }
        return lp;
    }

    public Mat getEditedImageRGB(Mat image, LookupTable lp) {
        Mat outputImage = image.clone();
        for (int i = 0; i< image.rows(); i++){
            Mat scanLine = image.row(i);
            Mat scanOutput = outputImage.row(i);
            for (int j = 0; j<image.cols() ; j++){
                double[] data = new double[3];
                data = scanLine.get(0,j);
                data[0] = lp.getLookuplv()[0][(int)data[0]];
                data[1] = lp.getLookuplv()[1][(int)data[1]];
                data[2] = lp.getLookuplv()[2][(int)data[2]];
                scanOutput.put(0,j,data);
            }
        }
        return outputImage;
    }
}
