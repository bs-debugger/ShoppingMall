package com.xq.live.web.controllerForManage;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Attachment;
import com.xq.live.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
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

/**
 * Created by admin on 2019/4/24.
 */
@RestController
@Api(value = "UploadForManage", tags = "材料上传")
@RequestMapping("/manage/img")
public class UploadForManageController {
    private static Logger logger = Logger.getLogger(UploadForManageController.class);

    @Autowired
    private UploadService uploadService;



    /**
     * 单个文件上传
     *
     * @param uploadFile
     * @param request
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("单个文件上传")
    public BaseResp<Attachment> upload(@RequestParam("file") MultipartFile uploadFile, HttpServletRequest request) {
        if (uploadFile.isEmpty()) {
            return new BaseResp<Attachment>(ResultStatus.error_file_upload_empty);
        }

        try {
            Attachment result = this.uploadToCos(uploadFile, this.getUploadPath(request), "manage");
            return new BaseResp<Attachment>(ResultStatus.SUCCESS, result);
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResp<Attachment>(ResultStatus.error_file_upload_error);
        }
    }

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
}
