package net.defekt.mc.chatclient.ui;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Icons {

    public static Icon X, GEAR, PLUG;

    static {
        X = tryLoad("iconsv2/x.png");
        GEAR = tryLoad("iconsv2/gear.png");
        PLUG = tryLoad("iconsv2/plug.png");
    }

    private static Icon tryLoad(String fileName) {
        try {
            return new ImageIcon(ImageIO.read(Icons.class.getResourceAsStream("/resources/" + fileName)));
        } catch (Exception e) {
            return new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        }
    }
}
