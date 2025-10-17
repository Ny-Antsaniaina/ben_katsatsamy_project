package org.example.projectface.service;

import org.example.projectface.entity.Face;
import org.example.projectface.repository.FaceRepository;
import org.springframework.stereotype.Service;

@Service
public class FaceService {
    private FaceRepository faceRepository =  new FaceRepository();

    public Face addFace(Face face) {
        return faceRepository.saveAll(face);
    }
}
