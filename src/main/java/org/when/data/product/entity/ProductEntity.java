package org.when.data.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 商品信息表
 * @Author: jeecg-boot
 * @Date: 2023-05-23
 * @Version: V1.0
 */
@Data
@TableName("sias_product")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ProductEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品类型
     */
    private Integer productType;
    /**
     * 图片链接
     */
    private String imageUrl;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 有效期/年
     */
    private Integer validityYears;
    /**
     * 是否赠品
     */
    private Boolean isGift;
    /**
     * 是否赠品
     */
    private Boolean isTicket;
    /**
     * 能否开票
     */
    private Boolean invoiced;
    /**
     * 商品标签
     */
    private String tag;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 是否上架
     */
    private Boolean enabled;
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
