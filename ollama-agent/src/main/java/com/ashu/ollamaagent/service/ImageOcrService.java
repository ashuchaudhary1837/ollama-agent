
package com.ashu.ollamaagent.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class ImageOcrService {

    @Value("${tesseract.datapath}")
    private String dataPath;

    public String extractText(byte[] imageBytes)
            throws IOException, TesseractException {

        BufferedImage image = ImageIO.read(
                new ByteArrayInputStream(imageBytes)
        );

        if (image == null) {
            throw new IllegalArgumentException(
                    "The uploaded file is not a supported image"
            );
        }

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(dataPath);
        tesseract.setLanguage("eng");

        return tesseract.doOCR(image);
    }
}
