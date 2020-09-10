/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import field.Field2n;
import field.Field2nElement;
import field.FieldElement;
import field.Util;
import java.util.Arrays;

/**
 *
 * @author eduar
 */
public class Functions {
    
    public static String padding(String binStr, int N) {
        String aux = binStr;
        while(aux.length() < N) {
            aux = "0" + aux;
        }
        return aux;
    }
    
    /**
     * Calculates XOR between two numbers x and y.
     * @param x First Integer.
     * @param y Second Integer.
     * @return add between x and y.
     */
    public static int add(int x, int y) {
        return x ^ y;
    }
    
    public static Field2nElement add(Field2n field, FieldElement a, FieldElement b) {
        return (Field2nElement) (field.addition(a, b));
    }
    
    public static Field2nElement mult(Field2n field, FieldElement a, FieldElement b) {
        return (Field2nElement) (field.multiplication(a, b));
    }
    
    public static Field2nElement binFieldMult(Field2n field, int bin, FieldElement f) {
        if(bin == 0) {
            return (Field2nElement) (field.zero());
        }else {
            return (Field2nElement) (f);
        }
    }
    
    /**
     * Calculates Matrix add Add over GF(2^r).
     * @param mat1 First Matrix.
     * @param mat2 Second Matrix.
     * @return Result Matrix.
     */
    public static int[][] binMatrixAdd(int[][] mat1, int[][] mat2) {
        int[][] result = new int[mat1.length][mat1[0].length];
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat1[0].length; j++) {
                result[i][j] = add(mat1[i][j], mat2[i][j]);
            }
        }
        return result;
    }
    
    public static Field2nElement[][] matrixAdd(Field2n field, Field2nElement[][] mat1, Field2nElement[][] mat2) {
        Field2nElement[][] result = new Field2nElement[mat1.length][mat1[0].length];
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat1[0].length; j++) {
                result[i][j] = add(field, mat1[i][j], mat2[i][j]);
            }
        }
        return result;
    }
    
    public static Field2nElement[][] binFieldMatrixAdd(Field2n field, int[][] bMat, Field2nElement[][] fMat) {
        Field2nElement[][] result = new Field2nElement[fMat.length][fMat[0].length];
        for (int i = 0; i < fMat.length; i++) {
            for (int j = 0; j < fMat[0].length; j++) {
                if(bMat[i][j] == 1) {
                    result[i][j] = add(field, fMat[i][j], field.one());
                }else {
                    result[i][j] = (Field2nElement) (fMat[i][j].copy());
                }
            }
        }
        return result;
    }
    
    /**
     * Calculates Matrix multiplication over GF(2^r).
     * @param mat1 First Matrix.
     * @param mat2 Second Matrix.
     * @return Result Matrix.
     */
    public static int[][] binMatrixMult(int[][] mat1, int[][] mat2) {
        int[][] result = new int[mat1.length][mat2[0].length];
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat2[0].length; j++) {
                for (int k = 0; k < mat1[0].length; k++) {
                    result[i][j] = add(result[i][j], (mat1[i][k] * mat2[k][j]));
                }
            }
        }
        return result;
    }
    
    public static Field2nElement[][] matrixMult(Field2n field, Field2nElement[][] mat1, Field2nElement[][] mat2) {
        Field2nElement[][] result = fieldZeroMatrix(field, mat1.length, mat2[0].length);
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat2[0].length; j++) {
                for (int k = 0; k < mat1[0].length; k++) {
                    result[i][j] = add(field, result[i][j], 
                            mult(field, mat1[i][k], mat2[k][j]));
                }
            }
        }
        return result;
    }
    
    public static Field2nElement[][] binFieldMatrixMultL(Field2n field, int[][] mat1, Field2nElement[][] mat2) {
        Field2nElement[][] result = fieldZeroMatrix(field, mat1.length, mat2[0].length);
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat2[0].length; j++) {
                for (int k = 0; k < mat1[0].length; k++) {
                    if(mat1[i][k] == 1) {
                        result[i][j] = add(field, result[i][j], mat2[k][j]); 
                    }
                }
            }
        }
        return result;
    }
    
    public static Field2nElement[][] binFieldMatrixMultR(Field2n field, Field2nElement[][] mat1, int[][] mat2) {
        Field2nElement[][] result = fieldZeroMatrix(field, mat1.length, mat2[0].length);
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat2[0].length; j++) {
                for (int k = 0; k < mat1[0].length; k++) {
                    if(mat2[k][j] == 1) {
                        result[i][j] = add(field, result[i][j], mat1[i][k]); 
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Calculates the transpose of a Matrix.
     * @param mat Matrix to be transposed.
     * @return Transposed Matrix.
     */
    public static int[][] transposeMatrix(int[][] mat) {
        int[][] result = new int[mat[0].length][mat.length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                result[j][i] = mat[i][j];
            }
        }
        return result;
    }
    
    public static Field2nElement[][] transposeMatrix(Field2nElement[][] mat) {
        Field2nElement[][] result = new Field2nElement[mat[0].length][mat.length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                result[j][i] = (Field2nElement) (mat[i][j].copy());
            }
        }
        return result;
    }
    
    public static Field2nElement[] vectorAdd(Field2n field, Field2nElement[] vec1, Field2nElement[] vec2) {
        Field2nElement[] result = new Field2nElement[vec1.length];
        for (int i = 0; i < vec1.length; i++) {
            result[i] = add(field, vec1[i], vec2[i]);
        }
        return result;
    }
    
    public static Field2nElement[][] transposeRowVector(Field2nElement[] vec) {
        Field2nElement[][] result = new Field2nElement[vec.length][1];
        for (int i = 0; i < vec.length; i++) {
            result[i][0] = (Field2nElement) (vec[i].copy());
        }
        return result;
    }
    
    public static Field2nElement[] transposeColumnVector(Field2nElement[][] vec) {
        Field2nElement[] result = new Field2nElement[vec.length];
        for (int i = 0; i < vec.length; i++) {
            result[i] = (Field2nElement) (vec[i][0].copy());
        }
        return result;
    }
    
    public static Field2nElement[] gaussianElimination(Field2n field, Field2nElement[][] A, Field2nElement[] b) {
        ComputeGaussian cg = new ComputeGaussian(field);
        return cg.solveEquation(A, b);
    }
    
    /**
     * Generates a Integer Matrix of same value.
     * @param row Row Length.
     * @param column Column Length.
     * @param value Matrix Value.
     * @return Matrix filled with value.
     */
    public static int[][] sameValueMatrix(int row, int column, int value) {
        int[][] result = new int[row][column];
        for (int i = 0; i < row; i++) {
            Arrays.fill(result[i], value);
        }
        return result;
    }
    
    public static Field2nElement[][] fieldZeroMatrix(Field2n field, int row, int column) {
        Field2nElement[][] result = new Field2nElement[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result[i][j] = (Field2nElement) (field.zero());
            }
        }
        return result;
    }
    
    /**
     * Generates a Identity Matrix.
     * @param dim Dimension of the Matrix.
     * @return Identity Matrix of (dim x dim).
     */
    public static int[][] identityMatrix(int dim) {
        int[][] result = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            result[i][i] = 1;
        }
        return result;
    }
    
    /**
     * Join the Rows of Two Matrix into a new one. 
     * @param mat1 First Matrix.
     * @param mat2 Second Matrix.
     * @return Union Matrix.
     */
    public static int[][] matrixRowUnion(int[][] mat1, int[][] mat2) {
        int row1 = mat1.length;
        int row2 = mat2.length;
        int column = mat1[0].length;
        int[][] result = new int[row1 + row2][column];
        for (int i = 0; i < row1; i++) {
            result[i] = Arrays.copyOf(mat1[i], column);
        }
        for (int i = row1; i < row1 + row2; i++) {
            result[i] = Arrays.copyOf(mat2[i - row1], column);
        }
        return result;
    }
    
    public static Field2nElement[][] matrixRowUnion(Field2nElement[][] mat1, Field2nElement[][] mat2) {
        int row1 = mat1.length;
        int row2 = mat2.length;
        int column = mat1[0].length;
        Field2nElement[][] result = new Field2nElement[row1 + row2][column];
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < row1; j++) {
                result[j][i] = (Field2nElement) (mat1[j][i].copy());
            }
            for (int j = row1; j < row1 + row2; j++) {
                result[j][i] = (Field2nElement) (mat2[j - row1][i].copy());
            }
        }
        return result;
    }
    
    /**
     * Join the Columns of Two Matrix into a new one.
     * @param mat1 First Matrix.
     * @param mat2 Second Matrix.
     * @return Union Matrix.
     */
    public static int[][] matrixColumnUnion(int[][] mat1, int[][] mat2) {
        int row = mat1.length;
        int column1 = mat1[0].length;
        int column2 = mat2[0].length;
        int[][] result = new int[row][column1 + column2];
        for (int i = 0; i < row; i++) {
            result[i] = concatenateVectors(mat1[i], mat2[i]);
        }
        return result;
    }
    
    public static Field2nElement[][] matrixColumnUnion(Field2nElement[][] mat1, Field2nElement[][] mat2) {
        int row = mat1.length;
        int column1 = mat1[0].length;
        int column2 = mat2[0].length;
        Field2nElement[][] result = new Field2nElement[row][column1 + column2];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column1; j++) {
                result[i][j] = (Field2nElement) (mat1[i][j].copy());
            }
            for (int j = column1; j < column1 + column2; j++) {
                result[i][j] = (Field2nElement) (mat2[i][j - column1].copy());
            }
        }
        return result;
    }
    
    /**
     * Concatenates Two Integer Vectors into a new one.
     * @param vec1 First Vector.
     * @param vec2 Second Vector.
     * @return Union Vector.
     */
    public static int[] concatenateVectors(int[] vec1, int[] vec2) {
        int[] result = new int[vec1.length + vec2.length];
        System.arraycopy(vec1, 0, result, 0, vec1.length);
        System.arraycopy(vec2, 0, result, vec1.length, vec2.length);  
        return result;
    }
    
    /**
     * Concatenates Two Byte Vectors into a new one.
     * @param vec1 First Vector.
     * @param vec2 Second Vector.
     * @return Union Vector.
     */
    public static byte[] concatenateVectors(byte[] vec1, byte[] vec2) {
        byte[] result = new byte[vec1.length + vec2.length];
        System.arraycopy(vec1, 0, result, 0, vec1.length);
        System.arraycopy(vec2, 0, result, vec1.length, vec2.length);  
        return result;
    }
    
    /**
     * Verify if Two Matrix are Equals.
     * @param mat1 First Matrix.
     * @param mat2 Second Matrix.
     * @return mat1 == mat2 ?
     */
    public static boolean matrixEquals(Field2nElement[][] mat1, Field2nElement[][] mat2) {
        if(!(mat1.length == mat2.length && mat1[0].length == mat2[0].length)) {
            return false;
        }
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat1[0].length; j++) {
                if(!mat1[i][j].repEquals(mat2[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static Field2nElement[][] binToFieldMatrix(Field2n field, int[][] bMat) {
        Field2nElement[][] result = new Field2nElement[bMat.length][bMat[0].length];
        for (int i = 0; i < bMat.length; i++) {
            for (int j = 0; j < bMat[0].length; j++) {
                if(bMat[i][j] == 0) {
                    result[i][j] = (Field2nElement) (field.zero());
                }else {
                    result[i][j] = (Field2nElement) (field.one());
                }
            }
        }
        return result;
    }
    
    public static int bytesToInteger(byte b1, byte b2, byte b3, byte b4) {
        return ((0xFF & b1) << 24) | ((0xFF & b2) << 16) | ((0xFF & b3) << 8) | (0xFF & b4);
    }
    
    /**
     * Transform a Byte Array to a Integer Array over GF(2^field).
     * @param data Byte Array to be transformed.
     * @param field Extension Field Number.
     * @param length Lenght of desired Array.
     * @return Transformed Integer Array over GF(2^field).
     */
    public static Field2nElement[][] bytesToFieldVector(byte[] data, int field, int length) {
        int p = 4 - (data.length % 4);
        byte[] pad = new byte[p];
        for (int i = 0; i < p; i++) {
            pad[i] = 0;
        }
        byte[] finalData = concatenateVectors(data, pad);
        int rounds = finalData.length / 4;
        int[] intData = new int[rounds];
        for (int i = 0; i < rounds; i++) {
            intData[i] = Functions.bytesToInteger(finalData[4*i], 
                    finalData[4*i+1], finalData[4*i+2], finalData[4*i+3]);
        }
        String bitData = (Util.toBinaryStringR(intData)).substring(0, field*length);
        int len = (field + 32 - 1) / 32;
        Field2nElement[][] result = new Field2nElement[length][1];
        for (int i = 0; i < length; i++) {
            String bits = bitData.substring(field*i, field*(i+1));
            int[] aux = new int[len];
            for (int j = 0; j < len - 1; j++) {
                aux[j] = (int) Long.parseLong(bits.substring(32*j, 32*(j+1)), 2);
            }
            aux[len - 1] = (int) Long.parseLong(bits.substring(32*(len - 1), bits.length()), 2);
            result[i][0] = new Field2nElement(aux);
        }
        return result;
    }
    
}
