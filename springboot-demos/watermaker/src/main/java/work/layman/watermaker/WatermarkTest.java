package work.layman.watermaker;

import work.layman.utils.WatermarkUtil;

/**
 * @ClassName WatermarkTest
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/10/23 10:18
 * @Version 3.0
 **/
public class WatermarkTest {
    public static void main(String[] args) throws Exception {
        // String srcImgPath, String markImgPath, String tarImgPath, Integer watermarkStyle
        // String srcImgPath, String tarImgPath, String waterMarkContent, Integer watermarkStyle
//        String srcImgPath = "C:\\Users\\Administrator\\Pictures\\开发使用图片\\徐总身份证_正.png";
        String markImgPath = "F:\\github-layman\\springboot-demos\\watermaker\\src\\main\\resources\\342×112.png";
        String srcImgPath = "E:\\身份证照片\\xiao.jpg";
//        String srcImgPath = "E:\\身份证照片\\IMG_20181213_162124.jpg";
        String tarImgPath = "E:\\身份证照片\\watermark.jpg";
        String waterMarkContent = "  **仅用于创拍网实名认证**  ";
        Integer watermarkStyle = 10;
//        WatermarkUtil.setTextWaterMarkForImage(srcImgPath, tarImgPath, waterMarkContent, watermarkStyle);
        WatermarkUtil.setImageWaterMarkForImage(srcImgPath, markImgPath, tarImgPath, watermarkStyle);
    }
}
