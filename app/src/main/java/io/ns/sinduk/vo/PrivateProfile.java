package io.ns.sinduk.vo;


import java.util.LinkedHashMap;
import java.util.Map;

public class PrivateProfile {

    private String profileId;
    private String fullName;
    private String email;
    private String publicKey;
    private String privateKey;
    private Map<String, SecretRecord> secrets = new LinkedHashMap<>();

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

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
