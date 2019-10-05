# LiteFTPD-UNIX
🚧 Project developed using Intellij IDEA 🚧
Interested friends are welcome to participate in assisting my development, see my signature for the WeChat account.
The main reason for this flaw is that I found that the encoding problem is very serious when I use the FTP server of MacOS, and Windows can hardly use it.
So I want to make a compatible, humanized, high-performance and low-occupancy FTP server in my spare time.

## Current development progress:
✳️ Goal: Strong compatibility, high performance, low occupancy
Current progress: **File upload, download, delete, move, rename function for FTP clients (or FTP-enabled programs) such as Windows, Linux, MacOS, FileZilla, FlashFXP, Thunder, Chrome, Windows Explorer, etc. *

- [x] Extremely simple multi-user identification && permission control && default directory && allows directory setting
- [x] User directory lock
- [x] passive mode
- [x] active mode
- [x] breakpoint resume function
- [x] Multi-platform client compatibility
- [x] binary download
- [x] binary upload
- [ ] ASCII download
- [ ] ASCII upload
- [x] file move/rename
- [x] memory recycling mechanism
- [x] User Defined Settings
- [x] idle port reclamation mechanism
- [x] Small function: Automatically judge time greeting information after successful login ( Good morning / noon / afternoon / evening / night )
- [x] long connection (noop)
- [x] User connection limit and settings (support global settings)
- [x] IP connection limit and setting (support fuzzy matching)
- [x] configuration file (delete will be automatically generated)
- [ ] Graphic Control Panel

## How to experience?

### GIF Demo

![How-to-LiteFTPD-UNIX.gif](/How-to-LiteFTPD-UNIX.gif)

### Text Description

The server ** supports most FTP client connections** for most systems, but the server itself is only required to run on Linux/MacOS systems (due to file system and encoding) due to the need to be stable.

You can clone the project or download it directly to the local (or [click here](https://github.com/AdlerED/LiteFTPD-UNIX/releases) directly download the separate Jar package), go to the release folder, run java -jar LiteFTPD -UNIX.jar is fine. LiteFTPD will automatically listen to port 21 by default.
If the configuration file `config.prop` does not exist in the same directory, it will automatically generate one when you first run it.
Modify the file in `config.prop`, you can modify the configuration of LiteFTPD, it is very simple and easy to use.

Controlling user permissions is also very simple. Edit the value of the `user` variable in `config.prop`. Let's take the default example:
```
Format: [username];[password];[permission];[directed access to the directory];[default directory after login]
user=anonymous;;r;/;/;admin;123456;r;/;/root;
```
In the default value we define the information of two users. We talk separately:

```
User 1 login name: anonymous
User 1 password: empty
User 1 permission: r Read only (r stands for read-read, w stands for write-write, d stands for delete-delete, c stands for create-create, m stands for move-move)
Directory that User 1 allows access to: / (all subdirectories and files under the root directory)
User 1 logs in to the directory by default: /

User 2 login name: admin
User 2 password: 123456
User 2 permission: r Read only
Directory that User 2 is allowed to access: /
User 2 logs in to the directory by default: /root
```

For the same reason, you can also add more users at will.
  
How to use LiteFTPD? Simply put, this will start:

```
git clone https://github.com/adlered/liteftpd-unix
mv liteftpd-unix/release/LiteFTPD-UNIX.jar ./
rm -rf liteftpd-unix
java -jar LiteFTPD-UNIX.jar
```

After the first startup, LiteFTPD will automatically generate the `config.prop` configuration file in the same name directory. You can modify the configuration of LiteFTPD in the configuration file and restart LiteFTPD to take effect.
No other files are required, you can run the FTP service only through LiteFTPD-UNIX.jar. You only need to run LiteFTPD-UNIX.jar directly on the computer with Java installed, which is very convenient.

![Screenshot 2019-09-30 8.45.56.png](https://pic.stackoverflow.wiki/uploadImages/79a47e02-0623-427f-ae43-e08ab4be11f9.png)

## Chinese is not garbled

MacOS Finder:

![Screenshot 2019-09-30 pm 8.32.54.png](https://pic.stackoverflow.wiki/uploadImages/0b1f4d52-16f0-447f-9e00-ac01d2d0309c.png)

Windows Explorer:

![Screenshot 2019-09-30 8.33.14.png](https://pic.stackoverflow.wiki/uploadImages/cb097630-5819-4d97-9c2a-18cbc77e9cb3.png)

MacOS FileZilla:

![Screenshot 2019-09-30 8.29.02.png](https://pic.stackoverflow.wiki/uploadImages/1afb6892-7bbd-43e5-aa41-3adf6ac33db6.png)

Windows FlashFXP:

![Screenshot 2019-09-30 8.30.00.png](https://pic.stackoverflow.wiki/uploadImages/8903355f-a31b-41cf-83df-5a7e534d8855.png)

Windows comes with a client (due to client specificity, you need to enter `quote gb` to adapt Chinese):

![Screenshot 2019-09-30 8.26.40.png](https://pic.stackoverflow.wiki/uploadImages/4a9b1c25-90df-44c1-ba14-c463b98089f5.png)

Windows Chrome:

![Screenshot 2019-09-30 8.27.58.png](https://pic.stackoverflow.wiki/uploadImages/c8962604-35b3-49c8-b840-36c7a54ede08.png)

Unix comes with a client:

![Screenshot 2019-09-30 8.28.32.png](https://pic.stackoverflow.wiki/uploadImages/067ea9f1-3bc1-4841-b45a-9375b0b53128.png)

MacOS Chrome:

![Screenshot 2019-09-30 8.27.41.png](https://pic.stackoverflow.wiki/uploadImages/f7257a4a-b892-4401-82bf-c92b92abe00d.png)