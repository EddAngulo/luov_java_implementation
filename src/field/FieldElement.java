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
public interface FieldElement {
    
    public FieldElement copy();
    public boolean repEquals(FieldElement a);
    
}
