package com.mingyue.mingyue.service;

import com.mingyue.mingyue.bean.UserAccount;
import com.mingyue.mingyue.dao.UserAccountDao;
import com.mingyue.mingyue.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserAccountServices extends BaseService<UserAccount,UserAccountDao>{

    @Autowired
    private UserAccountDao dao;

    @Override
    public UserAccountDao getDao() {
        return dao;
    }


    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public void register(UserAccount account) {
        //1.获取随机盐
        String salt = SaltUtils.getSalt(8);
        //2.将随机盐保存到数据
        account.setSalt(salt);
        //3.明文密码进行md5 + salt + hash散列
        Md5Hash MD5 = new Md5Hash(account.getPassWord(),salt,1024);
        account.setPassWord(MD5.toHex());
        account.setUuid(UUID.randomUUID().toString());
        create(account);
    }

    public UserAccount findByUsername(String username){
        return getDao().findByUsername(username);
    }
}
