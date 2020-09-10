/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package field;

/**
 *
 * @author Ricardo Villanueva
 */
public class Field2nElement implements FieldElement {
    
    int[] representation;

    public Field2nElement(int[] representation) {
        this.representation = representation;
    }

    public int[] getRepresentation() {
        return representation;
    }

    public int getLenght() {
        return representation.length;
    }

    public int getDegree() {
        int i = representation.length - 1;
        while (i >= 0 && representation[i] == 0) {
            i--;
        }
        if (i < 0) {
            return 0;
        }
        int degree = 32*i + Util.findPosTheMostSignificantbit(representation[i]) - 1;
        return degree;
    }

    @Override
    public FieldElement copy() {
        return new Field2nElement(representation);
    }
    
    @Override
    public boolean repEquals(FieldElement a) {
        Field2nElement a1 = (Field2nElement) a;
        return Util.repEquals(representation, a1.getRepresentation());
    }
    
    @Override
    public String toString() {
        return Util.toBinaryString(representation);
    }
    
}
