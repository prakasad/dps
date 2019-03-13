package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PreProcessedDataKeyUtils {


    public static final int unquieIdLength = 8;

    public String sentenceToUnquieHashId(String sentance) throws NoSuchAlgorithmException {
        String sentanceSHA = getSHA(sentance);
        // As github also uses 7 digits of its commitID to uniquely identy commit, will be using
        // the same logic. As this system usage increases we can increase the digits to create the sentence hash.
        if (sentanceSHA.length() > unquieIdLength) {
            return sentanceSHA.substring(sentanceSHA.length() - unquieIdLength);
        } else {
            return sentanceSHA;
        }
    }

    private String getSHA(String sentance) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(sentance.getBytes());

        BigInteger no = new BigInteger(1, messageDigest);


        String hashtext = no.toString(16);
        return hashtext;
    }
}
