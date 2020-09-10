/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.encoders;

/**
 * Exception thrown if an attempt is made to decode invalid data, or some other failure occurs.
 */
public class DecoderException
    extends IllegalStateException
{
    private Throwable cause;

    DecoderException(String msg, Throwable cause)
    {
        super(msg);

        this.cause = cause;
    }

    public Throwable getCause()
    {
        return cause;
    }
}
