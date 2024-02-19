package encryption;

import java.util.Random;

public final class Keys {
	public static char[] l_alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'w', 'v', 'x', 'y', 'z' };
    public static char[] u_alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'V', 'X', 'Y', 'Z' };
    public static char[] simbols = { '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '.', '/', '-', '_', ' ', '?', ';', ':', '\'', '"', '\\', ',', '=', '+', '[', ']', '{', '}', '|', '~', '`'};
    public static char[] numbers = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    
    
    private static char[] generateFullAlphabet() {
    	char[] ret = new char[0];
    	for (char[] a : new char[][] {l_alphabet, u_alphabet, simbols, numbers}) {
    		char[] temp = new char[ret.length + a.length];
    		System.arraycopy(ret, 0, temp, 0, ret.length);
    		System.arraycopy(a, 0, temp, ret.length, a.length);
    		ret = temp;
		}
    	
    	return ret;
    }
    
    public static char[] NEncryption(char[] toEncrypt, char[] key)
    {
    	char[] toEncryptClone = toEncrypt.clone();
    	
        for(int i = 0; i < toEncryptClone.length; i++) {
        	toEncryptClone[i] = (char) (toEncryptClone[i] + (key[i % key.length]) % 256);
        }
        return toEncryptClone;
    }
    
    public static char[] NDecryption(char[] toDecrypt, char[] key) {
    	char[] toDecryptClone = toDecrypt.clone();
    	
    	for (int i = 0; i < toDecryptClone.length; i++) {
    		toDecryptClone[i] = (char) (toDecryptClone[i] - (key[i % key.length]) % 256);
		}
    	return toDecryptClone;
    }
    
    public static char[] generateKey() {
    	char[] fullAlphabet = generateFullAlphabet();
    	
    	Random r = new Random();
        char[] array = new char[fullAlphabet.length];
        char[] alreadySeen = new char[fullAlphabet.length];
        for (int i = 0; i < array.length;)
        {
            int n = r.nextInt(0, fullAlphabet.length);
            char c = fullAlphabet[n];
            if (alreadySeen.toString().contains(String.valueOf(c)) || fullAlphabet[i] == c)
            {
                continue;
            }
            array[i] = c;
            alreadySeen[i] = c;
            i++;
        }
        return array;
    }
}
