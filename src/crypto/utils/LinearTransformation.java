/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.utils;

import utils.Functions;
import utils.Pack;

/**
 * Linear Transformation Class.
 * @author Eduardo Angulo
 * @author Sebastián Cabarcas
 * @author Andrés Duarte
 * @author Jorge Pinzón
 */
public class LinearTransformation {
    
    private String T;

    /**
     * Constructor Method.
     * @param T T Matrix Hex String.
     */
    public LinearTransformation(String T) {
        this.T = T;
    }

    /**
     * Get T Matrix Hex String.
     * @return T Matrix Hex String.
     */
    public String getT() {
        return T;
    }
    
    /**
     * Generates a matrix of 0s and 1s from linearTrans Bit String.
     * @param v
     * @param m
     * @return Integer (Binary) linearTrans Matrix.
     */
    public int[][] getTMatrix(int v, int m) {
        return Pack.unpackBin(T, v, m);
    }
    
    /**
     * Builds the Linear Transformation Matrix [[1v, linearTrans]; [0, 1m]].
     * @param v
     * @param m
     * @return Linear Transformation Matrix (n x n).
     */
    public int[][] buildLinearTransMatrix(int v, int m) {
        int[][] T_matrix = getTMatrix(v, m);
        int[][] upper = Functions.matrixColumnUnion(
                Functions.identityMatrix(v), T_matrix);
        int[][] lower = Functions.matrixColumnUnion(
                Functions.sameValueMatrix(m, v, 0), 
                Functions.identityMatrix(m));
        return Functions.matrixRowUnion(upper, lower);
    }
    
}
