package work.layman.service;

import org.springframework.stereotype.Service;
import work.layman.utils.WatermarkUtil;

/**
 * @ClassName WaterMakerService
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/10/24 11:44
 * @Version 3.0
 **/
@Service
public class WaterMakerService {

    public void addWaterMark() throws Exception {
        // String srcImgPath, String markImgPath, String tarImgPath, Integer watermarkStyle
        // String srcImgPath, String tarImgPath, String waterMarkContent, Integer watermarkStyle
//        String srcImgPath = "C:\\Users\\Administrator\\Pictures\\开发使用图片\\徐总身份证_正.png";
        String markImgPath = "classpath:342_112.png";
        String srcImgPath = "E:\\身份证照片\\xiao.jpg";
//        String srcImgPath = "E:\\身份证照片\\IMG_20181213_162124.jpg";
        String tarImgPath = "E:\\身份证照片\\watermark.jpg";
        String waterMarkContent = "  **仅用于创拍网实名认证**  ";
        Integer watermarkStyle = 10;
//        WatermarkUtil.setTextWaterMarkForImage(srcImgPath, tarImgPath, waterMarkContent, watermarkStyle);
        WatermarkUtil.setImageWaterMarkForImage(srcImgPath, markImgPath, tarImgPath, watermarkStyle);
    }
}
