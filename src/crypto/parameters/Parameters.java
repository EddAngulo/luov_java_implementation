/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.parameters;

import field.Field2n;

/**
 *
 * @author Eduardo Angulo
 */
public abstract class Parameters {
    
    int r, m, v;
    Field2n field;
    
    public int getR() {
        return r;
    }
    
    public int getM() {
        return m;
    }
    
    public int getV() {
        return v;
    }
    
    public Field2n getField() {
        return field;
    }
    
}
