# LiteFTPD-UNIX
🚧 项目使用Intellij IDEA开发 🚧  
欢迎感兴趣的朋友参与协助我的开发，微信号详见我的签名。  
造这个轱辘的原因主要是因为我在使用MacOS的FTP服务端时发现编码问题很严重，Windows几乎无法正常使用。  
于是想在业余时间做出一个兼容性强、人性化、高性能且低占用的FTP服务端。  

## 当前开发进度：
✳️ 目标：强兼容，高性能，低占用  
当前进度： **已支持Windows、Linux、MacOS系统FileZilla、FlashFXP、迅雷、Chrome、Windows资源管理器等FTP客户端（或支持FTP的程序）的文件上传、下载、删除、移动、重命名功能**

- [ ] 用户身份识别  
- [x] 用户目录锁定  
- [x] 被动模式  
- [x] 主动模式  
- [x] 断点续传功能  
- [x] 多平台客户端兼容  
- [x] 二进制下载  
- [x] 二进制上传  
- [ ] ASCII下载  
- [ ] ASCII上传  
- [x] 文件移动/重命名
- [x] 内存回收机制  
- [x] 用户自定义设置  
- [x] 空闲端口回收机制  
- [x] 小功能：登录成功后自动判断时间问候信息 ( Good morning / noon / afternoon / evening / night )
- [x] 长连接（noop）
- [ ] 单用户连接数限制及设置
- [ ] 图形控制面板

## 如何体验？
该服务端**支持绝大部分系统的绝大部分FTP客户端连接**，但服务端本身由于需要保证稳定，仅支持在Linux/MacOS系统上运行（由于文件系统和编码）。  
你可以将项目clone或直接下载到本地，使用Intellij IDEA打开，运行src/pers/adlered/liteftpd/main/Main类中的主方法即可，LiteFTPD默认会自动监听21端口。  
在`Permission.java`中调整自己的默认FTP目录；  
在`Variable.java`中调整FTP服务端设置（超时、欢迎信息，被动端口范围等)；  