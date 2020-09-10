/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.utils;

import field.Field2nElement;
import java.math.BigInteger;
import utils.Pack;
import utils.encoders.Hex;

/**
 * Signature Object Class.
 * @author Eduardo Angulo
 */
public class Signature {
    
    private String s;
    private String salt;
    
    /**
     * Constructor Method.
     * @param s S solution Hex String.
     * @param salt Salt Bytes Hex String.    
     */
    public Signature(String s, String salt) {
        this.s = s;
        this.salt = salt;
    }

    /**
     * Get S solution Hex String.
     * @return S Hex String.
     */
    public String getS() {
        return s;
    }

    /**
     * Get Salt Bytes Hex String.
     * @return Salt Bytes Hex String.
     */
    public String getSalt() {
        return salt;
    }
    
    /**
     * Get S solution Matrix over GF(2^7).
     * @param r
     * @param v
     * @param m
     * @return S Integer Matrix over GF(2^7).
     */
    public Field2nElement[][] getSMatrix(int r, int v, int m) {
        return Pack.unpackField(s, r, m + v, 1);
    }
    
    /**
     * Get Salt Bytes Vector.
     * @return Salt Bytes Vector.
     */
    public byte[] getSaltBytes() {
        return Hex.decode(salt);
    }
    
    /**
     * Override of toString of the Object.
     * @return To String of the Object.
     */
    @Override
    public String toString() {
        return "[" + (new BigInteger(s, 2)).toString(16) + ", " + salt + "]";
    }
    
}
