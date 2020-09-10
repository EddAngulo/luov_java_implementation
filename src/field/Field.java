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
public interface Field {
    
    public FieldElement addition(FieldElement a, FieldElement b);
    public FieldElement division(FieldElement a, FieldElement b);
    public FieldElement squaring(FieldElement a);//a^2
    public FieldElement multiplication(FieldElement a, FieldElement b);
    public FieldElement inversion(FieldElement a);
    public FieldElement squareRoot(FieldElement a);
    public FieldElement randomElement();
    public FieldElement zero();
    public FieldElement one();
    
}
