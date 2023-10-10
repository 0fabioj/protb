package app.controller;

import java.util.HashMap;

public class IPasswords {
    HashMap<String,String> logininfo = new HashMap<String,String>();
    IPasswords() {
        logininfo.put("fabio","ava");
    }

    protected HashMap<String,String> getLoginInfo() {
        return logininfo;
    }

}
