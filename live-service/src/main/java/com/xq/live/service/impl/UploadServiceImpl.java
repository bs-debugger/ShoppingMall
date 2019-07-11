package com.xq.live.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.xq.live.common.Constants;
import com.xq.live.common.ImageUtil;
import com.xq.live.common.VideoImage;
import com.xq.live.dao.AttachmentMapper;
import com.xq.live.model.Attachment;
import com.xq.live.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bytedeco.javacv.FrameGrabber;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-28 21:38
 * @copyright:hbxq
 **/
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private AttachmentMapper attachmentMapper;


    private static Logger logger = Logger.getLogger(UploadServiceImpl.class);

    // 1 初始化用户身份信息(secretId, secretKey)
    private COSCredentials cred = new BasicCOSCredentials(Constants.ACCESS_KEY, Constants.SECRET_KEY);
    // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
    private ClientConfig clientConfig = new ClientConfig(new Region(Constants.REGION_NAME));
    // 3 生成cos客户端
    private COSClient cosClient = new COSClient(cred, clientConfig);

    private final String bucketName = Constants.BUCKET_NAME;

    private final String bucketMp4Name = Constants.BUCKET_MP4_NAME;

    @Override
    public Attachment uploadPicToCos(String localPath,  String name){
        Attachment attachment = new Attachment();
        //图片压缩
        String sourceImgPath = ImageUtil.compressByQuality(localPath, 0.8f);
        String smallImgPath = ImageUtil.compressByQuality(localPath, 0.3f);
        String sourceImgRet = this.uploadFileToCos(sourceImgPath, name);      //上传原图到cos，压缩比例0.8
        if(StringUtils.isNotEmpty(sourceImgRet)){
            attachment.setPicUrl(sourceImgRet);
            String smallImgRet = this.uploadFileToCos(smallImgPath, name);      //上传原图到cos，压缩比例0.3
            if(StringUtils.isNotEmpty(smallImgRet)){
                attachment.setSmallPicUrl(smallImgRet);
                //图片都上传成功了，写入attachment文件附件表
                this.insertAttachment(attachment);
            }
        }
        /**
         * 删除服务器上的临时图片文件
         */
        this.deleteTempImage(new Triplet<String, String, String>(localPath, sourceImgPath, smallImgPath));
        return attachment;
    }

    /**
     * 上传文件到COS服务器，返回附件表对象 裁剪用
     * @param localPath
     * @param name
     * @return
     */
    @Override
    public Attachment uploadPicToCosCJ(String localPath, String name) {

        Attachment attachment = new Attachment();
        //图片压缩
        String sourceImgPath = ImageUtil.compressByQuality(localPath, 0.8f);
        String smallImgPath = ImageUtil.compressByQuality(localPath, 0.3f);
        String sourceImgRet = this.uploadFileToCos(sourceImgPath, name);      //上传原图到cos，压缩比例0.8
        if(StringUtils.isNotEmpty(sourceImgRet)){
            attachment.setPicUrl(sourceImgRet);
            String smallImgRet = this.uploadFileToCos(smallImgPath, name);      //上传原图到cos，压缩比例0.3
            if(StringUtils.isNotEmpty(smallImgRet)){
                attachment.setSmallPicUrl(smallImgRet);
                //图片都上传成功了，写入attachment文件附件表
                this.insertAttachment(attachment);
            }
        }
        /**
         * 删除服务器上的临时图片文件
         */
        this.deleteTempImage(new Triplet<String, String, String>(localPath, sourceImgPath, null));
        return attachment;
    }



    @Override
    public Attachment uploadPicToCosForNew(String localPath, String name) {
        Attachment attachment = new Attachment();
        //图片压缩
        String sourceImgPath = ImageUtil.compressByQuality(localPath, 1.0f);
        String smallImgPath = ImageUtil.compressByQuality(localPath, 0.3f);
        String sourceImgRet = this.uploadFileToCos(sourceImgPath, name);      //上传原图到cos，压缩比例0.8
        if(StringUtils.isNotEmpty(sourceImgRet)){
            attachment.setPicUrl(sourceImgRet);
            String smallImgRet = this.uploadFileToCos(smallImgPath, name);      //上传原图到cos，压缩比例0.3
            if(StringUtils.isNotEmpty(smallImgRet)){
                attachment.setSmallPicUrl(smallImgRet);
                //图片都上传成功了，写入attachment文件附件表
                this.insertAttachment(attachment);
            }
        }
        /**
         * 删除服务器上的临时图片文件
         */
        this.deleteTempImage(new Triplet<String, String, String>(localPath, sourceImgPath, smallImgPath));
        return attachment;
    }

    @Override
    public Attachment uploadPicToCosForMp4(String localPath,  String name){
        Attachment attachment = new Attachment();
        String sourceImgRet = this.uploadFileToCosForMp4(localPath, name);
        String targerPath = null;
        try {
           targerPath = VideoImage.randomGrabberFFmpegImage(localPath, 5);
            String smallPicUrl = this.uploadFileToCos(targerPath, name);
            if(StringUtils.isNotEmpty(smallPicUrl)){
                attachment.setSmallPicUrl(smallPicUrl);
            }
        } catch (FrameGrabber.Exception e) {
            logger.error("截取视频帧数异常 ：" + e.getMessage());
        }
        if(StringUtils.isNotEmpty(sourceImgRet)){
            attachment.setPicUrl(sourceImgRet);
            this.insertAttachment(attachment);
        }
        /**
         * 删除服务器上的临时图片文件
         */
        this.deleteTempImage(new Triplet<String, String, String>(localPath, localPath, targerPath));
        return attachment;
    }

    @Override
    public String uploadFileToCos(String localPath,  String userName) {
        Long timeStamp = System.currentTimeMillis() / 1000;
        String cosPath = null;
        // 获取文件名
        File file = new File(localPath);
        String key = "/" + userName + "_" +timeStamp.toString() + "_" + file.getName();
        try {
            PutObjectResult putObjectResult = cosClient.putObject(bucketName, key, file);
            String etag = putObjectResult.getETag();
            if (StringUtils.isNotEmpty(etag)) {
                cosPath = Constants.COS_IMAGE_BASE_PATH + key;
                return cosPath;
            }
        } catch (CosServiceException e) {
            logger.error("上传图片到cos异常CosServiceException ：" + e.getErrorMessage());
        } catch (CosClientException e1) {
            logger.error("上传图片到cos异常CosClientException ：" + e1.getStackTrace());
        }
        return null;
    }

    public String uploadCodeToCos(String localPath,  String userName) {
        String cosPath = null;
        // 获取文件名
        File file = new File(localPath);
        String key = "/" + userName + "_" + file.getName();
        try {
            PutObjectResult putObjectResult = cosClient.putObject(bucketName, key, file);
            String etag = putObjectResult.getETag();
            if (StringUtils.isNotEmpty(etag)) {
                cosPath = Constants.COS_IMAGE_BASE_PATH + key;
                return cosPath;
            }
        } catch (CosServiceException e) {
            logger.error("上传图片到cos异常CosServiceException ：" + e.getErrorMessage());
        } catch (CosClientException e1) {
            logger.error("上传图片到cos异常CosClientException ：" + e1.getStackTrace());
        }
        return null;
    }

    @Override
    public String uploadFileToCosForMp4(String localPath,  String userName) {
        Long timeStamp = System.currentTimeMillis() / 1000;
        String cosPath = null;
        // 获取文件名
        File file = new File(localPath);
        String key = "/" + userName + "_" +timeStamp.toString() + "_" + file.getName();
        try {
            PutObjectResult putObjectResult = cosClient.putObject(bucketMp4Name, key, file);
            String etag = putObjectResult.getETag();
            if (StringUtils.isNotEmpty(etag)) {
                cosPath = Constants.COS_MP4_BASE_PATH + key;
                return cosPath;
            }
        } catch (CosServiceException e) {
            logger.error("上传视频到cos异常CosServiceException ：" + e.getErrorMessage());
        } catch (CosClientException e1) {
            logger.error("上传视频到cos异常CosClientException ：" + e1.getStackTrace());
        }
        return null;
    }

    @Override
    public boolean deleteTempImage(Triplet<String, String, String> triplet) {
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

    /**
     * 写入数据到attachment表
     * @param attachment
     * @return
     */
    @Override
    public Long insertAttachment(Attachment attachment){
        int ret = attachmentMapper.insert(attachment);
        if(ret > 0){
            return attachment.getId();
        }
        return null;
    }

}
