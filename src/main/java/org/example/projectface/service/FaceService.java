package org.example.projectface.service;




import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.RectVector;
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
    private final FaceRepository faceRepository =  new FaceRepository();
    private final String folderUpLoad = "src/main/java/org/example/projectface/upload/face";
    private final String cascadePath = "src/main/resources/haarcascade_frontalface_alt.xml";

    public Face addFace(MultipartFile file , User user){
        try{
            File fileFolder = new File(folderUpLoad);
            if(!fileFolder.exists()){
                boolean created = fileFolder.mkdirs();
                if(!created){
                    throw new RuntimeException("Failed to create folder");
                }
            }

            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }

            String fileName = user.getName() + ext;
            String filePath = fileFolder.getAbsolutePath() + File.separator + fileName;


            File newFile = new File(filePath);

            if(newFile.exists()){
                throw new RuntimeException("File already exists");
            }

            file.transferTo(newFile);

            Face face = new Face();
            face.setName(fileName);
            int id = faceRepository.saveAll(face);
            face.setId(id);
            return face;
        }catch (Exception e) {
            throw new RuntimeException("Error to save face file", e);
        }
    }



    public double comparedImg(Mat img1 , Mat img2){
        try{
            opencv_imgproc.cvtColor(img1, img1, opencv_imgproc.COLOR_BGR2GRAY);
            opencv_imgproc.cvtColor(img2 , img2, opencv_imgproc.COLOR_BGR2GRAY);

            Mat hist1 = new Mat();
            Mat hist2 = new Mat();


            opencv_imgproc.calcHist(img1,1,new int[]{0}, new Mat() , hist1 , 1,new int[]{256} , new float[]{0,256} );
            opencv_imgproc.calcHist(img2 , 1 , new int[]{0} , new Mat() , hist2 , 1 , new int[]{256} , new float[]{0,256} );

            opencv_core.normalize(hist1,hist1);
            opencv_core.normalize(hist2, hist2);

            return opencv_imgproc.compareHist(hist1,hist2,opencv_imgproc.HISTCMP_CORREL);


        } catch (Exception e ) {
            throw new RuntimeException("Error to scan face file", e);
        }
    }

    public boolean reconizingFace(MultipartFile inputFile) throws IOException {
        File tempFile = new File(folderUpLoad, "temp.jpg");
        inputFile.transferTo(tempFile);

        CascadeClassifier faceDetectore = new CascadeClassifier(cascadePath);

        Mat image = opencv_imgcodecs.imread(tempFile.getAbsolutePath());

        if (image.empty()) {
            throw new RuntimeException("cannot photo uploads");
        }

        RectVector detectedFaces = new RectVector();
        faceDetectore.detectMultiScale(image, detectedFaces);

        if (detectedFaces.size() == 0) {
            System.out.println("no face detected");
            return false;
        }

        File fileFolder = new File(folderUpLoad);
        File[] existingFiles = fileFolder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));

        if (existingFiles == null || existingFiles.length == 0) {
            return false;
        }

        for (File existingFile : existingFiles) {
            if (existingFile.getName().equals("temp.jpg") || existingFile.getName().equals("temp.png")) {
                continue;
            }
            Mat existing = opencv_imgcodecs.imread(existingFile.getAbsolutePath());
            if (existing.empty()) {
                continue;
            }
            double similitary = comparedImg(image, existing);
            System.out.println("Comparing with " + existingFile.getName() + "-> " + similitary);

            if (similitary > 0.90) {
                tempFile.delete();
                return true;
            }


        }
        tempFile.delete();
        return false;
    }
}
