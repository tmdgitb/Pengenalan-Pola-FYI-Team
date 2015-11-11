package com.ocr.thinning;

/**
 * Created by Yusfia Hafid A on 10/29/2015.
 */
public class OperatorKernel {
    private int[] operatorX;
    private int[] operatorY;
    private int kelas;
    private int a = 0, b = 0, c = 0;

    public OperatorKernel(){
        setOperatorSobel();
    }

    public void setOperatorSobel() {
        operatorX = new int[9];
        operatorY = new int[9];
        a = 1;
        b = 2;
        c = 1;
        operatorX[0] = -a;
        operatorX[2] = a;
        operatorX[3] = -b;
        operatorX[5] = b;
        operatorX[6] = -c;
        operatorX[8] = c;
        operatorY[0] = -a;
        operatorY[1] = -b;
        operatorY[2] = -c;
        operatorY[6] = a;
        operatorY[7] = b;
        operatorY[8] = c;
        kelas = 1;
    }

    public void setOperatorPrewit() {
        operatorX = new int[9];
        operatorY = new int[9];
        a = 1;
        b = 1;
        c = 1;
        operatorX[0] = -a;
        operatorX[2] = a;
        operatorX[3] = -b;
        operatorX[5] = b;
        operatorX[6] = -c;
        operatorX[8] = c;
        operatorY[0] = -a;
        operatorY[1] = -b;
        operatorY[2] = -c;
        operatorY[6] = a;
        operatorY[7] = b;
        operatorY[8] = c;
        kelas = 1;
    }

    public void setCustomOperator(int ai, int bi, int ci) {
        operatorX = new int[9];
        operatorY = new int[9];
        a = ai;
        b = bi;
        c = ci;
        operatorX[0] = -a;
        operatorX[2] = a;
        operatorX[3] = -b;
        operatorX[5] = b;
        operatorX[6] = -c;
        operatorX[8] = c;
        operatorY[0] = -a;
        operatorY[1] = -b;
        operatorY[2] = -c;
        operatorY[6] = a;
        operatorY[7] = b;
        operatorY[8] = c;
        kelas = 1;
    }

    public int[] getOperatorX() {
        return this.operatorX;
    }

    public int[] getOperatorY() {
        return this.operatorY;
    }

    /*
     * p0p1p2 op0op1op2
     * p3p4p5 op3op4op5
     * p6p7p8 op6op7op8
     */
    public int getGradienY(int[] npixel) {
        return Math.abs((operatorX[0] * npixel[0]) + (operatorX[3] * npixel[3]) + (operatorX[6] * npixel[6]) + (operatorX[2] * npixel[2]) + (operatorX[5] * npixel[5]) + (operatorX[8] * npixel[8]));
    }

    public int getGradienX(int[] npixel) {
        return Math.abs((operatorY[0] * npixel[0]) + (operatorY[1] * npixel[1]) + (operatorY[2] * npixel[2]) + (operatorY[6] * npixel[6]) + (operatorY[7] * npixel[7]) + (operatorY[8] * npixel[8]));
    }

    public int getGradienXY(int[] npixel) {
        return getGradienX(npixel) + getGradienY(npixel);
    }

    public int normalizeXorY(int value){
        int max = (a*255)+(b*255)+(c*255);
        double result = ((double)value/(double)max)*255;
        return (int)result;
    }

    public int normalizeGrad(int value){
        int max = 2*((a*255)+(b*255)+(c*255));
        double result = ((double)value/(double)max)*255;
        return (int)result;
    }
}
