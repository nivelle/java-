### 粘包产生的原因

- 发送方每次写入数据<套接字缓冲区大小

- 接收方读取套接字缓冲区数据不够及时

### 半包产生的原因

- 发送方写入数据>套接字缓冲区大小

- 发生的数据大于协议的MTU(Maxmun Transmission unit最大传输单元)，必须拆包

### 根本原因: TCP是流式协议,消息无边界

### 解决问题的根本方法:找出消息的边界的方式

- tcp连接改成短连接，一个请求一个短连接
- 固定长度,比较浪费空间
- 分割符,特殊处理分割符
- 固定长度字段存内容的长度信息

**后面三种都是采用分帧方式实现确定消息边界**

### Netty对三种常用分帧方式的支持,解码器抽象类：ByteToMessageDecoder

-  固定长度:FixedLengthFrameDecoder ; int frameLength

- 分割符: DelimiterBasedFrameDecoder  byteBuf[] delimiters

- 固定长度字段存内容的长度信息:LengthFieldBasedFrameDecoder (LengthFiledOffset、LengthFieldLength、lengthAdjustment、initialBytesToStrip)

- 行解码器:LineBasedFrameDecode 