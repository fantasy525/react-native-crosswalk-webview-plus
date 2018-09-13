# react-native-crosswalk-webview-plus
Crosswalk's WebView for React Native on Android. 

#### thanks [jordansexton](https://github.com/jordansexton),He developed the original project,but  He hasn't been updated for two years so far (2018.9),and there are some bugs...,so I modified some source code
感谢 [jordansexton](https://github.com/jordansexton)，这个项目原本是他开发的，但是他已经两年多没有更新代码了，而且原始代码在我使用中有一些bugs，因此我修改了源码去避免bug,同时我增加了一些额外的功能

[![npm licence](http://img.shields.io/npm/l/react-native-webview-crosswalk.svg?style=flat-square)](https://npmjs.org/package/react-native-webview-crosswalk "View this project on npm")

### Dependencies

*  `react-native >=0.55.4`, `react >= 16.3.1`

### Installation

* From the root of your React Native project

```shell
npm install react-native-crosswalk-webview-plus --save
mkdir android/app/libs
cp node_modules/react-native-webview-crosswalk/libs/xwalk_core_library-22.52.561.4.aar android/app/libs/
```

### Include module in your Android project

* In `android/setting.gradle`

```gradle
...
include ':CrosswalkWebView', ':app'
project(':CrosswalkWebView').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-webview-crosswalk')
```

### Include libs in your Android project

* In `android/build.gradle`

```gradle
...
allprojects {
    repositories {
        mavenLocal()
        jcenter()

        flatDir {          // <--- add this line
            dirs 'libs'    // <--- add this line
        }                  // <--- add this line
    }
}
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
  ...
  compile (name: "xwalk_core_library-22.52.561.4", ext: "aar")     // <--- add this line
  compile project(':CrosswalkWebView')                             // <--- add this line
}
```
* Register package :

+ used add code into MainApplication.java

```java
import com.jordansexton.react.crosswalk.webview.CrosswalkWebViewPackage;    // <--- add this line

public class MainApplication extends Application implements ReactApplication {
  ......

  @Override
  protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new CrosswalkWebViewPackage()    // <--- add this line
    );
  }

  ......

}
```
## bugs
* 当我使用的时候我发现在任何页面我用键盘输入的时候不能输入任何文字，同时app会崩溃退出，我跟我的安卓朋友一起研究了这个问题。我们发现如果在MainActivity 的onCreate生命周期里面
``` new XWalkView(getApplicationContext(), this);```
也就是在mainActivity初始化的时候先new 一个webview实例的话app就不会崩溃了，同时我们测试如果在onCreate里面延时10s初始化的话也会崩溃，意味着react-native可能启动mainActivity后修改了Context,导致某些webivew依赖的参数发生了变化，这些参数可能是静态的，如果我们先初始化一下让app保存最初的context,就不会崩溃了，因为你需要在mainActicity先new 一下
```
 private XWalkView xmv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(xmv==null){
            xmv=new XWalkView(getApplicationContext(), this);
        }
        xmv.onDestroy();// must destory
    }
```
## features
* 增加了load方法，使用方法同reload方法
* 增加了onCrosswalkWebViewLoadFinished 监听，方便在页面加载完毕执行某些操作
* 其他的修改可以自己看源码，这个项目跟原来的项目还有些小小的区别
## License
MIT
