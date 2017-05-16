# godlibrary
======================
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
### BaseSimpleAdapter
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

### AdapterCheckLin 单选
### ExpandableListViewCheckAdapter 多选
### SimpleAdapter 已弃用
请使用 BaseSimpleAdapter




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
