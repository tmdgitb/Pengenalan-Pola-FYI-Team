package com.ocr.thinning;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by Yusfia Hafid A on 10/30/2015.
 */
@Service
public class OtsuThresholdingContainer {
    private static final Logger log = LoggerFactory.getLogger(OtsuThresholdingContainer.class);
    private Mat Input, Output,Otsuresult;
    private Histogram hist = new Histogram();
    private LookupTable lookupTable = new LookupTable();
    private BinnaryTreshold bn = new BinnaryTreshold();
    private OtsuTresholding otsuTresholding = new OtsuTresholding();
    private DFSAlgorithm dfs;
    private int threshold_value;

    public OtsuThresholdingContainer() {
    }

    public Histogram getHist(){return this.hist;}

    public void setInput(String sources) {
        final File imageFile = new File(sources);
        this.Input = Highgui.imread(imageFile.getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        log.info("Input Sudah row={} col={}",Input.rows(),Input.cols());
    }

    public void setInput(byte[] gambar){
        MatOfByte mb = new MatOfByte();
        mb.fromArray(gambar);
        this.Input = Highgui.imdecode(mb,Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    }

    public void setBinnaryTreshold(Integer treshold){
        if (0<=treshold && treshold<=255) bn.setTreshold(treshold);
    }

    public byte[] getOutput() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", Output, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public void setOutput() {
        log.info("lewat sini rows={} cols={}",Input.rows(), Input.cols());
        hist.setHistogram(Input);
        //--------------------------------
        lookupTable.setSinglelookup();
        //bn.setTreshold(70);
        otsuTresholding.setHist(hist);
        otsuTresholding.findThreshold2();
        threshold_value = otsuTresholding.getThresholdValue();
        bn.setTreshold(threshold_value);
        lookupTable = bn.createBinaryLookup();
        Mat Outputsemi = bn.getBinaryImage(Input, lookupTable);
        if (bn.getMin()==0)Outputsemi = bn.getInvers(Outputsemi);
        Otsuresult = Outputsemi.clone();
        ZhangSuen zs = new ZhangSuen();
        Outputsemi = zs.process(Outputsemi);
        //bn.cek(Outputsemi);
        ZhangSuenRefiner zr = new ZhangSuenRefiner();
        Outputsemi = zr.refineZhangSuen(Outputsemi);
        //bn.cek(Outputsemi);
        Output = Outputsemi.clone();
        for (int i = 0; i < Outputsemi.rows(); i++) {
            Mat scanLine = Outputsemi.row(i);
            for (int j = 0; j < Outputsemi.cols(); j++) {
                byte num[] = new byte[1];
                scanLine.get(0,j,num);
                if (num[0]==0){
                    Output.put(i,j,0);
                }else{
                    Output.put(i,j,255);
                }//
            }
        }
        log.info("Selesai sudah row={} col={}",Output.rows(),Output.cols());
        log.info("Treshold Otsu Value = {}",otsuTresholding.getThresholdValue());
    }

    public void setOutput2() {
        log.info("lewat sini rows={} cols={}",Input.rows(), Input.cols());
        hist.setHistogram(Input);
        //--------------------------------
        lookupTable.setSinglelookup();
        //bn.setTreshold(70);
        otsuTresholding.setHist(hist);
        otsuTresholding.findThreshold2();
        threshold_value = otsuTresholding.getThresholdValue();
        bn.setTreshold(threshold_value);
        lookupTable = bn.createBinaryLookup();
        Mat Outputsemi = bn.getBinaryImage(Input, lookupTable);
        if (bn.getMin()==0)Outputsemi = bn.getInvers(Outputsemi);
        Otsuresult = Outputsemi.clone();
        //log.info("Selesai sudah row={} col={}",Output.rows(),Output.cols());
        log.info("Treshold Otsu Value = {}",otsuTresholding.getThresholdValue());
    }

    public byte[] getInput() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", Input, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public byte[] getOtsuResult(){
        for (int i = 0; i < Otsuresult.rows(); i++) {
            Mat scanLine = Otsuresult.row(i);
            for (int j = 0; j < Otsuresult.cols(); j++) {
                byte num[] = new byte[1];
                scanLine.get(0,j,num);
                if (num[0]==0){
                    Otsuresult.put(i,j,0);
                }else{
                    Otsuresult.put(i,j,255);
                }
            }
        }
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", Otsuresult, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public String getThresholdValue(){
        return ""+threshold_value+"";
    }

    public String recognizeObject(){
        setOutput();
        dfs = new DFSAlgorithm(Output);
        dfs.processImage();
        return dfs.printInfo();
    }
}
