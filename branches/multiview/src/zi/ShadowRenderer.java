package zi;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.*;

/**
 * Renders shadow.
 *
 * @author www
 */
public class ShadowRenderer {
    /**
     * Creates shadow for given <code>image</code> with specified size, opacity and color.
     *
     * @param image   image to create shadow for.
     * @param size    size of shadow.
     * @param opacity opacity.
     * @param color   shadow's color.
     * @return image containing shadow.
     */
    public static BufferedImage createShadow(BufferedImage image, int size, float opacity, Color color) {
        BufferedImage subject = prepareImage(image, size);
        BufferedImage shadow = new BufferedImage(subject.getWidth(), subject.getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage shadowMask = createShadowMask(subject, opacity, color);
        getLinearBlurOp(size).filter(shadowMask, shadow);
        return shadow;
    }

    private static BufferedImage prepareImage(BufferedImage image, int size) {
        BufferedImage subject = new BufferedImage(image.getWidth() + size * 2,
                image.getHeight() + size * 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = subject.createGraphics();
        g2.drawImage(image, null, size, size);
        g2.dispose();
        return subject;
    }

    private static BufferedImage createShadowMask(BufferedImage image, float opacity, Color color) {
        BufferedImage mask = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mask.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(5, opacity));
        g2d.setColor(color);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.dispose();
        return mask;
    }

    private static ConvolveOp getLinearBlurOp(int size) {
        float data[] = new float[size * size];
        float value = 1.0f / (float) (size * size);
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }
        return new ConvolveOp(new Kernel(size, size, data));
    }
}
