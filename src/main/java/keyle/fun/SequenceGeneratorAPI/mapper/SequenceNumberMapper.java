package keyle.fun.SequenceGeneratorAPI.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import keyle.fun.SequenceGeneratorAPI.entity.SequenceNumber;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 使用框架时可这么写，此处保留。
 */
@Mapper
public interface SequenceNumberMapper extends BaseMapper<SequenceNumber> {
    /**
     * 根据序列号查找实体类
     * @param sequenceNo 序列号
     * @return SequenceNumber实体类
     */
    SequenceNumber findBySequenceNo(@Param(value = "sequenceNo")String sequenceNo);
}
