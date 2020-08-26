package kr.hs.dsm_scarfs.shank.service.image;

import com.amazonaws.util.IOUtils;
import kr.hs.dsm_scarfs.shank.entites.file.image.repository.ImageRepository;
import kr.hs.dsm_scarfs.shank.exceptions.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    private final ImageRepository imageRepository;

    @Value("${image.upload.dir}")
    private String imageDirPath;

    @SneakyThrows
    @Override
    public byte[] getImage(String imageName) {
        File file = new File(imageDirPath, imageName);
        if (!file.exists())
            throw new ImageNotFoundException();
        
        InputStream inputStream = new FileInputStream(file);

        return IOUtils.toByteArray(inputStream);
    }
}
