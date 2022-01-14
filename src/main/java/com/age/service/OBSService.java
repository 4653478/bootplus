package com.age.service;

import com.age.config.ObsConfig;
import com.alibaba.fastjson.JSONObject;
import com.obs.services.ObsClient;
import com.obs.services.internal.ObsProperties;
import com.obs.services.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class OBSService {


    private static final List<String> CONTENT_TYPES = Arrays.asList("image/jpeg", "image/gif","image/png");
    private static final Logger LOGGER = LoggerFactory.getLogger(OBSService.class);

    private static String _endPoint= ObsConfig.EndPoint;

    private static String _ak =ObsConfig.AK;

    private static String _sk =ObsConfig.SK;

    private static String _bucketName =ObsConfig.BucketName;

    private static String _filePath =ObsConfig.FileUploadPath;

    //@Autowired
    //private ObsProperties obsProperties;


    //上传文件，multipartFile就是你要的文件，
    //objectKey就是文件名，如果桶中有文件夹的话，如往test文件上传test.txt文件，那么objectKey就是test/test.txt
    public String uploadFile(MultipartFile file) {
        try{
            String objectKey = _filePath+file.getOriginalFilename();
            ObsClient obsClient = new ObsClient(_ak, _sk, _endPoint);
            InputStream inputStream = file.getInputStream();
            obsClient.putObject(_bucketName, objectKey, inputStream);
            inputStream.close();
            obsClient.close();
            return "ok";
        }catch (Exception ex){
            LOGGER.info("文件上传出错："+ex.toString());
            return "error:"+ex.getMessage();
        }


    }


    public String upload(MultipartFile file)
    {

        String contype=file.getContentType();
        String originalFilename = file.getOriginalFilename();
        String sub=originalFilename.substring(originalFilename.lastIndexOf("."),originalFilename.length());
        LOGGER.info("正上传文件:"+contype+" | "+originalFilename+" | "+sub);
        if(!CONTENT_TYPES.contains(contype))
        {
            LOGGER.info("文件类型出错"+originalFilename);
            return null;
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                LOGGER.info("文件内容不合法" + originalFilename);
                return null;
            }
        }catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("获取文件流失败");
        }
        //上传图片
        InputStream in=null;
        ObsClient obsClient = null;
        try{
            //获取图片名称，作为上传文件名参数
            String objectKey = file.getOriginalFilename();
            //获取流对象
            in= file.getInputStream();
            // 创建ObsClient实例
            obsClient = new ObsClient(_ak, _sk, _endPoint);
            // 使用访问OBS
            PutObjectResult putObjectResult = obsClient.putObject(_bucketName, _filePath+objectKey, in);
            //将图片信息封装起来，方便前端回显调用
            String url=putObjectResult.getObjectUrl();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", objectKey);
            jsonObject.put("url", url);
            return "ok";
        }catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("上传图片失败!");
        }
        finally {
            try
            {
                if(in!=null)
                {
                    in.close();
                }
            }catch (IOException e)
            {
                e.printStackTrace();
                System.err.println("输出流关闭失败！");
            }
            try
            {
                if(obsClient!=null)
                {
                    // 关闭obsClient
                    obsClient.close();
                }
            }catch (IOException e)
            {
                e.printStackTrace();
                System.err.println("obs客户端流对象关闭失败！");
            }

        }
        return null;
    }



}

