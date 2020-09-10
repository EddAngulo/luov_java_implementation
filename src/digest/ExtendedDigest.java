/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digest;

public interface ExtendedDigest 
    extends Digest
{
    /**
     * Return the size in bytes of the internal buffer the digest applies it's compression
     * function to.
     * 
     * @return byte length of the digests internal buffer.
     */
    public int getByteLength();
}
