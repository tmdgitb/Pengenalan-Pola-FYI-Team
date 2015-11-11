package com.ocr.thinning;

import java.util.ArrayList;

/**
 * Created by Yusfia Hafid A on 9/27/2015.
 */
public class Classifier {
    private String classifyString;
    private Chain cn;
    private boolean outputHuruf[];
    private boolean outputAngka[];
    private char[] indexHuruf = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V'
            , 'W', 'X', 'Y', 'Z'};

    public Classifier() {
        classifyString = "";
        outputHuruf = new boolean[26];
        outputAngka = new boolean[10];
        for (int i = 0; i < outputHuruf.length; i++) {
            outputHuruf[i] = false;
        }
        for (int i = 0; i < outputAngka.length; i++) {
            outputAngka[i] = false;
        }
    }

    public String getResultHuruf() {
        String result = "";
        for (int i = 0; i < outputHuruf.length; i++) {
            if (outputHuruf[i]) {
                result = indexHuruf[i]+" ";
            }
        }
        return result;
    }

    public String getResultAngka() {
        String result = "";
        for (int i = 0; i < outputAngka.length; i++) {
            if (outputAngka[i]) {
                result = ""+i+" ";
            }
        }
        return result;
    }

    public void setChain(Chain cn) {
        this.cn = cn;
    }

    public void setOutputAngka() {
        outputAngka[0] = classify0();
        outputAngka[1] = classify1();
        outputAngka[2] = classify2();
        outputAngka[3] = classify3();
        outputAngka[4] = classify4();
        outputAngka[5] = classify5();
        outputAngka[6] = classify6();
        outputAngka[7] = classify7();
        outputAngka[8] = classify8();
        outputAngka[9] = classify9();
    }

    public boolean classify0() {

        return false;
    }

    public boolean classify1() {
        return false;
    }

    public boolean classify2() {
        return false;
    }

    public boolean classify3() {
        return false;
    }

    public boolean classify4() {
        return false;
    }

    public boolean classify5() {
        return false;
    }

    public boolean classify6() {
        return false;
    }

    public boolean classify7() {
        return false;
    }

    public boolean classify8() {
        return false;
    }

    public boolean classify9() {
        return false;
    }


    public void setOutputHurus() {
        outputHuruf[0] = classifyA();
        outputHuruf[3] = classifyD();
        outputHuruf[4] = classifyE();
        outputHuruf[5] = classifyF();
        outputHuruf[7] = classifyH();
        outputHuruf[8] = classifyI();
        outputHuruf[11] = classifyL();
        outputHuruf[19] = classifyT();
    }

    public boolean classifyA() {
        char x[] = cn.chainString.toCharArray();
        char y[] = cn.belok.toCharArray();
        boolean result = false;
        int counter = 0;
        boolean node[] = new boolean[10];
        for (int i = 0; i < node.length; i++) {
            node[i] = false;
        }
        //node 0
        if (counter >= x.length) return false;
        if (x[counter] == '+') {
            node[0] = true;
            counter++;
        } else {
            node[0] = false;
            return false;
        }
        //node 1
        if (node[0]) {
            if (counter >= x.length) return false;
            int jump = counter;
            if (x[counter] == '+') {
                node[1] = true;
                counter++;
            } else {
                node[1] = false;
            }
            if (x[jump] == '-') {
                node[2] = true;
                counter++;
            } else {
                node[2] = false;
            }
        } else {
            node[1] = false;
            node[2] = false;
            return false;
        }
        //node 2
        if (node[1]) {
            if (counter >= x.length) return false;
            if (x[counter] == '-') {
                node[2] = true;
                counter++;
            } else {
                return false;
            }
        }
        //node 3
        if (node[2]) {
            counter++;
            if (counter >= x.length) return false;
            char xtemp = x[counter - 1];
            char xnow = x[counter];
            while (xtemp == '+' && xnow == '-' && counter < x.length) {
                xtemp = x[counter - 1];
                xnow = x[counter];
                counter = counter + 2;
            }
            if (counter >= x.length) {
                return false;
            } else {
                counter = counter - 1;
            }
            int sum = 0;
            while (x[counter] == '+' && y[counter] != '6') {
                counter++;
                sum++;
                if (counter >= x.length) return false;
            }
            if (sum <= 2) {
                node[3] = true;
                counter++;
            } else {
                node[3] = false;
                return false;
            }
        }
        //node 4
        if (node[3]) {
            if (counter >= x.length) return false;
            if (x[counter] == '+') {
                node[4] = true;
                counter++;
            } else {
                node[5] = true;
                counter++;
            }
        }
        //node 5
        if (node[4]) {
            if (counter >= x.length) return false;
            if (x[counter] == '-') {
                node[5] = true;
                counter++;
            }
        }
        //node 6
        if (node[5]) {
            counter++;
            if (counter >= x.length) return false;
            char xtemp = x[counter - 1];
            char xnow = x[counter];
            int sum = 0;
            while (xtemp == '+' && xnow == '-' && counter < x.length) {
                xtemp = x[counter - 1];
                xnow = x[counter];
                if (xtemp == '-' && xnow == '-') break;
                counter = counter + 2;
                sum++;
            }
            counter = counter - 1;
            if (counter >= x.length) return false;
            if (x[counter] == '-') {
                node[6] = true;
                counter++;
            } else {
                return false;
            }
        }
        //node 7
        if (node[6]) {
            int sum = 0;
            if (counter>=x.length) return false;
            while (x[counter] == '-' && counter < x.length) {
                sum++;
                counter++;
                if (counter>=x.length) return false;
            }
            if (sum == 2) {
                node[7] = true;
            } else {
                return false;
            }
        }
        //node 8
        if (node[7]) {
            counter++;
            if (counter >= x.length) return false;
            char xtemp = x[counter - 1];
            char xnow = x[counter];
            int sum = 0;
            while (xtemp == '+' && xnow == '-' && counter < x.length) {
                xtemp = x[counter - 1];
                xnow = x[counter];
                counter = counter + 2;
                sum++;
            }
            if (counter >= x.length) {
                return false;
            } else {
                counter = counter - 1;
            }
            if (x[counter] == '+') {
                if (counter>=x.length) return false;
                while (x[counter] == '+' && y[counter] != 6 && counter < x.length) {
                    counter++;
                    if (counter>=x.length) return false;
                }
                if (counter >= x.length) {
                    return false;
                } else {
                    node[8] = true;
                    counter++;
                }
            } else {
                counter++;
                if (counter >= x.length) return false;
                if (x[counter] == '+') {
                    while (x[counter] == '+' && y[counter] != 6 && counter < x.length) {
                        counter++;
                    }
                    if (counter >= x.length) {
                        return false;
                    } else {
                        node[8] = true;
                        counter++;
                    }
                }
            }
        }
        //node 9
        if (node[8]) {
            if (counter >= x.length) return false;
            if (x[counter] == '+' && cn.innerChain.size() == 1) {
                node[9] = true;
            }
        }
        return node[9];
    }

    public boolean classifyD() {
        boolean node[] = new boolean[6];
        char x[] = cn.chainString.toCharArray();
        int counter = 0;
        if (x[counter] == '+') {
            node[0] = true;
        } else {
            return false;
        }
        if (node[0]) {
            counter++;
            if (counter >= x.length) return false;
            char xtemp = x[counter - 1];
            char xnow = x[counter];
            while (xtemp == '+' && xnow == '-' && counter < x.length) {
                xtemp = x[counter - 1];
                xnow = x[counter];
                if (xnow == '+' && xtemp == '+') break;
                counter = counter + 2;
            }
            if (counter >= x.length) return false;
            if (x[counter] == '+') {
                node[1] = true;
            } else return false;

            if (node[1]) {
                counter++;
                if (counter >= x.length) return false;
                xtemp = x[counter - 1];
                xnow = x[counter];
                while (xtemp == '+' && xnow == '-' && counter < x.length) {
                    xtemp = x[counter - 1];
                    xnow = x[counter];
                    if (xnow == '+' && xtemp == '+') break;
                    counter = counter + 2;
                }
                if (counter >= x.length) return false;
                if (x[counter] == '+') {
                    node[2] = true;
                } else return false;
            }

            if (node[2]) {
                counter++;
                if (counter >= x.length) return false;
                xtemp = x[counter - 1];
                xnow = x[counter];
                while (xtemp == '+' && xnow == '-' && counter < x.length) {
                    xtemp = x[counter - 1];
                    xnow = x[counter];
                    if (xnow == '+' && xtemp == '+') break;
                    counter = counter + 2;
                }
                if (counter >= x.length) return false;
                if (x[counter] == '+') {
                    node[3] = true;
                } else return false;
            }
            if (node[3]) {
                counter++;
                if (counter >= x.length) return false;
                xtemp = x[counter - 1];
                xnow = x[counter];
                while (xtemp == '+' && xnow == '-' && counter < x.length) {
                    xtemp = x[counter - 1];
                    xnow = x[counter];
                    if (xnow == '+' && xtemp == '+') break;
                    counter = counter + 2;
                }
                if (counter >= x.length) return false;
                if (x[counter] == '+') {
                    node[4] = true;
                    counter++;
                } else return false;
            }

            if (node[4]) {
                if (!(x.length-counter==0))return false;
                if (cn.innerChain.size() == 1) {
                    node[5] = true;
                } else return false;
            }
        }
        return node[5];
    }

    public boolean classifyE() {
        if (cn.chainString.equals("++----++----+++")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean classifyF() {
        if (cn.chainString.equals("++----++--++")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean classifyH() {
        if (cn.chainString.equals("+----++++----++")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean classifyI() {
        if (cn.chainString.equals("+++")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean classifyL() {
        if (cn.chainString.equals("+--+++")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean classifyT() {
        if (cn.chainString.equals("++--++--+")) {
            return true;
        } else {
            return false;
        }
    }
}
