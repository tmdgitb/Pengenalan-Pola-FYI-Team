package com.ocr.thinning;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by Yusfia Hafid A on 11/9/2015.
 */
@Service
public class LineObserverContainer {
    private static final Logger log = LoggerFactory.getLogger(LineObserverContainer.class);
    private Mat Input;
    private LineObserver ln = new LineObserver();

    public void setInput(String sources) {
        final File imageFile = new File(sources);
        this.Input = Highgui.imread(imageFile.getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        log.info("Input Sudah row={} col={}",Input.rows(),Input.cols());
        ln.setInput(Input);
    }

    public void setInput(byte[] gambar){
        MatOfByte mb = new MatOfByte();
        mb.fromArray(gambar);
        this.Input = Highgui.imdecode(mb,Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        ln.setInput(Input);
    }

    public String getJSonC3String(int line){
        ln.observerLine(line);
        return ln.getC3format();
    }

    public byte[] getInput() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", Input, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public byte[] getResult(){
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", ln.getCpy(), result);
        byte[] nil = result.toArray();
        return nil;
    }

    public int getLine(){
        return ln.getLineInfo();
    }

    public int getMaxRow(){
        return Input.rows();
    }

    public int getMaxCol(){
        return Input.cols();
    }
}
