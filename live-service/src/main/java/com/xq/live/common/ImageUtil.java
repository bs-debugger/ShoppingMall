package com.xq.live.common;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

/**
 * 图片处理工具类
 *
 * @Author luosai
 * @Date 2017/4/19
 */
public class ImageUtil {
    private static Logger log = Logger.getLogger(ImageUtil.class);

/*    private static File waterMarkLarge = null;
    private static File waterMarkMedium = null;
    private static File waterMarkSmall = null;

    static {
*//*        String classPath = FileUtil.getClassPath();
        String waterMarkLargePath = classPath + "/waterMark/waterMarkLarge_2560px.png";
        String waterMarkMediumPath = classPath + "/waterMark/waterMarkMedium_658px-2560px.png";
        String waterMarkSmallPath = classPath + "/waterMark/waterMarkSmall_658px.png";
        waterMarkLarge = new File(waterMarkLargePath);
        waterMarkMedium = new File(waterMarkMediumPath);
        waterMarkSmall = new File(waterMarkSmallPath);*//*
    }*/

    /** 为了减少图片空间，所有图片统一转换为JPG格式 */
    private static final String IMG_FORMAT = "jpg";

    /** 视频格式上传 */
    private static final String MP4_FORAMT = "mp4";

    /**
     * 尺寸不变压缩图片
     *
     * @param sourceImgPath 原图文件路径
     * @param quality 压缩后的图片质量比例(0.1f-1.0f)
     * @return outputImgPath
     */
    public static String compressByQuality(String sourceImgPath, float quality) {
        String outputImgPath = "";
        try {
            outputImgPath = getOutputImgPath(sourceImgPath, String.valueOf(quality));
            Thumbnails.of(sourceImgPath).scale(1f).outputQuality(quality).outputFormat(IMG_FORMAT)
                    .toFile(outputImgPath);
        } catch (Exception e) {
            log.error("compressByQuality error,sourceImgPath:{},quality:{}"+sourceImgPath, e);
        }
        return outputImgPath;
    }

    /**
     * 尺寸不变压缩图片
     *
     * @param sourceImgPath 原图文件路径
     * @param quality 压缩后的图片质量比例(0.1f-1.0f)
     * @return triplet A:outputImgPath B:width C:height
     */
    public static Triplet<String, String, String> compressByQualityWithSize(String sourceImgPath, float quality) {
        try {
            String outputImgPath = getOutputImgPath(sourceImgPath, String.valueOf(quality));
            BufferedImage bufferedImage = Thumbnails.of(sourceImgPath).scale(1f).asBufferedImage();
            String width = ObjectUtils.toString(bufferedImage.getWidth());
            String height = ObjectUtils.toString(bufferedImage.getHeight());
            Thumbnails.of(bufferedImage).scale(1f).outputQuality(quality).outputFormat(IMG_FORMAT)
                    .toFile(outputImgPath);
            return Triplet.with(outputImgPath, width, height);
        } catch (Exception e) {
            log.error("compressByQuality error sourceImgPath:{},quality:{}"+ sourceImgPath, e);
        }
        return null;
    }

    /**
     * 按照指定尺寸压缩图片
     *
     * @param sourceImgPath 原图文件路径
     * @param width 图片宽度
     * @param height 图片高度
     * @return outputImgPath
     */
    public static String compressBySize(String sourceImgPath, int width, int height) {
        String outputImgPath = "";
        try {
            outputImgPath = getOutputImgPath(sourceImgPath, "cs" + width + "x" + height);
            Thumbnails.of(sourceImgPath).size(width, height).outputFormat(IMG_FORMAT).toFile(outputImgPath);
        } catch (Exception e) {
            log.error("compressBySize error,sourceImgPath:{},width:{},height:{}"+sourceImgPath, e);
        }
        return outputImgPath;
    }

    /**
     * 按照指定尺寸裁剪图片
     *
     * @param sourceImgPath 原图文件路径
     * @param width 图片宽度
     * @param height 图片高度
     * @return outputImgPath
     */
    public static String cropBySize(String sourceImgPath, int width, int height) {
        String outputImgPath = "";
        try {
            outputImgPath = getOutputImgPath(sourceImgPath, "cp" + width + "x" + height);
            Thumbnails.of(sourceImgPath).sourceRegion(Positions.CENTER, width, height).size(width, height)
                    .outputFormat(IMG_FORMAT).toFile(outputImgPath);
        } catch (Exception e) {
            log.error("cropBySize error,sourceImgPath:{},width:{},height:{}" + sourceImgPath, e);
        }
        return outputImgPath;
    }

    private static String getOutputImgPath(String sourceImgPath, String extraStr) {
        return sourceImgPath.substring(0, sourceImgPath.lastIndexOf(".")) + "_" + extraStr + "." + IMG_FORMAT;
    }

    /**
     * 给图片添加水印
     *
     * @param sourceImgPath 原图本地路径
     * @param quality 压缩后的图片质量比例(0.1f-1.0f)
     * @param width 原图宽度
     * @return outputImgPath
     */
/*    public static String addWaterMark(String sourceImgPath, float quality, int width) {
        String outputImgPath = "";
        try {
            File waterMarkFile;
            if (width < 658) {
                waterMarkFile = waterMarkSmall;
            } else if (width < 2560) {
                waterMarkFile = waterMarkMedium;
            } else {
                waterMarkFile = waterMarkLarge;
            }
            outputImgPath = getOutputImgPath(sourceImgPath, "wm");
            Thumbnails.of(sourceImgPath).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(waterMarkFile), 0.5f)
                    .outputQuality(quality).scale(1).outputFormat(IMG_FORMAT).toFile(outputImgPath);
        } catch (Exception e) {
            log.error("addWaterMark error,sourceImgPath:{},waterMarkImgPath:{}"+sourceImgPath, e);
        }
        return outputImgPath;
    }*/

    /**
     * 获取本地图片宽度、高度
     *
     * @param imgFilePath 图片文件路径
     * @return A:宽度、B:高度
     */
    public static Pair<String, String> getImageWidthHeight(String imgFilePath) {
        ImageInputStream iis = null;
        try {
            iis = ImageIO.createImageInputStream(new File(imgFilePath));
            return readImageWidthHeight(iis);
        } catch (Exception e) {
            log.error("getImageWidthHeight error imgFilePath:{}"+imgFilePath, e);
        } finally {
            IOUtils.closeQuietly(iis);
        }
        return null;
    }

    private static Pair<String, String> readImageWidthHeight(ImageInputStream iis) throws Exception {
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(iis);
        if (iterator.hasNext()) {
            ImageReader reader = iterator.next();
            reader.setInput(iis, true);
            String width = ObjectUtils.toString(reader.getWidth(0));
            String height = ObjectUtils.toString(reader.getHeight(0));
            return Pair.with(width, height);
        }
        return null;
    }

    /**
     * 获取远程URL图片宽度、高度
     *
     * @param imgUrl 远程图片URL
     * @return A:宽度、B:高度
     */
    public static Pair<String, String> getImageWidthHeightByUrl(String imgUrl) {
        URLConnection conn = null;
        ImageInputStream iis = null;
        try {
            URL url = new URL(imgUrl);
            conn = url.openConnection();
            iis = ImageIO.createImageInputStream(conn.getInputStream());
            return readImageWidthHeight(iis);
        } catch (Exception e) {
            log.error("getImageWidthHeightByUrl error imgUrl:{}"+ imgUrl, e);
        } finally {
            IOUtils.closeQuietly(iis);
            if (conn != null) {
                IOUtils.close(conn);
            }
        }
        return null;
    }
}
