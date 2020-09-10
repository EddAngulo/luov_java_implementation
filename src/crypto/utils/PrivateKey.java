/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.utils;

import utils.encoders.Hex;

/**
 * Private Key Object Class.
 * @author Eduardo Angulo
 */
public class PrivateKey {
    
    private final String privateSeed;

    /**
     * Constructor Method.
     * @param privateSeed Private Seed Hex String of LUOV Cryptosystem.
     */
    public PrivateKey(String privateSeed) {
        this.privateSeed = privateSeed;
    }
    
    /**
     * Get Private Seed Hex String of LUOV Cryptosystem.
     * @return Private Seed Hex String.
     */
    public String getPrivateSeed() {
        return privateSeed;
    }
    
    /**
     * Get Private Seed Byte Vector of LUOV Cryptosystem.
     * @return Private Seed Byte Vector.
     */
    public byte[] getPrivateSeedBytes() {
        return Hex.decode(privateSeed);
    }
    
}
