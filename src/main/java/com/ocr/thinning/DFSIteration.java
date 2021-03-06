package com.ocr.thinning;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Yusfia Hafid A on 11/19/2015.
 */
public class DFSIteration {
    private static final Logger log = LoggerFactory.getLogger(DFSIteration.class);
    private int background = 0;
    private int foreground = 255;
    private Mat binary;
    private boolean cek[][];
    private ArrayList<BoundingObject> bos;
    private BoundingObject bo;

    public DFSIteration() {
        bos = new ArrayList<>();
    }

    public void setBinary(Mat in) {
        binary = in.clone();
        cek = new boolean[in.rows()][in.cols()];
    }

    public ArrayList<BoundingObject> getBoundingObject() {
        return bos;
    }

    public void fillForeground() {
        log.info("binary rows {} cols {}", binary.rows(),binary.cols());
        for (int i = 0; i < binary.rows(); i++) {
            for (int j = 0; j < binary.cols(); j++) {
                if (checkNeightbouringElement(i, j)) {
                    assignMark(i,j);
                    bo = new BoundingObject();
                    bo.setX_max(i);
                    bo.setX_min(i);
                    bo.setY_min(j);
                    bo.setY_max(j);
                    iterate(i, j);
                    bos.add(bo);
                }
            }
        }
    }

    private void iterate(int i, int j) {
        if ((i > 0 && i < binary.rows()-1 && j > 0 && j < binary.cols()-1)) {
            if (i < bo.getX_min()) {
                bo.setX_min(i);
            }
            if (i > bo.getX_max()) {
                bo.setX_max(i);
            }
            if (j < bo.getY_min()) {
                bo.setY_min(j);
            }
            if (j > bo.getY_max()) {
                bo.setY_max(j);
            }
            if (checkNeightbouringElement(i - 1, j)) {
                assignMark(i - 1, j);
                iterate(i - 1, j);
            }
            if (checkNeightbouringElement(i, j + 1)) {
                assignMark(i, j + 1);
                iterate(i, j + 1);
            }
            if (checkNeightbouringElement(i + 1, j)) {
                assignMark(i + 1, j);
                iterate(i + 1, j);
            }
            if (checkNeightbouringElement(i, j - 1)) {
                assignMark(i, j - 1);
                iterate(i, j - 1);
            }
        }
    }

    private boolean checkNeightbouringElement(int i, int j) {
        if ((i > 0 && i < binary.rows()-1 && j > 0 && j < binary.cols()-1)) {
            byte[] data = new byte[1];
            binary.get(i, j, data);
            return Byte.toUnsignedInt(data[0]) == foreground && !cek[i][j];
        }else return false;
    }

    private void assignMark(int i, int j) {
        cek[i][j] = true;
    }


}
