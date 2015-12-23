package com.ocr.thinning;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.opencv.core.Core.circle;
import static org.opencv.core.Core.line;
import static org.opencv.core.Core.rectangle;

/**
 * Created by Yusfia Hafid A on 11/17/2015.
 */
public class BoundingObject {
    private static final Logger log = LoggerFactory.getLogger(BoundingObject.class);
    private int x_min;
    private int y_min;
    private int x_max;
    private int y_max;
    //nose
    private int x_nose;
    private int y_nose;
    //lips
    private int x_lips_left;
    private int y_lips_left;
    private int x_lips_right;
    private int y_lips_right;
    private ArrayList<String> lipsPattern;
    private boolean cek[][];
    int new_row_top;
    //eyes
    private int x_eye_left;
    private int y_eye_left;
    private int x_eye_right;
    private int y_eye_right;

    public BoundingObject() {
    }

    public int getX_min() {
        return x_min;
    }

    public void setX_min(int x_min) {
        this.x_min = x_min;
    }

    public int getY_min() {
        return y_min;
    }

    public void setY_min(int y_min) {
        this.y_min = y_min;
    }

    public int getX_max() {
        return x_max;
    }

    public void setX_max(int x_max) {
        this.x_max = x_max;
    }

    public int getY_max() {
        return y_max;
    }

    public void setY_max(int y_max) {
        this.y_max = y_max;
    }

    public Mat drawBoundingBox(Mat input, Mat grayscale, Mat binarry, Scalar color) {
        if (isFace(input, grayscale)) {
            rectangle(input, new Point(y_min, x_min), new Point(y_max, x_max), color);
            findNose(input, grayscale);
            findLips(input, binarry);
            findEye(input, grayscale, binarry);
        }
        return input;
    }

    private boolean isFace(Mat input, Mat grayscale) {
        int deltax = x_max - x_min;
        int deltay = y_max - y_min;
        double imagerat = (double) (input.cols() * input.rows()) / (double) (deltax * deltay);
        double ratio = (double) (deltax) / (double) (deltay);
        if (ratio > 1.1 && ratio < 1.618 && imagerat < 90) {
            log.info("rasio = {} deltaX = {}, deltaY = {}, imagerat = {}", ratio, deltax, deltay, imagerat);
            return true;
        }
        return false;
    }

    private void findNose(Mat input, Mat grayscsale) {
        byte[] data = new byte[1];
        int ylength = (int) ((float) (y_max - y_min) / (float) 3);
        int xlength = (int) ((float) (x_max - x_min) / (float) 3);
        grayscsale.get(x_min, y_min, data);
        int temp = Byte.toUnsignedInt(data[0]);
        for (int i = x_min + xlength; i < x_max - xlength; i++) {
            for (int j = y_min + ylength; j < y_max - ylength; j++) {
                grayscsale.get(i, j, data);
                int current = Byte.toUnsignedInt(data[0]);
                if (temp < current) {
                    temp = current;
                    x_nose = i;
                    y_nose = j;
                }
            }
        }
        circle(input, new Point(y_nose, x_nose), 2, new Scalar(255, 0, 0), 2);
    }


    private void findEye(Mat input, Mat gray, Mat binnary) {
        int xeyeline = x_nose - (int) ((x_max - x_nose) / 1.618); //batas x_atas
        int xeyeline2 = x_nose - (x_max - x_nose) / 4; //batas_x_bawah

        //--------------------------------------------------------------------------------------------------
        int ylength = (int) ((float) (y_max - y_min) / (float) 3);
        //--------------------------------------------------------------------------------------------------
        int yright = y_max - ylength;
        int yleft = y_min + ylength;
        //--------------------------------------------------------------------------------------------------
        rectangle(input, new Point(y_min, xeyeline), new Point(yleft, xeyeline2), new Scalar(255, 0, 0), 2);
        rectangle(input, new Point(yright, xeyeline), new Point(y_max, xeyeline2), new Scalar(255, 0, 0), 2);
        //circle(input, new Point(y_eye_left, x_eye_left), 2, new Scalar(255, 0, 0), 2);
        //circle(input, new Point(y_eye_right, x_eye_right), 2, new Scalar(255, 0, 0), 2);
        //line(input, new Point(y_min, xeyeline), new Point(y_max, xeyeline), new Scalar(255, 0, 0));
        //line(input, new Point(y_min, xeyeline2), new Point(y_max, xeyeline2), new Scalar(255, 0, 0));
        //line(input, new Point(yleft, x_min), new Point(yleft, x_max), new Scalar(255, 0, 0));
        //line(input, new Point(yright, x_min), new Point(yright, x_max), new Scalar(255, 0, 0));
    }

    private void findLips(Mat input, Mat binarry) {
        cek = new boolean[binarry.rows()][binarry.cols()];
        //int chin_nose = x_max - x_nose;
        //new_row_top = x_nose + (int) (0.3 * chin_nose);
        new_row_top = x_max - (int) ((x_max - x_nose) / 1.5);
        //log.info("xmax {} perbandingan {}", x_max, (int) (x_nose / 1.8));
        //int new_row_bottom = x_nose +(int)(0.7*chin_nose);
        //line(input, new Point(y_min, new_row_top), new Point(y_max, new_row_top), new Scalar(255, 0, 0));
        circle(input, new Point(y_min, new_row_top), 2, new Scalar(255, 0, 0), 1);
        circle(input, new Point(y_max, new_row_top), 2, new Scalar(255, 0, 0), 1);
        addLipsPattern();
        boolean exist = false;
        for (int i = new_row_top; i < x_max; i++) {
            String linePattern = "";
            for (int j = 0; j < lipsPattern.size(); j++) {
                linePattern = getLinePattern(binarry, i, y_min, y_max);
                if (linePattern.equals(lipsPattern.get(j))) {
                    exist = exist || true;
                    break;
                }
            }
            if (exist) {
                mostLeftAndRightLips(i, binarry, y_min, y_max);
                circle(input, new Point(y_lips_left, x_lips_left), 2, new Scalar(255, 0, 0), 2);
                circle(input, new Point(y_lips_right, x_lips_right), 2, new Scalar(255, 0, 0), 2);
                break;
            }
        }
        //line(input,new Point(y_min,new_row_bottom),new Point(y_max,new_row_bottom),new Scalar(255,0,0));
    }

    private void mostLeftAndRightLips(int row, Mat binnary, int min_y, int max_y) {
        Mat scanLine = binnary.row(row);
        for (int i = min_y; i <= max_y; i++) {
            byte[] data = new byte[1];
            scanLine.get(0, i, data);
            if (Byte.toUnsignedInt(data[0]) == 0 && i == min_y) {
                i++;
                scanLine.get(0, i, data);
                while (Byte.toUnsignedInt(data[0]) != 255) {
                    i++;
                    scanLine.get(0, i, data);
                }
            }
            if (Byte.toUnsignedInt(data[0]) == 0) {
                x_lips_left = row;
                y_lips_left = i;
                x_lips_right = row;
                y_lips_right = i;
                iterateLips(row, i, binnary);
                break;
            }
        }
    }

    private void iterateLips(int i, int j, Mat binnary) {
        if (i > new_row_top && i < x_max && j > y_min && j < y_max) {
            //log.info("row = {}, col = {}", i, j);
            cek[i][j] = true;
            if (j < y_lips_left) {
                y_lips_left = j;
                x_lips_left = i;
            }
            if (j > y_lips_right) {
                y_lips_right = j;
                x_lips_right = i;
            }

            if (!cek[i - 1][j] && isBlack(i - 1, j, binnary)) {
                iterateLips(i - 1, j, binnary);
            }
            if (!cek[i][j + 1] && isBlack(i, j + 1, binnary)) {
                iterateLips(i, j + 1, binnary);
            }
            if (!cek[i + 1][j] && isBlack(i + 1, j, binnary)) {
                iterateLips(i + 1, j, binnary);
            }
            if (!cek[i][j - 1] && isBlack(i, j - 1, binnary)) {
                iterateLips(i, j - 1, binnary);
            }
        }
    }

    private boolean isBlack(int i, int j, Mat binnary) {
        byte[] data = new byte[1];
        binnary.get(i, j, data);
        return Byte.toUnsignedInt(data[0]) == 0;
    }

    private String getLinePattern(Mat binary, int row, int min_y, int max_y) {
        String pattern = "";
        byte[] data = new byte[1];
        Mat scanLine = binary.row(row);
        for (int i = min_y; i <= max_y; i++) {
            scanLine.get(0, i, data);
            if (Byte.toUnsignedInt(data[0]) == 0) {
                pattern = pattern + "h";
            } else {
                pattern = pattern + "p";
            }
        }
        char[] p = pattern.toCharArray();
        char temp = ' ';
        pattern = "";
        for (int i = 0; i < p.length; i++) {
            if (temp != p[i]) {
                temp = p[i];
                pattern = pattern + p[i];
            }
        }
        return pattern;
    }

    private void addLipsPattern() {
        lipsPattern = new ArrayList<>();
        lipsPattern.add("hphph");
        lipsPattern.add("php");
    }

    /*
    private void findEye(Mat input, Mat gray, Mat binnary) {
        int xeyeline = x_nose - (int) ((x_max - x_nose) / 1.618); //batas x_atas
        int xeyeline2 = x_nose - (int) ((x_max - x_nose) / 4); //batas_x_bawah

        //--------------------------------------------------------------------------------------------------
        int ylength = (int) ((float) (y_max - y_min) / (float) 3);
        //--------------------------------------------------------------------------------------------------
        int yright = y_max - ylength;
        int yleft = y_min + ylength;
        //--------------------------------------------------------------------------------------------------
        int maxgraylevel = 0;
        int[] row_eyeleft = new int[xeyeline2 - xeyeline];
        int[] col_eyeleft = new int[yleft - y_min];
        for (int i = y_min; i < yleft; i++) {
            Mat scanLine = gray.row(i);
            Mat scanBin = binnary.row(i);
            for (int j = xeyeline; j < xeyeline2; j++) {
                byte[] tinyimg = new byte[1];
                byte[] tinybin = new byte[1];
                scanLine.get(0, j, tinyimg);
                scanBin.get(0, j, tinybin);
                int reference = Byte.toUnsignedInt(tinyimg[0]);
                int referenceBin = Byte.toUnsignedInt(tinybin[0]);
                col_eyeleft[i - y_min] = col_eyeleft[i - y_min] + reference;
                row_eyeleft[j - xeyeline] = row_eyeleft[j - xeyeline] + reference;
                //------------------------------------
                if (maxgraylevel < reference && referenceBin == 0) {
                    x_eye_left = j;
                    y_eye_left = i;
                    maxgraylevel = reference;
                }
                //------------------------
            }
        }
        int xmax_temp_left = 0;
        int xindex_left = 0;
        for (int i = 0; i < row_eyeleft.length; i++) {
            if (xmax_temp_left < row_eyeleft[i]) {
                xmax_temp_left = row_eyeleft[i];
                xindex_left = i;
            }
        }
        int ymax_temp_left = 0;
        int yindex_left = 0;
        for (int i = 0; i < col_eyeleft.length; i++) {
            if (ymax_temp_left < col_eyeleft[i]) {
                ymax_temp_left = col_eyeleft[i];
                yindex_left = i;
            }
        }
        x_eye_left = xeyeline + xindex_left;
        y_eye_left = y_min + yindex_left;
        log.info("col eyeleft {} ymin {} yleft{}", col_eyeleft, y_min, yleft);
        log.info("row eyeleft {}  xeyeline {} xeyeline2 {}", row_eyeleft, xeyeline, xeyeline2);
        maxgraylevel = 0;
        int[] row_eyeright = new int[xeyeline2 - xeyeline];
        int[] col_eyeright = new int[y_max - yright];
        for (int i = yright; i < y_max; i++) {
            Mat scanLine = gray.row(i);
            Mat scanBin = binnary.row(i);
            for (int j = xeyeline; j < xeyeline2; j++) {
                byte[] tinyimg = new byte[1];
                byte[] tinybin = new byte[1];
                scanLine.get(0, j, tinyimg);
                scanBin.get(0, j, tinybin);
                int reference = Byte.toUnsignedInt(tinyimg[0]);
                int referenceBin = Byte.toUnsignedInt(tinybin[0]);
                //log.info("gray lv : {}",referenceBin);
                col_eyeright[i - yright] = col_eyeleft[i - yright] + reference;
                row_eyeright[j - xeyeline] = row_eyeleft[j - xeyeline] + reference;
                //-------------------
                if (maxgraylevel < reference && referenceBin == 0) {
                    x_eye_right = j;
                    y_eye_right = i;
                    maxgraylevel = reference;
                }
                //-----------------
            }
        }
        int xmax_temp_right = 0;
        int xindex_right = 0;
        for (int i = 0; i < row_eyeright.length; i++) {
            if (xmax_temp_right < row_eyeright[i]) {
                xmax_temp_right = row_eyeright[i];
                xindex_right = i;
            }
        }
        int ymax_temp_right = 0;
        int yindex_right = 0;
        for (int i = 0; i < col_eyeright.length; i++) {
            if (ymax_temp_right < col_eyeright[i]) {
                ymax_temp_right = col_eyeright[i];
                yindex_right = i;
            }
        }
        x_eye_right = xeyeline + xindex_right;
        y_eye_right = yright + yindex_right;
        circle(input, new Point(y_eye_left, x_eye_left), 2, new Scalar(255, 0, 0), 2);
        circle(input, new Point(y_eye_right, x_eye_right), 2, new Scalar(255, 0, 0), 2);
        line(input, new Point(y_min, xeyeline), new Point(y_max, xeyeline), new Scalar(255, 0, 0));
        line(input, new Point(y_min, xeyeline2), new Point(y_max, xeyeline2), new Scalar(255, 0, 0));
        line(input, new Point(yleft, x_min), new Point(yleft, x_max), new Scalar(255, 0, 0));
        line(input, new Point(yright, x_min), new Point(yright, x_max), new Scalar(255, 0, 0));
    }
    */
}
