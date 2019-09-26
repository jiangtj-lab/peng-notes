package com.peng.daliy.dynamicconfig.client.model;

public class Dynamicconfig {

    private String address;

    private String localecachepath;



    public Dynamicconfig(String address, String localecachepath) {
        this.address = address;
        this.localecachepath = localecachepath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocalecachepath() {
        return localecachepath;
    }

    public void setLocalecachepath(String localecachepath) {
        this.localecachepath = localecachepath;
    }
}
