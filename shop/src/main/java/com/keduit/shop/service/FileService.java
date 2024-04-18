package com.keduit.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName,
                             byte[] fileData) throws Exception{
        // 이 부분은 독립적인 url을 생성하는 부분이다..
        // 백단에서 각 등록된 이미지의 url을 생성한다.
        UUID uuid = UUID.randomUUID();
        // Universally Unique Udentifier : 범용 고유 식별자를 의미하며 중복이 되지 않는 유일한 값을 구성하고자 할때 사용
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String saveFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + saveFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return saveFileName;
    }

    public void deleteFile(String filePath) throws Exception{
        // 저장된 파일의 경로를 이용하여 파일 객체를 생성.
        File deleteFile = new File(filePath);

        // 해당 파일이 있으면 삭제.
        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
