package com.ocr.thinning;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yusfia Hafid A on 11/1/2015.
 */
@Service
public class ConvolutionContainer {
    private static final Logger log = LoggerFactory.getLogger(ConvolutionContainer.class);
    private Mat input, output, convolutedOutput;
    private OperatorKernel operatorKernel  = new OperatorKernel();
    private int[] nmatrix = new int[9];

    public ConvolutionContainer() {
        nmatrix = new int[9];
        operatorKernel = new OperatorKernel();
        //operatorKernel.setOperatorSobel();
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

    public void setOperatorKernel(int opsi) {
        if (opsi==OperatorOption.SOBEL_OPERATOR){
            operatorKernel = new OperatorKernel();
            operatorKernel.setOperatorSobel();
        }else if(opsi==OperatorOption.PREWIT_OPERATOR){
            operatorKernel = new OperatorKernel();
            operatorKernel.setOperatorPrewit();
        }
    }

    public void setOperatorKernel(int a,int b, int c){
        operatorKernel = new OperatorKernel();
        operatorKernel.setCustomOperator(a,b,c);
    }

    private void createOutput() {
        output = new Mat(input.rows() + 2, input.cols() + 2, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        for (int i = 1; i < output.rows() - 1; i++) {
            for (int j = 1; j < output.cols() - 1; j++) {
                byte[] data = new byte[1];
                int x, y;
                x = i - 1;
                y = j - 1;
                input.get(x, y, data);
                output.put(i, j, data);
            }
        }
        byte[] data1 = new byte[1];
        byte[] data2 = new byte[1];
        byte[] data3 = new byte[1];
        for (int j = 1; j < output.cols() - 1; j++) {
            int i = 1;

            output.get(i - 1, j - 1, data1);
            output.get(i + 1, j + 1, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i - 1, j - 1, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i - 1, j - 1, data2);
                    }*/

            output.get(i - 1, j, data1);
            output.get(i + 1, j, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i - 1, j, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i - 1, j, data2);
                    }*/

            output.get(i - 1, j + 1, data1);
            output.get(i + 1, j - 1, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i - 1, j + 1, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i - 1, j + 1, data2);
                    }*/

            i = output.rows() - 2;

            output.get(i - 1, j - 1, data1);
            output.get(i + 1, j + 1, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i + 1, j + 1, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i + 1, j + 1, data1);
                    }*/

            output.get(i - 1, j, data1);
            output.get(i + 1, j, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i + 1, j, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i + 1, j, data1);
                    }*/

            output.get(i - 1, j + 1, data1);
            output.get(i + 1, j - 1, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i + 1, j - 1, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i + 1, j - 1, data1);
                    }*/
        }

        for (int i = 1; i < output.rows() - 1; i++) {
            int j = 1;
            output.get(i - 1, j - 1, data1);
            output.get(i + 1, j + 1, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i - 1, j - 1, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i - 1, j - 1, data2);
                    }*/

            output.get(i, j - 1, data1);
            output.get(i, j + 1, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i, j - 1, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i, j - 1, data2);
                    }*/

            output.get(i + 1, j - 1, data1);
            output.get(i - 1, j + 1, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i + 1, j - 1, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i + 1, j - 1, data2);
                    }*/
            j = output.cols() - 2;
            output.get(i - 1, j - 1, data1);
            output.get(i + 1, j + 1, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i + 1, j + 1, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i + 1, j + 1, data1);
                    }*/

            output.get(i, j - 1, data1);
            output.get(i, j + 1, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i, j + 1, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i, j + 1, data1);
                    }*/

            output.get(i + 1, j - 1, data1);
            output.get(i - 1, j + 1, data2);
            data3[0] = (byte)Math.abs(data1[0] - data2[0]);
            output.put(i - 1, j + 1, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i - 1, j + 1, data1);
                    }*/
        }
    }

    private void konvolusiOutput() {
        convolutedOutput = input.clone();
        for (int i = 1; i < output.rows() - 1; i++) {
            for (int j = 1; j < output.cols() - 1; j++) {
                byte[] data = new byte[1];

                output.get(i - 1, j - 1, data);
                nmatrix[0] = data[0];

                output.get(i - 1, j, data);
                nmatrix[1] = data[0];

                output.get(i - 1, j + 1, data);
                nmatrix[2] = data[0];

                output.get(i, j - 1, data);
                nmatrix[3] = data[0];

                output.get(i, j, data);
                nmatrix[4] = data[0];

                output.get(i, j + 1, data);
                nmatrix[5] = data[0];

                output.get(i + 1, j - 1, data);
                nmatrix[6] = data[0];

                output.get(i + 1, j, data);
                nmatrix[7] = data[0];

                output.get(i + 1, j + 1, data);
                nmatrix[8] = data[0];

                data[0] = (byte)operatorKernel.normalizeGrad(operatorKernel.getGradienXY(nmatrix));

                convolutedOutput.put(i-1,j-1,data);
            }
        }
    }

    public byte[] getConvolutedOutput(){
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", convolutedOutput, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public byte[] getInput(){
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", input, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public void processInput(){
        createOutput();
        konvolusiOutput();
    }

}
