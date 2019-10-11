# LiteFTPD-UNIX
🚧 项目使用Intellij IDEA开发 🚧  
欢迎感兴趣的朋友参与协助我的开发，微信号详见我的签名。  
造这个轱辘的原因主要是因为我在使用MacOS的FTP服务端时发现编码问题很严重，Windows几乎无法正常使用。  
于是想在业余时间做出一个兼容性强、人性化、高性能且低占用的FTP服务端。  

### [→ English Version README ←](https://github.com/AdlerED/LiteFTPD-UNIX/blob/master/README-EN.md)

## 当前开发进度：
✳️ 目标：强兼容，高性能，低占用  
当前进度： **已支持Windows、Linux、MacOS系统FileZilla、FlashFXP、迅雷、Chrome、Windows资源管理器等FTP客户端（或支持FTP的程序）的文件上传、下载、删除、移动、重命名功能**

- [x] 极其简单的多用户身份识别&&权限控制&&默认目录&&允许目录设定  
- [x] 用户目录锁定  
- [x] 被动模式  
- [x] 主动模式  
- [x] 断点续传功能  
- [x] 多平台客户端兼容  
- [x] 二进制下载  
- [x] 二进制上传  
- [x] ASCII下载  
- [x] ASCII上传  
- [x] 文件移动/重命名
- [x] 内存回收机制  
- [x] 用户自定义设置  
- [x] 空闲端口回收机制  
- [x] 小功能：登录成功后自动判断时间问候信息 ( Good morning / noon / afternoon / evening / night )
- [x] 长连接（noop）
- [x] 用户连接数限制及设置（支持全局设置）
- [x] IP连接数限制及设置（支持模糊匹配）
- [x] 配置文件（删除会自动生成）
- [ ] 图形控制面板

## 如何体验？

### GIF演示

如果你面临长城防火墙的阻挡，建议你将下面的GIF（位于项目根目录）直接下载下来预览，否则可能变成静态图片。

![How-to-LiteFTPD-UNIX.gif](/How-to-LiteFTPD-UNIX.gif)

### 国际化

虽然用处可能不是很大，但LiteFTPD支持国际化语言——支持一部分语言。以中文为例：  

![屏幕快照 2019-10-06 下午9.04.14.png](https://pic.stackoverflow.wiki/uploadImages/7f53ebe8-5b31-4baa-9a68-77de37bc69b0.png)

### 文本说明

该服务端**支持绝大部分系统的绝大部分FTP客户端连接**，但服务端本身由于需要保证稳定，仅支持在Linux/MacOS系统上运行（由于文件系统和编码）。  

你可以将项目clone或直接下载到本地（或[在这里](https://github.com/AdlerED/LiteFTPD-UNIX/releases)直接下载单独Jar包），进入release文件夹，运行`java -jar LiteFTPD-UNIX.jar`即可。LiteFTPD默认会自动监听21端口。  
**如果需要将LiteFTPD的语言修改为其它语言**，你可以在启动Jar时增加`-l [语言]`参数。例如`java -jar LiteFTPD-UNIX.jar -l zh-cn`，目前支持`zh-cn`、`en-us`。  
如果在同目录下不存在配置文件`config.prop`，在你第一次运行时它会自动生成一个。  
修改`config.prop`中的文件，就可以修改LiteFTPD的配置了，十分简单易用。  

控制用户权限也十分简单，编辑`config.prop`中`user`变量的值，我们以默认情况举例：  
```
格式：[用户名];[密码];[权限];[允许访问的目录];[登录后默认的目录]
user=anonymous;;r;/;/;admin;123456;r;/;/root;
```
默认值中我们定义了两个用户的信息。我们分开讲：  

```
用户1登录名：anonymous
用户1密码：空
用户1权限：r 只读（r代表read-读，w代表write-写，d代表delete-删除, c代表create-创建，m代表move-移动）
用户1允许访问的目录：/（根目录下的所有子目录和文件） 
用户1登录后默认定位到目录：/

用户2登录名：admin
用户2密码：123456
用户2权限：r 只读
用户2允许访问的目录：/
用户2登录后默认定位到目录：/root
```

同理，你也可以随意添加更多用户。  
  
如何使用LiteFTPD？简单来讲，这么启动就行了：  

```
git clone https://github.com/adlered/liteftpd-unix
mv liteftpd-unix/release/LiteFTPD-UNIX.jar ./
rm -rf liteftpd-unix
java -jar LiteFTPD-UNIX.jar
```

第一次启动后，LiteFTPD会自动在同名目录下生成`config.prop`配置文件，你可以在配置文件中修改LiteFTPD的配置，重启LiteFTPD即可生效。  
不需要其它文件，你可以只通过LiteFTPD-UNIX.jar运行FTP服务。你只需要在安装了Java的电脑中直接运行LiteFTPD-UNIX.jar就可以了，十分便捷。  

![屏幕快照 2019-09-30 下午8.44.56.png](https://pic.stackoverflow.wiki/uploadImages/79a47e02-0623-427f-ae43-e08ab4be11f9.png) 

## 多线程不占CPU、内存

![屏幕快照 2019-10-11 上午11.38.35.png](https://pic.stackoverflow.wiki/uploadImages/6dd550d3-825e-4fdd-828e-bd53b1104dbb.png)

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