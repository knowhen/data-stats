package org.when.data.order.entity;

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
import java.util.Date;

/**
 * @Description: 水票核销记录表
 * @Author: jeecg-boot
 * @Date: 2023-06-02
 * @Version: V1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sias_verify_record")
public class VerifyRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 核销人id
     */

    private String checkmanPhone;
    /**
     * 客户id
     */

    private String customerPhone;
    /**
     * 核销数量
     */

    private Integer quantity;
    /**
     * 核销码
     */

    private String verifyCode;
    /**
     * 核销时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime verifyTime;
    /**
     * 核销状态
     */

    private Integer verifyStatus;
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
}
