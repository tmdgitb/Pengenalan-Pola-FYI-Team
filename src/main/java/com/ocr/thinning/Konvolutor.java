package com.ocr.thinning;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

/**
 * Created by Yusfia Hafid A on 11/13/2015.
 */
public class Konvolutor {
    private int[][] np;
    private int[][] p;
    private int add;

    public int[][] getNp() {
        return np;
    }

    public void setNp(int[][] np) {
        this.np = np;
    }

    public int[][] getP() {
        return p;
    }

    public void setP(int[][] p) {
        this.p = p;
    }

    public int getAdd() {
        return this.add;
    }

    public Mat createImage(Mat input) {
        Mat output;
        add = (p.length - 1) / 2;
        output = new Mat(input.rows() + add, input.cols() + add, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        for (int i = 0; i < input.rows(); i++) {
            for (int j = 0; j < input.cols(); j++) {
                byte[] data = new byte[1];
                input.get(i, j, data);
                int x = i + add;
                int y = j + add;
                output.put(x, j + y, data);
                if (i == 0 && j == 0) {
                    byte[][] px = new byte[add][add];
                    for (int k = 1; k <= add; k++) {
                        for (int l = 1; l <= add; l++) {
                            input.get(k, l, data);
                            px[k - 1][l - 1] = data[0];
                        }
                    }
                    for (int k = 0; k < add; k++) {
                        for (int l = 0; l < add; l++) {
                            data[0] = px[add - k - 1][add - l - 1];
                            output.put(k, l, data);
                        }
                    }
                }
                if (i == 0 && j == input.rows() - 1) {
                    byte[][] px = new byte[add][add];
                    for (int k = 0; k < add; k++) {
                        for (int l = 0; l < add; l++) {

                        }
                    }
                }
            }
        }
        return output;
    }

    public int konvolusi() {
        int sum = 0;
        for (int i = 0; i < np.length; i++) {
            for (int j = 0; j < np[i].length; j++) {
                sum = sum + (np[i][j] * p[i][j]);
            }
        }
        return Math.abs(sum);
    }

    public static int konvolusi(int[][] np, int[][] p) {
        int sum = 0;
        for (int i = 0; i < np.length; i++) {
            for (int j = 0; j < np[i].length; j++) {
                sum = sum + (np[i][j] * p[i][j]);
            }
        }
        return Math.abs(sum);
    }
}

