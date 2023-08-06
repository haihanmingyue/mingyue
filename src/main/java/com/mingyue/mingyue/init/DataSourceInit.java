package com.mingyue.mingyue.init;


import javax.annotation.PostConstruct;
import org.apache.naming.factory.DataSourceLinkFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
public class DataSourceInit extends DataSourceLinkFactory {

    public DataSourceInit() {

    }
}
