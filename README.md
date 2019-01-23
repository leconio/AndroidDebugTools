# Android Debug工具

## 简介
1. `Android Debug工具`内置多线程的微型服务器，在同一个局域网中可以使用浏览器直接访问其内容。因此可以通过浏览器来操作当前App的行为以及adb等服务。
2. 目前实现了查看当前App数据库以及SD卡内容。
3. `Android Debug工具`提供了极高的拓展方式，可以通过Rest风格的Api来拓展，亦可以把它当成一个简单的静态资源服务器。
4. 由于Android性能有限，不建议负载太大的工作，更不能保证高并发。建议在开发环境使用，生产酌情使用。
5. 服务器暂时只支持支持GET和POST两种方式。

### 技术栈
Android 端无任何依赖，全部原生代码完成。
- Android：使用Java通过Socket方式实现HTTP协议的服务端。
- Web：使用Angular。

### 使用方法
#### 体验
1. 下载apk安装包，安装到手机。
2. 点击`数据库添加数据`和`获取SD卡权限`
3. 启动App，下拉通知栏
4. 打开你的浏览器，按照3的操作步骤体验吧

#### 集成
`Android Debug工具`完全无代码侵入性，意味着你只需添加依赖，无需编写任何代码即可以体验基础功能。
- 添加依赖 `debugImplementation 'io.lecon.debugtools:debugtools:1.0.8'`

#### 配置外部数据库
1. classpath 添加 `classpath 'io.lecon.debugtools.plugin:debugtoolsplugin:1.0.8'`
2. 添加依赖 `debugImplementation 'io.lecon.debugtools:debugtools:1.0.8'`
3. gradle 配置
``` groovy
apply plugin: 'io.lecon.debug_tools.plugin'
debug_tools {
    database {
        [数据库名] {
            path '[路径]'
            password '"[密码]"'
        }
    }
}
```
