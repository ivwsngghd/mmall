用户模块
-
1. 登陆、用户名验证、注册
2. 忘记密码、提交问题答案、重置密码
3. 获取用户信息、更新、退出登陆


学习目标:

- 横向越权、纵向越权安全漏洞 
    - 横向越权：攻击者尝试访问与他拥有相同权限的用户的资源
    - 纵向越权：低级别攻击者尝试访问高级别用户的资源
    - 实现方法：必须要通过Session关联userId，来精准确定操作的登陆用户，以及注意checkAdmin方法的使用；
    
- MD5明文加密及增加salt值
- Guava缓存的使用
- 高复用服务响应对象的设计思想及抽象封装
```
public class ServerResonse<T> implements Serializable{
    private int status;
    private String msg;
    private T data;
    
    ······
}
```
- MyBatis-plugin使用技巧
- session的使用
- 方法局部演进


分类管理模块开发
- 

功能介绍：
- 获取结点
- 增加结点
- 修改名字
- 获取分类ID
- 递归子结点ID



学习目标：
- 如何设计和封装无限层级的树状数据结构
- 递归算法的设计思想(重点，递归出口)
- 如何处理复杂数据的排重
- 重写hashcode()和equal()的注意事项

商品模块开发
-

功能

前台功能
-  产品搜索
-  动态排序
-  商品详情
-  ······


后台功能
- 商品列表
- 商品搜索
- 图片上传◆◆◆◆
- 富文本上传◆◆◆ sim editor 编码 对返回的json有自己固定的格式
- 商品详情
- 商品上下架
- 增加商品
- 更新商品

学习目标
- FTP服务器的对接 ★
- SpringMVC文件上传
- 流读取Properties配置文件
- 抽象POJO、BO、VO对象之间的转换关系及解决思路
- joda-time快速入门
- 静态块
- MyBatis-PageHelper
- MyBatis对List遍历的实现方法
- MyBatis对where语句动态拼装的几个版本的演变

复杂业务存在POJO的存在：
Dao -> POJO()
Service -> BO(business object)
Controller -> VO(value object)

- Dao -> Service -> Controller
- POJO -> BO -> VO

还有一种模型：
Dao -> Controller+Service

当前项目采用的模型是：POJO -> VO

譬如：
- Cart（用户和数据的关联关系）  是 POJO
- VO CartProductVo，通过ServerResponse<>封装返回前端

```
CREATE TABLE `mmall_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `category_id` int(11) NOT NULL COMMENT '分类id,对应mmall_category表的主键',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `subtitle` varchar(200) DEFAULT NULL COMMENT '商品副标题',
  `main_image` varchar(500) DEFAULT NULL COMMENT '产品主图,url相对地址',
  `sub_images` text COMMENT '图片地址,json格式,扩展用',
  `detail` text COMMENT '商品详情',
  `price` decimal(20,2) NOT NULL COMMENT '价格,单位-元保留两位小数',
  `stock` int(11) NOT NULL COMMENT '库存数量',
  `status` int(6) DEFAULT '1' COMMENT '商品状态.1-在售 2-下架 3-删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8
```

10/12更新
购物车模块
-
功能介绍：
- 加入、更新、移除、查询商品，查询商品数量
- 单选/取消 全选/取消
- 购物车列表实现

学习目标：

- 设计思想
- 高复用的购物车核心方法 (购物车的商品数量，总价)
- ★解决浮点型在商业运算中丢失精度的问题


10/16更新 

收货地址模块
-

功能：
- 地址的增删查改，相关信息的分页

学习目标：
- 复习SpringMVC 数据绑定中的对象绑定
- 复习MyBatis自动生成主键、配置和使用
- 复习如何避免横向越权漏洞的巩固







接口设计文档
- 

> http://git.oschina.net/imooccode/happymmallwiki/wikis/home







