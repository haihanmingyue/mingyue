package com.mingyue.mingyue.controller;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingyue.mingyue.bean.AttachSubType;
import com.mingyue.mingyue.bean.AttachType;
import com.mingyue.mingyue.bean.ReturnBean;
import com.mingyue.mingyue.bean.UserAccount;
import com.mingyue.mingyue.dao.AttachSubTypeDao;
import com.mingyue.mingyue.service.AttachSubTypeServices;
import com.mingyue.mingyue.service.UserAccountServices;
import com.mingyue.mingyue.utils.MapUtil;
import com.rabbitmq.tools.json.JSONUtil;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("attachSubType")
public class AttachSubTypeController extends BaseController<AttachSubType, AttachSubTypeDao,AttachSubTypeServices> {



    @Autowired
    private AttachSubTypeServices attachSubTypeServices;

    @Override
    public ReturnBean list(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> map = MapUtil.getRequestParamsMap(request);
        PageHelper.startPage(1,999);
        List<AttachSubType> list =  attachSubTypeServices.findByWhere(map);
        PageInfo pageInfo = new PageInfo(list);
        return ReturnBean.ok("查询成功").setData("success").setData(MapUtil.genMap("rows",pageInfo.getList(),"total",pageInfo.getTotal()));
    }

    @Override
    protected AttachSubTypeServices getService() {
        return attachSubTypeServices;
    }
}
