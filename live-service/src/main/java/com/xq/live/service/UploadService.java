package com.xq.live.service;

import com.xq.live.model.Attachment;
import org.javatuples.Triplet;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-28 21:36
 * @copyright:hbxq
 **/
public interface UploadService {
    /**
     * 上传文件到COS服务器，返回图片url
     * @param localPath
     * @param userName
     * @return
     */
    public String uploadFileToCos(String localPath, String userName);

    /**
     * 上传文件到COS服务器，返回视频url
     * @param localPath
     * @param userName
     * @return
     */
    public String uploadFileToCosForMp4(String localPath, String userName);

    /**
     * 删除服务器图片
     * @param triplet
     * @return
     */
    public boolean deleteTempImage(Triplet<String, String, String> triplet);


    /**
     * 新增数据到图片附件表
     * @param attachment
     * @return
     */
    public Long insertAttachment(Attachment attachment);

    /**
     * 上传文件到COS服务器，返回附件表对象
     * @param localPath
     * @param name
     * @return
     */
    public Attachment uploadPicToCos(String localPath,  String name);

    /**
     * 上传文件到COS服务器，返回附件表对象
     * @param localPath
     * @param name
     * @return
     */
    public Attachment uploadPicToCosCJ(String localPath,  String name);


    /**
     * 上传文件到COS服务器，返回附件表对象
     * @param localPath
     * @param name
     * @return
     */
    public Attachment uploadPicToCosForNew(String localPath,  String name);

    /**
     * 上传文件到COS服务器，返回附件表对象(针对视频无需压缩)
     * @param localPath
     * @param name
     * @return
     */
    public Attachment uploadPicToCosForMp4(String localPath,  String name);
}
