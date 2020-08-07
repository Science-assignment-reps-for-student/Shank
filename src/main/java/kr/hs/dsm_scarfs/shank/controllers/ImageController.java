package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping(
        value = "/{imageName}",
        produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE}
    )
    public byte[] getImages(@PathVariable String imageName) {
        return imageService.getImage(imageName);
    }

}
