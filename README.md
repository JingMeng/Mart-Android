English | [中文](README.md)

# Mart introduction

[Mart][7] is a platform for software outsourcing. It contact the developers and the demand side by smart match system, provide a money escrow service and a tool which can help online management, improve the efficiency of the deliverable, balance the rights of both sides.

This repository is the offical app of [Mart][7] platform, you can install it from [Google Play][9], [应用宝][10]

# Mart App has done

· Mass of developers
· Simple way to publish a demand
· Online valuation
· Divide a demand by some small stages, manage by stage
· Provide different features depend on the role of user (developers or demand side)

# App features quick facts

Main page|Project list|Message|Valuation|Userinfo
------------ | ------------- | ------------| ------------| ------------
![图片1][1]|![图片2][2]|![图片3][3]|![图片4][4]|![图片5][5]

# Setup

1. Clone code by Android Studio 3.0 or above
1. Wait gradle sync, don't update project gradle version, might cost a few minutes
1. Click "run" button in AS, :-)

# Project structure（package）
```
├── net.coding.mart 
│   ├── activity 
│   │   ├── guide       // guide page
│   │   ├── mpay        
│   │   ├── reward      // project page
│   │   ├── setting     // setting page
│   │   └── user        // user page
│   ├── common 
│   │   ├── constant    // some enum
│   │   ├── event       // event class for eventbus 
│   │   ├── htmltext    // help class
│   │   ├── local       // local data manager
│   │   ├── network     // network help class
│   │   ├── share       
│   │   ├── util        
│   │   └── widget      // custom widget
│   ├── developers   // developer user
│   ├── job     // project
│   ├── json    // network, classes corresponding to json
│   ├── login   // login and register
│   ├── setting // some static activity
│   ├── third   // some third party code
│   │   └── sidebar  // custom listview, could quick scroll by letter
│   ├── user    
│   │   └── identityAuthentication  
│   └── wxapi   // WeChat request

```

# License
Mart-Android use [MIT license](./LICENSE)

[1]: /doc/1.jpg
[2]: /doc/2.jpg
[3]: /doc/3.jpg
[4]: /doc/4.jpg
[5]: /doc/5.jpg
[7]: https://codemart.com
[9]: https://play.google.com/store/apps/details?id=net.coding.mart
[10]: http://sj.qq.com/myapp/detail.htm?apkName=net.coding.mart