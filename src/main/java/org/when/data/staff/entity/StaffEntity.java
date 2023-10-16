package org.when.data.staff.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 工作人员信息表
 * @Author: jeecg-boot
 * @Date: 2023-05-22
 * @Version: V1.0
 */
@Data
@TableName("sias_staff")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class StaffEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * openId
     */
    private String openid;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 盐
     */
    private String salt;
    /**
     * 摘要
     */
    private String digest;
    /**
     * 姓名
     */
    private String name;
    /**
     * 员工类型
     */
    private Integer staffType;
    /**
     * 楼栋id
     */
    private Integer dormId;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
    /**
     * 所属部门
     */
    private String sysOrgCode;
}
