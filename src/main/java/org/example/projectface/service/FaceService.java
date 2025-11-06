package org.example.projectface.service;

import org.apache.commons.imaging.ImageReadException;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.example.projectface.entity.Face;
import org.example.projectface.entity.User;
import org.example.projectface.repository.FaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Service
public class FaceService {

    private final FaceRepository faceRepository;
    private final String folderUpload = System.getProperty("user.dir") + "/uploads/face";
    private final String cascadePath = "src/main/resources/cascade/haarcascade_frontalface_alt.xml";

    public FaceService(FaceRepository faceRepository) {
        this.faceRepository = faceRepository;
    }

    // Ajouter un visage
    public Face addFace(MultipartFile file, User user) {
        try {
            File fileFolder = new File(folderUpload);
            if (!fileFolder.exists() && !fileFolder.mkdirs()) {
                throw new RuntimeException("Failed to create folder");
            }

            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }

            String fileName = user.getName() + ext;
            File newFile = new File(fileFolder, fileName);

            if (newFile.exists()) {
                throw new RuntimeException("File already exists: " + fileName);
            }

            file.transferTo(newFile);

            Face face = new Face();
            face.setName(fileName);
            faceRepository.saveAll(face);
            return face;
        } catch (Exception e) {
            throw new RuntimeException("Error saving face file", e);
        }
    }

    // Détecter un visage
    public boolean detectFace(String imagePath) {
        CascadeClassifier faceDetector = new CascadeClassifier(cascadePath);
        Mat image = opencv_imgcodecs.imread(imagePath);
        RectVector faceDetections = new RectVector();
        faceDetector.detectMultiScale(image, faceDetections);
        System.out.println("Faces detected: " + faceDetections.size());
        return faceDetections.size() > 0;
    }

    // Comparer deux images par histogramme
    public double comparedImg(File img1, File img2) throws IOException {
        // Lire les deux images en niveaux de gris
        Mat image1 = opencv_imgcodecs.imread(img1.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE);
        Mat image2 = opencv_imgcodecs.imread(img2.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE);

        if (image1.empty() || image2.empty()) {
            throw new IOException("Une ou les deux images n'ont pas pu être lues.");
        }

        Mat hist1 = new Mat();
        Mat hist2 = new Mat();
        Mat mask = new Mat();

        // Channels
        IntPointer channels = new IntPointer(1);
        channels.put(0, 0); // canal 0 pour grayscale

        // Histogram size
        IntPointer histSize = new IntPointer(1);
        histSize.put(0, 256); // 256 bins

        // Ranges
        FloatPointer ranges = new FloatPointer(2);
        ranges.put(0, 0f);
        ranges.put(1, 256f);

        // Calculer histogrammes
        opencv_imgproc.calcHist(
                new MatVector(image1),
                channels,
                mask,
                hist1,
                histSize,
                ranges,
                true // accumulate = true (or false depending on your need)
        );

        opencv_imgproc.calcHist(
                new MatVector(image2),
                channels,
                mask,
                hist2,
                histSize,
                ranges,
                true
        );

        // Normaliser
        opencv_core.normalize(hist1, hist1, 0, 1, opencv_core.NORM_MINMAX, -1, null);
        opencv_core.normalize(hist2, hist2, 0, 1, opencv_core.NORM_MINMAX, -1, null);

        // Comparer
        return opencv_imgproc.compareHist(hist1, hist2, opencv_imgproc.HISTCMP_CORREL);
    }

    // Reconnaissance (comparaison du visage uploadé avec les visages existants)
    public boolean recognizingFace(MultipartFile inputFile) throws IOException, ImageReadException {
        File tempFile = new File(folderUpload, "temp.jpg");
        inputFile.transferTo(tempFile);

        File folder = new File(folderUpload);
        File[] existingFiles = folder.listFiles((dir, name) ->
                name.endsWith(".jpg") || name.endsWith(".png"));

        if (existingFiles == null || existingFiles.length == 0) {
            tempFile.delete();
            return false;
        }

        for (File existingFile : existingFiles) {
            if (existingFile.getName().equals("temp.jpg")) continue;

            double similarity = comparedImg(tempFile, existingFile);
            System.out.println("Comparing with " + existingFile.getName() + " -> " + similarity);

            if (similarity > 0.90) {
                tempFile.delete();
                return true;
            }
        }

        tempFile.delete();
        return false;
    }
}
