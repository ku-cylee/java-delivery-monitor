package cose102.delivery_monitor.main;

import cose102.delivery_monitor.db_handler.DatabaseHandler;
import cose102.delivery_monitor.forms.MainFrame;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseHandler.getInstance().initialize();
            new MainFrame();
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
