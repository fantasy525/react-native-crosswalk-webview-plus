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
# Notes
 In development mode(yarn start) ,You should add the following code to MainActivity onCreate lifecyle
```
 protected void onCreate(Bundle savedInstanceState) {
        // initialization first and onDestroy immediately
        new XWalkView(getApplicationContext()).onDestroy();// just add this line for RN dev mode,and release mode you can delete it
        super.onCreate(savedInstanceState);
    }
```

Because  when you open the WebView page input box to enter text, app will crash,may be activity change ApplicationContext when activity onResume,so we need init XWalkView on Create,some 
variables may be static ,so it get value onCreate,and It's exactly what XWalkView runtime need. and I hope react-native can fix this for later version,and it happend in development mode,release mode is OK,so you can delete  in release mode
## features
1.props:
```javascript
    injectedJavaScript:      PropTypes.string,
    localhost:               PropTypes.bool,
    onError:                 PropTypes.func,
    onMessage:               PropTypes.func,
    onNavigationStateChange: PropTypes.func,
    onProgress:              PropTypes.func,
    allowUniversalAccessFromFileURLs: PropTypes.bool,
    domStorageEnabled: PropTypes.bool,
    mediaPlaybackRequiresUserAction:PropTypes.bool,
    javaScriptEnabled:PropTypes.bool,
    userAgent:PropTypes.string,
    scalesPageToFit:PropTypes.bool,
    saveFormDataDisabled:PropTypes.bool,
    source:                  PropTypes.oneOfType([
      PropTypes.shape({
        uri: PropTypes.string,  // URI to load in WebView
      }),
      PropTypes.shape({
        html: PropTypes.string, // static HTML to load in WebView
      }),
      PropTypes.number,           // used internally by React packager
    ]),
    url:PropTypes.string,
```
methods:
```javascript
goBack(){}
goForward(){}
reload(){}
load(url:string){}
postMessage(data:any){}
```
useages:
```javascript
/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native';
import { RCTCrossWalkWebView } from 'react-native-crosswalk-webview-plus'

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
      <View style={styles.container}>
      	<View >
					<RCTCrossWalkWebView style={{width:500,flex:1}}  source={{uri:'https://www.baidu.com'}}/>
				</View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

```



## License
MIT
