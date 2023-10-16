package org.when.data.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @Description: 水票核销明细表
 * @Author: jeecg-boot
 * @Date: 2023-06-02
 * @Version: V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sias_verify_detail")
public class VerifyDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 水票核销记录id
     */

    private String recordId;
    /**
     * 水票id
     */
    private String ticketId;

}
