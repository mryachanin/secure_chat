package us.blint.securechat.encryption.RSA;

import java.math.BigInteger;

/**
 *  Holds the values for a RSA public key
 *      allows the encryption of messages
 *      allows for the decryption of messages for the purpose of verification (digital signature)
 */
public class RSAPublicKey {
    public BigInteger exponent;
    public BigInteger mod;
    
    
    /**
     *  Instantiates a RSA public key with a given mod and exponent
     *  
     *  @param exponent  exponent to use
     *  @param mod       modulus to use
     */
    public RSAPublicKey(String exponent, String mod) {
        this.exponent = new BigInteger(exponent);
        this.mod = new BigInteger(mod);
    }
    
    
    /**
     *  Decrypts a message. This is to be used with creating a digital signature.
     * 
     *  @param c    Message to decrypt
     *  @return 	Decrypted message
     */
    public String decrypt(String c) {
        return ((new BigInteger(c)).modPow(this.exponent, mod)).toString();
    }
    
    
    /**
     *  Encrypts a message. 
     * 
     *  @param m    Message to encrypt
     *  @return 	Encrypted message
     */
    public String encrypt(String m) {
        BigInteger message = new BigInteger(m);
        if(message.bitLength() > mod.bitLength())
            return null;
        return message.modPow(exponent, mod).toString();
    }
}
