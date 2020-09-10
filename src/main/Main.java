/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import crypto.LUOV;
import crypto.parameters.*;
import crypto.utils.Signature;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Eduardo Angulo
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        //Parameters params = new LUOVr07m057v197();
        //Parameters params = new LUOVr07m083v283();
        //Parameters params = new LUOVr07m110v374();
        //Parameters params = new LUOVr47m042v182();
        Parameters params = new LUOVr61m060v261();
        //Parameters params = new LUOVr79m076v341();
        LUOV luov1 = new LUOV(params);
        //luov1.buildMessageVector("1110101110001000101011101101101011110000000011101110000110100110111111010110100010011000100001");
        //luov1.buildMessageVector("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        //luov1.printKeyPair();
        LUOV luov2 = new LUOV(params);
        //luov.printKeyPair();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Message to be Signed: ");
        String messageToBeSigned = br.readLine();
        Signature sign = luov1.sign(messageToBeSigned.getBytes());
        //System.out.println("Signed Message: " + sign.toString());
        System.out.print("Message to be Verified: ");
        String messageToBeVerified = br.readLine();
        //Signature wrongSign = new Signature(sign.getS(), "00000000000000000000000000000000");
        System.out.println("Valid Message Signature: " + 
                luov2.verify(luov1.publicKey, messageToBeVerified.getBytes(), sign));
    }
    
}
