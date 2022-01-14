package com.age.util.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 常量帮助类
 *
 * @author age
 * @Email 4653478@qq.com
 */
@Component
@PropertySource("classpath:/config.properties")
public class Constant {

    /**
     * 上传保存路径文件夹名称(格式化日期格式)
     */
    public static String uploadSavePathFormat = "yyyyMM";

    /**
     * 文件上传保存路径-不提供set方法，防止恶意篡改值
     */
    @Value("${file.UploadPath}")
    @Getter
    private String uploadPath;

    @Value("${database.adminId}")
    public Long adminId;

    /**
     * 默认管理员账号
     */
    @Value("${database.test.admin.name}")
    public String defaultAdminName;

    /**
     * 管理员账号密码
     */
    @Value("${database.test.admin.pwd}")
    public String defaultAdminPwd;


    /**
     * 菜单类型
     *
     * @author age
     * @Email 4653478@qq.com
     */
    @Deprecated
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        private MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 上传文件类型
     *
     * @author age
     * @Email 4653478@qq.com
     */
    public enum UploadType {
        /**
         * 其他
         */
        other(-1),

        /**
         * 管理员上传头像
         */
        adminAvatar(0);

        private int value;

        private UploadType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
