package org.when.data.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Description: 客户订单表
 * @Author: jeecg-boot
 * @Date: 2023-05-29
 * @Version: V1.0
 */
@Data
@TableName("sias_order")
public class OrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private String id;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 客户id
     */
    private String customerId;
    /**
     * 支付记录id
     */
    private String paymentId;
    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 订单金额
     */
    private java.math.BigDecimal totalPrice;
    /**
     * 备注
     */
    private String remark;
    /**
     * 套餐id
     */
    private Integer bundleId;
    /**
     * 推广码
     */
    private String promoCode;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateBy;

    public LocalDate getCreateTimeAsLocalDate() {
        return createTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
