package com.age.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ah用户实体

 */
@Data
@TableName("t_ahb_user")
public class AhUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 微信用户名
     */
    @TableField
    //@NotEmpty(message = "用户名不能为空")
    private String wxName;

    @TableField
    private String wxId;

    @TableField
    private int gender;

    @TableField
    private String province;

    @TableField
    private String city;

    @TableField
    private String did;

    @TableField
    //@Temporal(TemporalType.DATE)
    private Timestamp loginTime;


    @TableField
    //@Temporal(TemporalType.DATE)
    private Timestamp logoutTime;

    @TableField
    //@Temporal(TemporalType.DATE)
    private Timestamp createTime;



}
