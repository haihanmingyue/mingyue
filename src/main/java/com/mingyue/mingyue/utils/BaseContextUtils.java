package com.mingyue.mingyue.utils;

import com.mingyue.mingyue.bean.UserAccount;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Lazy(false)
@Component
public class BaseContextUtils {
    public final static String SYS_NAME = "sys";

    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext();// Spring 上下文.

    public static Logger logger = Logger.getLogger(BaseContextUtils.class);
    private static String shiroDbRealmName = null;


    /***
     * 获取shiro的sessionManager
     * @return
     * @author zhangyz created on 2015-8-12
     */
    public static DefaultWebSessionManager getSessionManager() {
        //参见applicationContext-shiro-base.xml
        return (DefaultWebSessionManager)applicationContext.getBean("sessionManager");
    }

    /**
     * 从session中获取登录的用户id
     * @param session
     * @return 若没有返回null
     * @author zhangyz created on 2015-9-4
     */
    public static String getUserIdFromSession(Session session) {
        SimplePrincipalCollection sp = (SimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (sp != null) {
            Object principal = sp.getPrimaryPrincipal();
            if (principal instanceof UserAccount user) {
                return user.getUuid();
            }
        }
        return null;
    }

    /**
     * 把更改后的user对象重新设置到map里，如果是单机环境该执行没有意义，但集群环境下需要。
     * 此处的参数user是subject中的值（subject位于本机环境线程上下文中），但还有个地方就是session里也有user信息，需要明确去设置一下才能触发远程缓存更新。
     * @param user
     * @author zhangyz created on 2015-8-12
     */
    public static void reStoreUser(UserAccount user, DefaultWebSessionManager sm) {
        Subject sub = SecurityUtils.getSubject();
        if(sub != null){
            Session session = sub.getSession(false);
            if (session != null) {
                SimplePrincipalCollection sp = null;//(SimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                if (sp == null) {
                    sp = new SimplePrincipalCollection();
                }
                if (shiroDbRealmName == null) {
                    AuthorizingRealm rm = (AuthorizingRealm)applicationContext.getBean("Realm");
                    shiroDbRealmName = rm.getName();
                }
                sp.add(user, shiroDbRealmName);
                session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY, sp);
            }

            //上面setAttribute已经会刷新远程缓存了
            //session.touch(); //虽然只是更新最新访问时间,最终目的是为了将其重新刷新到远程缓存中

            //if (sm == null)
            //    sm = getSessionManager();
            //sm.getSessionDAO().update(session);//或直接调用sm.onChange(session),但是是protected
        }
    }

    /**
     * 把更改后的user对象重新设置到map里，如果是单机环境该执行没有意义，但集群环境下需要。
     * 此处的参数user是subject中的值（subject位于本机环境线程上下文中），但还有个地方就是session里也有user信息，需要明确去设置一下才能触发远程缓存更新。
     * @param user
     */
    public static void reStoreUser(UserAccount user) {
        reStoreUser(user, getSessionManager());
    }

    /**
     * 返回当前登录用户
     * @return
     * @author zhangyz created on 2014-4-30
     */
    public static UserAccount getCurrentUser() {
        try {
            Subject sub = SecurityUtils.getSubject();
            if(sub != null){
                System.err.println("sub is not null");
                UserAccount userAccount = (UserAccount) sub.getPrincipal();
                return userAccount;
            }
            System.err.println("sub is null");
            return null;
        }
        catch(Throwable ex) {
            logger.error(ex);
            //在异步处理中调用ContextUtils.getCurrentUserName()会导致No SecurityManager accessible
            return null;
        }
    }


    /**
     * 返回当前humanId
     * @return
     * @author zhangyz created on 2015-1-30
     */
    public static String getCurrentHumanId() {
        UserAccount user = getCurrentUser();
        if (user == null) {
            return null;
        }
        else
            return user.getUuid();
    }

    /**
     * 返回当前humanId
     * @return
     * @author zhangyz created on 2015-1-30
     */
    public static String checkCurrentHumanId() throws LoginException {
        String humanId = getCurrentHumanId();
        if (humanId == null)
            throw new LoginException("您还未登录!");
        return humanId;
    }

    /**
     * 获取当前会话
     * @return
     * @author zhangyz created on 2014-8-2
     */
    public static Session getCurrentSession() {
        try {
            Subject sub = SecurityUtils.getSubject();
            if(sub != null){// && (sub.isRemembered() || sub.isAuthenticated())
                return sub.getSession(false);
            }
            return null;
        }
        catch(Throwable ex) {
            return null;
        }
    }

    /**
     * 注销
     *
     * @author zhangyz created on 2014-8-2
     */
    public static void loginout(){
        try {
            Subject sub = ThreadContext.getSubject();
            if(sub != null){
                Session shiroSession = sub.getSession(false);//已经相当于对session做了一次访问
                sub.logout();
                //是否需要添加？
                if (shiroSession != null)
                    shiroSession.stop();
            }
        }
        catch(Throwable ignored){
            ;
        }
    }

    /**
     * 是否已登录验证过
     * @return
     * @author zhangyz created on 2014-8-2
     */
    public static boolean isAuthenticated(){
        Subject sub = SecurityUtils.getSubject();
        if(sub != null){
            return sub.isAuthenticated();
        }
        return false;
    }

    public static void login(UserAccount userAccount) {
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername(userAccount.getUserName());
        token.setPassword(userAccount.getPassWord().toCharArray());
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        org.apache.shiro.session.Session session = subject.getSession(false);
        session.setTimeout(1000L * 60 * 60 * 24 * 30); // 一个月
    }

    public static void login(UserAccount userAccount,Integer rememberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername(userAccount.getUserName());
        token.setPassword(userAccount.getPassWord().toCharArray());
        if (rememberMe != null && rememberMe == 1) {
            token.setRememberMe(true);
        }
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        org.apache.shiro.session.Session session = subject.getSession(false);
        session.setTimeout(1000L * 60 * 60 * 24 * 30); // 一个月
    }
}
