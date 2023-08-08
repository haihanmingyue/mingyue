package com.mingyue.mingyue.factory;

import com.mingyue.mingyue.utils.MapUtil;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.lang.Nullable;
import reactor.util.annotation.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 加载自定义yml使用，不重写方法只能加载properties文件
 * hhmy
 * 2023年8月8日17:07:26
 *
 * */
public class YmlFactory extends DefaultPropertySourceFactory {

    @Override
    @NonNull
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {

        System.err.println("name->" + name);
        Resource resourceResource = resource.getResource();
        if (resourceResource.getFilename() != null) {
            if (
                    resourceResource.getFilename().endsWith(".yml") || resourceResource.getFilename().endsWith(".yaml")
            ) {
               return yml(resourceResource);
            }
        }
        return super.createPropertySource(name,resource);
    }

    public PropertySource<?> yml(Resource resourceResource) throws IOException {
        List<PropertySource<?>> sources = new YamlPropertySourceLoader().load(resourceResource.getFilename(), resourceResource);
        if (sources != null && sources.size() > 0) {
            return sources.get(0);
        } else {
            return new OriginTrackedMapPropertySource(resourceResource.getFilename(), MapUtil.genMap());
        }
    }

}
