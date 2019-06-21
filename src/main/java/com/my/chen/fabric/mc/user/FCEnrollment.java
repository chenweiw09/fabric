package com.my.chen.fabric.mc.user;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.security.PrivateKey;

/**
 * @author chenwei
 * @version 1.0
 * @date 2019/6/21
 * @description
 */
public class FCEnrollment implements Enrollment, Serializable {

    private static final long serialVersionUID = -4274445336349657179L;
    private PrivateKey key;
    private String cert;

    public FCEnrollment(String signedPem, String key) {
        PrivateKey privateKey = null;
        try {
            privateKey = getPrivateKeyFromString(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.key = privateKey;
        this.cert = signedPem;
    }

    @Override
    public PrivateKey getKey() {
        return key;
    }

    @Override
    public String getCert() {
        return cert;
    }

    private static PrivateKey getPrivateKeyFromString(String data)
            throws IOException {

        final Reader pemReader = new StringReader(data);

        final PrivateKeyInfo pemPair;
        try (PEMParser pemParser = new PEMParser(pemReader)) {
            pemPair = (PrivateKeyInfo) pemParser.readObject();
        }

        return new JcaPEMKeyConverter().getPrivateKey(pemPair);
    }
}
