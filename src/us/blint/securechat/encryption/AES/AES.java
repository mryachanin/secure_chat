package us.blint.securechat.encryption.AES;
import java.util.Arrays;

/**
 *  Implementation of AES
 *  
 *  NOTE: if the message is not a multiple of 16-bytes, this will zero extend the end of the message
 *  		to a multiple of 16 bytes. For instance, if the message is 00112233, it will be decrypted
 *  		as 0011223300000000. This is not a problem with printing ASCII as '00' represents the null
 *  		character and signifies the end of a string anyways
 *  
 *  NOTE: please only use a 16 bit key. It sort of breaks with a 24 or 32 bit key
 */
public class AES {
														// these are for 128-bit
	private static final int Nb = 4;					// Number of columns
	private static final int Nk = 4;					// Number of 32 bit words comprising the cipher key
	private static final int Nr = Nk + 6;				// Number of rounds
	private Key key;
	private State state;
	
	/**
	 *  Takes in a 128-bit key to use as the symmetric key
	 *  If the key is null, this will generate a 128-bit key to use
	 *  
	 *  @param key
	 */
	public AES(byte[] key) {
		if (key.length == 32) {
			this.key = new Key(key, Nb, Nr, Nk);
		}
		else if (key.length == 48) {
			this.key = new Key(key, 6, 12, Nk);
		}
		else if (key.length == 64) {
			this.key = new Key(key, 8, 14, Nk);
		}
		else {
			System.out.println("Invalid key size");
			System.exit(1);
		}
	}
	
	
	/**
	 *  Takes in a message and encrypts it
	 *  
	 *  @param m  message to encrypt
	 *  @return   encrypted message
	 */
	public byte[] encrypt(byte[] message) {
		int encryptedLength;
		if ((message.length % 16) == 0) {
			encryptedLength = message.length;
		}
		else {
			encryptedLength = message.length + 16 - (message.length % 16);
		}
		byte[] encrypted = new byte[encryptedLength];
		
		
		for (int i = 0; i < message.length; i += 16) {					// break up the byte array into 16-byte 2d arrays
			this.state = new State(Arrays.copyOfRange(message, i, i + 16));
			
			System.out.println("round[" + 0 + "].initial: " + "state: " + Functions.bytesToHex(state.getBytes()));
			state.addRoundKey(key.getKey());
			
			for (int k = 0; k < (Nr - 1); k++) {
				System.out.println("round[" + k + "].start: " + "state: " + Functions.bytesToHex(state.getBytes()));
				state.subBytes();
				System.out.println("round[" + k + "].s_box: " + "state: " + Functions.bytesToHex(state.getBytes()));
				state.shiftRows();
				System.out.println("round[" + k + "].s_row: " + "state: " + Functions.bytesToHex(state.getBytes()));
				state.mixColumns();
				System.out.println("round[" + k + "].m_col: " + "state: " + Functions.bytesToHex(state.getBytes()));
				state.addRoundKey(key.getKey());
			}
			
			state.subBytes();											// final round does not include mixColumns()
			state.shiftRows();
			state.addRoundKey(key.getKey());
			
			byte[] stateBytes = state.getBytes();
			for (int n = 0; n < 16; n++) {
				encrypted[i+n] = stateBytes[n];
			}
			if ((i + 16) != encryptedLength) {
				key.resetCounter();
			}
		}
		
		return encrypted;
	}
	
	
	/**
	 *  Takes in a cipher and decrypts it
	 *  
	 *  @param c  cipher to decrypt
	 *  @return   decrypted message
	 */
	public byte[] decrypt(byte[] cipher) {
		int decryptedLength = cipher.length;
		byte[] decrypted = new byte[decryptedLength];
		
		for (int i = 0; i < cipher.length; i += 16) {						// break up the byte array into 16-byte 2d arrays
			this.state = new State(Arrays.copyOfRange(cipher, i, i + 16));

			state.addRoundKey(key.getDecryptKey());
			
			for (int k = 0; k < (Nr - 1); k++) {
				state.invShiftRows();
				state.invSubBytes();
				state.addRoundKey(key.getDecryptKey());
				state.invMixColumns();
			}
			
			state.invShiftRows();										// final round does not include mixColumns()
			state.invSubBytes();
			state.addRoundKey(key.getDecryptKey());
			
			byte[] stateBytes = state.getBytes();
			for (int n = 0; n < 16; n++) {
				decrypted[i+n] = stateBytes[n];
			}
			if ((i + 16) != decryptedLength) {
				key.resetDecryptCounter();
			}
		}
		
		return decrypted;
	}
	
	/**
	 *  tests an encryption and decryption
	 *  
	 *  NOTE: if you insert "\0" anywhere in the string, java will treat this as the "null" character 
	 *  		and end the string. The encryption / decryption still works fine, it just means that simply
	 *  		instantiating a new String is not sufficient. However, not many messages contain the literal 
	 *  		'\0', so for the sake of simplicity, this "error" does exist. 
	 *  
	 *  @param args  args[0] = message
	 *  			 args[1] = key (optional: if none is provided, a random 16-bit key will be generated and used)
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: <message> <key>");
			System.out.println("The message can be any length but the key must be 16 charactors long.");
			System.exit(1);
		}
		AES aes = new AES(args[1].getBytes());
		byte[] cipher = aes.encrypt(args[0].getBytes());
		System.out.println("Cipher:  " + Functions.bytesToHex(cipher));
		System.out.println("Message: " + new String(aes.decrypt(cipher)));
	}
}
