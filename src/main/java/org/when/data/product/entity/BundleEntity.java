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
 * @Description: 套餐信息表
 * @Author: jeecg-boot
 * @Date: 2023-05-23
 * @Version: V1.0
 */
@Data
@TableName("sias_bundle")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class BundleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 套餐名称
     */
    private String bundleName;
    /**
     * 图片链接
     */
    private String imageUrl;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 标签
     */
    private String tag;
    /**
     * 套餐描述
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;
}
