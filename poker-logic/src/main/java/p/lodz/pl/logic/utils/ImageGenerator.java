package p.lodz.pl.logic.utils;

import p.lodz.pl.logic.model.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ImageGenerator {

    public BufferedImage generateTable(List<Card> cards) throws IOException {
        BufferedImage image = readImage(cards.get(0));
        int height = image.getHeight();
        int width = image.getWidth();

        BufferedImage out = new BufferedImage(
                width * cards.size() + (width / 10) * (cards.size() - 1),
                height,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D graphics = out.createGraphics();

        BufferedImage card;

        for (int i = 0; i < cards.size(); i++) {
            card = readImage(cards.get(i));
            if (i != 0) {
                graphics.translate(width * 1.1, 0);
            }
            graphics.drawImage(card, null, 0, 0);
        }

        return out;
    }


    public BufferedImage generateHand(List<Card> cards) throws IOException {
        BufferedImage image = readImage(cards.get(0));
        int height = image.getHeight();
        int width = image.getWidth();

        int translateDistance = 45;
        double rotationAngle = Math.toRadians(9);
        int numberOfCards = cards.size();

        BufferedImage tempImage = new BufferedImage(
                (width * numberOfCards),
                (height * numberOfCards),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D tempGraphics = tempImage.createGraphics();
        BufferedImage temp;
        for (int i = 0; i < numberOfCards; i++) {
            temp = readImage(cards.get(i));
            if (i != 0) {
                tempGraphics.translate(translateDistance, 0);
                tempGraphics.rotate(rotationAngle, 0, height);

            }
            tempGraphics.drawImage(temp, null, 0, 0);

        }

        BufferedImage genImage = new BufferedImage(
                width * numberOfCards,
                height * numberOfCards,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D genGraphics = genImage.createGraphics();
        switch (numberOfCards % 2) {
            case 0: {
                for (int i = 0; i < numberOfCards / 2; i++) {
                    genGraphics.rotate(-rotationAngle, 0, height);
                    genGraphics.translate(-translateDistance, 0);
                }
                genGraphics.rotate(-rotationAngle, 0, height);
                genGraphics.translate(-translateDistance, 0);
                break;
            }
            case 1: {
                for (int i = 0; i < numberOfCards / 2; i++) {
                    genGraphics.rotate(-rotationAngle, 0, height);
                    genGraphics.translate(-translateDistance, 0);
                }
                break;

            }
        }

        genGraphics.drawImage(
                tempImage,
                null,
                (int) Math.floor(tempImage.getWidth() * 0.25),
                (int) Math.floor(tempImage.getHeight() * 0.25)
        );


        genGraphics.dispose();
        return trimmedImage(genImage);

    }

    public BufferedImage readImage(Card card) throws IOException {
        return ImageIO.read(
                Objects.requireNonNull(getClass()
                        .getResource(
                                String.format(
                                        "/cards/%s-of-%s.png",
                                        card.rank().toString().toLowerCase(),
                                        card.suit().toString().toLowerCase()
                                )
                        )
                )
        );
    }

    public static BufferedImage trimmedImage(BufferedImage source) {
        final int minAlpha = 1;
        final int srcWidth = source.getWidth();
        final int srcHeight = source.getHeight();
        Raster raster = source.getRaster();
        int l = srcWidth, t = srcHeight, r = 0, b = 0;

        int alpha, x, y;
        int[] pixel = new int[4];
        for (y = 0; y < srcHeight; y++) {
            for (x = 0; x < srcWidth; x++) {
                raster.getPixel(x, y, pixel);
                alpha = pixel[3];
                if (alpha >= minAlpha) {
                    l = Math.min(x, l);
                    t = Math.min(y, t);
                    r = Math.max(x, r);
                    b = Math.max(y, b);
                }
            }
        }

        if (l > r || t > b) {
            // No pixels, couldn't trim
            return source;
        }

        return source.getSubimage(l, t, r - l + 1, b - t + 1);
    }
}
