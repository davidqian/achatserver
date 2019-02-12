# achatserver
## 架构图  
![architecture](https://github.com/davidqian/achatserver/blob/master/resource/img/architecture.jpg)
## 通信协议
### websocket
> 便于前后端联调，暂时采用
### http
>  rpc 调用
## 数据协议
### 数据传输协议
> 客户端是浏览器，暂时采用 json
### 数据同步协议
> 使用类 sync 同步协议
## 模块
### cocos-js
### connector
> 使用 go 语言编写，用 redis 进行对应位置缓存    
> 进程间通信使用 rabbitmq & http rpc    
> 主要业务有：客户端服务端链接维护、客户端请求到其它模块转发
### user
> 主要业务有：注册、登录、token 验证    
> 工程实现:    
> 使用 java 语言,基于 springboot 编写    
> 使用 spring cloud 进行微服务部署    
> 用 mysql 进行数据存储,redis进行缓存处理
### uid
> uid在架构中有分段需求，所以需要尽量保证 uid 连续    
> 采用 leaf-segment，数据库压力较小，数据库宕机，服务在一段时间内可以提供服务，uid 连续性较好    
> 采用线程模型，以机器为单位进行健康检查，减少运维复杂度    
> 工程实现：    
> 采用 octopus 框架进行开发
### seqid    
> 使用 mysql 进行数据存储
> 需要对 uid 进行分段提供序列号
### message
> 使用 java 语言基于 springboot 编写
> 使用 mysql 进行数据存储
> 采用写时扩散机制
