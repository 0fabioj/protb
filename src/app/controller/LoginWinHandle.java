package app.controller;

import app.view.LoginWin;
import java.util.HashMap;

public class LoginWinHandle {
    LoginWin ui;
    public ActionResult loginResult;
    public LoginWinHandle() {
        ui = new LoginWin();
        setupActions();
    }
    public void showLoginAndWait() {
        ui.show();
    }
    public ActionResult getResultLogin() {
        return loginResult;
    }

    private void setupActions() {
        ui.btnCancel.setOnAction(e -> {
            loginResult = ActionResult.CANCEL;
            ui.close();
        });
        ui.btnOK.setOnAction(e -> {
            if (checkCredentials()) {
                loginResult = ActionResult.SUCCESS;
                ui.close();
            }
        });
    }

    private boolean checkCredentials() {
        String userId = ui.tfUser.getText();
        String userPassword = String.valueOf(ui.pfPassWord.getText());
        HashMap<String,String> logininfo;// = new HashMap<String,String>();
        IPasswords iPasswords = new IPasswords();
        logininfo = iPasswords.getLoginInfo();

        if (logininfo.containsKey(userId)) {
            if (logininfo.get(userId).equals(userPassword)) {
                return true;
            }
            else {
                //errorLabel.setText("errou senha!");
                return false;
            }
        }
        else {
            //errorLabel.setText("errou usuario!");
            return false;
        }
    }
}
