package com.ocr.thinning;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.opencv.core.Mat;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Yusfia Hafid A on 9/21/2015.
 */
public class ChainCode {
    private Mat image;
    private byte checkImage[][];
    private int background = 0;
    private int foreground = 1;
    private ArrayList<Chain> chainlist;

    public ChainCode() {
        chainlist = new ArrayList<Chain>();
    }

    public int getForeground() {
        return foreground;
    }

    public void setForeground(int foreground) {
        this.foreground = foreground;
    }

    public Mat getImage() {
        return image;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setImage(Mat image) {
        this.image = image;
        this.checkImage = new byte[image.rows()][image.cols()];
    }

    public void getChainCoordinate() {
        chainlist = new ArrayList<Chain>();
        int row = image.rows();
        int col = image.cols();
        int colour[] = new int[1];
        int counter = 0;
        for (int i = 0; i < row; i++) {
            Mat scnLine = image.row(i);
            for (int j = 0; j < col; j++) {
                byte[] tinyimg = new byte[1];
                scnLine.get(0, j, tinyimg);
                if (BinnaryTreshold.grayScale(tinyimg) == foreground && i != 0
                        && j != 0 && i != row - 1 && j != col - 1 && checkImage[i][j] == 0) {
                    counter++;
                    Chain chain = new Chain();
                    chain.coordX = i;
                    chain.coordY = j;
                    getRecursiveChain(i, j, chain, true, 0, foreground);
                    Point start = findStartFillPoint(i, j, foreground);
                    fillInside(start.x, start.y, chain, foreground);
                    chain.remove274();
                    chain.setChainString();
                    //chain.printChainString();
                    chain.changeStringChain();
                    //chain.printSubtChain();
                    chainlist.add(chain);
                    //chain.printChain();
                }
            }
        }
        //printBinner();
    }

    private Point findStartFillPoint(int i, int j, int fg) {
        Point p = new Point(i, j);
        if (checkNearby(i - 1, j + 1, fg) && checkImage[i - 1][j + 1] == 0) {
            p = new Point(i - 1, j + 1);
        } else if (checkNearby(i, j + 1, fg) && checkImage[i][j + 1] == 0) {
            p = new Point(i, j + 1);
        } else if (checkNearby(i + 1, j + 1, fg) && checkImage[i + 1][j + 1] == 0) {
            p = new Point(i + 1, j + 1);
        } else if (checkNearby(i + 1, j, fg) && checkImage[i + 1][j] == 0) {
            p = new Point(i + 1, j);
        } else if (checkNearby(i + 1, j - 1, fg) && checkImage[i + 1][j - 1] == 0) {
            p = new Point(i + 1, j - 1);
        } else if (checkNearby(i, j - 1, fg) && checkImage[i][j - 1] == 0) {
            p = new Point(i, j - 1);
        } else if (checkNearby(i - 1, j - 1, fg) && checkImage[i - 1][j - 1] == 0) {
            p = new Point(i - 1, j - 1);
        } else if (checkNearby(i - 1, j, fg) && checkImage[i - 1][j] == 0) {
            p = new Point(i - 1, j);
        } else {
            if (checkNearby(i - 1, j + 1, fg) && checkImage[i - 1][j + 1] == 1) {
                p = findStartFillPoint(i - 1, j + 1, fg);
            } else if (checkNearby(i, j + 1, fg) && checkImage[i][j + 1] == 1) {
                p = findStartFillPoint(i, j + 1, fg);
                ;
            } else if (checkNearby(i + 1, j + 1, fg) && checkImage[i + 1][j + 1] == 1) {
                p = findStartFillPoint(i + 1, j + 1, fg);
            } else if (checkNearby(i + 1, j, fg) && checkImage[i + 1][j] == 1) {
                p = findStartFillPoint(i + 1, j, fg);
            } else if (checkNearby(i + 1, j - 1, fg) && checkImage[i + 1][j - 1] == 1) {
                p = findStartFillPoint(i - 1, j + 1, fg);
            } else if (checkNearby(i, j - 1, fg) && checkImage[i][j - 1] == 1) {
                p = findStartFillPoint(i - 1, j + 1, fg);
            } else if (checkNearby(i - 1, j - 1, fg) && checkImage[i - 1][j - 1] == 1) {
                p = findStartFillPoint(i - 1, j + 1, fg);
            } else if (checkNearby(i - 1, j, fg) && checkImage[i - 1][j] == 1) {
                p = findStartFillPoint(i - 1, j + 1, fg);
            }
        }
        return p;
    }

    private void fillInside(int i, int j, Chain chain, int fg) {
        //cek atas
        if (checkNearby(i - 1, j, fg) && checkImage[i - 1][j] == 0) {
            setCheckImageBool(i - 1, j);
            fillInside(i - 1, j, chain, fg);
        } else if (checkNearby(i - 1, j, background) && checkImage[i - 1][j] == 0) {
            Chain inChain = new Chain();
            inChain.coordX = i - 1;
            inChain.coordY = j;
            getRecursiveChain(i - 1, j, inChain, true, 6, background);
            chain.innerChain.add(inChain);
        }

        //cek kanan
        if (checkNearby(i, j + 1, fg) && checkImage[i][j + 1] == 0) {
            setCheckImageBool(i, j + 1);
            fillInside(i, j + 1, chain, fg);
        } else if (checkNearby(i, j + 1, background) && checkImage[i][j + 1] == 0) {
            Chain inChain = new Chain();
            inChain.coordX = i;
            inChain.coordY = j + 1;
            getRecursiveChain(i, j + 1, inChain, true, 0, background);
            chain.innerChain.add(inChain);
        }

        //cek bawah
        if (checkNearby(i + 1, j, fg) && checkImage[i + 1][j] == 0) {
            setCheckImageBool(i + 1, j);
            fillInside(i + 1, j, chain, fg);
        } else if (checkNearby(i + 1, j, background) && checkImage[i + 1][j] == 0) {
            Chain inChain = new Chain();
            inChain.coordX = i + 1;
            inChain.coordY = j;
            getRecursiveChain(i + 1, j, inChain, true, 2, background);
            chain.innerChain.add(inChain);
        }

        //cek kiri
        if (checkNearby(i, j - 1, fg) && checkImage[i][j - 1] == 0) {
            setCheckImageBool(i, j - 1);
            fillInside(i, j - 1, chain, fg);
        } else if (checkNearby(i, j - 1, background) && checkImage[i][j - 1] == 0) {
            Chain inChain = new Chain();
            inChain.coordX = i;
            inChain.coordY = j - 1;
            getRecursiveChain(i, j - 1, inChain, true, 4, background);
            chain.innerChain.add(inChain);
        }
    }

    private void getRecursiveChain(int i, int j, Chain chain, boolean first, int dir, int fg) {
        if ((chain.coordX == i) && (chain.coordY == j) && !first) {
            setCheckImageBool(i, j);
            return;
        } else {
            if (first && fg == foreground) {
                if (checkNearby(i - 1, j, fg)) {
                    dir = 0;
                } else if (checkNearby(i - 1, j + 1, fg)) {
                    dir = 1;
                } else if (checkNearby(i, j + 1, fg)) {
                    dir = 2;
                } else if (checkNearby(i + 1, j + 1, fg)) {
                    dir = 3;
                } else if (checkNearby(i + 1, j, fg)) {
                    dir = 4;
                } else if (checkNearby(i + 1, j - 1, fg)) {
                    dir = 5;
                } else if (checkNearby(i, j - 1, fg)) {
                    dir = 6;
                } else if (checkNearby(i - 1, j - 1, fg)) {
                    dir = 7;
                } else dir = 8;
            }
            first = false;
            switch (dir) {
                case 0:
                    chain.chain.add(dir);
                    if (checkNearby(i + 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 5;
                        getRecursiveChain(i + 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 6;
                        getRecursiveChain(i, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 7;
                        getRecursiveChain(i - 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 0;
                        getRecursiveChain(i - 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 1;
                        getRecursiveChain(i - 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 2;
                        getRecursiveChain(i, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 3;
                        getRecursiveChain(i + 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 4;
                        getRecursiveChain(i + 1, j, chain, first, dir, fg);
                    }
                    break;
                case 1:
                    chain.chain.add(dir);
                    if (checkNearby(i, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 6;
                        getRecursiveChain(i, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 7;
                        getRecursiveChain(i - 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 0;
                        getRecursiveChain(i - 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 1;
                        getRecursiveChain(i - 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 2;
                        getRecursiveChain(i, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 3;
                        getRecursiveChain(i + 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 4;
                        getRecursiveChain(i + 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 5;
                        getRecursiveChain(i + 1, j - 1, chain, first, dir, fg);
                    }
                    break;
                case 2:
                    chain.chain.add(dir);
                    if (checkNearby(i - 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 7;
                        getRecursiveChain(i - 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 0;
                        getRecursiveChain(i - 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 1;
                        getRecursiveChain(i - 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 2;
                        getRecursiveChain(i, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 3;
                        getRecursiveChain(i + 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 4;
                        getRecursiveChain(i + 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 5;
                        getRecursiveChain(i + 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 6;
                        getRecursiveChain(i, j - 1, chain, first, dir, fg);
                    }
                    break;
                case 3:
                    chain.chain.add(dir);
                    if (checkNearby(i - 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 0;
                        getRecursiveChain(i - 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 1;
                        getRecursiveChain(i - 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 2;
                        getRecursiveChain(i, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 3;
                        getRecursiveChain(i + 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 4;
                        getRecursiveChain(i + 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 5;
                        getRecursiveChain(i + 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 6;
                        getRecursiveChain(i, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 7;
                        getRecursiveChain(i - 1, j - 1, chain, first, dir, fg);
                    }
                    break;
                case 4:
                    chain.chain.add(dir);
                    if (checkNearby(i - 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 1;
                        getRecursiveChain(i - 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 2;
                        getRecursiveChain(i, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 3;
                        getRecursiveChain(i + 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 4;
                        getRecursiveChain(i + 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 5;
                        getRecursiveChain(i + 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 6;
                        getRecursiveChain(i, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 7;
                        getRecursiveChain(i - 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 0;
                        getRecursiveChain(i - 1, j, chain, first, dir, fg);
                    }
                    break;
                case 5:
                    chain.chain.add(dir);
                    if (checkNearby(i, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 2;
                        getRecursiveChain(i, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 3;
                        getRecursiveChain(i + 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 4;
                        getRecursiveChain(i + 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 5;
                        getRecursiveChain(i + 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 6;
                        getRecursiveChain(i, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 7;
                        getRecursiveChain(i - 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 0;
                        getRecursiveChain(i - 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 1;
                        getRecursiveChain(i - 1, j + 1, chain, first, dir, fg);
                    }
                    break;
                case 6:
                    chain.chain.add(dir);
                    if (checkNearby(i + 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 3;
                        getRecursiveChain(i + 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 4;
                        getRecursiveChain(i + 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 5;
                        getRecursiveChain(i + 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 6;
                        getRecursiveChain(i, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 7;
                        getRecursiveChain(i - 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 0;
                        getRecursiveChain(i - 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 1;
                        getRecursiveChain(i - 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 2;
                        getRecursiveChain(i, j + 1, chain, first, dir, fg);
                    }
                    break;
                case 7:
                    chain.chain.add(dir);
                    if (checkNearby(i + 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 4;
                        getRecursiveChain(i + 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 5;
                        getRecursiveChain(i + 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 6;
                        getRecursiveChain(i, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j - 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 7;
                        getRecursiveChain(i - 1, j - 1, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j, fg)) {
                        setCheckImageBool(i, j);
                        dir = 0;
                        getRecursiveChain(i - 1, j, chain, first, dir, fg);
                    } else if (checkNearby(i - 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 1;
                        getRecursiveChain(i - 1, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 2;
                        getRecursiveChain(i, j + 1, chain, first, dir, fg);
                    } else if (checkNearby(i + 1, j + 1, fg)) {
                        setCheckImageBool(i, j);
                        dir = 3;
                        getRecursiveChain(i + 1, j + 1, chain, first, dir, fg);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private boolean checkNearby(int i, int j, int fg) {
        byte[] tinyimg = new byte[1];
        image.get(i, j, tinyimg);
        if (tinyimg[0] == fg) {
            return true;
        } else {
            return false;
        }
    }

    private void setCheckImageBool(int i, int j) {
        this.checkImage[i][j] = 1;
    }

    private void printBinner() {
        for (int i = 0; i < image.rows(); i++) {
            for (int j = 0; j < image.cols(); j++) {
                System.out.print("" + checkImage[i][j] + " ");
            }
            System.out.println();
        }
    }

    public String printInfo() {
        String result = "";
        for (int i = 0; i < chainlist.size(); i++) {
            result = result + "================================================\n";
            result = result + chainlist.get(i).printChain()+"\n";
            for (int j = 0; j < chainlist.get(i).innerChain.size(); j++) {
                result = result + chainlist.get(i).innerChain.get(j).printChain()+ "\n";
            }
            Classifier cl = new Classifier();
            cl.setChain(chainlist.get(i));
            cl.setOutputHurus();
            result = result + cl.getResultHuruf()+"\n";
            result = result + "================================================\n";
        }
        return result;
    }
}
