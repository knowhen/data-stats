package org.when.data.delivery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description: 水工送水记录表
 * @Author: jeecg-boot
 * @Date: 2023-06-06
 * @Version: V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sias_delivery_record")
public class DeliveryRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 客户手机号
     */
    private String customerPhone;
    /**
     * 客户手机号
     */
    private String receiverPhone;
    /**
     * 客户手机号
     */
    private String receiverName;
    /**
     * 地址
     */
    private String address;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 期望送达时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedTime;
    /**
     * 实际送达时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveredTime;
    /**
     * 水工id
     */
    private String watermanId;
    /**
     * 水工电话
     */
    private String watermanPhone;
    /**
     * 配送状态
     */
    private Integer recordStatus;
    /**
     * 备注
     */
    private String remark;
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
}
