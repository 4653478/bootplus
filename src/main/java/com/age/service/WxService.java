package com.age.service;

import cn.hutool.core.date.DateTime;
import com.age.config.WxConfig;
import com.age.entity.TemplateData;
import com.age.entity.WxMssEntity;
import com.age.util.http.HttpClient;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WxService {

    @Resource
    private AhUserService ahUserService;

    private static final Logger logger = LoggerFactory.getLogger(WxService.class);

    public String receiveMessage(Map<String, Object> msg) throws IOException {
        //token
        String access_token=getAccess_token(WxConfig.AppId,WxConfig.AppSecret);
        logger.info("receiveMessage======"+msg.toString());
        //获取用户发送的消息体
        String sMsgType=msg.get("MsgType").toString();//image;text
        String sPicUrl="";
        if(sMsgType=="image") sPicUrl=msg.get("PicUrl").toString();
        String sMsgContent=msg.get("Content").toString();
        String sToUser=msg.get("FromUserName").toString();

        //存储消息数据


        //回复信息
        sendMsToCustomer(access_token,sToUser,sMsgContent);
        return "success";
    }


    /**
     * 发送消息给用户
     * @param toUserName 用户的openId
     */
    private void sendMsToCustomer(String access_token, String toUserName, String msgFrom) throws IOException {

        String msgReply=msgFrom+"?";

        String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+access_token;
        RestTemplate restTemplate = new RestTemplate();
        String messageJson = " {\"touser\":\""+toUserName+"\",\"msgtype\":\"text\",\"text\":{\"content\":\""+msgReply+"\" }} ";//
        JSONObject jsonMsg=JSONObject.parseObject(messageJson);
        logger.info("send jsonMsg======"+jsonMsg.toString());
        String result = restTemplate.postForEntity(url,jsonMsg, String.class).getBody();

        /*HttpClient hc = new HttpClient();
        Map<String, String> postParams=new HashMap<String, String>();
        postParams.put("touser",toUserName);
        postParams.put("msgtype","text");
        postParams.put("text","{\"content\":\"Hello World\" }");
        String result=hc.post(url,postParams).asString();*/


        logger.info("sendMsToCustomer======"+result.toString());
    }

    /*
     * 获取Access_token（拿到的token2小时有效），应该再增加缓存的逻辑避免每次调用获取
     * */
    public String getAccess_token(String appid, String appsecret) throws IOException {
        String url="https://api.weixin.qq.com/cgi-bin/token"+"?appid="+appid+"&secret="+appsecret+"&grant_type=client_credential";
        //String url = Const.ConstInter.GET_TOKEN.replace("APPID", Const.ConstInter.APP_APPID).replace("SECRET", Const.ConstInter.APPSECRET);
        HttpClient hc = new HttpClient();
        JSONObject jsonObject=hc.get(url).asJsonObject();
        //JSONObject jsonObject = HttpClient().doGet(url);
        System.out.println(jsonObject.toString());
        String errCode = jsonObject.getString("expires_in");
        if (!StringUtils.isEmpty(errCode)  && !StringUtils.isEmpty(jsonObject.getString("access_token").toString())) {

            //logger.info("token获取成功，内容={}", jsonObject.toJSONString());
            String token = jsonObject.get("access_token").toString();
            logger.info("getAccess_token="+token);
            return token;
        } else {
            //logger.error("token获取失败，内容={}", jsonObject.toJSONString());
            return null;
        }
    }

    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        //与token 比较
        String[] arr = new String[] { WxConfig.Token, timestamp, nonce };
        // 将token、timestamp、nonce三个参数进行字典排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        content = null;
        // 将sha1加密后的字符串可与signature对比
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    private static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }


    //推送小程序订阅消息
    public String pushSubscribeMsg(WxMssEntity wxMssVo) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String access_token=getAccess_token(WxConfig.AppId,WxConfig.AppSecret);
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + access_token;

        ResponseEntity<String> responseEntity =restTemplate.postForEntity(url, wxMssVo, String.class);
        return responseEntity.getBody();
    }

}
