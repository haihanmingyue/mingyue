package com.mingyue.base.service;

import com.mingyue.base.bean.LoginInfo;
import com.mingyue.base.bean.ReturnBean;
import com.mingyue.base.bean.UserAccount;
import com.mingyue.base.dao.UserAccountDao;
import com.mingyue.mingyue.RedisConstants;
import com.mingyue.mingyue.utils.Base64Util;
import com.mingyue.mingyue.utils.BaseContextUtils;
import com.mingyue.mingyue.utils.RsaUtils;
import com.mingyue.mingyue.utils.SaltUtils;
import com.mingyue.userrole.service.UserMenuServices;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class UserAccountServices extends BaseService<UserAccount,UserAccountDao>{

    @Autowired
    private UserAccountDao dao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private UserMenuServices userMenuServices;

    private ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    public UserAccountDao getDao() {
        return dao;
    }


    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public void register(UserAccount account, HttpServletRequest request) throws Exception {


        Jedis jedis = jedisPool.getResource();

        if (!jedis.exists(account.getUserName() + RedisConstants.REGISTER_CODE_KEY)) {
            logger.warn("验证码不存在");
            throw new RuntimeException("验证码已失效");
        }

        if (jedis.ttl(account.getUserName() + RedisConstants.REGISTER_CODE_KEY) <= 0L) {
            throw new RuntimeException("验证码已失效");
        }

        String code = jedis.get(account.getUserName() + RedisConstants.REGISTER_CODE_KEY);

        logger.warn(String.format("code->{%s}", code));

        String userCode = ServletRequestUtils.getRequiredStringParameter(request,"code");
        if (!StringUtils.hasText(code) || !userCode.equals(code)) {
            throw new RuntimeException("验证码错误");
        }


        UserAccount userAccount = findByUsername(account.getUserName());
        if ( null != userAccount ) {
            jedis.del(account.getUserName() + RedisConstants.REGISTER_CODE_KEY);
            throw new RuntimeException("账户已经存在");
        }

        String pass = account.getPassWord();

        pass = new String(RsaUtils.decryptByPrivateKey(Base64Util.decode(pass), RsaUtils.RSA_PRIVATE_KEY));

        account.setPassWord(pass);

        //1.获取随机盐
        String salt = SaltUtils.getSalt(8);
        //2.将随机盐保存到数据
        account.setSalt(salt);
        //3.明文密码进行md5 + salt + hash散列
        Md5Hash MD5 = new Md5Hash(account.getPassWord(), salt, 1024);
        account.setPassWord(MD5.toHex());
        account.setUuid(UUID.randomUUID().toString());


        try {
            //加锁
            reentrantLock.lock();
            jedis.del(account.getUserName() + RedisConstants.REGISTER_CODE_KEY);
            create(account);
        }catch (Exception e) {
            throw e;
        }finally {
            //释放锁
            reentrantLock.unlock();
        }

    }


    public ReturnBean login(UserAccount userAccount,String password,Integer rememberMe) throws Exception {

        if (userAccount == null) {

            throw new RuntimeException("账号不存在");
        } else {

            password = new String(RsaUtils.decryptByPrivateKey(Base64Util.decode(password),RsaUtils.RSA_PRIVATE_KEY));

            Md5Hash MD5 = new Md5Hash(password,userAccount.getSalt(),1024);
            password = MD5.toHex();


            if (!password.equals(userAccount.getPassWord())) {
                throw new RuntimeException("用户名或密码错误");
            }

            try {

                BaseContextUtils.login(userAccount,rememberMe);
                Session session = BaseContextUtils.getCurrentSession();
                LoginInfo loginInfo = new LoginInfo(userAccount,session);

                loginInfo.setUserMenuLists(userMenuServices.getRoleList());

                //登录失败就报错
                return ReturnBean.ok("登录成功").setData(loginInfo);

            }catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("服务器内部错误，登录失败");
            }

        }
    }

    public UserAccount findByUsername(String username){
        return getDao().findByUsername(username);
    }
}
