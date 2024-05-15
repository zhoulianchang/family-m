package com.zlc.family.common.third.oss;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author zlc
 * @date 2022/7/29 10:31 上午
 * oss 标准api接口
 */
public interface OssClient {
    /**
     * 上传文件-自定义路径
     * fileName中包含桶名称
     *
     * @param file     上传文件
     * @param fileName 上传至OSS的文件完整路径，例：cf/abc.png
     *                 上传至根目录，例：abc.png
     * @return
     */
    public OssResult uploadFile(File file, String fileName);

    /**
     * 上传文件-自定义路径
     * fileName中包含桶名称
     *
     * @param is       文件输入流
     * @param fileName 上传至OSS的文件完整路径，例：cf/abc.png
     *                 上传至根目录，例：abc.png
     * @return
     */
    public OssResult uploadFile(InputStream is, String fileName);

    /**
     * 上传文件-自定义路径+桶名
     *
     * @param file          上传文件
     * @param fileName      上传至OSS的文件完整路径，例：cf/abc.png
     *                      上传至根目录，例：abc.png
     * @param bucketName    桶名
     * @param limitFileSize 是否开启文件大小限制
     * @return
     */
    public OssResult uploadFile(File file, String fileName, String bucketName, Boolean limitFileSize);

    /**
     * 上传文件-自定义路径+桶名
     *
     * @param file          上传文件
     * @param fileName      上传至OSS的文件完整路径，例：cf/abc.png
     *                      上传至根目录，例：abc.png
     * @param bucketName    桶名
     * @param limitFileSize 是否开启文件大小限制
     * @return
     */
    public OssResult uploadFile(InputStream is, String fileName, String bucketName, Boolean limitFileSize);

    /**
     * 通过文件名下载文件
     *
     * @param fileName      要下载的文件名（OSS服务器上的）
     *                      例如：4DB049D0604047989183CB68D76E969D.jpg
     * @param localFileName 本地要创建的文件名（下载到本地的）
     *                      例如：C:\Users\Administrator\Desktop\test.jpg
     */
    public boolean downloadFile(String fileName, String localFileName);

    /**
     * 根据文件名删除文件
     *
     * @param fileName 需要删除的文件名
     * @return boolean 是否删除成功
     * 例如：4DB049D0604047989183CB68D76E969D.jpg
     */
    public boolean deleteFile(String fileName);

    /**
     * 列举共同前缀的所有文件url(非必要实现)
     *
     * @param prefix 列举的前缀
     * @return 返回文件对象的集合
     */
    public List<String> listObjects(String prefix);
}
