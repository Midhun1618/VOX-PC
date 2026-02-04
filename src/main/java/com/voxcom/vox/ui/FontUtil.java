package com.voxcom.vox.ui;

import java.awt.Font;

public class FontUtil {

    public static Font PIXEL_12;
    public static Font PIXEL_16;

    static {
        try {
            Font base = Font.createFont(
                Font.TRUETYPE_FONT,
                FontUtil.class.getResourceAsStream("/font/pixel.otf")
            );
            PIXEL_12 = base.deriveFont(12f);
            PIXEL_16 = base.deriveFont(16f);
        } catch (Exception e) {
            PIXEL_12 = new Font("Monospaced", Font.PLAIN, 12);
            PIXEL_16 = new Font("Monospaced", Font.BOLD, 16);
        }
    }
}
