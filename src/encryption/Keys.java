package encryption;

import java.util.Iterator;
import java.util.Random;

public final class Keys {
	public static int[] keyToInt(char[] key) {
		int[] ret = new int[key.length];
		
		for (int i = 0; i < key.length; i++) {
			ret[i] = key[i];
		}
		
		return ret;
	}
	
	public static char[] keyToChar(int[] key) {
    	char[] ret = new char[key.length];
    	
    	for(int i = 0; i < key.length; i++) {
    		ret[i] = (char) key[i];
    	}
    	
    	return ret;
    }
	
    public static char[] NEncryption(char[] toEncrypt, int[] key)
    {
    	char[] toEncryptClone = toEncrypt.clone();
    	
        for(int i = 0; i < toEncryptClone.length; i++) {
        	toEncryptClone[i] = (char) (toEncryptClone[i] + (key[i % key.length]) % 256);
        }
        return toEncryptClone;
    }
    
    public static char[] NDecryption(char[] toDecrypt, int[] key) {
    	char[] toDecryptClone = toDecrypt.clone();
    	
    	for (int i = 0; i < toDecryptClone.length; i++) {
    		toDecryptClone[i] = (char) (toDecryptClone[i] - (key[i % key.length]) % 256);
		}
    	return toDecryptClone;
    }
    
    public static int[] generateKey() {
    	Random r = new Random();
        int[] array = new int[256];
        for (int i = 0; i < array.length; i++)
        {
            int n = r.nextInt(0, array.length);
            array[i] = n;
        }
        return array;
    }
}
