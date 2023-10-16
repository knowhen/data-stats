package org.when.data.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description: 客户商品库存表
 * @Author: jeecg-boot
 * @Date: 2023-05-31
 * @Version: V1.0
 */
@Data
@TableName("sias_stock")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class StockEntity implements Serializable {
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
     * 用户id
     */
    private String customerId;
    /**
     * 宿舍楼id
     */
    private Integer dormId;
    /**
     * 宿管id
     */
    private String dormKeeperId;
    /**
     * 水工id
     */
    private String watermanId;
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 商品类型
     */
    private Integer productType;
    /**
     * 库存编号
     */
    private String stockNo;
    /**
     * 能否开票
     */
    private Boolean invoiced;
    /**
     * 是否已开票
     */
    private Boolean alreadyInvoiced;
    /**
     * 是否赠品
     */
    private Boolean isGift;
    /**
     * 是否水票
     */
    private Boolean isTicket;
    /**
     * 核销状态
     */
    private Boolean verified;
    /**
     * 到期时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationTime;
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
