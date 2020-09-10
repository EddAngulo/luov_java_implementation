
package utils;

import field.Field2nElement;
import java.math.BigInteger;

/**
 * Hex String and GF(2^7) Arrays Packer Class.
 * @author Eduardo Angulo
 */
public class Pack {
    
    /**
     * Generates the Hex String of Given Matrix.
     * @param mat Integer GF(2^7) Matrix.
     * @return Hex String of Matrix.
     */
    public static String pack(int[][] mat) {
        String result = "";
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                result += Functions.padding((new BigInteger(
                        "" + mat[i][j])).toString(16), 2);
            }
        }
        return result;
    }
    
    /**
     * Generates the Hex String of Given Vector.
     * @param vec Integer GF(2^7) Vector.
     * @return Hex String of Vector.
     */
    public static String pack(int[] vec) {
        String result = "";
        for (int i = 0; i < vec.length; i++) {
            result += Functions.padding((new BigInteger(
                    "" + vec[i])).toString(16), 2);
        }
        return result;
    }
    
    /**
     * Generates the Binary String of Given Binary Matrix.
     * @param mat Binary Matrix.
     * @return Binary String of Matrix.
     */
    public static String packBin(int[][] mat) {
        String result = "";
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                result += mat[i][j];
            }
        }
        return result;
    }
    
    /**
     * Generates the Hex String of Given Binary Vector.
     * @param vec Binary Vector.
     * @return Hex String of Vector.
     */
    public static String packBin(int[] vec) {
        String result = "";
        for (int i = 0; i < vec.length; i++) {
            result += vec[i];
        }
        return (new BigInteger(result, 2)).toString(16);
    }
    
    /**
     * Generates the Binary String of Given Field Matrix.
     * @param mat Field Matrix.
     * @param field Extension Field Number.
     * @return Binary String of Matrix.
     */
    public static String packField(Field2nElement[][] mat, int field) {
        int len = (field + 32 - 1) / 32;
        String result = "";
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                result += (mat[i][j].toString()).substring(32*len - field);
            }
        }
        return result;
    }
    
    /**
     * Generates a matrix of elements in GF(2^7) from Given Hex String.
     * @param hex Matrix String in hexagesimal.
     * @param row Matrix Row Dimension.
     * @param column Matrix Column Dimension.
     * @return Integer (GF(2^7) elements) Matrix.
     */
    public static int[][] unpack(String hex, int row, int column) {
        int[][] unpackedMatrix = new int[row][column];
        for (int i = 0; i < row; i++) {
            String rowHex = hex.substring(2*column*i, 2*column*(i+1));
            for (int j = 0; j < column; j++) {
                String actual = rowHex.substring(2*j, 2*(j+1));
                unpackedMatrix[i][j] = (new BigInteger(actual, 16)).intValue();
            }
        }
        return unpackedMatrix;
    }
    
    /**
     * Generates a vector of elements in GF(2^7) from Given Hex String.
     * @param hex Vector String in hexagesimal.
     * @param dim Vector Dimension.
     * @return Integer (GF(2^7) elements) Vector.
     */
    public static int[] unpack(String hex, int dim) {
        int[] unpackedVector = new int[dim];
        for (int i = 0; i < dim; i++) {
            String actual = hex.substring(2*i, 2*(i+1));
            unpackedVector[i] = (new BigInteger(actual, 16)).intValue();
        }
        return unpackedVector;
    }
    
    /**
     * Generates a binary matrix from Given Bit String.
     * @param bitStr Matrix String in binary.
     * @param row Matrix Row Dimension.
     * @param column Matrix Column Dimension.
     * @return Binary Matrix.
     */
    public static int[][] unpackBin(String bitStr, int row, int column) {
        int[][] unpackedMatrix = new int[row][column];
        for (int i = 0; i < row; i++) {
            String rowBin = bitStr.substring(column*i, column*(i+1));
            for (int j = 0; j < column; j++) {
                unpackedMatrix[i][j] = (int) (rowBin.charAt(j)) - 48;
            }
        }
        return unpackedMatrix;
    }
    
    /**
     * Generates a binary vector from Given Bit String.
     * @param bitStr Vector String in binary.
     * @param dim Vector Dimension.
     * @return Binary Vector.
     */
    public static int[] unpackBin(String bitStr, int dim) {
        int[] unpackedVector = new int[dim];
        for (int i = 0; i < dim; i++) {
            unpackedVector[i] = (int) (bitStr.charAt(i)) - 48;
        }
        return unpackedVector;
    }
    
    /**
     * Generates a field matrix GF(2^7) from Given Bit String.
     * @param bitStr Matrix String in binary.
     * @param field Extension Field Number.
     * @param row Matrix Row Dimension.
     * @param column Matrix Column Dimension.
     * @return Integer (GF(2^7) elements) Matrix.
     */
    public static Field2nElement[][] unpackField(String bitStr, int field, int row, int column) {
        int len = (field + 32 - 1) / 32;
        int piv = field - 32*(len - 1);
        Field2nElement[][] unpackedMatrix = new Field2nElement[row][column];
        for (int i = 0; i < row; i++) {
            String rowBin = bitStr.substring(field*column*i, field*column*(i+1));
            for (int j = 0; j < column; j++) {
                String actual = rowBin.substring(field*j, field*(j+1));
                int[] aux = new int[len];
                aux[len - 1] = (int) Long.parseLong(actual.substring(0, piv), 2);
                for (int k = 0; k < len - 1; k++) {
                    aux[len - k - 2] = (int) Long.parseLong(actual.substring(32*k + piv, 32*(k + 1) + piv), 2);
                }
                unpackedMatrix[i][j] = new Field2nElement(aux);
            }
        }
        return unpackedMatrix;
    }
    
}