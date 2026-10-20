
package com.ashu.ollamaagent.controller;

import com.ashu.ollamaagent.service.ImageOcrService;
import com.ashu.ollamaagent.service.RagService;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class RagPageController {

    private final ImageOcrService imageOcrService;
    private final RagService ragService;

    public RagPageController(
            ImageOcrService imageOcrService,
            RagService ragService) {

        this.imageOcrService = imageOcrService;
        this.ragService = ragService;
    }

    @PostMapping("/rag/upload-image")
    public String uploadImage(
            @RequestParam("file") MultipartFile file,
            Model model) {

        try {
            // 1. Check whether a file was selected
            if (file == null || file.isEmpty()) {
                model.addAttribute(
                        "uploadError",
                        "Please select an image to upload."
                );
                return "chat";
            }

            // 2. Validate the uploaded image type
            String contentType = file.getContentType();

            if (contentType == null ||
                    !(contentType.equalsIgnoreCase("image/png")
                            || contentType.equalsIgnoreCase("image/jpeg"))) {

                model.addAttribute(
                        "uploadError",
                        "Invalid file type. Please upload a PNG or JPEG image."
                );
                return "chat";
            }

            // 3. Extract text from the image using Tesseract OCR
            String extractedText =
                    imageOcrService.extractText(file.getBytes());

            // 4. Check whether OCR found readable text
            if (extractedText == null || extractedText.isBlank()) {
                model.addAttribute(
                        "uploadError",
                        "No readable text was found in the image. "
                                + "Please upload a clearer image."
                );
                return "chat";
            }

            // 5. Add extracted text to the RAG knowledge base
            ragService.ingestText(extractedText);

            // 6. Show success message on the frontend
            model.addAttribute(
                    "uploadMessage",
                    "Image uploaded successfully! "
                            + "Its extracted text has been added to the AI knowledge base."
            );

            // Optional: show the extracted text on the page
            model.addAttribute("extractedText", extractedText);

        } catch (IllegalStateException e) {

            // For example, missing eng.traineddata
            model.addAttribute(
                    "uploadError",
                    "OCR configuration error: " + e.getMessage()
            );

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "uploadError",
                    e.getMessage()
            );

        } catch (IOException | TesseractException e) {

            model.addAttribute(
                    "uploadError",
                    "Could not process the image. "
                            + "Check the image file and Tesseract installation."
            );

            // Print details in IntelliJ's Run console
            e.printStackTrace();

        } catch (Exception e) {

            model.addAttribute(
                    "uploadError",
                    "Failed to add the image text to the knowledge base. "
                            + "Please check the application console."
            );

            e.printStackTrace();
        }

        // Render the Thymeleaf page
        return "chat";
    }
}
