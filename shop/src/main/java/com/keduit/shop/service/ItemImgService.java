package com.keduit.shop.service;

import com.keduit.shop.entity.ItemImg;
import com.keduit.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    // application.properties의 등록한 itemImgLocation의 값을 가져와서 itemImgLocation 변수에 할당
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile multipartFile) throws Exception {

        String oriImgName = multipartFile.getOriginalFilename();

        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        // 등록여부를 확인 후 fileService.uploadFiles()를 호출
        // 이땨 저장할 경로와 파일의 이름, 이미지 파일의 바이트 배열을 파라미터로 전달
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, multipartFile.getBytes());
            imgUrl = "/images/item/" + imgName; // 저장한 파일을 불러올 경로 설정
            // WebMvcConfig 클래스에서 "/images/**"로 설정했으며 uploadPath프로퍼티 경로인
            // "C:/shop/" 아래 item폴더에 이미지를 저장하므로 상품이미지 경로는 "/images/item/"을 붙인다.
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl); // 간단하게 set하기 위한 매서드를 만들었었다.
        itemImgRepository.save(itemImg); // img 테이블에 등록까지 완료.
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile)
            throws Exception {
        if (!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/"
                        + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(
                    itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
