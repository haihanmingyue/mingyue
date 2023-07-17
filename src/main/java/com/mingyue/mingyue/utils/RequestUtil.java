package com.mingyue.mingyue.utils;

import javax.servlet.ServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

public class RequestUtil extends ServletRequestUtils {

    public static Integer getIntParameterMust(ServletRequest request, String name) throws ServletRequestBindingException {
        Object o = request.getParameter(name);
        if (o == null)
            throw new RuntimeException("this " + name + "is not null");
        else
            return getRequiredIntParameter(request, name);
    }
}
