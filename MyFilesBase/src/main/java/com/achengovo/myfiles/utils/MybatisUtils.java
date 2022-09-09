package com.achengovo.myfiles.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionTemplate;

import java.io.InputStream;

/**
 * Mybatis工具类
 */
public class MybatisUtils {
    //获得SqlSession工厂
    private static SqlSessionFactory factory;
    //创建ThreadLocal绑定当前线程中的SqlSession对象
    private static final ThreadLocal<SqlSession> THREAD_LOCAL = new ThreadLocal<SqlSession>();

    static {
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            factory = new SqlSessionFactoryBuilder().build(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获得连接（从THREAD_LOCAL中获得当前线程SqlSession）
    private static SqlSession openSession() {
        SqlSession session = THREAD_LOCAL.get();
        if (session == null) {
            session=new SqlSessionTemplate(factory);
            THREAD_LOCAL.set(session);
        }
        return session;
    }

    /**
     * 获得接口实现类对象
     *
     * @param clazz 泛型对象
     * @param <T>   泛型
     * @return
     */
    public static <T> T getMapper(Class<T> clazz) {
        SqlSession session = openSession();
        return session.getMapper(clazz);
    }
}