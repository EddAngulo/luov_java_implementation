/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package field;

/**
 *
 * @author Eduardo Angulo
 */
public class Field207 extends Field2n {
    
    public Field207() {
        super(7, 0, 0, 1);
    }

    @Override
    protected int[] reduction_rep(int[] b_rep) {
        int T = b_rep[0] & 0x1F80;
        b_rep[0] = b_rep[0] ^ (T >>> 6) ^ (T >>> 7);
        int [] b_out = new int[1];
        b_out[0] = b_rep[0] & ((1 << 7) - 1);
        return b_out;
    }

    @Override
    protected int[] zero_rep() {
        int[] zero = {0x0};
        return zero;
    }

    @Override
    protected int[] one_rep() {
        int[] one = {0x1};
        return one;
    }

    @Override
    protected int[] xsqrt_rep() {
        int[] x_sqrt = {0x12};
        return x_sqrt;
    }
    
}
