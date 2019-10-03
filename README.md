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
- [x] 配置文件（删除会自动生成）
- [ ] 图形控制面板

## 如何体验？

由于目前项目进展原因，暂不支持用户账户，输入任意用户名密码即可登入，拥有全部权限，暂时仅供测试/本地使用，尽请期待！

该服务端**支持绝大部分系统的绝大部分FTP客户端连接**，但服务端本身由于需要保证稳定，仅支持在Linux/MacOS系统上运行（由于文件系统和编码）。  
你可以将项目clone或直接下载到本地，进入release文件夹，运行java -jar LiteFTPD-UNIX.jar即可。LiteFTPD默认会自动监听21端口。  
如果在同目录下不存在配置文件`config.prop`，在你第一次运行时它会自动生成一个。  
修改`config.prop`中的文件，就可以修改LiteFTPD的配置了，十分简单易用。

简单来讲，这么启动就行了：

![屏幕快照 2019-09-30 下午8.44.56.png](https://pic.stackoverflow.wiki/uploadImages/6a6029f4-c17e-41b4-ae24-39cf839239ae.png) 

## 中文不乱码

MacOS Finder：

![屏幕快照 2019-09-30 下午8.32.54.png](https://pic.stackoverflow.wiki/uploadImages/0b1f4d52-16f0-447f-9e00-ac01d2d0309c.png)

Windows 资源管理器：

![屏幕快照 2019-09-30 下午8.33.14.png](https://pic.stackoverflow.wiki/uploadImages/cb097630-5819-4d97-9c2a-18cbc77e9cb3.png)

MacOS FileZilla：

![屏幕快照 2019-09-30 下午8.29.02.png](https://pic.stackoverflow.wiki/uploadImages/1afb6892-7bbd-43e5-aa41-3adf6ac33db6.png)

Windows FlashFXP：

![屏幕快照 2019-09-30 下午8.30.00.png](https://pic.stackoverflow.wiki/uploadImages/8903355f-a31b-41cf-83df-5a7e534d8855.png)

Windows自带客户端（由于客户端特殊性，需要先输入`quote gb`适应中文）：

![屏幕快照 2019-09-30 下午8.26.40.png](https://pic.stackoverflow.wiki/uploadImages/4a9b1c25-90df-44c1-ba14-c463b98089f5.png)

Windows Chrome：

![屏幕快照 2019-09-30 下午8.27.58.png](https://pic.stackoverflow.wiki/uploadImages/c8962604-35b3-49c8-b840-36c7a54ede08.png)

Unix自带客户端：

![屏幕快照 2019-09-30 下午8.28.32.png](https://pic.stackoverflow.wiki/uploadImages/067ea9f1-3bc1-4841-b45a-9375b0b53128.png)

MacOS Chrome：

![屏幕快照 2019-09-30 下午8.27.41.png](https://pic.stackoverflow.wiki/uploadImages/f7257a4a-b892-4401-82bf-c92b92abe00d.png)