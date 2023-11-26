package app.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Utils {
    private static final String CSS_PATH = "/style.css";
    //private static final String CONFIG_PATH = "src/app/resource/protb.ini";
    private static final String CONFIG_PATH = "protb.ini";
    public static int ValidateID(String idStr) {
        int id = 0;
        if (!idStr.isBlank()) {
            try {
                id = Integer.parseInt(idStr);
                System.out.println("ValidateID Success | Value: "+id);
            } catch (Exception e) {
                System.out.print("ValidateID Error | ");
                System.out.println(e.getMessage());
            }
        }
        return id;
    }

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }
    public static void addTextLimiter(final TextArea ta, final int maxLength) {
        ta.textProperty().addListener((ov, oldValue, newValue) -> {
            if (ta.getText().length() > maxLength) {
                String s = ta.getText().substring(0, maxLength);
                ta.setText(s);
            }
        });
    }

    public static String getCSS() {
        return CSS_PATH;
    }

    public static String getServerConfig() {
        try {
            String ip;
            ip = new Scanner(new File(CONFIG_PATH)).useDelimiter("\\Z").next();
            System.out.println(ip);
            return ip;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
