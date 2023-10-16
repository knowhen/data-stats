package org.when.data.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.when.data.order.entity.VerifyDetail;

import java.util.List;

/**
 * @Description: 水票核销明细表
 * @Author: jeecg-boot
 * @Date: 2023-06-02
 * @Version: V1.0
 */
public interface VerifyDetailMapper extends BaseMapper<VerifyDetail> {

    /**
     * 通过主表id删除子表数据
     *
     * @param mainId 主表id
     * @return boolean
     */
    public boolean deleteByMainId(@Param("mainId") String mainId);

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<VerifyDetail>
     */
    public List<VerifyDetail> selectByMainId(@Param("mainId") String mainId);
}
