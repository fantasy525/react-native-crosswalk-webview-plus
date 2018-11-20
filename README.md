# react-native-crosswalk-webview-plus
Crosswalk's WebView for React Native on Android. 

#### thanks [jordansexton](https://github.com/jordansexton),He developed the original project,but  He hasn't been updated for two years so far (2018.9),and there are some bugs...,so I fix them and I add some new features

[![npm licence](http://img.shields.io/npm/l/react-native-webview-crosswalk.svg?style=flat-square)](https://npmjs.org/package/react-native-webview-crosswalk "View this project on npm")

### Dependencies

*  `react-native >=0.57.0`, `react >= 16.5.0`

### Installation

* From the root of your React Native project

```shell
npm install react-native-crosswalk-webview-plus --save or
yarn add react-native-crosswalk-webview-plus
```
copy 

```node_modules/react-native-webview-crosswalk/libs/xwalk_core_library-23.53.589.4-arm.aar ```

to your 

```android/app/libs``` 


### Include module in your Android project

* In `android/setting.gradle`

```gradle
...
include ':CrosswalkWebView', ':app'
project(':CrosswalkWebView').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-crosswalk-webview-plus')
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
  implementation (name: "xwalk_core_library-23.53.589.4-arm", ext: "aar")     // <--- add this line
  implementation project(':CrosswalkWebView')                             // <--- add this line
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
# Note
在react-native 0.57.1 版本 任何页面我用键盘输入的时候不能输入任何文字，同时app会crash 这个bug依旧存在，但是只发生在开发期间，离线打包后不存在，目前可以这样解决
在MainActivity的onCreate方法中加入这些代码
```java
 protected void onCreate(Bundle savedInstanceState) {
        // initialization first and onDestroy immediately
        new XWalkView(getApplicationContext()).onDestroy();//add this line
        super.onCreate(savedInstanceState);
    }
```
## bugs

* ~~当我使用的时候我发现在任何页面我用键盘输入的时候不能输入任何文字，同时app会崩溃退出，我跟我的安卓朋友一起研究了这个问题。我们发现如果在MainActivity 的onCreate生命周期里面``` new XWalkView(getApplicationContext(), this);```
也就是在mainActivity初始化的时候先new 一个webview实例的话app就不会崩溃了，同时我们测试如果在onCreate里面延时10s初始化的话也会崩溃，意味着react-native可能启动mainActivity后修改了Context,导致某些webivew依赖的参数发生了变化，这些参数可能是静态的，如果我们先初始化一下让app保存最初的context,就不会崩溃了，因为你需要在mainActicity先new 一下~~

* ~~在react-native 0.57版本里面修复了上面的bug~~,同时这个webview还一直存在一个偶尔出现的空指针异常bug,时而有，时而没有，经过安卓同学的排查发现是每次初始化时有问题，所以增加了xml布局文件来解决这个问题
 
## features
* 增加了load方法，使用方法同reload方法
* 增加了onCrosswalkWebViewLoadFinished 监听，方便在页面加载完毕执行某些操作
* 其他的修改可以自己看源码，这个项目跟原来的项目还有些小小的区别
## License
MIT
