package com.ocr.thinning;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by Yusfia Hafid A on 11/2/2015.
 */

@Service
public class ImageSharpnessContainer {
    private static final Logger log = LoggerFactory.getLogger(ImageSharpnessContainer.class);
    private Mat input, output;
    private int[] nmatrix;
    private ImageSharpness imgs;

    public ImageSharpnessContainer(){
        nmatrix = new int[9];
        imgs = new ImageSharpness();

    }

    public void setInput(String sources) {
        final File imageFile = new File(sources);
        this.input = Highgui.imread(imageFile.getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        log.info("Input Sudah row={} col={}", input.rows(), input.cols());
    }

    public void setInput(byte[] gambar) {
        MatOfByte mb = new MatOfByte();
        mb.fromArray(gambar);
        this.input = Highgui.imdecode(mb, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    }
    public byte[] getOutput(){
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", output, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public byte[] getInput(){
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", input, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public void processImg(int option){
        output = input.clone();
        for (int i = 1; i < input.rows() - 1; i++) {
            for (int j = 1; j < input.cols() - 1; j++) {
                byte[] data = new byte[1];

                input.get(i - 1, j - 1, data);
                nmatrix[0] = data[0];

                input.get(i - 1, j, data);
                nmatrix[1] = data[0];

                input.get(i - 1, j + 1, data);
                nmatrix[2] = data[0];

                input.get(i, j - 1, data);
                nmatrix[3] = data[0];

                input.get(i, j, data);
                nmatrix[4] = data[0];

                input.get(i, j + 1, data);
                nmatrix[5] = data[0];

                input.get(i + 1, j - 1, data);
                nmatrix[6] = data[0];

                input.get(i + 1, j, data);
                nmatrix[7] = data[0];

                input.get(i + 1, j + 1, data);
                nmatrix[8] = data[0];

                imgs.setPixel(nmatrix);
                if (option == OperatorOption.DIFFERENCE_SHARPNESS){
                    data[0] = (byte)imgs.differenceSharpness();
                }else if (option == OperatorOption.HOMOGEN_SHARPNESS){
                    data[0] = (byte)imgs.homogenSharpness();
                }else{
                    data[0] = (byte)nmatrix[4];
                }
                output.put(i,j,data);
            }
        }
    }
}
