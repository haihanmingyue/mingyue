package com.mingyue.userrole.service;


import com.alibaba.fastjson2.JSON;
import com.mingyue.base.service.BaseService;
import com.mingyue.mingyue.utils.BaseContextUtils;
import com.mingyue.mingyue.utils.MapUtil;
import com.mingyue.userrole.SysMenuConstants;
import com.mingyue.userrole.bean.SysMenu;
import com.mingyue.userrole.bean.UserMenu;
import com.mingyue.userrole.bean.UserMenuBean;
import com.mingyue.userrole.dao.UserMenuDao;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserMenuServices extends BaseService<UserMenu, UserMenuDao> {

    @Autowired
    private UserMenuDao dao;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserMenuDao getDao() {
        return dao;
    }



    /**
     * 获取当前用户权限表
     *
     * */
    public List<UserMenuBean> getRoleList() {
        List<UserMenuBean> list = new LinkedList<>();
        //倒叙排序
        Map<Integer,Map<String,UserMenuBean>> menuMap = new TreeMap<>(Comparator.reverseOrder());

        //查询公有menu
        List<SysMenu> publicSysMenuList =  sysMenuService.findByWhere(MapUtil.genMap(
                "isAllMenu", SysMenuConstants.PUBLIC_MENU
        ));
        logger.warn("公有menu->" + publicSysMenuList.size());

        List<SysMenu> userSysMenuList = null;

        List<UserMenu> userMenuList;
        String uuid = BaseContextUtils.getCurrentHumanId();

        logger.warn("获取权限的用户-> " + uuid);

        //查询用户私有menu
        if (StringUtil.isNullOrEmpty(uuid))
            userMenuList = null;
        else
            userMenuList = getDao().findByUser(uuid);

        // 去查询对应的sysMenu
        if (userMenuList != null && !userMenuList.isEmpty()) {
            userSysMenuList = new LinkedList<>();
            for (UserMenu userMenu : userMenuList) {
                SysMenu sysMenu = sysMenuService.get(userMenu.getMenuUuid());
                if (sysMenu != null) {
                    userSysMenuList.add(sysMenu);
                }
            }
        }

        if (userSysMenuList != null && !userSysMenuList.isEmpty()) {
            publicSysMenuList.addAll(userSysMenuList);
        }

        //遍历
        Iterator<SysMenu> iterator = publicSysMenuList.listIterator();
        logger.warn("全部menu->" + publicSysMenuList.size());
        while (iterator.hasNext()) {
            SysMenu sysMenu = iterator.next();
            if (!menuMap.containsKey(Integer.parseInt(sysMenu.getUrlLevel()))) {
                menuMap.put(Integer.parseInt(sysMenu.getUrlLevel()),new HashMap<>());
            }
            Map<String,UserMenuBean> levelMap = menuMap.get(Integer.parseInt(sysMenu.getUrlLevel()));
            levelMap.put(sysMenu.getUuid(),new UserMenuBean(sysMenu));
            iterator.remove();
        }

        if(!menuMap.isEmpty()) {

            logger.warn("menuMap->" + JSON.toJSONString(menuMap));
            //倒叙从数字最大，实际等级最小的等级开始
            Iterator<Map.Entry<Integer,Map<String,UserMenuBean>>> itor = menuMap.entrySet().iterator();

           while (itor.hasNext()){
               Map.Entry<Integer,Map<String,UserMenuBean>> mapEntry = itor.next();
                //不处理最顶级的menu
                if (mapEntry.getKey() - 1 >= 0) {
                    //父map
                    Map<String,UserMenuBean> fatherMap = menuMap.get(mapEntry.getKey() - 1);
                    //当前等级map
                    Map<String,UserMenuBean> childrenMap = menuMap.get(mapEntry.getKey());

                    Iterator<UserMenuBean> childrenTor = childrenMap.values().iterator();
                    //遍历当前等级map
                    while (childrenTor.hasNext()) {
                        UserMenuBean childMenu = childrenTor.next();
                        //父id
                        String fatherId = childMenu.getFatherId();
                        //查询对应的父id
                        if (fatherMap.containsKey(fatherId)) {
                            //有父类
                            UserMenuBean fatherBean = fatherMap.get(fatherId);
                            //添加进list
                            fatherBean.getChildrenList().add(childMenu);
                        }
                        //移除
                        childrenTor.remove();
                    }
                    itor.remove();
                } else {
                    logger.warn("mapEntry.getKey()->" + mapEntry.getKey());
                    Map<String,UserMenuBean> map = mapEntry.getValue();
                    logger.warn("mapEntry.getValue()->" + mapEntry.getValue().size());
                    list.addAll(map.values());
                    break;
                }
            }

        }
        logger.warn("list->" + list.size());
        return list;
    }
}
