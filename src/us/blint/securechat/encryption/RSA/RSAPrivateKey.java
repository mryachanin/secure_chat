package us.blint.securechat.encryption.RSA;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *  Holds the values for a RSA private key
 *      allows the decryption of messages
 *      allows for the encryption of messages for the purpose of verification (digital signature)
 */
public class RSAPrivateKey {
    private static final int n = 4096;
    private static final SecureRandom rand = new SecureRandom();
    
    private RSAPublicKey publicKey;
    private BigInteger privateExponent;
    private BigInteger pub;
    private BigInteger mod;
    
    
    /**
     *  Instantiates a RSA private key with the default of 4096 bits
     */
    public RSAPrivateKey() {
        this(n);
    }
    
    
    /**
     *  Instantiates a RSA private key with n bits
     *  
     *  @param bits  number of bits to use for the RSA key
     */
    public RSAPrivateKey(int bits) {
    	BigInteger pub = new BigInteger("65537");
    	BigInteger p = new BigInteger(n, 100, rand);
    	BigInteger q = new BigInteger(n, 100, rand);
    	BigInteger mod = p.multiply(q);
    	BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
    	p = BigInteger.ZERO;
        q = BigInteger.ZERO;
        while(phi.mod(pub).equals(BigInteger.ZERO)) {
            pub = pub.nextProbablePrime();
        }
        privateExponent = pub.modInverse(phi);
        phi = BigInteger.ZERO;
        this.pub = pub;
        this.mod = mod;
    }
    
    
    /**
     *  Returns the public exponent for this public / private key pair
     *  
     *  @return  public exponent
     */
    public String getPublicExponent() {
        return this.pub.toString();
    }
    
    
    /**
     *  Returns the modulus
     *  
     *  @return  modulus to use
     */
    public String getMod() {
    	return this.mod.toString();
    }
    
    
    /**
     *  Decrypts a message.
     * 
     *  @param c    Message to decrypt
     *  @return     Decrypted message
     */
    public String decrypt(String c) {
        return ((new BigInteger(c)).modPow(this.privateExponent, publicKey.mod)).toString();
    }
    
    
    /**
     *  Encrypts a message. This is to be used with creating a digital signature.
     * 
     *  @param m    Message to encrypt
     *  @return 	Encrypted message
     */
    public String encrypt(String m) {
        BigInteger message = new BigInteger(m);
        if(message.bitLength() > mod.bitLength())
            return null;
        return message.modPow(privateExponent, mod).toString();
    }
}