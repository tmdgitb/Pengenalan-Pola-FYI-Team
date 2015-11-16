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
    private OperatorKernel operatorKernel = new OperatorKernel();
    private int[] nmatrix;

    public ConvolutionContainer() {
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

    public void setInput(Mat a){
        this.input = a;
    }

    public void setOperatorKernel(int opsi) {
        if (opsi == OperatorOption.SOBEL_OPERATOR) {
            operatorKernel = new OperatorKernel();
            operatorKernel.setOperatorSobel();
        } else if (opsi == OperatorOption.PREWIT_OPERATOR) {
            operatorKernel = new OperatorKernel();
            operatorKernel.setOperatorPrewit();
        }
    }

    public void setOperatorKernel(int a, int b, int c) {
        operatorKernel = new OperatorKernel();
        operatorKernel.setCustomOperator(a, b, c);
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
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i - 1, j - 1, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i - 1, j - 1, data2);
                    }*/

            output.get(i - 1, j, data1);
            output.get(i + 1, j, data2);
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i - 1, j, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i - 1, j, data2);
                    }*/

            output.get(i - 1, j + 1, data1);
            output.get(i + 1, j - 1, data2);
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i - 1, j + 1, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i - 1, j + 1, data2);
                    }*/

            i = output.rows() - 2;

            output.get(i - 1, j - 1, data1);
            output.get(i + 1, j + 1, data2);
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i + 1, j + 1, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i + 1, j + 1, data1);
                    }*/

            output.get(i - 1, j, data1);
            output.get(i + 1, j, data2);
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i + 1, j, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i + 1, j, data1);
                    }*/

            output.get(i - 1, j + 1, data1);
            output.get(i + 1, j - 1, data2);
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i + 1, j - 1, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i + 1, j - 1, data1);
                    }*/
        }

        for (int i = 1; i < output.rows() - 1; i++) {
            int j = 1;
            output.get(i - 1, j - 1, data1);
            output.get(i + 1, j + 1, data2);
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i - 1, j - 1, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i - 1, j - 1, data2);
                    }*/

            output.get(i, j - 1, data1);
            output.get(i, j + 1, data2);
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i, j - 1, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i, j - 1, data2);
                    }*/

            output.get(i + 1, j - 1, data1);
            output.get(i - 1, j + 1, data2);
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i + 1, j - 1, data3);
                    /*if (data1[0] < data2[0]) {
                        output.put(i + 1, j - 1, data2);
                    }*/
            j = output.cols() - 2;
            output.get(i - 1, j - 1, data1);
            output.get(i + 1, j + 1, data2);
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i + 1, j + 1, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i + 1, j + 1, data1);
                    }*/

            output.get(i, j - 1, data1);
            output.get(i, j + 1, data2);
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i, j + 1, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i, j + 1, data1);
                    }*/

            output.get(i + 1, j - 1, data1);
            output.get(i - 1, j + 1, data2);
            data3[0] = (byte) Math.abs(data1[0] - data2[0]);
            output.put(i - 1, j + 1, data3);
                    /*if (data1[0] > data2[0]) {
                        output.put(i - 1, j + 1, data1);
                    }*/
        }
    }

    private void konvolusiOutput() {
        nmatrix = new int[9];
        convolutedOutput = input.clone();
        for (int i = 1; i < output.rows() - 1; i++) {
            for (int j = 1; j < output.cols() - 1; j++) {
                byte[] data = new byte[1];

                output.get(i - 1, j - 1, data);
                nmatrix[0] = Konversi.byteInt(data[0]);

                output.get(i - 1, j, data);
                nmatrix[1] = Konversi.byteInt(data[0]);

                output.get(i - 1, j + 1, data);
                nmatrix[2] = Konversi.byteInt(data[0]);

                output.get(i, j - 1, data);
                nmatrix[3] = Konversi.byteInt(data[0]);

                output.get(i, j, data);
                nmatrix[4] = Konversi.byteInt(data[0]);

                output.get(i, j + 1, data);
                nmatrix[5] = Konversi.byteInt(data[0]);

                output.get(i + 1, j - 1, data);
                nmatrix[6] = Konversi.byteInt(data[0]);

                output.get(i + 1, j, data);
                nmatrix[7] = Konversi.byteInt(data[0]);

                output.get(i + 1, j + 1, data);
                nmatrix[8] = Konversi.byteInt(data[0]);

                data[0] = (byte) operatorKernel.normalizeGrad(operatorKernel.getGradienXY(nmatrix));
                convolutedOutput.put(i - 1, j - 1, data);
            }
        }
    }

    public byte[] getConvolutedOutput() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", convolutedOutput, result);
        byte[] nil = result.toArray();
        return nil;
    }

    public Mat getConvolutedOutputMat(){
        return convolutedOutput;
    }

    public byte[] getInput() {
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", input, result);
        byte[] nil = result.toArray();
        return nil;
    }

    /*
     * 0  1  2  3  4
     * 5  6  7  8  9
     * 10 11 12 13 14
     * 15 16 17 18 19
     * 20 21 22 23 24
     */
    public void processGaussian() {
        operatorKernel.setOperatorGaussian();
        output = new Mat(input.rows() + 4, input.cols() + 4, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        int[][] cek = new int[output.rows()][output.cols()];
        byte[] datain = new byte[1];
        datain[0] = 127;
        for (int i = 0; i < output.rows(); i++) {
            for (int j = 0; j < output.cols(); j++) {
                output.put(i, j, datain);
            }
        }

        for (int i = 0; i < input.rows(); i++) {
            for (int j = 0; j < input.cols(); j++) {
                input.get(i,j,datain);
                output.put(i+2,j+2,datain);
            }
        }

        nmatrix = new int[25];
        convolutedOutput = input.clone();
        for (int i = 2; i < output.rows() - 2; i++) {
            for (int j = 2; j < output.cols() - 2; j++) {
                byte[] data = new byte[1];
                output.get(i - 2, j - 2, data);
                nmatrix[0] = Konversi.byteInt(data[0]);
                output.get(i - 2, j - 1, data);
                nmatrix[1] = Konversi.byteInt(data[0]);
                output.get(i - 2, j, data);
                nmatrix[2] = Konversi.byteInt(data[0]);
                output.get(i - 2, j + 1, data);
                nmatrix[3] = Konversi.byteInt(data[0]);
                output.get(i - 2, j + 2, data);
                nmatrix[4] = Konversi.byteInt(data[0]);
                output.get(i - 1, j - 2, data);
                nmatrix[5] = Konversi.byteInt(data[0]);
                output.get(i - 1, j - 1, data);
                nmatrix[6] = Konversi.byteInt(data[0]);
                output.get(i - 1, j, data);
                nmatrix[7] = Konversi.byteInt(data[0]);
                output.get(i - 1, j + 1, data);
                nmatrix[8] = Konversi.byteInt(data[0]);
                output.get(i - 1, j + 2, data);
                nmatrix[9] = Konversi.byteInt(data[0]);
                output.get(i, j - 2, data);
                nmatrix[10] = Konversi.byteInt(data[0]);
                output.get(i, j - 1, data);
                nmatrix[11] = Konversi.byteInt(data[0]);
                output.get(i, j, data);
                nmatrix[12] = Konversi.byteInt(data[0]);
                output.get(i, j + 1, data);
                nmatrix[13] = Konversi.byteInt(data[0]);
                output.get(i, j + 2, data);
                nmatrix[14] = Konversi.byteInt(data[0]);
                output.get(i + 1, j - 2, data);
                nmatrix[15] = Konversi.byteInt(data[0]);
                output.get(i + 1, j - 1, data);
                nmatrix[16] = Konversi.byteInt(data[0]);
                output.get(i + 1, j, data);
                nmatrix[17] = Konversi.byteInt(data[0]);
                output.get(i + 1, j + 1, data);
                nmatrix[18] = Konversi.byteInt(data[0]);
                output.get(i + 1, j + 2, data);
                nmatrix[19] = Konversi.byteInt(data[0]);
                output.get(i + 2, j - 2, data);
                nmatrix[20] = Konversi.byteInt(data[0]);
                output.get(i + 2, j - 1, data);
                nmatrix[21] = Konversi.byteInt(data[0]);
                output.get(i + 2, j, data);
                nmatrix[22] = Konversi.byteInt(data[0]);
                output.get(i + 2, j + 1, data);
                nmatrix[23] = Konversi.byteInt(data[0]);
                output.get(i + 2, j + 2, data);
                nmatrix[24] = Konversi.byteInt(data[0]);
                cek[i][j]= operatorKernel.normalizeGaussian(operatorKernel.getGradienGauss(nmatrix));
                convolutedOutput.put(i-2, j-2, operatorKernel.normalizeGaussian(operatorKernel.getGradienGauss(nmatrix)));
            }
        }
        log.info("cek {} ",cek);
    }


    public void processInput() {
        createOutput();
        konvolusiOutput();
    }

}
