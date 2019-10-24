package com.mmall;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-datasource.xml","classpath:applicationContext.xml"}) //声明junit ,spring配置文件的位置
public class BaseTest {
}
