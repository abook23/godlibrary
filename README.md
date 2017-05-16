# godlibrary
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)


# 简介
1. Android 常规开发
2. 参考 示例app

# 使用方法

## 引用
```java
compile 'com.abook23:godlibrary:1.3.1'
```

## Adapter
### 1.BaseSimpleAdapter
```java
listView.setAdapter(new BaseSimpleAdapter<Object[]>(R.layout.item_list_02, array) {
                @Override
                public void convert(BaseViewHolder holder, int position, Object[] objects) {
                    holder.setText(R.id.tv_title, (String) objects[0]).setImageResource(R.id.iv1, (Integer) objects[1]);
                    holder.setVisible(R.id.describe, false);
                    holder.setVisible(R.id.iv2, false);
                }
            });
```

### 2.AdapterCheckLin          单选
### 3.ExpandableListViewCheckAdapter 多选
### 4.SimpleAdapter            已弃用
请使用 BaseSimpleAdapter

## 异步线程
### AsyncThreadPool
使用方法 同 AsyncThread

## 广播
### NetBroadcastReceiver 网络变化监听
```java
    1.注册广播
        NetBroadcastReceiver netBroadcastReceiver = new NetBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netBroadcastReceiver,filter);
    2.网络监听（随便在哪里监听）
        NetBroadcastReceiver.setOnNetListener(new OnNetStatusListener() {
            @Override
            public void onNetStatus(int netType, String netName) {
                //-1 NoNetWork; 0 TYPE_MOBILE; 1 TYPE_WIFI; 2 TYPE_ETHERNET; 3 OtherNetWork
            }
        });
```

## dialog
### DialogDate              时间选择
### DialogEdit              输入框 自带校验
### DialogFragmentList      只带 单选和多选
### DialogFragmentExpList   两级菜单 只带 单选和多选
### DialogGpsNet            GPS 监听
### DialogListView
### DialogLoading           加载等等
### DialogMsgBox            消息提示

## Fragmet
### FragmentListView        只带 单选和多选
### FragmentExpListView     只带 单选和多选

## 图片处理
### BitmapUitls

## SharedPreferences
### Preference
 这是可用实例化 的，针对 一个APP 要调用 多个 SharedPreferences
### PreferenceUtils
 SharedPreferences 单列 方便使用
### SharedPreferencesUtils  弃用

## util
### Encode                  加密
### AESCipher               AES 加密
### RSAUtlis                RSA 加密
### AndroidUtils            Android系统常用的工具类
### CameraUtil              系统相机调用
### CheckUtils              文本输入框的判断等等
### ContactsUtils           电话号码
### DateUtils               时间工具类
### DBUtil                  数据库（cursor2Bean）
### DiskLruCache            来源 git
### EditTextWatcher         数控监听
### Emoji                   deletEmoji
### FastBlur                高斯模糊（图片）
### FileUtils               文件
### IdUtils                 getUUId
### JavaBeanUtil
### JsonUtils               json 操作
### L                       日志管理
### ListViewRefresh         ListView 刷新
### Notifications           通知栏
### PermissionUtil          权限管理
### SoftInputMethodUtils    键盘工具
### SpannableUtils          Spannable字符串工具类
### StringUtils
### TextUtils
### ToastUtils              Toast 管理类
1.包含可以取消的 toast

## widget
### ButtonProgress          带进度 的button，适用于下载
### CheckLayout
### GridViewByScrollview
### HorizontalListView
### ImageViewSquare         正方形图片
### KeyboardListenRelativeLayout 输入框弹出键盘
### LinearLayoutScroll
### LinearLayoutSquare      正方形LinearLayout
### ListViewByScrollview
### ListViewRefresh
### PinchImageView          可放大缩小的ImageView
### ViewPagerTabHost(*)     使用与主页
### WheelView               滚动的View



# 项目中的 dependencies

```java
dependencies {
        compile 'com.google.code.gson:gson:2.6.2'
        compile 'com.android.support:appcompat-v7:24.2.1'
        compile 'com.android.support:support-v4:24.2.1'
}
```

License
-------

    Copyright 2017 Wasabeef

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
