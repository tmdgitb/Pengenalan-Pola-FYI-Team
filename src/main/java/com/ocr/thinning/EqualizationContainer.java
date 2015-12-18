package com.ocr.thinning;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Yusfia Hafid A on 10/16/2015.
 */

@Service
public class EqualizationContainer {
    private static final Logger log = LoggerFactory.getLogger(EqualizationContainer.class);
    private Histogram hist, histoutput;
    private Mat image, output;
    private LookupTable lookupTable;
    private GammaCorrection gammaCorrection;
    private CDFEqualization cdfEqualization;

    public EqualizationContainer() {
        gammaCorrection = new GammaCorrection();
        cdfEqualization = new CDFEqualization();
        lookupTable = new LookupTable();
        hist = new Histogram();
        histoutput = new Histogram();
    }

    public void setGammaValue(float value) {
        gammaCorrection.setGamma(value);
    }

    public void setImage(byte[] inputimage) {
        this.output = image.clone();
        MatOfByte mb = new MatOfByte();
        mb.fromArray(inputimage);
        this.image = Highgui.imdecode(mb, 1);
    }

    public Histogram getHist() {
        log.info("pixel sum {} ", image.rows() * image.cols());
        return hist;
    }

    public void setHist() {
        hist = new Histogram();
        hist.setHistogram(image);
        log.info("sini : row={} col={}", image.rows(), image.cols());
        log.info("hist color : clr={} ", hist.uniqueColor);
    }

    public Histogram getHistoutput() {
        return histoutput;
    }

    public void setHistoutput() {
        histoutput = new Histogram();
        histoutput.setHistogram(output);
    }

    public Mat getImage() {
        return image;
    }

    public void setImage(Mat image) {
        this.image = image;
    }

    public void setImageTest() {
        this.image = Highgui.imread("no.jpg");
    }

    public void gammaProcessing() {
        lookupTable = gammaCorrection.createGammaCorrectionLookup();
        this.output = gammaCorrection.getEditedImageRGB(image, lookupTable);
    }

    public void cdfProcessing(){
        setHist();
        lookupTable = cdfEqualization.createCDFLookup(hist);
        this.output = cdfEqualization.getEditedImageRGB(image, lookupTable);
    }

    public byte[] getInput() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", image, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public byte[] getOutput() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", output, result);
        byte[] nil = result.toArray();
        return nil;
    }
}
