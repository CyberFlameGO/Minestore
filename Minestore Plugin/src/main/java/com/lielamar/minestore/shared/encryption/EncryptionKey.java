package com.lielamar.minestore.shared.encryption;

import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class EncryptionKey {

    private String keyRaw;
    private SecretKey secretKey;

    public EncryptionKey(String path) {
        File file = new File(path + "/key.json");
        loadKey(file);
    }

    /**
     * Loads the file with the key inside of it/generates a new key if there isn't one
     *
     * @param file   File to load key from/to
     */
    private void loadKey(File file) {
        try {
            if(!file.exists() && file.createNewFile()) {
                System.out.println("Generating a Secret Key!");

                javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance("AES");
                generator.init(128);

                this.keyRaw = getSaltString();
                this.secretKey = AES.getKeyObject(this.keyRaw);

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(this.keyRaw.getBytes());
                bos.close();
                bos.flush();
                System.out.println("Generated a key!");
            } else {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

                StringBuilder keyBuilder = new StringBuilder();
                while(bis.available() > 0)
                    keyBuilder.append(bis.read());

                this.keyRaw = keyBuilder.toString();
                this.secretKey = AES.getKeyObject(keyBuilder.toString());
            }
        } catch(IOException | NoSuchAlgorithmException exception) {
            this.secretKey = null;
            System.out.println("Could not create a Secret Key!");

            exception.printStackTrace();
        }
    }

    /**
     * Returns the key used to encrypt/decrypt stuff
     *
     * @return   Key
     */
    @Nullable
    public String getKey() {
        return this.keyRaw;
    }

    final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    protected String getSaltString() {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 128) {
            int index = (int) (rnd.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        return salt.toString();
    }
}