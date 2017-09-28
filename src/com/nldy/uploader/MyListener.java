package com.nldy.uploader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * servlet 监听类用于加载配置文件
 *
 * created by shui 2017/9/28
 */
public class MyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        new MyConfiguration();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}

