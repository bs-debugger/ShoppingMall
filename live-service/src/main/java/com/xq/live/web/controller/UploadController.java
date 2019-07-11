package com.xq.live.web.controller;

import com.xq.live.common.*;
import com.xq.live.config.ConstantsConfig;
import com.xq.live.model.Attachment;
import com.xq.live.model.User;
import com.xq.live.service.UploadService;
import com.xq.live.vo.in.CouponInVo;
import com.xq.live.web.utils.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片上传controller
 *
 * @author zhangpeng32
 * @date 2018-02-13 18:53
 * @copyright:hbxq
 **/
@RestController
@RequestMapping("/img")
public class UploadController {
    private static Logger logger = Logger.getLogger(UploadController.class);

    @Autowired
    private UploadService uploadService;

    @Autowired
    private ConstantsConfig constantsConfig;

    /**
     * 单个文件上传
     *
     * @param uploadFile
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResp<Attachment> upload(@RequestParam("file") MultipartFile uploadFile, HttpServletRequest request) {
        User user = UserContext.getUserSession();
        if (user == null || StringUtils.isEmpty(user.getUserName())) {
            return new BaseResp<Attachment>(ResultStatus.error_para_user_empty);
        }

        if (uploadFile.isEmpty()) {
            return new BaseResp<Attachment>(ResultStatus.error_file_upload_empty);
        }

        try {
            Attachment result = this.uploadToCos(uploadFile, this.getUploadPath(request), user.getUserName());
            return new BaseResp<Attachment>(ResultStatus.SUCCESS, result);
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResp<Attachment>(ResultStatus.error_file_upload_error);
        }
    }

    /**
     * 上传多个图片(不压缩比例)
     *
     * @param uploadfiles
     * @param request
     * @return
     */
    @PostMapping("/uploadForNew/multi")
    public BaseResp<List<Attachment>> uploadForNew(@RequestParam("file") MultipartFile[] uploadfiles, HttpServletRequest request) {
        User user = UserContext.getUserSession();
        if (user == null || StringUtils.isEmpty(user.getUserName())) {
            return new BaseResp<List<Attachment>>(ResultStatus.error_para_user_empty);
        }

        if (uploadfiles.length == 0) {
            return new BaseResp<List<Attachment>>(ResultStatus.error_file_upload_empty);
        }
        List<Attachment> result = new ArrayList<Attachment>();
        try {
            for (MultipartFile uploadFile : uploadfiles) {
                Attachment p = this.uploadToCosForNew(uploadFile, this.getUploadPath(request), user.getUserName());
                result.add(p);
            }
            return new BaseResp<List<Attachment>>(ResultStatus.SUCCESS, result);
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResp<List<Attachment>>(ResultStatus.error_file_upload_error);
        }
    }

    /**
     * 上传多个图片(压缩比例) 裁剪用
     * @param uploadfiles
     * @param request
     * @return
     */
    @PostMapping("/uploadForNew/multiCJ")
    public BaseResp<List<Attachment>> uploadForNewCj(@RequestParam("file") MultipartFile[] uploadfiles, HttpServletRequest request) {
        User user = UserContext.getUserSession();
        if (user == null || StringUtils.isEmpty(user.getUserName())) {
            return new BaseResp<List<Attachment>>(ResultStatus.error_para_user_empty);
        }

        if (uploadfiles.length == 0) {
            return new BaseResp<List<Attachment>>(ResultStatus.error_file_upload_empty);
        }
        if (uploadfiles.length >2) {
            return new BaseResp<List<Attachment>>(ResultStatus.error_file_upload_length);
        }
        List<Attachment> result = new ArrayList<Attachment>();
        try {
            for (MultipartFile uploadFile : uploadfiles) {
                Attachment p = this.uploadToCosForNewCJ(uploadFile, this.getUploadPath(request), user.getUserName());
                result.add(p);
            }
            return new BaseResp<List<Attachment>>(ResultStatus.SUCCESS, result);
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResp<List<Attachment>>(ResultStatus.error_file_upload_error);
        }
    }


    /**
     * 单个视频上传
     *
     * @param uploadFile
     * @param request
     * @return
     */
    @PostMapping("/uploadMp4")
    public BaseResp<Attachment> uploadMp4(@RequestParam("file") MultipartFile uploadFile, HttpServletRequest request) {
        User user = UserContext.getUserSession();
        if (user == null || StringUtils.isEmpty(user.getUserName())) {
            return new BaseResp<Attachment>(ResultStatus.error_para_user_empty);
        }

        if (uploadFile.isEmpty()) {
            return new BaseResp<Attachment>(ResultStatus.error_file_upload_empty);
        }

        try {
            Attachment result = this.uploadToCosForMp4(uploadFile, this.getUploadPath(request), user.getUserName());
            return new BaseResp<Attachment>(ResultStatus.SUCCESS, result);
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResp<Attachment>(ResultStatus.error_file_upload_error);
        }
    }


    /**
     * 上传多个图片
     *
     * @param uploadfiles
     * @param request
     * @return
     */
    @PostMapping("/upload/multi")
    public BaseResp<List<Attachment>> upload(@RequestParam("file") MultipartFile[] uploadfiles, HttpServletRequest request) {
        User user = UserContext.getUserSession();
        if (user == null || StringUtils.isEmpty(user.getUserName())) {
            return new BaseResp<List<Attachment>>(ResultStatus.error_para_user_empty);
        }

        if (uploadfiles.length == 0) {
            return new BaseResp<List<Attachment>>(ResultStatus.error_file_upload_empty);
        }
        List<Attachment> result = new ArrayList<Attachment>();
        try {
            for (MultipartFile uploadFile : uploadfiles) {
                Attachment p = this.uploadToCos(uploadFile, this.getUploadPath(request), user.getUserName());
                result.add(p);
            }
            return new BaseResp<List<Attachment>>(ResultStatus.SUCCESS, result);
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResp<List<Attachment>>(ResultStatus.error_file_upload_error);
        }
    }

    @PostMapping("/qrcode")
    public BaseResp<String> upload(CouponInVo inVo, HttpServletRequest request) {
        if (inVo == null || inVo.getId() == null || StringUtils.isEmpty(inVo.getCouponCode())) {
            return new BaseResp<String>(ResultStatus.error_param_empty);
        }
        String imagePath = this.getImagePath(request) + "logo.jpg";
        String destPath = this.getUploadPath(request) + inVo.getCouponCode() + ".jpg";
        String text = constantsConfig.getDomainXqUrl() + "/cp/get/" + inVo.getId();

        //生成logo图片到destPath
        try {
            QRCodeUtil.encode(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadService.uploadFileToCos(destPath, "coupon");
        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        }

        //删除服务器上临时文件
        uploadService.deleteTempImage(new Triplet<String, String, String>(destPath, null, null));
        return new BaseResp<String>(ResultStatus.SUCCESS, imgUrl);
    }


    /**
     * 上传文件到临时目录，并上传到云COS
     *
     * @param file
     * @param uploadPath
     * @param userName
     * @return
     * @throws IOException
     */
    private Attachment uploadToCos(MultipartFile file, String uploadPath, String userName) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        String localPath = uploadPath + file.getOriginalFilename();
        byte[] bytes = file.getBytes();
        Path path = Paths.get(localPath);
        if (!path.toFile().getParentFile().exists()) {
            path.toFile().getParentFile().mkdirs();
        }
        Files.write(path, bytes);
        logger.error("图片上传到服务器成功：" + localPath);

        Attachment result = uploadService.uploadPicToCos(localPath, userName);
        if (result != null && result.getId() != null) {
            return result;
        }
        return null;
    }

    /**
     * 上传文件到临时目录，并上传到云COS
     *
     * @param file
     * @param uploadPath
     * @param userName
     * @return
     * @throws IOException
     */
    private Attachment uploadToCosForNew(MultipartFile file, String uploadPath, String userName) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        String localPath = uploadPath + file.getOriginalFilename();
        byte[] bytes = file.getBytes();
        Path path = Paths.get(localPath);
        if (!path.toFile().getParentFile().exists()) {
            path.toFile().getParentFile().mkdirs();
        }
        Files.write(path, bytes);
        logger.error("图片上传到服务器成功：" + localPath);

        Attachment result = uploadService.uploadPicToCosForNew(localPath, userName);
        if (result != null && result.getId() != null) {
            return result;
        }
        return null;
    }

    /**
     * 上传文件到临时目录，并上传到云COS 裁剪用
     *
     * @param file
     * @param uploadPath
     * @param userName
     * @return
     * @throws IOException
     */
    private Attachment uploadToCosForNewCJ(MultipartFile file, String uploadPath, String userName) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        //Long timeStamp = System.currentTimeMillis() / 1000;
        String localPath = uploadPath + file.getOriginalFilename();
        byte[] bytes = file.getBytes();
        Path path = Paths.get(localPath);
        if (!path.toFile().getParentFile().exists()) {
            path.toFile().getParentFile().mkdirs();
        }
        Files.write(path, bytes);
        logger.error("图片上传到服务器成功：" + localPath);
        Attachment result = uploadService.uploadPicToCosCJ(localPath, userName);
        if (result != null && result.getId() != null) {
            return result;
        }
        return null;
    }

    /**
     * 上传文件到临时目录，并上传到云COS(针对视频)
     *
     * @param file
     * @param uploadPath
     * @param userName
     * @return
     * @throws IOException
     */
    private Attachment uploadToCosForMp4(MultipartFile file, String uploadPath, String userName) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        String localPath = uploadPath + file.getOriginalFilename();
        byte[] bytes = file.getBytes();
        Path path = Paths.get(localPath);
        if (!path.toFile().getParentFile().exists()) {
            path.toFile().getParentFile().mkdirs();
        }
        Files.write(path, bytes);
        logger.error("视频上传到服务器成功：" + localPath);

        Attachment result = uploadService.uploadPicToCosForMp4(localPath, userName);
        if (result != null && result.getId() != null) {
            return result;
        }
        return null;
    }
/*

    */
    /**
     * 上传文件到腾讯云cos
     *
     * @param localPath
     * @return
     *//*

    public String uploadFileToCos(String localPath, String userName) {
        String cosPath = null;
        // 获取文件名
        File file = new File(localPath);
        String API_KEY = "/" + userName + "_" + file.getName();
        try {
            PutObjectResult putObjectResult = cosClient.putObject(bucketName, API_KEY, file);
            String etag = putObjectResult.getETag();
            if (StringUtils.isNotEmpty(etag)) {
                cosPath = Constants.COS_IMAGE_BASE_PATH + API_KEY;
                return cosPath;
            }
        } catch (CosServiceException e) {
            logger.error("上传图片到cos异常CosServiceException ：" + e.getErrorMessage());
        } catch (CosClientException e1) {
            logger.error("上传图片到cos异常CosClientException ：" + e1.getStackTrace());
        }
        return null;
    }


    */
    /**
     * 删除临时文件
     *
     * @param triplet
     * @return
     *//*

    private boolean d   eleteTempImage(Triplet<String, String, String> triplet) {
        try {
            for (Object tl : triplet) {
                if (tl == null) {
                    continue;
                }
                File file = new File(tl.toString());
                if (file.exists()) {
                    file.delete();
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("删除文件异常");
        }
        return false;
    }
*/

    /**
     * 获取上传图片临时目录
     *
     * @param request
     * @return
     */
    public String getUploadPath(HttpServletRequest request) {
        //        return Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator;
        return request.getSession().getServletContext().getRealPath("") + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "upload" + File.separator;
    }

    /**
     * 本地图片路径
     *
     * @param request
     * @return
     */
    public String getImagePath(HttpServletRequest request) {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator;
    }

    /*@RequestMapping(value = "/testDownload", method = RequestMethod.GET)
    public void testDownload(HttpServletResponse res) {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() +"static"+File.separator+ "upload" +  File.separator + "H567B9652_0702190916.apk";
        String fileName = "H567B9652_0702190916.apk";
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(path)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("success");
    }

    //文件下载相关代码
    @RequestMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response){
        String fileName = "H567B9652_0702190916.apk";// 设置文件名，根据业务需要替换成要下载的文件名
        if (fileName != null) {
            //设置文件路径
            String realPath1 = request.getSession().getServletContext().getRealPath("");
            String realPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() +"static"+File.separator+ "upload" + File.separator;
            File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    *//**
     * @功能 下载临时素材接口
     * @return
     *//*

    @RequestMapping("/download1")
    public File saveUrlAs(){
        String  url = "https://xqmp4-1256079679.file.myqcloud.com/xiangqi_H567B9652_0702190916.apk";
        String  filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() +"static"+File.separator+ "upload" +  File.separator +"xiangqi_H567B9652_0702190916.apk";
        String method = "GET";
        //System.out.println("fileName---->"+filePath);
        //创建不同的文件夹目录
        File file=new File(filePath);
        //判断文件夹是否存在
        if (!file.exists())
        {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try
        {
            // 建立链接
            URL httpUrl=new URL(url);
            conn=(HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream=conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {

                filePath += "/";

            }
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(filePath+"123.png");
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while(length != -1)
            {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }

        return file;

    }*/


}
