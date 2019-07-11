package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Constants;
import com.xq.live.common.QRCodeUtil;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.ConstantsConfig;
import com.xq.live.model.Attachment;
import com.xq.live.model.User;
import com.xq.live.service.UploadService;
import com.xq.live.vo.in.CouponInVo;
import com.xq.live.web.utils.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片上传controller
 *
 * @author lipeng
 * @date 2018-02-13 18:53
 * @copyright:hbxq
 **/
@RestController
@RequestMapping("/app/img")
public class UploadForAppController {
    private static Logger logger = Logger.getLogger(UploadForAppController.class);

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

}
