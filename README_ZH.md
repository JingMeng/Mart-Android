[English](./README_EN.md) | 中文

# 简介
码市是一个软件外包服务平台，通过智能匹配系统快速连接开发者与需求方，提供在线的项目管理工具与资金托管服务，提高软件交付的效率，保障需求方和开发者权益，帮助软件开发行业实现高效的资源匹配。

该项目是[码市][7]平台所对应的官方 App，您可去 [Google Play][9], [应用宝][10] 进行下载。

—— 码市 App 实现了 ——

· 海量悬赏供开发者挑选 
· 简单一步极速发布悬赏
· 自助评估您的项目价格
· 项目按阶段划分，自助验收，轻松交付
· 自由切换开发者、需求方视图

#### App 功能概览

首页|项目列表|私信|功能评估|个人中心
------------ | ------------- | ------------| ------------| ------------
![图片1][1]|![图片2][2]|![图片3][3]|![图片4][4]|![图片5][5]

# 如何运行

1. 使用 Android Studio 3.0 或以上 clone 代码
1. 等待 gradle 同步依赖，因为很多依赖都在国外，保证网络连接通畅，这一步可能需要几分钟至十几分钟
1. 点击 run，应该已经可以跑起来了

# 项目结构（按包名）
```
├── net.coding.mart // 主入口类
│   ├── activity // 界面
│   │   ├── guide       // 引导页
│   │   ├── mpay        // 开发宝
│   │   ├── reward      // 项目
│   │   ├── setting     // 设置页
│   │   └── user        // 个人资料
│   ├── common // 公共模块
│   │   ├── constant    // 一些枚举
│   │   ├── event       // eventbus 事件
│   │   ├── htmltext    // html 相关
│   │   ├── local       // 本地数据管理
│   │   ├── network     // 网络辅助类
│   │   ├── share       // 分享到第三方平台
│   │   ├── util        // 工具类
│   │   └── widget      // 自定义控件
│   ├── developers   // 开发者
│   ├── job     // 项目
│   ├── json    // 网络和数据
│   ├── login   // 登录注册
│   ├── setting // 静态展示页
│   ├── third   // 一些第三方代码
│   │   └── sidebar  // 可以按字母快速滑动的列表
│   ├── user    // 身份认证
│   └── wxapi   // 微信要求

```

# License
Mart-Android 用的是 [MIT license](./LICENSE) 。

[1]: https://user-images.githubusercontent.com/1555670/38722457-8f5e3f10-3f30-11e8-8ea6-39be8778023e.jpg
[1]: https://user-images.githubusercontent.com/1555670/38722457-8f5e3f10-3f30-11e8-8ea6-39be8778023e.jpg
[2]: https://user-images.githubusercontent.com/1555670/38722458-8fa398bc-3f30-11e8-873a-243554c727ff.jpg
[3]: https://user-images.githubusercontent.com/1555670/38722459-8feecb3e-3f30-11e8-93b6-78054ff81305.jpg
[4]: https://user-images.githubusercontent.com/1555670/38722460-904478fe-3f30-11e8-925e-90ca79327244.jpg
[5]: https://user-images.githubusercontent.com/1555670/38722461-91017dfa-3f30-11e8-8fd2-7a9d437f4bcb.jpg
[7]: https://codemart.com
[9]: https://play.google.com/store/apps/details?id=net.coding.mart
[10]: http://sj.qq.com/myapp/detail.htm?apkName=net.coding.mart