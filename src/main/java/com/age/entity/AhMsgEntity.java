package com.age.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("t_ahb_msg")
public class AhMsgEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    private Long id;


    @TableField
    //@NotEmpty(message = "用户名不能为空")
    private String fromDid;

    @TableField
    private int fromUser;

    @TableField
    private int toUser;

    @TableField
    private String toDid;

    @TableField
    private String msg;

    @TableField
    private int unread;

    @TableField
    private String uid;

    @TableField
    //@Temporal(TemporalType.DATE)
    private Timestamp createTime;



}
