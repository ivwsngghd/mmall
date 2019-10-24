package com.mmall;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:WEB-INF/lib/applicationContext-datasource.xml", "classpath:WEB-INF/lib/applicationContext.xml"}) //声明junit ,spring配置文件的位置
public class BaseTest {
}
