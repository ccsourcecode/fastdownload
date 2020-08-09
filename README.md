## 概述
fastdownload是一个快速从页面异步导出数据的下载中心的方案例子，100w条只需要20秒左右！

## 运行
PULL完代码后，修改application.properties中的数据库连接信息。建表脚本在`resource/db/db.sql`中。

直接运行`Runner`运行，打开浏览器输入http://127.0.0.1:8080访问到测试页面

运行`创建测试数据`进行模拟数据创建，一次创建5w条。

下图为50w数据和100w数据导出的耗时示例


![img](img/img1.png)
