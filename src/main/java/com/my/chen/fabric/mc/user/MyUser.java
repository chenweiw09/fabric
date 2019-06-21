package com.my.chen.fabric.mc.user;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.util.Set;
import java.util.UUID;

/**
 * @author chenwei
 * @version 1.0
 * @date 2019/6/14
 * @description
 */

public class MyUser implements User {

    private String name;
    private Set<String> roles;
    private String account;
    private String affiliation;
    private Enrollment enrollment;
    private String mspId;

    public MyUser(String mspid, String pem, String key) {
        this.name= UUID.randomUUID().toString();
        this.mspId = mspid;
        this.enrollment = new FCEnrollment(pem, key);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public void setMspId(String mspId) {
        this.mspId = mspId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getAffiliation() {
        return affiliation;
    }

    @Override
    public Enrollment getEnrollment() {
        return enrollment;
    }

    @Override
    public String getMspId() {
        return mspId;
    }

}
