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
public class Field279 extends Field2n {
    
    public Field279() {
        super(79, 0, 0, 9);
    }

    @Override
    protected int[] reduction_rep(int[] b_rep) {
        int T;
        for (int i = b_rep.length - 1; i >= 3; i--) {
            T = b_rep[i];
            b_rep[i - 1] = b_rep[i - 2] ^ (T >>> 6) ^ (T >>> 15);
            b_rep[i - 3] = b_rep[i - 3] ^ (T << 26) ^ (T << 17);
        }
        T = (b_rep[2] >>> 15);
        b_rep[0] = b_rep[0] ^ T ^ (T << 9);
        int[] b_out = new int[2];
        b_out[2] = b_rep[2] & ((1 << 15) - 1);
        b_out[1] = b_rep[1];
        b_out[0] = b_rep[0];
        return b_out;
    }

    @Override
    protected int[] zero_rep() {
        int[] zero = {0x0, 0x0, 0x0};
        return zero;
    }

    @Override
    protected int[] one_rep() {
        int[] one = {0x1, 0x0, 0x0};
        return one;
    }

    @Override
    protected int[] xsqrt_rep() {
        int[] x_sqrt = {0x20, 0x100, 0x0};
        return x_sqrt;
    }
    
}
