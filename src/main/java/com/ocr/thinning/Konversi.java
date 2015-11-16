package com.ocr.thinning;

/**
 * Created by Yusfia Hafid A on 11/15/2015.
 */
public class Konversi {
    public static int byteInt(byte i) {
        if (i < 0) {
            return 256 + i;
        } else {
            return i;
        }
    }
}
