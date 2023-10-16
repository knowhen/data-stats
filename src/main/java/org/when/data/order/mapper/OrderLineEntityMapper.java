package org.when.data.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.when.data.order.entity.OrderLineEntity;

import java.util.List;

/**
 * @Description: 客户订单明细表
 * @Author: jeecg-boot
 * @Date: 2023-05-29
 * @Version: V1.0
 */
public interface OrderLineEntityMapper extends BaseMapper<OrderLineEntity> {

    /**
     * 批量插入订单明细
     *
     * @param orderLines 订单明细列表
     */
//    void batchInsert(List<OrderLineEntity> orderLines);

    /**
     * 通过主表id删除子表数据
     *
     * @param mainId 主表id
     * @return boolean
     */
    boolean deleteByMainId(@Param("mainId") String mainId);

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<OrderLineEntity>
     */
    List<OrderLineEntity> selectByMainId(@Param("mainId") String mainId);
}
