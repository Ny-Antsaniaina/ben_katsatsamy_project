package org.example.projectface.service;

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
    private String folderUpLoad = "src/main/java/org/example/projectface/upload/face";

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
            String filePath = fileFolder.getAbsolutePath() + File.separator +fileName;


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
}
