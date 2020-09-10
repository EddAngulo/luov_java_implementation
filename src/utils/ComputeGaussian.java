/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import field.Field2n;
import field.Field2nElement;

/**
 *
 * @author Eduardo Angulo
 */
public class ComputeGaussian {
    
    protected Field2nElement A[][];
    Field2nElement[] x;
    protected final Field2n field;
    
    /**
     * Constructor Method.
     * @param field
     */
    public ComputeGaussian(Field2n field) {
        this.field = field;
    }
    
    /**
     * This function finds a solution of the equation Bx = b.
     * Exception is thrown if the linear equation system has no solution
     *
     * @param B this matrix is the left part of the
     *          equation (B in the equation above)
     * @param b the right part of the equation
     *          (b in the equation above)
     * @return x  the solution of the equation if it is solvable
     * null otherwise
     * @throws RuntimeException if LES is not solvable
     */
    public Field2nElement[] solveEquation(Field2nElement[][] B, Field2nElement[] b) {
        if (B.length != b.length) {
            return null;   // not solvable in this form
        }
        try {
            
            /** initialize **/
            // this matrix stores B and b from the equation B*x = b
            // b is stored as the last column.
            // B contains one column more than rows.
            // In this column we store a free coefficient that should be later subtracted from b
            A = new Field2nElement[B.length][B.length + 1];
            for (int i = 0; i < B.length; i++) {
                for (int j = 0; j < B.length + 1; j++) {
                    A[i][j] = (Field2nElement) (field.zero());
                }
            }
            
            // stores the solution of the LES
            x = new Field2nElement[B.length];
            for (int i = 0; i < B.length; i++) {
                x[i] = (Field2nElement) (field.zero());
            }

            /** copy B into the global matrix A **/
            for (int i = 0; i < B.length; i++) { // rows
                for (int j = 0; j < B[0].length; j++) { // cols
                    A[i][j] = (Field2nElement) (B[i][j].copy());
                }
            }

            /** copy the vector b into the global A **/
            //the free coefficient, stored in the last column of A( A[i][b.length]
            // is to be subtracted from b
            for (int i = 0; i < b.length; i++) {
                A[i][b.length] = (Field2nElement) (field.addition(b[i], A[i][b.length]));
            }

            /** call the methods for gauss elimination and backward substitution **/
            computeZerosUnder(false);     // obtain zeros under the diagonal
            substitute();

            return x;

        }catch (RuntimeException rte) {
            return null; // the LES is not solvable!
        }
    }
    
    /**
     * Elimination under the diagonal.
     * This function changes a matrix so that it contains only zeros under the
     * diagonal(Ai,i) using only Gauss-Elimination operations.
     * <p>
     * It is used in solveEquaton as well as in the function for
     * finding an inverse of a matrix: {@link}inverse. Both of them use the
     * Gauss-Elimination Method.
     * </p>
     * <p>
     * The result is stored in the global matrix A
     * </p>
     *
     * @param usedForInverse This parameter shows if the function is used by the
     *                       solveEquation-function or by the inverse-function and according
     *                       to this creates matrices of different sizes.
     * @throws RuntimeException in case a multiplicative inverse of 0 is needed
     */
    protected void computeZerosUnder(boolean usedForInverse) throws RuntimeException {

        //the number of columns in the global A where the tmp results are stored
        int length;
        Field2nElement tmp = (Field2nElement) (field.zero());

        //the function is used in inverse() - A should have 2 times more columns than rows
        if (usedForInverse) {
            length = 2 * A.length;
        }else { //the function is used in solveEquation - A has 1 column more than rows
            length = A.length + 1;
        }

        //elimination operations to modify A so that that it contains only 0s under the diagonal
        for (int k = 0; k < A.length - 1; k++) { // the fixed row
            for (int i = k + 1; i < A.length; i++) { // rows
                Field2nElement factor1 = (Field2nElement) (A[i][k].copy());
                Field2nElement factor2 = (Field2nElement) (field.inversion(A[k][k]));

                //The element which multiplicative inverse is needed, is 0
                //in this case is the input matrix not invertible
                if (factor2.repEquals(field.zero())) {
                    throw new IllegalStateException("Matrix not invertible! We have to choose another one!");
                }

                for (int j = k; j < length; j++) {// columns
                    // tmp=A[k,j] / A[k,k]
                    tmp = (Field2nElement) (field.multiplication(A[k][j], factor2));
                    // tmp = A[i,k] * A[k,j] / A[k,k]
                    tmp = (Field2nElement) (field.multiplication(factor1, tmp));
                    // A[i,j]=A[i,j]-A[i,k]/A[k,k]*A[k,j];
                    A[i][j] = (Field2nElement) (field.addition(A[i][j], tmp));
                }
            }
        }
    }
    
    /**
     * This function uses backward substitution to find x
     * of the linear equation system (LES) B*x = b,
     * where A a triangle-matrix is (contains only zeros under the diagonal)
     * and b is a vector
     * <p>
     * If the multiplicative inverse of 0 is needed, an exception is thrown.
     * In this case is the LES not solvable
     * </p>
     *
     * @throws RuntimeException in case a multiplicative inverse of 0 is needed
     */
    protected void substitute() throws IllegalStateException {

        // for the temporary results of the operations in field
        Field2nElement tmp, temp;

        temp = (Field2nElement) (field.inversion(A[A.length - 1][A.length - 1]));
        if (temp.repEquals(field.zero())) {
            throw new IllegalStateException("The equation system is not solvable");
        }

        /** backward substitution **/
        x[A.length - 1] = (Field2nElement) (field.multiplication(A[A.length - 1][A.length], temp));
        for (int i = A.length - 2; i >= 0; i--) {
            tmp = (Field2nElement) (A[i][A.length].copy());
            for (int j = A.length - 1; j > i; j--) {
                temp = (Field2nElement) (field.multiplication(A[i][j], x[j]));
                tmp = (Field2nElement) (field.addition(tmp, temp));
            }

            temp = (Field2nElement) (field.inversion(A[i][i]));
            if (temp.repEquals(field.zero())) {
                throw new IllegalStateException("Not solvable equation system");
            }
            x[i] = (Field2nElement) (field.multiplication(tmp, temp));
        }
    }
    
}
