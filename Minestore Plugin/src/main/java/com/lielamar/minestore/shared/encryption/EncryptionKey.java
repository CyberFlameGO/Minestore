package com.lielamar.minestore.shared.encryption;

import javax.crypto.SecretKey;
import java.io.*;
import java.security.NoSuchAlgorithmException;

public class EncryptionKey {

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

                this.secretKey = generator.generateKey();

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(this.secretKey.toString().getBytes());
                bos.close();
                bos.flush();
            } else {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

                StringBuilder keyBuilder = new StringBuilder();
                while(bis.available() > 0)
                    keyBuilder.append(bis.read());

                this.secretKey = AES.getKeyObject(keyBuilder.toString());
            }
        } catch(IOException | NoSuchAlgorithmException exception) {
            System.out.println("Could not create a Secret Key!");

            exception.printStackTrace();
        }
    }

    /**
     * Returns the key used to encrypt/decrypt stuff
     *
     * @return   Key
     */
    public String getKey() {
        return this.secretKey.toString();
    }
}
