package com.onthe7.pupsroad.common.util;

import com.onthe7.pupsroad.module.resource.domain.enums.ResourceType;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.onthe7.pupsroad.module.resource.domain.enums.ResourceType.*;

public class ResourceUtil {
    private static final Map<String, ResourceType> resourceTypeMap;

    static {
        resourceTypeMap = new HashMap<>();
        resourceTypeMap.put("M4A",AUDIO); resourceTypeMap.put("FLAC",AUDIO); resourceTypeMap.put("MP3",AUDIO);
        resourceTypeMap.put("WAV",AUDIO); resourceTypeMap.put("AAC",AUDIO); resourceTypeMap.put("AVI",VIDEO);
        resourceTypeMap.put("DIF",VIDEO); resourceTypeMap.put("DV",VIDEO); resourceTypeMap.put("M4U",VIDEO);
        resourceTypeMap.put("M4V",VIDEO); resourceTypeMap.put("MOV",VIDEO); resourceTypeMap.put("MOVIE",VIDEO);
        resourceTypeMap.put("MP4",VIDEO); resourceTypeMap.put("MPE",VIDEO); resourceTypeMap.put("MPEG",VIDEO);
        resourceTypeMap.put("MPG",VIDEO); resourceTypeMap.put("MXU",VIDEO); resourceTypeMap.put("QT",VIDEO);
        resourceTypeMap.put("BMP",IMAGE); resourceTypeMap.put("CGM",IMAGE); resourceTypeMap.put("DJV",IMAGE);
        resourceTypeMap.put("DJVU",IMAGE); resourceTypeMap.put("GIF",IMAGE); resourceTypeMap.put("ICO",IMAGE);
        resourceTypeMap.put("IEF",IMAGE); resourceTypeMap.put("JP2",IMAGE); resourceTypeMap.put("JPE",IMAGE);
        resourceTypeMap.put("JPEG",IMAGE); resourceTypeMap.put("JPG",IMAGE); resourceTypeMap.put("MAC",IMAGE);
        resourceTypeMap.put("PBM",IMAGE); resourceTypeMap.put("PIC",IMAGE); resourceTypeMap.put("SVG",IMAGE);
        resourceTypeMap.put("PNG",IMAGE);
    }

    public static ResourceType of(MultipartFile file) {
        ResourceType resourceType = resourceTypeMap.get(
                Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename())).toUpperCase());

        return Objects.isNull(resourceType) ? DEFAULT: resourceType;
    }
}
