package org.when.data.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 客户订单明细表
 * @Author: jeecg-boot
 * @Date: 2023-05-29
 * @Version: V1.0
 */
@Data
@TableName("sias_order_line")
public class OrderLineEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private String id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品类型
     */
    private Integer productType;
    /**
     * 商品数量
     */
    private Integer quantity;
    /**
     * 商品单价
     */
    private java.math.BigDecimal price;
    /**
     * 商品总价
     */
    private java.math.BigDecimal totalPrice;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
