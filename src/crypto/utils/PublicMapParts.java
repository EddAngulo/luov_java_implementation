/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.utils;

import utils.Pack;

/**
 * Public Map Constant, Linear and First Quadratic Part Class.
 * @author Eduardo Angulo
 */
public class PublicMapParts {
    
    private String C;
    private String L;
    private String Q1;

    /**
     * Constructor Method.
     * @param C Constant Part Matrix Hex String of Public Map of LUOV Cryptosystem.
     * @param L Linear Part Matrix Hex String of Public Map of LUOV Cryptosystem.
     * @param Q1 First Part of Quadratic Part Matrix Hex String of Public Map of LUOV Cryptosystem.
     */
    public PublicMapParts(String C, String L, String Q1) {
        this.C = C;
        this.L = L;
        this.Q1 = Q1;
    }

    /**
     * Get C Constant Part Hex String.
     * @return C Hex String.
     */
    public String getC() {
        return C;
    }

    /**
     * Get L Linear Part Hex String.
     * @return L Hex String.
     */
    public String getL() {
        return L;
    }

    /**
     * Get Q1 First Quadratic Part Hex String.
     * @return Q1 Hex String.
     */
    public String getQ1() {
        return Q1;
    }
    
    /**
     * Get C Constant Part Binary Matrix.
     * @param m
     * @return C Binary Matrix.
     */
    public int[][] getCMatrix(int m) {
        return Pack.unpackBin(C, m, 1);
    }
    
    /**
     * Get L Linear Part Binary Matrix.
     * @param v
     * @param m
     * @return L Binary Matrix.
     */
    public int[][] getLMatrix(int v, int m) {
        return Pack.unpackBin(L, m, m + v);
    }
    
    /**
     * Get Q1 First Quadratic Part Binary Matrix.
     * @param v
     * @param m
     * @return Q1 Binary Matrix.
     */
    public int[][] getQ1Matrix(int v, int m) {
        return Pack.unpackBin(Q1, m, (v*(v + 1)/2) + (v * m));
    }
    
}
