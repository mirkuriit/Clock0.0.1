package com.terabyte.clock001;

import android.util.Pair;

import java.util.Random;

/**
 * so, this class has one static method to generate a puzzle. It returns a string of puzzle and answer
 * together
 */
public class AlarmPuzzleGenerator {
    public static Pair<String, Integer> createRandomPuzzle(int hardcoreLevel) {
        Random random = new Random();
        int answer = 2;
        String str = "1 + 1";

        switch (hardcoreLevel) {
            case 0:
                int a = random.nextInt(71);
                int b = random.nextInt(71);

                //50 percent of probability
                if(random.nextBoolean()) {
                    if(a>=b) {
                        answer = a-b;
                        str = a +" - "+b;
                    }
                    else {
                        answer = b-a;
                        str = b +" - "+a;
                    }
                }
                else {
                    answer = a + b;
                    str = a+" + " +b;
                }
                break;
            case 1:
                if(random.nextBoolean()) {
                    //simple linear equation
                    answer = random.nextInt(30);
                    int k = random.nextInt(30);
                    str = k+" * x = "+(answer*k);
                }
                else {
                    a = random.nextInt(501);
                    b = random.nextInt(11);
                    answer = a*b;
                    str = a+" * "+b;
                }
                break;
            case 2:
                int x1 = random.nextInt(20);
                if(random.nextBoolean()) {
                    x1*= -1;
                }
                int x2 = random.nextInt(10);
                if(random.nextBoolean()) {
                    x2*= -1;
                }
                int c = x1*x2;
                b = -(x1+x2);
                answer = Math.min(x1, x2);
                str = "x^2";
                if(b!= 0) {
                    if(Math.abs(b)==1) {
                        if(b == 1) {
                            str+=" + x";
                        }
                        if(b == -1) {
                            str+= " - x";
                        }
                    }
                    else {
                        if(b>0) {
                            str+=" + "+b+"x";
                        }
                        if(b<0) {
                            str+=" - "+Math.abs(b)+"x";
                        }
                    }
                }
                if(c!=0) {
                    if(c>0) {
                        str+=" + "+c;
                    }
                    if(c<0) {
                        str+= " - "+Math.abs(c);
                    }
                }
                break;
            case 3:
                int[] quadratics = new int[10];
                for(int i = 1;i<=10;i++) {
                    quadratics[i-1] = i*i;
                }

                int t1 = quadratics[random.nextInt(10)];
                int t2 = quadratics[random.nextInt(10)];

                c = t1*t2;
                b = -(t1+t2);

                answer = Math.round((float) Math.sqrt(Math.max(t1,t2))*-1);

                str = "x^4";
                if(b!= 0) {
                    if(Math.abs(b)==1) {
                        if(b == 1) {
                            str+=" + x^2";
                        }
                        if(b == -1) {
                            str+= " - x^2";
                        }
                    }
                    else {
                        if(b>0) {
                            str+=" + "+b+"x^2";
                        }
                        if(b<0) {
                            str+=" - "+Math.abs(b)+"x^2";
                        }
                    }
                }
                if(c!=0) {
                    if(c>0) {
                        str+=" + "+c;
                    }
                    if(c<0) {
                        str+= " - "+Math.abs(c);
                    }
                }
                break;

        }

        return new Pair<String, Integer>(str, answer);
    }

}
