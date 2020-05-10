5/10
修复后台权限校验错误导致无法上传图片的问题

4/4
优化代码，在服务器关闭的时候，把所有已知的线程池关闭。否则会有进程残留，占用内存。

3/30
通过Redis缓存了商品网站高频点击查询词汇的页面

3/29
项目部署速度非常缓慢，通过查找发现了解决办法：

> https://www.cnblogs.com/dingxiaochao/p/9544005.html

3/14
修复了一些细微的BUG，把接口的返回信息稍微修改了一下

2/14
增加了针对上传文件图片的删除和查询操作接口；

2/13
优化了cookieUtils，并且优化了UserController的代码，使前台，后台登出功能使用同一接口却不冲突；增加了status字段

2/12
通过查看运行日志得知，经过服务器内部nginx转发后的请求地址，是不带有域名的，即为127.0.0.1/controller/service.do

所以是不能够通过判断域名等信息来识别相应的功能，譬如如前台、后台的登出功能；解决办法，增加参数，识别status

2/9
学习使用分布式锁双重防死锁
并且增加了基于分布式的定时删除关闭订单的功能；


2/1
使用了Spring MVC拦截器实现了后台的权限认证功能；
RESTful风格的接口优化；

1/15
学习使用Spring Session + Redis,实现对代码无侵入式的Session管理，缺点是仅支持单个Redis节点；已弃置；
使用SpringMVC对全局异常进行包装；

12/30
把RedisPool进行更新升级优化，更新成RedisShardedPool；
新的分片式RedisShardedPoolUtil，支持多个Redis的配置使用，增加了横向拓展性；
Guava缓存的token信息转移至Redis里；
把Session的缓存转移到Redis中；

12/18
封装了JsonUtil工具类，实现类的序列化和反序列化操作；
封装了RedisPoolUtil工具类，实现对Redis的基本操作；
编写SessionExpireFilter，对操作的Session进行时间更新；

11/15 
修復了模糊查询的表达式BUG
只实现了后台超管，商家分管并没有实现，后面可以改进实现； TODO

11/12
修復bug 严格注意后台的pojo字段和接口一致，譬如subImages带s
后台在Requestmapping 指定对应的请求方法，如果请求方法错误，会弹出405
团队合作时，强烈建议加上Method字段，限制请求方式！！！
@RequestMapping(value = "add.do",method = RequestMethod.POST)

内存不足，无法线上实时编译

tomcat 在配置文件中加上了 URIEncoding = "utf-8" ;
否则会获取乱码，web.xml的配置无法解决这个问题

11/10
修复bug：在生成订单的时候无法正确获取购物车信息，导致无法正确生成订单

11/7
修复bug,注意MyBatis查询的数据的顺序要和pojo类里的构造函数顺序要一致，否则会报错；

11/5
备案

10/31
maven 环境隔离

10/24 线上发布的过程出现了bug

在Linux的自动化脚本发布里，maven无法正确编译class文件，导致项目的内容缺失；

导致这个原因的可能是服务器的内存不够，无法正确编译项目；(历时3天，至今未查明真正的原因)

折中解决办法：通过在本地编译好再进行上传Linux进行发布；


10/20 支付宝模块bug

支付宝回调函数里，有一个字符串转换Date类型的调用；必须注意format格式参数与字符串匹配；
否则会卡住，而且不报任何错误和异常，直至连接超时返回；


10/18 笔记

支付宝回调函数需要外网IP地址。

用户对二维码进行扫码时候，支付宝会向服务器端发送第一次回调信息；trade_status字段信息为：WAIT_BUTER_PAY

用户成功付款的时候，支付宝会向服务器端发送第二次回调信息；trade_status字段信息为：TRADE_SUCCESS; 当收到这条回调信息的时候，证明订单付款成功，更新数据库的数据；



用户模块
-

- 横向越权、纵向越权安全漏洞 
    - 横向越权：攻击者尝试访问与他拥有相同权限的用户的资源
    - 纵向越权：低级别攻击者尝试访问高级别用户的资源
    - 实现方法：必须要通过Session关联userId，来精准确定操作的登陆用户，以及注意checkAdmin方法的使用；
    
- MD5明文加密及增加salt值
- Guava包，用于密码找回的token生成；
- 高复用服务响应对象的设计思想及抽象封装

分类管理模块开发
- 

重点：
- 无限层级的树状数据结构
- 递归算法的设计思想(重点，递归出口)
- 如何处理复杂数据的排重；重写hashcode()和equal()的注意事项

商品模块开发
-

后台功能
- 图片上传◆◆◆◆
- 富文本上传◆◆◆ sim editor 编码 对返回的json有自己固定的格式

重点:
- FTP服务器的对接 ★
- SpringMVC文件上传
- 流读取Properties配置文件
- 抽象POJO、BO、VO对象之间的转换关系及解决思路
- joda-time快速入门

复杂业务存在POJO的结构模型：
Dao -> POJO()
Service -> BO(business object)
Controller -> VO(value object)

- Dao -> Service -> Controller
- POJO -> BO -> VO

该项目常用模型是：POJO -> VO

譬如：
- Cart（用户和数据的关联关系）  是 POJO
- VO CartProductVo，通过ServerResponse<>封装返回前端

10/12更新
购物车模块
-

★ 解决浮点型在商业运算中丢失精度的问题


10/16更新 

收货地址模块
-

功能：
- 地址的增删查改，相关信息的分页

学习目标：
- 复习SpringMVC 数据绑定中的对象绑定
- 复习MyBatis自动生成主键、配置和使用
- 复习如何避免横向越权漏洞的巩固


支付模块开发
- 

功能：
- 支付宝对接
- 支付回调
- 查询支付状态

重点：

- 熟悉支付宝对接核心文档，调通支付宝支付功能官方demo
- 了解支付宝SDK对接源码
- RSA1和RSA2验证签名以及加密
- 避免支付宝重复通知和数据校验
- natapp外网穿透(即无需外网IP也可以获取支付宝回调)和Tomcat remote debug(tomcat远程debug)
- 生成二维码，并持久化到图片服务器


接口设计文档
- 

> http://git.oschina.net/imooccode/happymmallwiki/wikis/home

