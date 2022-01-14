package com.age.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**

 */
@Data
//@TableName("sys_user_role")
public class OrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String prodName;
    private String prodDesc;
    private Integer orderType;
    private Long payMoney;
    private Integer payState;
    private Date createTime;

}
