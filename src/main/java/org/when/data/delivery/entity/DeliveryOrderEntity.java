package org.when.data.delivery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 配送订单表
 * @Author: jeecg-boot
 * @Date: 2023-05-23
 * @Version: V1.0
 */
@Data
@TableName("sias_delivery_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DeliveryOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 订单编号
     */


    private String orderNo;
    /**
     * 水工id
     */


    private String watermanId;
    /**
     * 水工姓名
     */


    private String watermanName;
    /**
     * 宿管id
     */


    private String dormKeeperId;
    /**
     * 宿舍楼id
     */


    private Integer dormId;
    /**
     * 配送数量
     */


    private Integer deliveredBucket;
    /**
     * 回收数量
     */


    private Integer recycledBucket;
    /**
     * 电子水票
     */


    private Integer recycledETicket;
    /**
     * 纸质水票
     */


    private Integer recycledPaperTicket;
    /**
     * 配送状态
     */


    private Integer deliveryStatus;
    /**
     * 创建人
     */

    private String createBy;
    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Date createTime;
    /**
     * 更新人
     */

    private String updateBy;
    /**
     * 更新日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Date updateTime;
    /**
     * 所属部门
     */

    private String sysOrgCode;

}
