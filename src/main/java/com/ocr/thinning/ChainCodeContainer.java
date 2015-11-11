package com.ocr.thinning;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by Yusfia Hafid A on 10/17/2015.
 */
@Service
public class ChainCodeContainer {
    private static final Logger log = LoggerFactory.getLogger(ChainCodeContainer.class);
    private Mat Input, Output;
    //private Histogram hist = new Histogram();
    private LookupTable lookupTable = new LookupTable();
    private BinnaryTreshold bn = new BinnaryTreshold();
    private ChainCode chnCode = new ChainCode();
    private String textResult = "";

    public ChainCodeContainer() {
    }

    //public Histogram getHist(){return this.hist;}

    public void setInput(String sources) {
        final File imageFile = new File(sources);
        this.Input = Highgui.imread(imageFile.getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        log.info("Input Sudah row={} col={}", Input.rows(), Input.cols());
    }

    public void setInput(byte[] gambar) {
        MatOfByte mb = new MatOfByte();
        mb.fromArray(gambar);
        Input = Highgui.imdecode(mb, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        log.info("size row ={} col ={}",Input.rows(),Input.cols());
        if (Input.cols() > 500 || Input.rows() > 500) {
            //Mat cpyInput = new Mat();
            Size e = Resizer.resizeTo500Max(Input.rows(), Input.cols());
            log.info("Matrix size = {}",e);
            Imgproc.resize(Input, Input, e,0,0,Imgproc.INTER_CUBIC);
            //Input = cpyInput.clone();
        }
    }

    public void setBinnaryTreshold(Integer treshold) {
        if (0 <= treshold && treshold <= 255) bn.setTreshold(treshold);
    }

    public byte[] getOutput() {
        MatOfByte result = new MatOfByte();
        Mat cpyOutput = Output.clone();
        for (int i = 0; i < cpyOutput.rows(); i++) {
            Mat scanLine = cpyOutput.row(i);
            for (int j = 0; j < cpyOutput.cols(); j++) {
                //int[] data = new int[1];
                double[] data =scanLine.get(0,j);
                if ((int)data[0]==0){
                    data[0]=255;
                    scanLine.put(0,j,data);
                }else{
                    data[0]=0;
                    scanLine.put(0,j,data);
                }
            }
        }
        Highgui.imencode(".png", cpyOutput, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public byte[] getInput() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", Input, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public void chainCodeProcessingStep1(int treshold) {
        setTreshold(treshold);
        createBinaryLookup();
        setOutputImage();
    }

    public void chainCodeProcessingStep2() {
        chnCode.setBackground(bn.getMax());
        chnCode.setForeground(bn.getMin());
        chnCode.setImage(Output);
        chnCode.getChainCoordinate();
        textResult = "";
        textResult = chnCode.printInfo();
        log.info("result = {}",textResult);
    }

    public String getTextResult() {
        return textResult;
    }

    private void setTreshold(int treshold) {
        bn.setTreshold(treshold);
    }

    private void createBinaryLookup() {
        lookupTable = bn.createBinaryLookup();
    }

    private void setOutputImage() {
        this.Output = bn.getBinaryImage(Input, lookupTable);
    }
}
