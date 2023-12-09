package keyle.fun.SequenceGeneratorAPI.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 如果用Mybatis框架可使用下面方式自动填充数据
 */
@Component
public class MybatisPlusConfig implements MetaObjectHandler {

    /**
     * 使用 MP 进行插入操作时，将执行此方法
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 设置属性值，使用新的列名
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
    }

    /**
     * 使用 MP 进行更新操作时，将执行此方法
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 设置属性值，使用新的列名
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }
}
