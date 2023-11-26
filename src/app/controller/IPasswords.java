package app.controller;

import java.util.HashMap;

public class IPasswords {
    HashMap<String,String> logininfo = new HashMap<>();
    IPasswords() {
        logininfo.put("fabio", "poiu");
    }

    protected HashMap<String,String> getLoginInfo() {
        return logininfo;
    }
}
