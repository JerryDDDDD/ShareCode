package work.layman.utils;

//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.pdf.*;

import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class WatermarkUtil {

    /**水印格式：居中*/
    public static final Integer WATERMARK_STYLE_CENTER = 10;

    /**水印格式：平铺*/
    public static final Integer WATERMARK_STYLE_TILED = 20;

    /**水印间隔距离*/
//    public static final Integer WATERMARK_INTERVAL = 20;
    public static final Integer WATERMARK_INTERVAL = 10;


    /**
     * 给pdf添加文字水印
     * @param srcFile 原文件位置
     * @param destFile  加水印后文件位置
     * @param text  文字水印内容
     * @param watermarkStyle 水印格式(10: 居中， 20：平铺)
     */
//    public static void setTextWaterMarkForPdf(String srcFile, String destFile, String text, Integer watermarkStyle)throws Exception {
//        PdfReader reader = new PdfReader(srcFile);
//        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destFile));
//
//        PdfGState gs = new PdfGState();
//        gs.setFillOpacity(0.3f);// 设置透明度
//        BaseFont font = BaseFont.createFont("Courier", "Cp1252", false);
//
//        JLabel label = new JLabel();
//        label.setText(text);
//        FontMetrics metrics = label.getFontMetrics(label.getFont());
//        int textH = metrics.getHeight() * 2;
//        int textW = metrics.stringWidth(label.getText()) * 2;
//
//        int total = reader.getNumberOfPages() + 1;
//        PdfContentByte content;
//        for (int i = 1; i < total; i++) {
//            content = stamper.getUnderContent(i);
//            content.beginText();
//            content.setGState(gs);
//
//            content.setColorFill(BaseColor.RED);
//            content.setFontAndSize(font, 30);
//
//            // 开始写入水印
//            float pageWidth = reader.getPageSize(i).getWidth();
//            float pageHigh = reader.getPageSize(i).getHeight();
//            if (WATERMARK_STYLE_TILED.equals(watermarkStyle)) {
//                for (float heigh = WATERMARK_INTERVAL; heigh < pageHigh; heigh += textH * 5) {
//                    for (float width = WATERMARK_INTERVAL; width < pageWidth; width += textW * 2) {
//                        content.showTextAligned(Element.ALIGN_LEFT, text, width, heigh, -15);
//                    }
//                }
//            } else {
//                float textWidth = (pageWidth - textW) / 2;
//                float textHeight = (pageHigh - textH) / 2;
//                content.showTextAligned(Element.ALIGN_LEFT, text, textWidth, textHeight, -15);
//            }
//            content.endText();
//        }
//        stamper.close();
//    }
//
//    /**
//     * 给pdf设置图片水印
//     * @param sourceFilePath 原文件位置
//     * @param fileWaterMarkPath 加水印后文件输出位置
//     * @param watermarkPath   水印位置
//     * @param watermarkStyle 水印格式(10: 居中， 20：平铺)
//     */
//    public static void setImageWaterMarkForPdf(String sourceFilePath, String fileWaterMarkPath, String watermarkPath, Integer watermarkStyle) throws Exception {
//        PdfReader reader = new PdfReader(sourceFilePath);
//        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(fileWaterMarkPath));
//
//        Image img = Image.getInstance(watermarkPath);
//        img.setRotationDegrees(-15);
//        float watermarkWidth = img.getWidth();
//        float watermarkHigh = img.getHeight();
//
//        PdfGState gs = new PdfGState();
//        gs.setFillOpacity(0.3f);// 设置透明度
//
//        int total = reader.getNumberOfPages() + 1;
//        PdfContentByte content;
//        for (int i = 1; i < total; i++) {
//            content = stamp.getUnderContent(i); // 在内容下方加水印
//            content.setGState(gs);
//
//            float pageWidth = reader.getPageSize(i).getWidth();
//            float pageHigh = reader.getPageSize(i).getHeight();
//            if(WATERMARK_STYLE_TILED.equals(watermarkStyle)) {
//                for(float heigh = WATERMARK_INTERVAL; heigh < pageHigh; heigh += watermarkHigh * 1.5) {
//                    for(float width = WATERMARK_INTERVAL; width < pageWidth; width += watermarkWidth * 1.5) {
//                        img.setAbsolutePosition(width, heigh);
//                        content.addImage(img);
//                    }
//                }
//            }else {
//                img.setAbsolutePosition((pageWidth-watermarkWidth)/2, (pageHigh - watermarkHigh)/2);
//                content.addImage(img);
//            }
//        }
//        stamp.close();
//        reader.close();
//    }
//

    /**
     * 给图片添加文字水印
     * @param srcImgPath 原图片位置
     * @param tarImgPath 加水印后图片输出位置
     * @param waterMarkContent 文字水印内容
     * @param watermarkStyle 水印格式(10: 居中， 20：平铺)
     */
    public static void setTextWaterMarkForImage(String srcImgPath, String tarImgPath, String waterMarkContent, Integer watermarkStyle) throws Exception {
        File srcImgFile = new File(srcImgPath);
        java.awt.Image srcImg = ImageIO.read(srcImgFile);
        int srcImgWidth = srcImg.getWidth(null);
        int srcImgHeight = srcImg.getHeight(null);
        // 加水印
        BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
//        g.setColor(Color.gray);
        g.setColor(Color.RED);
        g.setFont(new Font("宋体", Font.PLAIN, 50));
        g.rotate(Math.toRadians(15)); //水印旋转
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.3f)); //设置水印透明度

        //设置水印的坐标
        int textWidth = g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
        int textHight = g.getFontMetrics(g.getFont()).getHeight();
        if(WatermarkUtil.WATERMARK_STYLE_TILED.equals(watermarkStyle)) {
            for (float y = 0; y < srcImgHeight; y += textHight * 2) {
                for (float x = 0; x < srcImgWidth; x += textWidth) {
                    g.drawString(waterMarkContent, x, y);
//                    g.drawString(waterMarkContent, x + y, y-x);
                }
            }
        }else {
            int x = Math.abs((srcImgWidth - textWidth)/2);
            int y = Math.abs((srcImgHeight - textHight*4));
            g.drawString(waterMarkContent, x, y);
        }
        g.dispose();
        // 输出图片
        FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
        ImageIO.write(bufImg, "jpg", outImgStream);
        outImgStream.flush();
        outImgStream.close();
    }


    /**
     * 给图片添加图片水印
     * @param srcImgPath 原图片位置
     * @param markImgPath 图片水印的位置
     * @param tarImgPath 加水印后图片位置
     * @param watermarkStyle 水印格式(10: 居中， 20：平铺)
     */
    public static void setImageWaterMarkForImage(String srcImgPath, String markImgPath, String tarImgPath, Integer watermarkStyle) throws Exception{
        java.awt.Image img = ImageIO.read(new File(srcImgPath));
        File markFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/810_150.png");
        java.awt.Image mark = ImageIO.read(markFile);

        int imgWidth = img.getWidth(null);
        System.out.println(imgWidth);
        int imgHeight = img.getHeight(null);
        System.out.println(imgHeight);
        BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = bufImg.createGraphics();
        g.drawImage(img, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
        g.rotate(Math.toRadians(15)); //水印旋转
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f)); //设置水印透明度

        int waterWidth = mark.getWidth(null);
        int waterHight = mark.getHeight(null);
        if(WatermarkUtil.WATERMARK_STYLE_TILED.equals(watermarkStyle)) {
            for (int y = 0; y < imgHeight; y += waterHight * 2) {
                for (int x = 0; x < imgWidth; x += waterWidth) {
                    g.drawImage(mark, x + y, y-x, null);
                }
            }
        }else {
            int x = Math.abs((imgWidth - waterWidth/2)/2);
            int y = Math.abs((imgHeight - waterHight)/2);
            g.drawImage(mark, x, y, null);
        }
        g.dispose();
        FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
        ImageIO.write(bufImg, "jpg", outImgStream);
        outImgStream.flush();
        outImgStream.close();
    }


    /**获取文件类型*/
    public static String getFileType(String uri) throws IOException {
        Path path = Paths.get(uri);
        return Files.probeContentType(path);
    }
}