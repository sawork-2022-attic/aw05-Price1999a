# RESTful microPoS 


请参考spring-petclinic-rest/spring-petclinic-microserivces 将aw04的webpos项目改为rest风格的微服务架构
（至少包含产品管理服务pos-products和购物车管理服务pos-carts以及discovery/gateway等微服务架构下需要的基础设施服务）。具体要求包括：

1. 请使用OpenAPI的定义每个服务的rest接口（参考pos-products）
2. 请使用ehcache管理缓存；done
3. 请注意使用断路器等机制；简单来讲，这里断路器就是说假如连续多次调用都寄了，就不再尝试了
4. 有兴趣的同学可自学一些reactjs或vuejs等为microPoS开发一个前端。完全不懂

## 简介

在pos-products与pos-cart模块中，使用OpenAPI的定义每个服务的rest接口。

使用ehcache缓存了从京东获取的数据库，使用redis管理了session数据库

在pos-gateway上配置了断路器

在当前架构中pos-discovery是EurekaServer，其他模块则是EurekaClient。网关模块为两个微服务以及客户端提供统一的路由，断路器配置在网关模块中，而pos-cart模块对pos-products相关内容的远程调用也通过网关路由，由此全系统上都配置了断路器。

## aw07拓展
aw07要求我们在原有的程序中拓展一个快递服务，通过消息中间件来异步处理。

使用redditMQ来作为消息中间件
目前在cart服务中做好了发送状态 