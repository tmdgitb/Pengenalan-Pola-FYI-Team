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
    private Mat input, cpy;
    private int lineInfo;
    private String c3format;

    public void setInput(Mat in){
        input = in.clone();
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
        c3format = "";
        c3format = "['data'";
        cpy = input.clone();
        Mat scanLine = cpy.row(line);
        for (int i = 0; i < input.cols(); i++) {
            byte[] data = new byte[1];
            scanLine.get(0,i,data);
            c3format = c3format+","+Math.abs((int)data[0]);
            data[0] = (byte)255;
            scanLine.put(0,i,data);
        }
        c3format = c3format+"]";
    }

    public String getC3format(){
        return c3format;
    }

    public int getLineInfo(){
        return lineInfo;
    }

    public Mat getCpy(){
        return cpy;
    }
}
