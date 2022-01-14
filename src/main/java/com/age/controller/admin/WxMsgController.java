package com.age.controller.admin;


import com.age.datasources.DataSource;
import com.age.datasources.DataSourceContextHolder;
import com.age.service.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Map;

/**
 * 微信相关接口
 */
@RestController
@RequestMapping("/admin/sys/ahuser")

public class WxMsgController extends AbstractController {

    @Autowired
    private WxService wxService;

    /*
    * 开通小程序消息推送时的验证接口，和接收客服消息的接口路径一样，get和post的区别
    * */
    @RequestMapping("/getdata/wxkfpost")
    @ResponseBody
    public void getWXKFPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");// 随机数
        String echostr = request.getParameter("echostr"); // 随机字符串
        PrintWriter out = null;
        try {
            out = response.getWriter();
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败
            if (wxService.checkSignature(signature, timestamp, nonce)) {
                out.print(echostr);
                out.flush();   //这个地方必须画重点，消息推送配置Token令牌错误校验失败，搞了我很久，必须要刷新！！！！！！！
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
            out = null;
        }
    }

    /**
     * 微信客服及消息推送相关接口，接收微信后台发来的用户消息
     * @return
     */
    @RequestMapping(value = "/getdata/wxkfpost", method = RequestMethod.POST)
    @ResponseBody
    public String receiveMessage(@RequestBody Map<String, Object> msg) throws IOException {

        String sRet=wxService.receiveMessage(msg);
        return sRet;
    }





}
