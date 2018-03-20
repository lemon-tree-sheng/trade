package org.sheng.trade.coupon.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.sheng.trade.common.constant.TradeEnums.ServerConfigEnum;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author shengxingyue, created on 2018/3/17
 */
public class CouponRestServer {
    public static void main(String[] args) throws Exception {
        // 监听端口
        Server server = new Server(ServerConfigEnum.COUPON.getPort());
        // 类似于原来的 web.xml
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/" + ServerConfigEnum.COUPON.getContextPath());

        // 实例化 applicationContext
        XmlWebApplicationContext applicationContext = new XmlWebApplicationContext();
        applicationContext.setConfigLocation("classpath:applicationContext.xml");
        // 设置 listener
        servletContextHandler.addEventListener(new ContextLoaderListener(applicationContext));
        // 设置 dispatcherServlet
        servletContextHandler.addServlet(new ServletHolder(new DispatcherServlet(applicationContext)), "/*");
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}
