package com.ocr.thinning;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Yusfia Hafid A on 9/21/2015.
 */
public class Chain {
    public ArrayList<Integer> chain = new ArrayList<Integer>();
    public String chainString = "";
    public String subtChain = "";
    public String belok = "";
    public int coordX;
    public int coordY;
    public ArrayList<Chain> innerChain = new ArrayList<Chain>();

    public void printChainString() {
        System.out.println(chainString);
    }

    public String printChain() {
        String result = "";
        //System.out.print("(" + coordX + "," + coordY + ")");
        for (int i = 0; i < chain.size(); i++) {
            result = result+chain.get(i) + ",";
        }
        return result;
    }

    public void printSubtChain() {
        System.out.println(subtChain);
    }

    public void changeStringChain() {
        char x[] = chainString.toCharArray();
        String subtitute = "";
        String tempChanged = "";
        int first = 0;
        for (int i = 1; i < x.length; i++) {
            char temp = x[i - 1];
            char crnt = x[i];
            char subtchar = ' ';
            if (temp != crnt) {
                first++;
                if (first == 1) {
                    subtchar = temp;
                    subtitute = subtitute + subtchar;
                }
                tempChanged = tempChanged + temp;
            } else {
                subtitute = subtitute + temp;
                first = 0;
            }
        }
        subtChain = subtitute;
    }

    public void setChainString() {
        for (int i = 1; i < chain.size(); i++) {
            String sign = "";
            int temp = chain.get(i - 1);
            int current = chain.get(i);
            if (temp == 0) {
                if (current == 0) {
                    sign = "";
                } else if (current == 1) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 2) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 3) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 4) {
                    sign = "";
                } else if (current == 5) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 6) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 7) {
                    sign = "-";
                    belok = belok+current+"";
                }
            } else if (temp == 1) {
                if (current == 0) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 1) {
                    sign = "";
                } else if (current == 2) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 3) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 4) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 5) {
                    sign = "";
                } else if (current == 6) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 7) {
                    sign = "-";
                    belok = belok+current+"";
                }
            } else if (temp == 2) {
                if (current == 0) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 1) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 2) {
                    sign = "";
                } else if (current == 3) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 4) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 5) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 6) {
                    sign = "";
                } else if (current == 7) {
                    sign = "-";
                    belok = belok+current+"";
                }
            } else if (temp == 3) {
                if (current == 0) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 1) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 2) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 3) {
                    sign = "";
                } else if (current == 4) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 5) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 6) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 7) {
                    sign = "";
                }
            } else if (temp == 4) {
                if (current == 0) {
                    sign = "";
                } else if (current == 1) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 2) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 3) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 4) {
                    sign = "";
                } else if (current == 5) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 6) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 7) {
                    sign = "+";
                    belok = belok+current+"";
                }
            } else if (temp == 5) {
                if (current == 0) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 1) {
                    sign = "";
                } else if (current == 2) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 3) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 4) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 5) {
                    sign = "";
                } else if (current == 6) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 7) {
                    sign = "+";
                    belok = belok+current+"";
                }
            } else if (temp == 6) {
                if (current == 0) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 1) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 2) {
                    sign = "";
                } else if (current == 3) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 4) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 5) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 6) {
                    sign = "";
                } else if (current == 7) {
                    sign = "+";
                    belok = belok+current+"";
                }
            } else if (temp == 7) {
                if (current == 0) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 1) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 2) {
                    sign = "+";
                    belok = belok+current+"";
                } else if (current == 3) {
                    sign = "";
                } else if (current == 4) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 5) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 6) {
                    sign = "-";
                    belok = belok+current+"";
                } else if (current == 7) {
                    sign = "";
                }
            }
            chainString = chainString + sign;
        }
    }

    public void remove274() {
        //System.out.println("Size : " + innerChain.size());
        ArrayList<Chain> c = new ArrayList<Chain>();
        for (int i = 0; i < innerChain.size(); i++) {
            //System.out.println(innerChain.get(i).chain.size());
            if (innerChain.get(i).chain.size() == 3) {
                if (innerChain.get(i).chain.get(0) == 2 && innerChain.get(i).chain.get(1) == 7 && innerChain.get(i).chain.get(2) == 4) {
                    c.add(innerChain.get(i));
                }
            }
        }
        for (int i = 0; i < c.size(); i++) {
            innerChain.remove(c.get(i));
        }
    }
}
