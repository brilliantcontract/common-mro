package bc.internal.image.placeholders.generator;

import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.FontMetrics;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

public class PlaceholderGenerator {

    private static final Logger LOG = Logger.getLogger(PlaceholderGenerator.class.getName());

    private static final String OUTPUT_IMAGE_FORMAT = "JPG";

    private static final String STORE_DIRECTORY = "placeholders";

    private static final int LOGO_MAX_WIDTH = 205;
    private static final int LOGO_MAX_HEIGHT = 70;

    private static final int IMAGE_WIDTH = 205;
    private static final int IMAGE_HEIGHT = 150;

    public void generate(Product product) {
        BufferedImage logo = ImageScaler.resize(
                load(product),
                ImageScaler.Method.BALANCED,
                ImageScaler.Mode.BEST_FIT_BOTH,
                LOGO_MAX_WIDTH,
                LOGO_MAX_HEIGHT,
                null
        );

        BufferedImage placeholder = new BufferedImage(
                IMAGE_WIDTH,
                IMAGE_HEIGHT,
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D graphics = placeholder.createGraphics();

        // Give the whole image a white background
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 205, 150);
        graphics.setColor(Color.BLACK);

        // Add catalog number
        drawCenteredString(
                graphics,
                product.getCatalogNumber(),
                new Rectangle(0, 101, 205, 0),
                new Font("Arial", Font.BOLD, 14)
        );

        // Add product ID
        drawCenteredString(
                graphics,
                "Item #" + product.getId(),
                new Rectangle(0, 120, 205, 0),
                new Font("Arial", Font.BOLD, 16)
        );

        // Add website address
        drawCenteredString(
                graphics,
                "MROsupply.com",
                new Rectangle(0, 138, 205, 0),
                new Font("Arial", Font.PLAIN, 16)
        );

        int x = (IMAGE_WIDTH - logo.getWidth()) / 2;
        graphics.drawImage(logo, null, x, 0);

        save(placeholder, product);
    }

    public void drawCenteredString(Graphics2D g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }

    private BufferedImage load(Product product) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(product.getLogoPath().toFile());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return image;
    }

    private void save(BufferedImage image, Product product) {
        try {
            if (Files.isDirectory(Paths.get(STORE_DIRECTORY))) {
                Files.createDirectories(Paths.get(STORE_DIRECTORY));
            }

            ImageIO.write(
                    image,
                    OUTPUT_IMAGE_FORMAT,
                    new FileOutputStream(STORE_DIRECTORY + "/" + product.getId() + ".jpg")
            );
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}
