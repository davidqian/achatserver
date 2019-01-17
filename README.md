# achatserver
## 架构图  
![architecture](https://github.com/davidqian/achatserver/blob/master/resource/img/architecture.jpg)
## 通信协议
### websocket
> 便于后台开发，暂时采用
### http
> rpc 调用
## 数据同步协议
> 使用类微信的通信协议
## 模块
### connector
> 使用 go 语言编写，用 redis 进行对应位置缓存
> 进程间通信使用 rabbitmq & http rpc
