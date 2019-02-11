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
> 暂时采用 json
## 数据同步协议
> 使用类 sync 同步协议
## 模块
### cocos-js
### connector
> 使用 go 语言编写，用 redis 进行对应位置缓存
> 进程间通信使用 rabbitmq & http rpc
### user
> 使用 java 语言基于 springboot 编写
> 用 mysql 进行数据存储
### uid
> 使用 java 语言基于 springboot 编写
> leaf-segment，生成的 uid 趋势递增，可能存在不连续的情况
### seqid
> 使用 java 语言基于 springboot 编写
> 使用 mysql 进行数据存储
> 需要对 uid 进行分段提供序列号
### message
> 使用 java 语言基于 springboot 编写
> 使用 mysql 进行数据存储
> 采用写时扩散机制
