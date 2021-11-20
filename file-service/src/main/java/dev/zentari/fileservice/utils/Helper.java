package dev.zentari.fileservice.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helper {

    /**
     * Generate new filename that will be used to store the file. Generated using original filename and current time.
     * @param filename original name of the file
     * @return newly generate name that is hash from original time and current time
     * @throws NoSuchAlgorithmException
     */
    public static String generateFilename(String filename) throws NoSuchAlgorithmException {

        String timestamp = String.valueOf(System.currentTimeMillis());
        String baseString = filename + timestamp;

        byte[] bytesOfString = baseString.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(bytesOfString);
        String hexValue = toHexString(digest);

        String fileExtension = "";
        int index = filename.lastIndexOf('.');
        if (index >= 0) {
            fileExtension = filename.substring(index + 1);
        }

        return hexValue + "." + fileExtension;
    }

    public static String toHexString(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}
