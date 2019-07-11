package com.xq.live.common;

/**
 * Created by ss on 2018/6/2.
 */

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;
import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class ShopCodeUtil {

    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;

    /**
     * 生成二维码的方法
     *
     * @param content      目标URL
     * @param imgPath      LOGO图片地址
     * @param needCompress 是否压缩LOGO
     * @return 二维码图片
     * @throws Exception
     */
    private static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
        // 插入图片
        ShopCodeUtil.insertImage(image, imgPath, needCompress);
        return image;
    }


    /**
     * 插入LOGO
     *
     * @param source       二维码图片
     * @param imgPath      LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param destPath     存放目录
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {

        try {
            BufferedImage biQR = ShopCodeUtil.createImage(content, imgPath, needCompress);//输出二维码到缓冲区
            String bgroup = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "bdFile.jpg";
            File bgFile = new File(bgroup);
            File logoFile = new File(imgPath);
            if (!bgFile.exists()) {
                System.err.println("" + bgroup + "   该文件不存在！");
                return;
            }
            Image bgSrc = ImageIO.read(bgFile);
            Image logoSrc = ImageIO.read(logoFile);
            int width = bgSrc.getWidth(null);
            int height = bgSrc.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(bgSrc, 0, 0, width, height, null);

            //g.drawImage(logoSrc, 277, 283, 162,162, null);    // 绘制微信头像logo
            //参数分别为image，x，y, width, height，  x表示离背景图左边框距离，y表示离上边边框距离，w和h表示二维码大小，大致是这样
            g.drawImage(biQR, 118, 362, 590, 590, null);// 绘制二维码
            /*g.setColor(Color.BLACK);
            Font f = new Font("宋体", Font.BOLD, 30);//bold表示粗体，后面45是字体大小
            Color mycolor = new Color(136, 135, 135);//new Color(0, 0, 255);
            g.setColor(mycolor);
            g.setFont(f);
            //g.drawString("享七支付", 260, 840);
            g.drawString(("我是某某"),360,210);
            g.drawString("我为享七代言",360,260);*/
            g.dispose();// 绘制背景图
            /*g.setColor(Color.BLACK);
            Font f = new Font("宋体", Font.BOLD, 45);//bold表示粗体，后面45是字体大小
            Color mycolor = new Color(136, 135, 135);//new Color(0, 0, 255);
            g.setColor(mycolor);
            g.setFont(f);
            g.drawString("享七支付", 260, 840);
            //           g.drawString(("我是"+xm),360,210);
            //           g.drawString("我为享七代言",360,260);
            g.dispose();*/

           /* FileOutputStream out = new FileOutputStream(outputPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();*/
            mkdirs(destPath);
            ImageIO.write(image, FORMAT_NAME, new File(destPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String file = new Random().nextInt(99999999) + ".jpg";
//        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
    }

    /**
     * 生成二维码(内嵌LOGO)(专门为活动选手分享的)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param destPath     存放目录
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encodeForAct(String content, String imgPath, String destPath, boolean needCompress) throws Exception {

        try {
            BufferedImage biQR = ShopCodeUtil.createImage(content, imgPath, needCompress);//输出二维码到缓冲区
            String bgroup = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "share.jpg";
            File bgFile = new File(bgroup);
            File logoFile = new File(imgPath);
            if (!bgFile.exists()) {
                System.err.println("" + bgroup + "   该文件不存在！");
                return;
            }
            Image bgSrc = ImageIO.read(bgFile);
            Image logoSrc = ImageIO.read(logoFile);
            int width = bgSrc.getWidth(null);
            int height = bgSrc.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(bgSrc, 0, 0, width, height, null);

            //g.drawImage(logoSrc, 277, 283, 162,162, null);    // 绘制微信头像logo
            //参数分别为image，x，y, width, height，  x表示离背景图左边框距离，y表示离上边边框距离，w和h表示二维码大小，大致是这样
            g.drawImage(biQR, 118, 362, 590, 590, null);// 绘制二维码
            /*g.setColor(Color.BLACK);
            Font f = new Font("宋体", Font.BOLD, 30);//bold表示粗体，后面45是字体大小
            Color mycolor = new Color(136, 135, 135);//new Color(0, 0, 255);
            g.setColor(mycolor);
            g.setFont(f);
            //g.drawString("享七支付", 260, 840);
            g.drawString(("我是某某"),360,210);
            g.drawString("我为享七代言",360,260);*/
            g.dispose();// 绘制背景图
            /*g.setColor(Color.BLACK);
            Font f = new Font("宋体", Font.BOLD, 45);//bold表示粗体，后面45是字体大小
            Color mycolor = new Color(136, 135, 135);//new Color(0, 0, 255);
            g.setColor(mycolor);
            g.setFont(f);
            g.drawString("享七支付", 260, 840);
            //           g.drawString(("我是"+xm),360,210);
            //           g.drawString("我为享七代言",360,260);
            g.dispose();*/

           /* FileOutputStream out = new FileOutputStream(outputPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();*/
            mkdirs(destPath);
            ImageIO.write(image, FORMAT_NAME, new File(destPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String file = new Random().nextInt(99999999) + ".jpg";
//        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
    }

    /**
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath 存放目录
     */
    public static void mkdirs(String destPath) {
//        File file = new File(destPath);
//        if (!file.exists() && !file.isDirectory()) {
//            file.mkdirs();
//        }
        File file = new File(destPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content  内容
     * @param imgPath  LOGO地址
     * @param destPath 存储地址
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath) throws Exception {
        ShopCodeUtil.encode(content, imgPath, destPath, false);
    }

    /**
     * 生成二维码
     *
     * @param content      内容
     * @param destPath     存储地址
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String destPath, boolean needCompress) throws Exception {
        ShopCodeUtil.encode(content, null, destPath, needCompress);
    }



    /**
     * 生成二维码
     *
     * @param content  内容
     * @param destPath 存储地址
     * @throws Exception
     */
    public static void encode(String content, String destPath) throws Exception {
        ShopCodeUtil.encode(content, null, destPath, false);
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param output       输出流
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = ShopCodeUtil.createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param output  输出流
     * @throws Exception
     */
    public static void encode(String content, OutputStream output) throws Exception {
        ShopCodeUtil.encode(content, null, output, false);
    }

    /**
     * 解析二维码
     *
     * @param file 二维码图片
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 解析二维码
     *
     * @param path 二维码图片地址
     * @return 不是二维码的内容返回null, 是二维码直接返回识别的结果
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return ShopCodeUtil.decode(new File(path));
    }

    public static void main(String[] args) {

        // 生成二维码
        String text = "http://www.hbxq001.cn/shop/top";
        String imagePath = System.getProperty("user.dir") + "/data/1.png";
        String destPath = System.getProperty("user.dir") + "/data/output/";
        try {
            ShopCodeUtil.encode(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //验证图片是否含有二维码
        //        String destPath1 = System.getProperty("user.dir") + "/data/3.jpg";
        //        try {
        //            String result = decode(destPath1);
        //            System.out.println(result);
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //            System.out.println(destPath1 + "不是二维码");
        //        }
    }
}
