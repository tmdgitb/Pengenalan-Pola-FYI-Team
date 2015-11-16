package com.ocr.thinning;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by Yusfia Hafid A on 11/9/2015.
 */
public class LineObserver {
    private static final Logger log = LoggerFactory.getLogger(LineObserver.class);
    private Mat input, cpy, bin;
    private int lineInfo;
    private String c3format, c3binformat;
    private Histogram hist;
    private LookupTable lookupTable;
    private BinnaryTreshold bn;
    private OtsuTresholding otsu;
    private ConvolutionContainer cn = new ConvolutionContainer();

    public void setInput(Mat in) {
        input = in.clone();
        hist = new Histogram();
        lookupTable = new LookupTable();
        bn = new BinnaryTreshold();
        otsu = new OtsuTresholding();
        //Imgproc.equalizeHist(input,input);
    }

    public void observerLine(int line) {
        if (line <= 0) {
            line = 0;
        }
        if (line >= input.rows()) {
            line = input.rows() - 1;
        }
        lineInfo = line;
        cpy = input.clone();
        bin = input.clone();
        cn.setInput(cpy);
        cn.processGaussian();
        bin = cn.getConvolutedOutputMat();
        /*
        hist.setHistogram(bin);
        lookupTable.setSinglelookup();
        otsu.setHist(hist);
        otsu.findThreshold3();
        bn.setTreshold(otsu.getThresholdValue());
        lookupTable = bn.createBinaryLookup();
        bin = bn.getBinaryImage(bin, lookupTable);
        if (bn.getMin() == 1) bin = bn.getInvers(bin);
        for (int i = 0; i < bin.rows(); i++) {
            Mat scanLine = bin.row(i);
            for (int j = 0; j < bin.cols(); j++) {
                byte num[] = new byte[1];
                scanLine.get(0, j, num);
                if (num[0] == 0) {
                    bin.put(i, j, 0);
                } else {
                    bin.put(i, j, 255);
                }
            }
        }
        */
        c3format = "";
        c3format = "['data'";
        c3binformat = "";
        c3binformat = "['data'";
        Mat scanLine = cpy.row(line);
        Mat scanLineBin = bin.row(line);
        for (int i = 0; i < input.cols(); i++) {
            byte[] data = new byte[1];
            scanLine.get(0, i, data);
            c3format = c3format + "," + Konversi.byteInt(data[0]);
            data[0] = (byte) 255;
            scanLine.put(0, i, data);

            scanLineBin.get(0, i, data);
            c3binformat = c3binformat + "," + Konversi.byteInt(data[0]);
            data[0] = (byte) 255;
            scanLineBin.put(0, i, data);
        }
        c3format = c3format + "]";
        c3binformat = c3binformat + "]";
        log.info("c3bin val {} ", c3binformat);
    }

    public String getC3format() {
        return c3format;
    }

    public String getC3binformat() {
        return c3binformat;
    }

    public int getLineInfo() {
        return lineInfo;
    }

    public Mat getCpy() {
        return cpy;
    }

    public Mat getBin() {
        return bin;
    }
}
