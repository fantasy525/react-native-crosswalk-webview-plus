# react-native-crosswalk-webview-plus
Crosswalk's WebView for React Native on Android. 

#### thanks [jordansexton](https://github.com/jordansexton),He developed the original project,but  He hasn't been updated for two years so far (2018.9),and there are some bugs...,so I fix them and I add some new features

[![npm licence](http://img.shields.io/npm/l/react-native-webview-crosswalk.svg?style=flat-square)](https://npmjs.org/package/react-native-webview-crosswalk "View this project on npm")

### Dependencies

*  `react-native >=0.57.0`, `react >= 16.5.0`

### 1. Installation

* From the root of your React Native project

```shell
npm install react-native-crosswalk-webview-plus --save or
yarn add react-native-crosswalk-webview-plus
```
* copy 

```node_modules/react-native-webview-crosswalk/libs/xwalk_core_library-23.53.589.4-arm.aar ``` to your 
```android/app/libs``` 


### 2.Include module in your Android project

* In `android/setting.gradle`

```gradle
...
include ':CrosswalkWebView'
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
You should add the following code to MainActivity onCreate lifecyle
```
 protected void onCreate(Bundle savedInstanceState) {
        // initialization first and onDestroy immediately
        new XWalkView(getApplicationContext()).onDestroy();//add this line
        super.onCreate(savedInstanceState);
    }
```
Because when you open the WebView page input box to enter text, app will crash,may be activity change ApplicationContext when activity onResume,so we need init XWalkView on Create,some 
variables may be static ,so it get value onCreate,and It's exactly what XWalkView runtime need. and I hope react-native can fix this for later version
## features
1.props:

## License
MIT
