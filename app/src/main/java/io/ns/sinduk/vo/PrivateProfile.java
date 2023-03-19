package io.ns.sinduk.vo;


import java.util.LinkedHashMap;
import java.util.Map;

public class PrivateProfile extends PublicProfile {

    private String privateKey;
    private Map<String, SecretRecord> secrets = new LinkedHashMap<>();

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Map<String, SecretRecord> getSecrets() {
        return secrets;
    }

    public void setSecrets(Map<String, SecretRecord> secrets) {
        this.secrets = secrets;
    }

}
