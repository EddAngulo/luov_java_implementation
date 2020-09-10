/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.utils;

import digest.SHAKEDigest;
import field.Util;
import java.math.BigInteger;
import java.security.SecureRandom;
import utils.Functions;
import utils.encoders.Hex;

/**
 * PRNG Class.
 * @author Eduardo Angulo
 */
public class PRNG {
    
    /**
     * Generates a Random Byte Array of the Given Length.
     * @param length Array Length.
     * @return Random Byte Array.
     * @throws java.lang.Exception     
     */
    public static byte[] randomBytes(int length) throws Exception {
        byte[] rnd = new byte[length];
        SecureRandom.getInstanceStrong().nextBytes(rnd);
        return rnd;
    }
    
    /**
     * Generates a pseudo random C, L, Q1 using Chacha Engine.
     * @param public_seed Public Seed of LUOV cryptosystem.
     * @param v
     * @param m
     * @return Array that contains C, L, Q1 Hex Strings.
     * @throws java.lang.Exception
     */
    public static PublicMapParts generateCLQ1(String public_seed, int v, int m) throws Exception {
        SHAKEDigest shake = new SHAKEDigest(256);
        byte[] pk = Hex.decode(public_seed);
        String ALL = "";
        int N = m + v;
        int Q1_DIM = (v*(v + 1)/2) + (v * m);
        float TOT_DIM = (1 + N + Q1_DIM)*m;
        shake.update(pk, 0, pk.length);
        for (int i = 0; i < (int) (Math.ceil(TOT_DIM/256)); i++) {
            byte[] ac = new byte[32];
            shake.doOutput(ac, 0, ac.length);
            int[] aux = new int[8];
            for (int j = 0; j < 8; j++) {
                aux[j] = Functions.bytesToInteger(ac[4*j], ac[4*j+1], ac[4*j+2], ac[4*j+3]);
            }
            ALL += Util.toBinaryStringR(aux);
        }
        String C = ALL.substring(0, m);
        String L = ALL.substring(m, m*(N+1));
        String Q1 = ALL.substring(m*(N+1), (int) TOT_DIM);
        return new PublicMapParts(C, L, Q1);
    }
    
    /**
     * Hash Given Data using Keccak512 taking only the Required Bits.
     * @param data Data to be Hashed.
     * @param bitLength Required Bit Length.
     * @return Hashed Byte Array.
     */
    public static String getHashDigest(byte[] data, int bitLength) {
        SHAKEDigest shake = new SHAKEDigest(256);
        int rounds = (bitLength + 256 - 1) / 256;
        String bin = "";
        shake.update(data, 0, data.length);
        for (int i = 0; i < rounds; i++) {
            byte[] ac = new byte[32];
            shake.doOutput(ac, 0, ac.length);
            int[] aux = new int[8];
            for (int j = 0; j < 8; j++) {
                aux[j] = Functions.bytesToInteger(ac[4*j], ac[4*j+1], ac[4*j+2], ac[4*j+3]);
            }
            bin += Util.toBinaryStringR(aux);
        }
        return bin.substring(0, bitLength);
    }
    
}
