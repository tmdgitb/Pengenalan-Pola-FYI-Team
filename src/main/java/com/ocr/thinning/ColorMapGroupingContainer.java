package com.ocr.thinning;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

/*
 * Created by Yusfia Hafid A on 11/15/2015.
 */

@Service
public class ColorMapGroupingContainer {
    private static final Logger log = LoggerFactory.getLogger(ColorMapGroupingContainer.class);
    private Mat input, output, sample, bin, grayscale;
    private ArrayList<ColorMap> colorMaps = new ArrayList<>();

    public void setInput(String sources) {
        final File imageFile = new File(sources);
        this.input = Highgui.imread(imageFile.getPath(), Highgui.CV_LOAD_IMAGE_COLOR);
        this.bin = Highgui.imread(imageFile.getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        resizeInput();
        resizeBin();
        grayscale = bin.clone();
        zerosBin();
        //resizeBin();
        //resizeInput();
        log.info("Input Sudah row={} col={}", input.rows(), input.cols());
    }

    public void setInput(byte[] gambar) {
        MatOfByte mb = new MatOfByte();
        mb.fromArray(gambar);
        this.input = Highgui.imdecode(mb, Highgui.CV_LOAD_IMAGE_COLOR);
        this.bin = Highgui.imdecode(mb, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        resizeInput();
        resizeBin();
        grayscale = bin.clone();
        zerosBin();
        //resizeBin();
        //resizeInput();
        //Imgproc.GaussianBlur(input, input, new Size(5, 5), 2.2, 2);
    }

    public void setInput(Mat a) {
        this.input = a;
        resizeInput();
    }

    public void setSample(byte[] gambar) {
        MatOfByte mb = new MatOfByte();
        mb.fromArray(gambar);
        this.sample = Highgui.imdecode(mb, Highgui.CV_LOAD_IMAGE_COLOR);
        Imgproc.GaussianBlur(sample, sample, new Size(5, 5), 2.2, 2);
    }

    public byte[] getSample() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", sample, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public byte[] getOutput() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", output, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public byte[] getBin() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", bin, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public byte[] getInput() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", input, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public Mat getOutputMat() {
        return output;
    }

    public void setColorMaps(String name, int radious) {
        Histogram hist = new Histogram();
        hist.setHistogram(sample);
        ColorMap clrMap = new ColorMap();
        clrMap.setRadious(radious);
        clrMap.setGrup(name);
        for (int r = 0; r < 256; r++) {
            for (int g = 0; g < 256; g++) {
                for (int b = 0; b < 256; b++) {
                    ColorPixel pix = new ColorPixel();
                    pix.r = r;
                    pix.g = g;
                    pix.b = b;
                    if (hist.getDistribution()[r * 256 * 256 + g * 256 + b] > 0) {
                        //log.info("location r,g,b {},{},{} ",pix.r,pix.g,pix.b);
                        clrMap.addMap(pix);
                    }
                }
            }
        }
        log.info("ukuran pix {}", clrMap.getMap().size());
        ColorPixel clp = new ColorPixel();
        clp.r = 255;
        clp.b = 255;
        clp.g = 255;
        clrMap.setRepresentative(clp);
        //clrMap.setRepresentative(clrMap.getMap().get(0));
        colorMaps.add(clrMap);
    }

    public void resetColorMap() {
        colorMaps = new ArrayList<>();
    }

    private ColorMap getRepresentativeColorMap(ColorPixel px) {
        ColorMap clrMap = new ColorMap();
        clrMap.setRepresentative(px);
        clrMap.setGrup("no group");
        double min = 4096;
        for (int i = 0; i < colorMaps.size(); i++) {
            ColorMap clrMapTemp = colorMaps.get(i);
            double temp = clrMapTemp.matchPixel(px);
            if (temp < min) {
                min = temp;
                clrMap = clrMapTemp;
            }
        }
        return clrMap;
    }

    public void colorGrouping() {
        output = input.clone();
        for (int i = 0; i < input.rows(); i++) {
            Mat scanLine = output.row(i);
            Mat scanLineBin = bin.row(i);
            for (int j = 0; j < input.cols(); j++) {
                for (int k = 0; k < colorMaps.size(); k++) {
                    ColorPixel px = new ColorPixel();
                    byte[] tinyimg = new byte[3];
                    scanLine.get(0, j, tinyimg);
                    px.r = Byte.toUnsignedInt(tinyimg[0]);
                    px.g = Byte.toUnsignedInt(tinyimg[1]);
                    px.b = Byte.toUnsignedInt(tinyimg[2]);
                    ColorMap temp = getRepresentativeColorMap(px);
                    //log.info("location r,g,b {},{},{} temp {}",px.r,px.g,px.b,temp.getGrup());

                    if (!temp.getGrup().equals("no group")) {
                        //log.info("location x,y {},{}",i,j);
                        tinyimg[0] = (byte) 255;
                        tinyimg[1] = (byte) 255;
                        tinyimg[2] = (byte) 255;
                        scanLineBin.put(0, j, tinyimg);
                    }
                }
            }
        }

        DFSIteration dfi = new DFSIteration();
        dfi.setBinary(bin);
        dfi.fillForeground();
        ArrayList<BoundingObject> bo = dfi.getBoundingObject();
        for (int i = 0; i < bo.size(); i++) {
            bo.get(i).drawBoundingBox(output, grayscale,bin, new Scalar(255, 0, 0));
        }
    }

    public void zerosBin() {
        for (int i = 0; i < bin.rows(); i++) {
            Mat scanLine = bin.row(i);
            for (int j = 0; j < bin.cols(); j++) {
                byte[] data = new byte[1];
                data[0] = 0;
                scanLine.put(0, j, data);
            }
        }
    }

    private void resizeBin() {
        if (bin.cols() > 500 || bin.rows() > 500) {
            Size e = Resizer.resizeTo500Max(bin.rows(), bin.cols());
            log.info("Matrix size = {}",e);
            Imgproc.resize(bin, bin, e,0,0,Imgproc.INTER_CUBIC);
        }
    }

    private void resizeInput() {
        if (input.cols() > 500 || input.rows() > 500) {
            Size e = Resizer.resizeTo500Max(input.rows(), input.cols());
            log.info("Matrix size = {}",e);
            Imgproc.resize(input, input, e,0,0,Imgproc.INTER_CUBIC);
        }
    }
}
