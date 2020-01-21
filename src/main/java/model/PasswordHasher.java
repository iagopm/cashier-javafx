package model;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordHasher {
    public String hash(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
