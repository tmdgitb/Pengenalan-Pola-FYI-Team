package com.ocr.thinning;

import org.opencv.core.Size;

/**
 * Created by Yusfia Hafid A on 10/17/2015.
 */
public class Resizer {
    public static Size resizeTo500Max(int x, int y){
        int xcpy = x, ycpy = y;
        if (x>y) {
            xcpy = ((int)((double)x/(double)x)*500);
            ycpy = ((int)((double)y/(double)x)*500);
        }else{
            xcpy = (int)(((double)x/(double)y)*500);
            ycpy = (int)(((double)y/(double)y)*500);
        }
        return new Size(ycpy,xcpy);
    }
}
