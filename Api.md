
## 接口开放
### 本地接口
Android端微型服务器使用Json方式传递数据，内置静态服务器，非常适合前后端分离。内置功能包含：
1. 路由注册
2. 文件下载
3. 静态服务
``` java
        if (urlSplit.length > 1) {
            // error-demo
            if ("error".equals(urlSplit[1])) {
                Route errorRoute = new ErrorRoute();
                Result process = errorRoute.process(request);
                response = new JsonResponse(process);
                return response;
            }

            if ("db".equals(urlSplit[1])) {
                Route dbRoute = new DbRoute(mContext);
                Result process = dbRoute.process(request);
                response = new JsonResponse(process);
                return response;
            }

            if ("disk".equals(urlSplit[1])) {
                Route diskRoute = new DiskRoute(mContext);
                Result process = diskRoute.process(request);
                response = new JsonResponse(process);
                return response;
            }

            //SimpleHTTPServer
            if ("file".equals(urlSplit[1])) {
                return new ByteResponse(Utils.loadFileContent(requestURI.split("file")[1]));
            }
        }
        //资源处理
        return new ByteResponse(Utils.loadAssetContent(WEB_FOLDER + requestURI, mContext.getAssets()));
```

其中，第3-20行是内置路由规则，`error`处理了错误异常的Response，`db`处理了内置数据库查看工具的Api，`disk`处理了SDcard查看工具的Api，这三个路由返回结果都是Json方式。

`file`是下载工具，通过这个Api可以下载Sdcard内任何有权限的文件，遇到目录则压缩后下载。

如果匹配不到，那么就到Assets目录下面匹配静态资源，他可以根据文件类型自动匹配Content-Type。所以如果想输出静态Html那么就放到这个匹配规则里边。


### Api
目前此工具提供数据库查看和SD卡查看两种功能，这两个功能通过Restful风格提供。下面开放的Api
#### 数据库工具
##### 列出数据库

```
http://127.0.0.1:8089/db/listDatabase
```

方法：GET
参数：无
响应：
``` json
{
    "success": true,
    "message": "ok",
    "obj": [
        {
            "name": "Contact.db",
            "path": "/data/user/0/cn.liucl.unitedebugtoolsclient/databases/Contact.db"
        },
        {
            "name": "Car.db",
            "path": "/data/user/0/cn.liucl.unitedebugtoolsclient/databases/Car.db"
        }
    ]
}
```

---
##### 列出表

```
http://127.0.0.1:8089/db/listTable?dbName=Car.db
```

方法：GET
参数：dbName
响应：

```
{
    "success": true,
    "message": "ok",
    "obj": [
        "cars"
    ]
}
```

---
##### 查询数据

```
http://127.0.0.1:8089/db/query?dbName=Car.db&tableName=cars
```

方法：GET
参数：

| 参数 | 描述 |
| --- | --- |
| dbName | 数据库名称 |
| tableName | 表名 |
| condition | 查询条件 |
| limit | 查询多少条 |
| offset | 偏移量 |

响应：
```
{
    "success": true,
    "message": "ok",
    "obj": {
        "columns": [
            {
                "columnName": "id",
                "isPrimary": true
            },
            {
                "columnName": "name",
                "isPrimary": false
            },
            {
                "columnName": "color",
                "isPrimary": false
            },
            {
                "columnName": "mileage",
                "isPrimary": false
            }
        ],
        "list": [
            {
                "id": 1,
                "name": "name_0",
                "color": "RED",
                "mileage": 10.449999809265137
            },
            {
                "id": 2,
                "name": "name_1",
                "color": "RED",
                "mileage": 11.449999809265137
            },
            {
                "id": 3,
                "name": "name_2",
                "color": "RED",
                "mileage": 12.449999809265137
            },
            {
                "id": 4,
                "name": "name_3",
                "color": "RED",
                "mileage": 13.449999809265137
            }
        ],
        "pageInfo": {
            "count": 50
        }
    }
}
```

---
##### 插入数据

```
http://127.0.0.1:8089/db/insert
```

方法：POST
参数：

| 参数 | 描述 |
| --- | --- |
| dbName | 数据库名 |
| tableName | 表名 |
| newValue | 插入值的json |

响应：
```
{
    "success": true,
    "message": "ok",
    "obj": null
}
```

---
##### 更新数据

```
http://127.0.0.1:8089/db/update
```

方法：POST
参数：

| 参数 | 描述 |
| --- | --- |
| dbName | 数据库名 |
| tableName | 表名 |
| condition | 条件 |
| newValue | 新值 |

响应：
```
{
    "success": true,
    "message": "ok",
    "obj": null
}
```

---
##### 删除数据

```
http://127.0.0.1:8089/db/delete
```

方法：GET
参数：

| 参数 | 描述 |
| --- | --- |
| dbName | 数据库名 |
| tableName | 表名 |
| condition | 条件 |

响应：
```
{
    "success": true,
    "message": "ok",
    "obj": null
}
```

---
##### 查看当前数据库版本号

```
http://127.0.0.1:8089/db/version
```

方法：GET
参数：

| 参数 | 描述 |
| --- | --- |
| dbName | 数据库名 |

响应：
```
{
    "success": true,
    "message": "ok",
    "obj": {
        "version":20
    }
}
```

---
##### 查看表数据个数

```
http://127.0.0.1:8089/db/count
```

方法：GET
参数：

| 参数 | 描述 |
| --- | --- |
| dbName | 数据库名 |
| tableName | 表名 |
| condition | 条件 |

响应：
```
{
    "success": true,
    "message": "ok",
    "obj": {
        "count":50
    }
}
```

#### SDCard工具
##### 查看当前目录下所有文件和文件夹
```
127.0.0.1:8089/disk/list?type=sdcard&/data/io.lecon/
```
方法：GET
参数：

| 参数 | 描述 |
| --- | --- |
| type | sdcard、inner SD卡还是App内部存储 |
| path | 要列出的目录 |

响应：
```
{
    "success": true,
    "message": "ok",
    "obj": [
        {
            "filename": "log",
            "isFolder": true,
            "size": 0,
            "permission": "rwx",
            "realPath": "/storage/emulated/0/data/io.lecon/log"
        }
    ]
}
```

---
##### 重命名
```
127.0.0.1:8089/disk/rename?type=inner&/data/io.lecon/
```
方法：GET
参数：

| 参数 | 描述 |
| --- | --- |
| type | sdcard、inner SD卡还是App内部存储 |
| path | 要列出的目录 |

响应：
```
{
    "success": true,
    "message": "ok",
    "obj": null
}
```

---
##### 删除当前目录下所有文件和文件夹
```
127.0.0.1:8089/disk/delete?type=sdcard&/data/io.lecon/
```
方法：GET
参数：

| 参数 | 描述 |
| --- | --- |
| type | sdcard、inner SD卡还是App内部存储 |
| path | 要列出的目录 |

响应：
```
{
    "success": true,
    "message": "ok",
    "obj": null
}
```

---

#### 其他
##### 下载
遇到文件下载，遇见文件夹压缩后下载
```
127.0.0.1:8089/file/you/file/path
```
方法：GET
参数：无
响应：
```
{
    "success": true,
    "message": "ok",
    "obj": null
}
```

---
