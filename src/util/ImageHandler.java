package util;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class ImageHandler {
    Map<String, Image> images = new HashMap<String, Image>();
    
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

    private Image getImageFromDisc(String path, int w, int h) {
        try {
            Image tempImg = ImageIO.read(new File(path));
            int originalWidth = tempImg.getWidth(null);
            int originalHeight = tempImg.getHeight(null);
            double widthScale = (double) w / originalWidth;
            double heightScale = (double) h / originalHeight;
            double scale = Math.min(widthScale, heightScale);
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);
            tempImg = tempImg.getScaledInstance(scaledWidth/15, scaledHeight/15, Image.SCALE_SMOOTH);
            return tempImg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}