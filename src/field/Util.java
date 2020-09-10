/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package field;

/**
 *
 * @author Ricardo Villanueva
 * @author Eduardo Angulo
 */
public class Util {
    
    public static final String A = "00000000000000000000000000000000";

    public static String toBinaryString(int i) {
        String output = Integer.toBinaryString(i);
        output = A.substring(output.length()) + output;
        return output;
    }

    public static String toBinaryString(int[] rep) {
        String output = "";
        for (int i = 0; i < rep.length; i++) {
            output = Util.toBinaryString(rep[i]) + output;
        }
        return output;
    }
    
    public static String toBinaryStringR(int[] rep) {
        String output = "";
        for (int i = 0; i < rep.length; i++) {
            output += Util.toBinaryString(rep[i]);
        }
        return output;
    }
    
    //1000001..--->1100001 si usas >>
    //1000001..--->0100001 si usas >>>
    public static int findPosTheMostSignificantbit(int i) {
        int bitpos = 0;
        while (i != 0) {
            bitpos++;
            i = i >>> 1;
        }
        return bitpos;
    }
    
    //0,1,2,3,....,65535
    //0000000000000000
    //0000000000000001---->0000000000000100
    //0000000000000010----->0000000000000100

    public static int[] generateTable(int t) {
        int m = 1 << t, v, ac, pos;  //1 << 2
        int[] table = new int[m];
        int maks = 1 << 3 - 1;
        for (int i = 0; i < m; i++) {
            v = i;
            if ((v & 1) == 1) {
                ac = i;  // i*1
            } else {
                ac = 0; //
            }
            for (int j = 1; j < t; j++) {
                v = v >>> 1;
                if ((v & 1) == 1) {
                    ac = ac ^ (i << j); // i*x^j----> (x^2+x+1)*x^3=x^5+x^4+x^3 
                } 
            }
            table[i] = ac;
        }
        return table;
    }
    
    public static boolean isOne(int[] a) {
        for (int i = a.length - 1; i > 0; i--) {
            if(a[i] != 0) {
                return false;
            }
        }
        return a[0] == 1;
    }
    
    public static int getDegree(int[] rep) {
        int i = rep.length - 1;
        while (i >= 0 && rep[i] == 0) {
            i--;
        }
        if (i < 0) {
            return 0;
        }
        int degree = 32*i + findPosTheMostSignificantbit(rep[i]) - 1;
        return degree;
    }
    
    public static boolean repEquals(int[] rep1, int[] rep2) {
        if(rep1.length != rep2.length) {
            return false;
        }
        for (int i = 0; i < rep1.length; i++) {
            if(rep1[i] != rep2[i]) {
                return false;
            }
        }
        return true;
    }
    
}
