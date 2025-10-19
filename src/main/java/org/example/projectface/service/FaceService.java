package org.example.projectface.service;

import org.example.projectface.entity.Face;
import org.example.projectface.repository.FaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class FaceService {
    private final FaceRepository faceRepository =  new FaceRepository();
    private String folderUpLoad = "../../main/resources/upload/face";

    public Face addFace(MultipartFile file) {
        try{
            File fileFolder = new File(folderUpLoad);
            if(!fileFolder.exists()){
                boolean created = fileFolder.mkdirs();
                if(!created){
                    throw new RuntimeException("Failed to create folder");
                }
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = fileFolder.getAbsolutePath() + File.separator +fileName;

            file.transferTo(new File(filePath));

            Face face = new Face();
            face.setName(filePath);
            int id = faceRepository.saveAll(face);
            face.setId(id);
            return face;
        }catch (Exception e) {
            throw new RuntimeException("Error to save face file", e);
        }
    }
}
