package com.ocr.thinning;

import org.opencv.core.Mat;

/**
 * Created by Yusfia Hafid A on 9/20/2015.
 */
public class BinnaryTreshold {
    private float treshold = 125;
    private int max;
    private int min;

    public int getMax() {
        return this.max;
    }

    public int getMin() {
        return this.min;
    }

    public float getTreshold() {
        return treshold;
    }

    public void setTreshold(float treshold) {
        this.treshold = treshold;
    }

    public LookupTable createBinaryLookup() {
        LookupTable lp = new LookupTable();
        lp.setSinglelookup();
        for (int i = 0; i < 256; i++) {
            if (i < treshold) {
                lp.getSinglelookup()[i] = 1;
            } else {
                lp.getSinglelookup()[i] = 0;
            }
        }
        return lp;
    }

    public Mat getBinaryImage(Mat image, LookupTable lp) {
        int row = image.rows();
        int col = image.cols();
        Mat output = image.clone();
        int temp1 = 0, temp0 = 0;
        int colour[] = new int[1];
        for (int i = 0; i < row; i++) {
            Mat scnLine = output.row(i);
            for (int j = 0; j < col; j++) {
                byte[] tinyimg = new byte[1];
                scnLine.get(0, j, tinyimg);
                colour[0] = grayScale(tinyimg);
                colour[0] = lp.getSinglelookup()[colour[0]];
                scnLine.put(0, j, colour[0]);
                if (colour[0] == 1) {
                    temp1++;
                } else {
                    temp0++;
                }
            }
        }
        if (temp0 > temp1) {
            this.max = 0;
            this.min = 1;
        } else {
            this.max = 1;
            this.min = 0;
        }
        return output;
    }

    public Mat getInvers(Mat image) {
        int row = image.rows();
        int col = image.cols();
        //if (min == 0) {
            int colour[] = new int[1];
            for (int i = 0; i < row; i++) {
                //Mat scnLine = image.row(i);
                for (int j = 0; j < col; j++) {
                    byte[] tinyimg = new byte[1];
                    int clr =  image.get(i, j, tinyimg);
                    if (tinyimg[0] == 1) {
                        //System.out.print("1-");
                        colour[0] = 0;
                    } else {
                        //System.out.print("0-");
                        colour[0] = 1;
                    }
                    image.put(i, j, colour[0]);
                }
              //  System.out.println();
            }
        //}
        return image;
    }

    public static int grayScale(byte[] tinyb) {
        return Byte.toUnsignedInt(tinyb[0]);
    }

    public void cek(Mat image) {
        int row = image.rows();
        int col = image.cols();
        int colour[] = new int[1];
        for (int i = 0; i < row; i++) {
            Mat scnLine = image.row(i);
            for (int j = 0; j < col; j++) {
                byte[] tinyimg = new byte[1];
                scnLine.get(0, j, tinyimg);
                System.out.print(tinyimg[0] + ",");
            }
            System.out.println();
        }
    }
}
