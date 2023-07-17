package com.mingyue.mingyue.Mylister;

import javax.servlet.ServletContextEvent;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.web.context.ContextLoaderListener;


/**
 * Spring上下文加载监听器, 从Spring继承而来.
 *
 * */
public class MyLister extends ContextLoaderListener {

    protected void beforeInitContext() {
      System.err.println("Spring上下文加载监听器, 从Spring继承而来");
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
//        ContextHolder.initWebContext(event.getServletContext());
        beforeInitContext();
        super.contextInitialized(event);
//        ContextHolder.initApplicationContext(event.getServletContext());
    }
}
