/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import crypto.parameters.Parameters;
import crypto.utils.*;
import digest.SHAKEDigest;
import field.Field2n;
import field.Field2nElement;
import field.Util;
import java.math.BigInteger;
import utils.Functions;
import utils.Pack;
import utils.encoders.Hex;

/**
 * LUOV Cryptosystem Class
 * @author Eduardo Angulo
 */
public class LUOV {
    
    public final Parameters parameters;
    private final SHAKEDigest shake;
    
    private KeyPair keyPair;
    
    private PrivateKey privateKey;
    private LinearTransformation linearTrans;
    private PublicMapParts publicMapParts;
    public PublicKey publicKey;
    
    /**
     * Constructor Method.
     * @param parameters Cryptosystem Parameters
     * @throws java.lang.Exception
     */
    public LUOV(Parameters parameters) throws Exception {
        System.out.println("Initializing LUOV Cryptosystem...");
        this.parameters = parameters;
        this.shake = new SHAKEDigest(256);
        this.keyGen();
        System.out.println("LUOV Cryptosystem Successfully Initialized");
    }
    
    /**
     * Generates a Key Pair (Private Key, Public Key).
     * <p>
     * Private Key = private_seed.
     * </p>
     * <p>
     * Public Key = (publicSeed, Q2).
     * </p>
     * @throws java.lang.Exception
     */
    private void keyGen() throws Exception {
        int m = parameters.getM();
        int v = parameters.getV();
        this.privateKey = new PrivateKey(generatePrivateSeed());
        String publicSeed = generatePublicSeed(privateKey);
        this.linearTrans = generateLinearTransformation(privateKey);
        this.publicMapParts = PRNG.generateCLQ1(publicSeed, v, m);
        String Q2 = Pack.packBin(findQ2(publicMapParts.getQ1Matrix(v, m), linearTrans));
        this.publicKey = new PublicKey(publicSeed, Q2);
        this.keyPair = new KeyPair(privateKey, publicKey);
    }
    
    /**
     * Print the Key Pair (Private Key, Public Key).
     */
    public void printKeyPair() {
        System.out.println(keyPair.toString());
    }
    
    /**
     * Generates a pseudo random private key for LUOV cryptosystem.
     * @return Hex String corresponding to the Private Seed.
     * @throws java.lang.Exception
     */
    private String generatePrivateSeed() throws Exception {
        byte[] private_seed = PRNG.randomBytes(32);
        return Hex.toHexString(private_seed);
    }
    
    /**
     * Generates a pseudo random public seed.
     * @param privateKey Private seed of LUOV cryptosystem.
     * @return Hex String corresponding to the Public Seed.
     * @throws java.lang.Exception 
     */
    private String generatePublicSeed(PrivateKey privateKey) throws Exception {
        byte[] sk = privateKey.getPrivateSeedBytes();
        shake.reset();
        shake.update(sk, 0, sk.length);
        byte[] pk = new byte[32];
        shake.doFinal(pk, 0);
        return Hex.toHexString(pk);
    }
    
    /**
     * Generates a pseudo random linearTrans matrix.
     * @param privateKey Private seed of LUOV cryptosystem.
     * @return linearTrans matrix Bit String.
     * @throws java.lang.Exception 
     */
    private LinearTransformation generateLinearTransformation(PrivateKey privateKey) throws Exception {
        float vm = parameters.getV()*parameters.getM();
        String T = "";
        byte[] sk = privateKey.getPrivateSeedBytes();
        shake.reset();
        shake.update(sk, 0, sk.length);
        for (int i = 0; i < (int) (Math.ceil(vm/256)); i++) {
            byte[] pk = new byte[32];
            shake.doOutput(pk, 0, pk.length);
            T += Hex.toHexString(pk);
        }
        BigInteger aux = new BigInteger(T, 16);
        if(aux.bitLength() < vm) {
            T = Functions.padding(aux.toString(2), (int) vm);
        }else {
            T = aux.toString(2).substring(0, (int) vm);
        }
        return new LinearTransformation(T);
    }
    
    /**
     * Calculates the second part of the quadratic part of the Public Map.
     * @param Q1 Matrix Q1.
     * @param T Matrix LinearTrans.
     * @return Q2 Matrix
     */
    private int[][] findQ2(int[][] Q1, LinearTransformation linearTrans) {
        int m = parameters.getM();
        int v = parameters.getV();
        int DIM = m*(m + 1)/2;
        int[][] T = linearTrans.getTMatrix(v, m);
        int[][] Q2 = new int[m][DIM];
        for (int k = 0; k < m; k++) {
            int[][] Pk1 = findPk1(k, Q1);
            int[][] Pk2 = findPk2(k, Q1);
            int[][] Pk3 = findPk3(T, Pk1, Pk2);
            int column = 0;
            for (int i = 0; i < m; i++) {
                Q2[k][column] = Pk3[i][i];
                column++;
                for (int j = i+1; j < m; j++) {
                    Q2[k][column] = Functions.add(Pk3[i][j], Pk3[j][i]);
                    column++;
                }
            }
        }
        return Q2;
    }
    
    /**
     * Calculates the part of Pk that is quadratic in vinegar variables.
     * @param k Iteration Number.
     * @param Q1 Binary Q1 Matrix (Quadratic part of Public Map). 
     * @return Pk part Pk1.
     */
    private int[][] findPk1(int k, int[][] Q1) {
        int m = parameters.getM();
        int v = parameters.getV();
        int[][] Pk1 = new int[v][v];
        int column = 0;
        for (int i = 0; i < v; i++) {
            for (int j = i; j < v; j++) {
                Pk1[i][j] = Q1[k][column];
                column++;
            }
            column += m;
        }
        return Pk1;
    }
    
    /**
     * Calculates the part of Pk that is bilinear in vinegar and oil variables.
     * @param k Iteration Number.
     * @param Q1 Binary Q1 Matrix (Quadratic part of Public Map).
     * @return Pk part Pk2.
     */
    private int[][] findPk2(int k, int[][] Q1) {
        int m = parameters.getM();
        int v = parameters.getV();
        int[][] Pk2 = new int[v][m];
        int column = 0;
        for (int i = 0; i < v; i++) {
            column +=  (v - (i+1) + 1);
            for (int j = 0; j < m; j++) {
                Pk2[i][j] = Q1[k][column];
                column++;
            }
        }
        return Pk2;
    }
    
    /**
     * Calculates the last part of Pk using the formula 
     * Pk3 = -Tt*Pk1*linearTrans + Tt*Pk2 over GF(2^7).
     * @param T linearTrans Matrix.
     * @param Pk1 Pk1 Matrix.
     * @param Pk2 Pk2 Matrix.
     * @return Pk part Pk3.
     */
    private int[][] findPk3(int[][] T, int[][] Pk1, int[][] Pk2) {
        int[][] T_transposed = Functions.transposeMatrix(T);
        int[][] first = Functions.binMatrixMult(
                Functions.binMatrixMult(T_transposed, Pk1), T);
        int[][] second = Functions.binMatrixMult(T_transposed, Pk2);
        int[][] Pk3 = Functions.binMatrixAdd(first, second);
        return Pk3;
    }
    
    /**
     * Generates the int Message Vector over GF(2^r) from the message byte array
     * after hashing.
     * @param bitMsg Byte Array of the Message after hashing.
     * @return Message Vector over GF(2^r).
     */
    public Field2nElement[][] buildMessageVector(String bitMsg) {
        int m = parameters.getM();
        int r = parameters.getR();
        int len = (r + 32 - 1) / 32;
        Field2nElement[][] msgVector = new Field2nElement[m][1];
        for (int i = 0; i < m; i++) {
            String bits = bitMsg.substring(r*i, r*(i+1));
            int[] aux = new int[len];
            for (int j = 0; j < len - 1; j++) {
                aux[j] = (int) Long.parseLong(bits.substring(32*j, 32*(j+1)), 2);
            }
            aux[len - 1] = (int) Long.parseLong(bits.substring(32*(len - 1), bits.length()), 2);
            msgVector[i][0] = new Field2nElement(aux);
        }
        return msgVector;
    }
    
    /**
     * Builds the Augmented Matrix for the Equation System to solve.
     * @param C C Matrix.
     * @param L L Matrix.
     * @param Q1 Q1 Matrix.
     * @param T LinearTrans Matrix.
     * @param h Int Message Vector over GF(2^r).
     * @param vi Random Assign for Vinegar Vars.
     * @return Augmneted Matrix (LHS||RHS).
     */
    private EquationSystem buildAugmentedMatrix(int[][] C, int[][] L, 
            int[][] Q1, int[][] T, Field2nElement[][] h, Field2nElement[][] vi) {
        int r = parameters.getR();
        int m = parameters.getM();
        int v = parameters.getV();
        Field2n field = parameters.getField();
        
        Field2nElement[][] RHS = Functions.matrixAdd(field, 
                Functions.binFieldMatrixAdd(field, C, h), 
                Functions.binFieldMatrixMultL(field, L, 
                        Functions.matrixRowUnion(vi, 
                                Functions.fieldZeroMatrix(field, m, 1))));
        Field2nElement[][] LHS = Functions.binToFieldMatrix(field, 
                Functions.binMatrixMult(L, Functions.matrixRowUnion(T, 
                        Functions.identityMatrix(m))));
        for (int k = 0; k < m; k++) {
            int[][] Pk1 = findPk1(k, Q1);
            int[][] Pk2 = findPk2(k, Q1);
            Field2nElement[][] temp1 = Functions.matrixMult(field, 
                    Functions.binFieldMatrixMultR(field, Functions.transposeMatrix(vi), Pk1), vi);
            RHS[k][0] = Functions.add(field, RHS[k][0], temp1[0][0]);
            int[][] Fk2 = Functions.binMatrixAdd(Functions.binMatrixMult(
                    Functions.binMatrixAdd(Pk1, Functions.transposeMatrix(Pk1)), T), Pk2);
            Field2nElement[][] temp2 = Functions.binFieldMatrixMultR(field, 
                    Functions.transposeMatrix(vi), Fk2);
            LHS[k] = Functions.vectorAdd(field, LHS[k], temp2[0]);
        }
        return new EquationSystem(LHS, Functions.transposeColumnVector(RHS));
    }
    
    /**
     * Sign the given Message.
     * @param M Byte Data to be Signed.
     * @return Message Signature (s, salt).
     * @throws java.lang.Exception
     */
    public Signature sign(byte[] M) throws Exception {
        int m = parameters.getM();
        int v = parameters.getV();
        int r = parameters.getR();
        Field2n field = parameters.getField();
        int vin = (r*v + 8 - 1) / 8;
        boolean solutionFound = false;
        Field2nElement[][] s_prime = null;
        byte[] zero = {0};
        byte[] salt = PRNG.randomBytes(16);
        byte[] finalMsg = Functions.concatenateVectors(
                Functions.concatenateVectors(M, zero), salt);
        String hashedMsg = PRNG.getHashDigest(finalMsg, r*m);
        Field2nElement[][] h = buildMessageVector(hashedMsg);
        while(!solutionFound) {
            //System.out.println("No");
            byte[] vinegarAssign = PRNG.randomBytes(vin);
            Field2nElement[][] vi = Functions.bytesToFieldVector(vinegarAssign, r, v);
            EquationSystem A = buildAugmentedMatrix(publicMapParts.getCMatrix(m), 
                    publicMapParts.getLMatrix(v, m), 
                    publicMapParts.getQ1Matrix(v, m), 
                    linearTrans.getTMatrix(v, m), h, vi);
            Field2nElement[] oVect = Functions.gaussianElimination(field, 
                            A.getCoef(), A.getResu());
            if(oVect != null) {
                //System.out.println("Yes");
                Field2nElement[][] o = Functions.transposeRowVector(oVect);
                solutionFound = true;
                s_prime = Functions.matrixRowUnion(vi, o);
            }
        }
        Field2nElement[][] s = Functions.binFieldMatrixMultL(field, 
                linearTrans.buildLinearTransMatrix(v, m), s_prime);
        return new Signature(Pack.packField(s, r), Hex.toHexString(salt));
    }
    
    /**
     * Calculates the Evaluation of s in the Public Map P.
     * <p>
     * For a Given Signature s, calculates P(s) = C + L(s) + Q(s), 
     * with Q = (Q1||Q2).
     * </p>
     * @param publicKey Public Key used for Verification.
     * @param s s Signature Matrix.
     * @return Evaluation of s in the Public Map P, i.e. P(s).
     */
    private Field2nElement[][] evaluatePublicMap(PublicKey publicKey, Field2nElement[][] s) throws Exception {
        int r = parameters.getR();
        int m = parameters.getM();
        int v = parameters.getV();
        int N = m + v;
        Field2n field = parameters.getField();
        PublicMapParts map = PRNG.generateCLQ1(publicKey.getPublicSeed(), v, m);
        int[][] C = map.getCMatrix(m);
        int[][] L = map.getLMatrix(v, m);
        int[][] Q1 = map.getQ1Matrix(v, m);
        int[][] Q2 = publicKey.getQ2Matrix(m);
        int[][] Q = Functions.matrixColumnUnion(Q1, Q2);
        Field2nElement[][] e = Functions.binFieldMatrixAdd(field, C, 
                Functions.binFieldMatrixMultL(field, L, s));
        int column = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {
                for (int k = 0; k < m; k++) {
                    e[k][0] = Functions.add(field, e[k][0], 
                            Functions.mult(field, 
                                    Functions.binFieldMult(field, 
                                            Q[k][column], s[i][0]), s[j][0]));
                }
                column++;
            }
        }
        return e;
    }
    
    /**
     * Verify if a Signature (s, salt) is Valid for a Message M.
     * <p>
     * For a signature (s, salt) verify if the Public Map (P) evaluated in s is
     * equal to Hashed Message M, i.e. P(s) == [h = Hash(M||0x00||salt)].
     * </p>
     * @param publicKey Public Key used for Verification.
     * @param M Message to be Verified.
     * @param sign Sign to be Verified.
     * @return Verification of a Signature P(s) == h.
     * @throws java.lang.Exception     
     */
    public boolean verify(PublicKey publicKey, byte[] M, Signature sign) throws Exception {
        int r = parameters.getR();
        int m = parameters.getM();
        int v = parameters.getV();
        byte[] zero = {0};
        byte[] salt = sign.getSaltBytes();
        byte[] finalMsg = Functions.concatenateVectors(
                Functions.concatenateVectors(M, zero), salt);
        String hashedMsg = PRNG.getHashDigest(finalMsg, r*m); 
        Field2nElement[][] h = buildMessageVector(hashedMsg);
        Field2nElement[][] e = evaluatePublicMap(publicKey, sign.getSMatrix(r, v, m));
        return Functions.matrixEquals(e, h);
    }
    
    private class EquationSystem {
        
        private Field2nElement[][] coef;
        private Field2nElement[] resu;
        
        public EquationSystem(Field2nElement[][] coef, Field2nElement[] resu) {
            this.coef = coef;
            this.resu = resu;
        }
        
        public Field2nElement[][] getCoef() {
            return coef;
        }
        
        public Field2nElement[] getResu() {
            return resu;
        }
        
    }
    
}
