package util;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler {
    Map<String, Image> images = new HashMap<String, Image>();
    Map<String, BufferedImage> backgrounds = new HashMap<String, BufferedImage>();
    
    public ImageHandler() {}

    public Image getImage(String path, int w, int h) throws IOException {
        if (images.containsKey(path)) {
            return images.get(path);
        } else {
            Image img = getImageFromDisc(path, w, h);
            images.put(path, img);
            return img;
        }
    }

    public void resize(int w, int h) {
        for (String path : images.keySet()) {
            images.put(path, getImageFromDisc(path, w, h));
        }
    }

    public BufferedImage getBackground(String path, List<Boolean> doors, Boolean open) {
        BufferedImage img = getImageFromDisc(path);
        Graphics g = img.getGraphics();
        for (int i = 0; i < 4; i++) {
            if (doors.get(i)) {
                if (i == 0) {
                    if (open) {
                        g.drawImage(getImageFromDisc("img/doors/openUp.png", img.getWidth(), img.getHeight()), 0, 0, null);
                    } else {
                        g.drawImage(getImageFromDisc("img/doors/closedUp.png", img.getWidth(), img.getHeight()), 0, 0, null);
                    }
                } else if (i == 1) {
                    if (open) {
                        g.drawImage(getImageFromDisc("img/doors/openRight.png", img.getWidth(), img.getHeight()), 0, 0, null);
                    } else {
                        g.drawImage(getImageFromDisc("img/doors/closedRight.png", img.getWidth(), img.getHeight()), 0, 0, null);
                    }
                } else if (i == 2) {
                    if (open) {
                        g.drawImage(getImageFromDisc("img/doors/openDown.png", img.getWidth(), img.getHeight()), 0, 0, null);
                    } else {
                        g.drawImage(getImageFromDisc("img/doors/closedDown.png", img.getWidth(), img.getHeight()), 0, 0, null);
                    }
                } else if (i == 3) {
                    if (open) {
                        g.drawImage(getImageFromDisc("img/doors/openLeft.png", img.getWidth(), img.getHeight()), 0, 0, null);
                    } else {
                        g.drawImage(getImageFromDisc("img/doors/closedLeft.png", img.getWidth(), img.getHeight()), 0, 0, null);
                    }
                }
            }
        }
        g.dispose();
        return img;
    }

    private BufferedImage getImageFromDisc(String path) {
        if (backgrounds.containsKey(path)) {
            return backgrounds.get(path);
        } else {
            try {
                BufferedImage img = ImageIO.read(new File(path));
                backgrounds.put(path, img);
                return img;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }

    private Image getImageFromDisc(String path, int w, int h) {
        try {
            Image tempImg = ImageIO.read(new File(path));
            int originalWidth = tempImg.getWidth(null);
            int originalHeight = tempImg.getHeight(null);
            double widthScale = (double) w / originalWidth;
            double heightScale = (double) h / originalHeight;
            double scale = Math.min(widthScale, heightScale);
            scale = Math.min(scale, 1.0);
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);
            tempImg = tempImg.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            return tempImg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}