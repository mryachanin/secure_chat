package us.blint.securechat.encryption.AES;
import java.util.Arrays;

/**
 *  Represents a key for an AES implementation
 *
 */
public class Key extends Functions {
	private byte[] key;
	private int keyCount;
	private int Nb, Nr, Nk;
	
	/**
	 *  Construct the key
	 * 
	 *  @param key  bytes representing the key
	 *  @param Nb   Number of columns (32-bit words) comprising the State. For this standard, Nb = 4
	 *  @param Nr   Number of rounds
	 *  @param Nk   Number of 32 bit words comprising the cipher key
	 */
	public Key(byte[] key, int Nb, int Nr, int Nk) {
		this.key  = new byte[4 * Nk * (Nr + 1)];
		this.keyCount = 0;
		this.Nb = Nb;
		this.Nr = Nr;
		this.Nk = Nk;
		
		keyExpansion(key);
	}
	
	
	/**
	 * 
	 *  @param key  bytes representing the initial key
	 *  @param Nk   Number of 32 bit words comprising the cipher key
	 */
	private void keyExpansion(byte[] key) {
		byte[] temp = new byte[4];
		
		int i = 0;
		
		while (i < 4*Nk) {
			this.key[i] = key[i];
			i++;
		}
		
		System.out.println("Initial key: " + Functions.bytesToHex(this.key));
		
		i = Nk;
		
		while (i < Nb * (Nr + 1)) {
			for (int tmp = 0; tmp < 4; tmp++) {
				temp[tmp] = this.key[((i-1) * 4) + tmp];
			}
			
			if (i % Nk == 0) {
				temp = subWord(rotWord(temp,1));
				temp[0] ^= r_con[(i/Nk) - 1];
			}
			else if (Nk > 6 && (i % Nk) == 4) {
				temp = subWord(temp);
			}
			for (int k = 4*i; k < (4*i) + 4; k++) {
				this.key[k] = (byte)(this.key[k - (4*Nk)] ^ temp[k % (4*i)]);				// k % 4*i produces 0, 1, 2, 3
			}
			i++;
		}
	}
	
	
	/**
	 *  returns the next 16 bytes of the expanded key
	 *  
	 *  @return  next 16 bites of the expanded key
	 */
	public byte[] getKey() {
		byte[] keyPart = Arrays.copyOfRange(this.key, keyCount, keyCount + 16);
		keyCount += 16;
		return keyPart;
	}
	
	
	/**
	 *  returns the previous 16 bytes of the expanded key
	 *  
	 *  @return  previous 16 bites of the expanded key
	 */
	public byte[] getDecryptKey() {
		byte[] keyPart = Arrays.copyOfRange(this.key, keyCount - 16, keyCount);
		keyCount -= 16;
		return keyPart;
	}
	
	
	/**
	 *  Resets the index counter so the key can be used again to encrypt the next 16-bytes
	 */
	public void resetCounter() {
		keyCount = 0;
	}
	
	
	public void resetDecryptCounter() {
		keyCount = 4 * Nk * (Nr + 1);
	}
	
	protected final int[] r_con = {
			0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a
	};
}
