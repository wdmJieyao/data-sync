package top.lijieyao.datasync.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * @Description: Object对象强转
 * @Author: LiJieYao
 * @Date: 2022/4/23 16:12
 */
@Slf4j
public class ObjectConvertUtils {

    /**
     * 创建过的BeanCopier实例放到缓存中，下次可以直接获取，提升性能
     */
    private static final Table<Class, Class, BeanCopier> BEAN_COPIER_TABLE = HashBasedTable.create();

    public static <T, R> R copy(T t, Class<R> klass) {
        if (Objects.isNull(t) || Objects.isNull(klass)) {
            log.info("ObjectConvertUtils..copy..params is null..t:{}..klass:{}", t, klass);
            throw new RuntimeException("ObjectConvertUtils..copy..params is null");
        }

        // 缓存beanCopier
        BeanCopier beanCopier = BEAN_COPIER_TABLE.get(t.getClass(), klass);
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(t.getClass(), klass, false);
            BEAN_COPIER_TABLE.put(t.getClass(), klass, beanCopier);
        }
        try {
            // 允许调用无参构造
            Constructor<R> dc = klass.getDeclaredConstructor();
            dc.setAccessible(true);
            // 反射一个目标对象
            R r = dc.newInstance();
            beanCopier.copy(t, r, null);
            return r;
        } catch (Exception e) {
            log.info("ObjectConvertUtils..copy..error:{}", e.toString());
            throw new RuntimeException(e.getCause());
        }
    }
}
