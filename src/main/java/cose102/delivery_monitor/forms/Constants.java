package cose102.delivery_monitor.forms;

import java.awt.*;

class Constants {
    static final Color BUTTON_COLOR = new Color(204, 204, 204);

    static Font getFont() {
        return getFont(Font.PLAIN, 12);
    }

    static Font getFont(int fontType, int fontSize) {
        return new Font("Dotum", fontType, fontSize);
    }
}
