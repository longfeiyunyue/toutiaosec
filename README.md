# toutiaosec
仿照今日头条做的一个Java web项目，后台用Spring Boot+Mybatis+Redis实现快速开发，前台用Velocity模板引擎渲染。
<br>实现的功能有：资讯首页、用户登录注册、添加资讯、查看资讯、评论资讯、站内信会话、Redis资讯点赞/踩、Redis异步消息处理。
### 数据库用到5张表<br>
    user：用户表<br>
    news：新闻资讯表<br>
    message：消息会话表<br>
    login_ticket：登录的ticket信息<br>
    comment：资讯评论<br>
 
### 资讯首页<br>
本次项目在后端返前端新闻资讯时是把一条条新闻资讯先存入ViewObject类包装的一个Map集合对象，再统一存入list返给前端。<br>
![](https://img-blog.csdnimg.cn/20190512164130855.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2NTU2NjUx,size_16,color_FFFFFF,t_70)  
### 用户登录注册<br>
>    登录注册的业务层实现统一在UserService中，值得注意的是，通过Map集合来返回业务层的处理结果给Controller。
    在注册方法中，注册失败会通过Map存储注册失败原因，注册成功会存储注册成功的ticket，并在Controller层将ticket存入cookie，ticket用于存储用户登录信息。
    通过校验时，为了保证用户密码安全性，随机生成salt值与password相加并用MD5加密存入数据库。
### 资讯的添加、查看和评论<br>
>    添加资讯需要上传图片，这里可以用七牛云或者阿里云的服务，把图片上传到云存储，我这里借鉴了一位大佬@How_2_Play_Life的阿里云服务实现。
### 站内信会话<br>
>    这里有个值得一提的，在数据库的表字段设计上，message表的conversation_id字段表示会话ID，是标示A用户与B用户的会话，由A用户和B用户id组成，
    且小ID排前边，比如A用户发给B用户消息，就表示A_B；B用户发给A用户消息就是B_A，这样就能很灵活表示这条会话记录是谁发给谁的了。<br>
### Redis资讯点赞/踩<br>
>    用户点赞一条资讯，该资讯的点赞数量增加，点踩则踩数量增加，本项目的点赞点踩功能是通过redis数据库实现的。<br><br>在赞踩业务层设计上，对一条资讯的赞和踩通过redis的set集合实现，
    为每一条资讯创建一个set集合，set集合内存储点赞/点踩的用户数，用户点赞则把用户加入赞集合，点踩则把用户加入踩集合，
    通过判断用户在哪个集合来传递信息到前端应该高亮赞/踩状态。<br>
### Redis异步消息处理<br>
>    点赞、回复评论的时候，表面上是赞数增加了，其实还有很多其他的工作要做。比如，对方要收到消息提醒，成就值增加。一些行为会引起一系列连锁反应。
    如果在点赞时立马处理，会影响程序运行效率,所以大型服务需要异步化。<br>
    <br>
    redis异步处理的实现（把耗时的操作异步化，让网站的操作速度更快），异步处理就是把不是很紧急的事情留在后台慢慢的更新，把紧急的数据返给前端，
    把业务切开，比如评论后的积分值增长，就不需要很紧急，又比如点赞操作后系统发送站内信通知被点赞了。<br>
    <br>
    这里需要设计异步事件生产者EventProducer、消费者EventConsumer、异步消息处理Handler等几个类，EventProducer是把事件接过来并传入Redis的队列，
    EventConsumer是把事件分发出去，交由预先写好的Handler类处理。
### 拦截器HandlerInterceptor获取用户信息<br>
    在本次项目中拦截器用于获取用户信息保存到线程本地变量ThreadLocal，并供后续Controller调用。
