/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package field;

import java.util.Random;

/**
 *
 * @author Ricardo Villanueva
 * @author Eduardo Angulo
 */
public abstract class Field2n implements Field {
    
    int n, m3, m2, m1;
    int t;
    int W = 32;
    private static final int w = 8;
    private static final int[] C;
    
    static {
        C = Util.generateTable(16);
    }
    
    public Field2n(int n, int m3, int m2, int m1) {
        this.n = n;
        this.m3 = m3;
        this.m2 = m2;
        this.m1 = m1;
        t = (n + W - 1) / W; //ceil(n/W)
    }
    
    @Override
    public FieldElement addition(FieldElement a, FieldElement b) {
        Field2nElement a1 = (Field2nElement) a;
        Field2nElement b1 = (Field2nElement) b;
        int[] a_rep = a1.getRepresentation();
        int[] b_rep = b1.getRepresentation();
        return new Field2nElement(addition_rep(a_rep, b_rep, t));
    }

    @Override
    public FieldElement division(FieldElement a, FieldElement b) {
        return multiplication(a, inversion(b));
    }
    
    //  (c_2x^2+c_1x+c_0)^2=c_2x^4+c_1x^2+c_0 
    //  111---->10101
    
    //10101011----->  100010001000101   
    //0100010001000101
    @Override
    public FieldElement squaring(FieldElement a) {
        Field2nElement a1 = (Field2nElement) a;
        int[] a_rep = a1.getRepresentation();
        int[] c_rep = reduction_rep(squaring16_rep(a_rep));
        return new Field2nElement(c_rep);
    }

    public int[][] precompute(int[] b_rep, int window) {
        int m = 1 << window;

        int B[][] = new int[m][];
        int t1 = (n + window + W - 1) / W;
        B[0] = new int[t1]; // 0*b(X)
        B[1] = new int[t1];
        System.arraycopy(b_rep, 0, B[1], 0, t); //b(X)
        
        //B_0,B_1
        //B_2^1 = B_1*x
        //B_3 = B_2 + B_1
        //B_2^2 = B_2*x
        //B_5 = B_4 + B_1
        //B_6 = B_4 + B_2
        //B_7 = B_4 + B_3
        //B_2^3 = b_2^2*x
        //.
        //.
        //.
        //B_255 = B_2^7 + B_127
        
        int j1, j2;
        for (int i = 1; i < window; i++) {  // computing x^i*B(X)=X*X^(i-1)*B(X);
            j1 = 1 << i; // 2^i
            j2 = 1 << (i - 1);// 2^(i-1)
            B[j1] = this.shift_left(B[j2], 1);  // X*X^(i-1)*B(X)=x^i*B(X); B_2^i
            for (int j = 1; j < j1; j++) {
                B[j1 + j] = addition_rep(B[j1], B[j], t1);
            }
        }
        /*
        for(int i = 0; i < m; i++) {
            System.out.println(i + " := " + Util.toBinaryString(B[i]));
        }
        */
        return B;
    }

    @Override
    public FieldElement multiplication(FieldElement a, FieldElement b) {
        Field2nElement a1 = (Field2nElement) a;
        Field2nElement b1 = (Field2nElement) b;
        int[] a_rep = a1.getRepresentation();
        int[] b_rep = b1.getRepresentation();
        int[] c_rep = reduction_rep(multiplication_rep(a_rep, b_rep, w));
        return new Field2nElement(c_rep);
    }

    @Override
    public FieldElement inversion(FieldElement a) {
        Field2nElement a1 = (Field2nElement) a;
        int[] f_rep = generate_f();
        //System.out.println(new Field2nElement(f_rep).toString());
        int[] a_inv_rep = inversion_rep(a1.getRepresentation(), f_rep);
        return new Field2nElement(a_inv_rep);
    }

    @Override
    public FieldElement squareRoot(FieldElement a) {
        Field2nElement a1 = (Field2nElement) a;
        int[] a_sqrt_rep = squareRoot_rep(a1.getRepresentation());
        return new Field2nElement(a_sqrt_rep);
    }

    @Override
    public FieldElement randomElement() {
        Random random = new Random();
        int rep[] = new int[t];
        for (int i = 0; i < t; i++) {
            rep[i] = random.nextInt();
        }
        int e = (n % 32) + 1, ee;
        ee = (1 << e) - 1;
        rep[t - 1] = rep[t - 1] & ee;
        return new Field2nElement(rep);
    }

    @Override
    public FieldElement zero() {
        return new Field2nElement(zero_rep());
    }
    
    @Override
    public FieldElement one() {
        return new Field2nElement(one_rep());
    }
    
    protected abstract int[] reduction_rep(int[] a_rep);
    
    protected abstract int[] zero_rep();
    
    protected abstract int[] one_rep();
    
    protected abstract int[] xsqrt_rep();

    private int[] addition_rep(int[] a_rep, int[] b_rep, int t) {
        int[] c_rep = new int[t];
        for (int i = 0; i < t; i++) {
            c_rep[i] = a_rep[i] ^ b_rep[i];
        }
        return c_rep;
    }

    private int[] squaring16_rep(int[] a_rep) {
        int[] c_rep = new int[a_rep.length * 2];
        int mask = (1 << 16) - 1, index;
        int j = 0;
        for (int i = 0; i < a_rep.length; i++) {
            index = a_rep[i] & mask;
            c_rep[j] = this.C[index];
            j++;
            index = (a_rep[i] >>> 16) & mask;
            c_rep[j] = this.C[index];
            j++;
        }
        return c_rep;
    }

    private int[] multiplication_rep(int[] a_rep, int[] b_rep, int window) {
        if (a_rep.length != t || a_rep.length != b_rep.length) {
            return null;
        }
        int c_rep[], B[][] = precompute(b_rep, window);
        int te = 2 * t;
        c_rep = new int[te];
        int mask_w = (1 << window) - 1;
        for (int k = W / window - 1; k >= 0; k--) {
            for (int j = 0; j < t; j++) {
                // System.out.println("w:="+window * k+"j:="+j);
                // System.out.println("-"+Util.toBinaryString(a_rep[j] ));
                int index = ((a_rep[j] >>> (window * k)) & mask_w);
                // System.out.println("*"+Integer.toBinaryString(u));
                for (int i = 0; i < B[index].length; i++) {
                    c_rep[j + i] = c_rep[j + i] ^ B[index][i];
                }
            }
            if (k != 0) {
                c_rep = shift_left(c_rep, window);
            }
        }
        return c_rep;
    }

    private int[] inversion_rep(int[] a_rep, int[] f_rep) {
        int[] u = a_rep, v = f_rep, temp;
        int[] g1 = new int[t], g2 = new int[t];
        g1[0] = 1;
        while(!Util.isOne(u)) {
            int j = Util.getDegree(u) - Util.getDegree(v);
            if(j < 0) {
                temp = u; u = v; v = temp;
                temp = g1; g1 = g2; g2 = temp;
                j = -j;
            }
            int[] zj = new int[t]; zj[j / W] = (1 << (j % W));
            u = addition_rep(u, multiplication_rep(v, zj, w), t);
            //System.out.println(Util.toBinaryString(u));
            g1 = addition_rep(g1, multiplication_rep(g2, zj, w), t);
            //System.out.println(Util.toBinaryString(g1));
        }
        return g1;
    }
    
    private int[] squareRoot_rep(int[] a_rep) {
        int limit = (n + 1)/2;
        int[] copy_even = a_rep, copy_odd = shift_right(a_rep, 1);
        int[] even = new int[t], odd = new int[t];
        for (int i = 0; i < limit; i++) {
            even[i / W] ^= ((copy_even[0] & 1) << (i % W));
            odd[i / W] ^= ((copy_odd[0] & 1) << (i % W));
            copy_even = shift_right(copy_even, 2);
            copy_odd = shift_right(copy_odd, 2);
        }
        //System.out.println((new Field2nElement(even)).toString());
        //System.out.println((new Field2nElement(odd)).toString());
        return addition_rep(even, reduction_rep(
                multiplication_rep(xsqrt_rep(), odd, w)), t);
    }
    
    private int[] generate_f() {
        int[] f_rep;
        if(n % W != 0) {
            f_rep = new int[t];
        }else {
            f_rep = new int[t + 1]; 
        }
        f_rep[0] = 1;
        f_rep[n / W] ^= (1 << (n % W));
        if(m1 != 0){
            f_rep[m1 / W] ^= (1 << (m1 % W));
        }
        if(m2 != 0){
            f_rep[m2 / W] ^= (1 << (m2 % W));
        }
        if(m3 != 0){
            f_rep[m3 / W] ^= (1 << (m3 % W));
        }
        return f_rep;
    }
    
    //n > 0
    private int[] shift_left(int[] rep, int n) {
        //System.out.println(Util.toBinaryString(rep));
        int[] reps = new int[rep.length];
        int mask = (1 << n) - 1, i;
        for (i = rep.length - 1; i >= 1; i--) {
            reps[i] = rep[i] << n;
            reps[i] = reps[i] ^ ((rep[i - 1] >>> (W - n)) & mask);
        }
        reps[0] = (rep[0] << n);
        //System.out.println(Util.toBinaryString(reps));
        return reps;
    }
    
    private int[] shift_right(int[] rep, int n) {
        //System.out.println(Util.toBinaryString(rep));
        int[] reps = new int[rep.length];
        int mask = (1 << n) - 1;
        reps[0] = (rep[0] >>> n);
        for (int i = 1; i < rep.length; i++) {
            reps[i] = rep[i] >>> n;
            reps[i - 1] = reps[i - 1] ^ ((rep[i] & mask) << (W - n));
        }
        //System.out.println(Util.toBinaryString(reps));
        return reps;
    }
    
}
