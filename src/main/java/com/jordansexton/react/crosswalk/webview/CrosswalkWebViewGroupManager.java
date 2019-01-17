package com.jordansexton.react.crosswalk.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.common.annotations.VisibleForTesting;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.webview.events.TopMessageEvent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.XWalkCookieManager;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

import java.util.Map;

import javax.annotation.Nullable;

public class CrosswalkWebViewGroupManager extends ViewGroupManager<CrosswalkWebView> {

    public static final int GO_BACK = 1;

    public static final int GO_FORWARD = 2;

    public static final int RELOAD = 3;

    public static final int POST_MESSAGE = 4;

    public static final int LOAD = 5;

    @VisibleForTesting
    public static final String REACT_CLASS = "CrosswalkWebView";

    private ReactApplicationContext reactContext;

    private static final String BLANK_URL = "about:blank";

    public CrosswalkWebViewGroupManager (ReactApplicationContext _reactContext) {
        reactContext = _reactContext;
    }

    @Override
    public String getName () {
        return REACT_CLASS;
    }

    @Override
    public CrosswalkWebView createViewInstance (ThemedReactContext context) {

        CrosswalkWebView crosswalkWebView;
        Activity _activity = reactContext.getCurrentActivity();
        crosswalkWebView = (CrosswalkWebView) LayoutInflater.from(_activity).inflate(R.layout.xwalk_webview, null);
        crosswalkWebView.bindContext(context);

        XWalkSettings settings =crosswalkWebView.getSettings();
        settings.setDomStorageEnabled(true);// open local storage
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setDatabaseEnabled(true);

        settings.setCacheMode(XWalkSettings.LOAD_CACHE_ELSE_NETWORK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(false);
            setAllowUniversalAccessFromFileURLs(crosswalkWebView, false);
        }
        // Fixes broken full-screen modals/galleries due to body height being 0.
        crosswalkWebView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        // open cookies
        XWalkCookieManager mCookieManager = new XWalkCookieManager();
        mCookieManager.setAcceptCookie(true);
        mCookieManager.setAcceptFileSchemeCookies(true);

        context.addLifecycleEventListener(crosswalkWebView);
        reactContext.addActivityEventListener(new XWalkActivityEventListener(crosswalkWebView));
        return crosswalkWebView;
    }

    @Override
    public void onDropViewInstance(CrosswalkWebView view) {
        super.onDropViewInstance(view);
        view.unbindContext();
        view.onDestroy();
		 new Handler().post(new Runnable() {
            @Override
            public void run() {
				if(reactContext.getCurrentActivity()==null) return;
                reactContext.getCurrentActivity().findViewById(android.R.id.content).requestLayout();
            }
        });
    }

    @ReactProp(name = "source")
    public void setSource(final CrosswalkWebView view, @Nullable ReadableMap source) {
      Activity _activity = reactContext.getCurrentActivity();
      if (_activity != null) {
          if (source != null) {
              if (source.hasKey("html")) {
                  final String html = source.getString("html");
                  _activity.runOnUiThread(new Runnable() {
                      @Override
                      public void run () {
                          view.load(null, html);
                      }
                  });
                  return;
              }
              if (source.hasKey("uri")) {
                  final String url = source.getString("uri");
                  _activity.runOnUiThread(new Runnable() {
                      @Override
                      public void run () {
                          view.load(url, null);
                      }
                  });
                  return;
              }
          }
      }
      setUrl(view, BLANK_URL);
    }


    @ReactProp(name = "injectedJavaScript")
    public void setInjectedJavaScript (XWalkView view, @Nullable String injectedJavaScript) {
        ((CrosswalkWebView) view).setInjectedJavaScript(injectedJavaScript);
    }

    @ReactProp(name = "messagingEnabled")
    public void setMessagingEnabled(XWalkView view, boolean enabled) {
        ((CrosswalkWebView) view).setMessagingEnabled(enabled);
    }

    @ReactProp(name = "url")
    public void setUrl (final CrosswalkWebView view, @Nullable final String url) {
        Activity _activity = reactContext.getCurrentActivity();
        if (_activity != null) {
            _activity.runOnUiThread(new Runnable() {
                @Override
                public void run () {
                    view.load(url, null);
                }
            });
        }
    }

    @ReactProp(name = "localhost")
    public void setLocalhost (CrosswalkWebView view, Boolean localhost) {
        view.setLocalhost(localhost);
    }
    // 启用js?
    @ReactProp(name = "javaScriptEnabled")
    public void setJavaScriptEnabled(XWalkView view, boolean enabled) {
        view.getSettings().setJavaScriptEnabled(enabled);
    }
    //禁止保存formData?
    @ReactProp(name = "saveFormDataDisabled")
    public void setSaveFormDataDisabled(XWalkView view, boolean disable) {
        view.getSettings().setSaveFormData(!disable);
    }
    //启用本地存储？
    @ReactProp(name = "domStorageEnabled")
    public void setDomStorageEnabled(XWalkView view, boolean enabled) {
        view.getSettings().setDomStorageEnabled(enabled);
    }
    // 媒体播放需要用户主动操作?
    @ReactProp(name = "mediaPlaybackRequiresUserAction")
    public void setMediaPlaybackRequiresUserAction(XWalkView view, boolean requires) {
        view.getSettings().setMediaPlaybackRequiresUserGesture(requires);
    }
    @ReactProp(name = "allowUniversalAccessFromFileURLs")
    public void setAllowUniversalAccessFromFileURLs(XWalkView view, boolean allow) {
        view.getSettings().setAllowUniversalAccessFromFileURLs(allow);
    }
    // 设置用户代理
    @ReactProp(name = "userAgent")
    public void setUserAgent(XWalkView view, @Nullable String userAgent) {
        if (userAgent != null) {
            // TODO(8496850): Fix incorrect behavior when property is unset (uA == null)
            view.getSettings().setUserAgentString(userAgent);
        }
    }
    @ReactProp(name = "scalesPageToFit")
    public void setScalesPageToFit(XWalkView view, boolean enabled) {
        view.getSettings().setUseWideViewPort(!enabled);
    }

    @Override
    public
    @Nullable
    Map<String, Integer> getCommandsMap () {
        return MapBuilder.of(
            "goBack", GO_BACK,
            "goForward", GO_FORWARD,
            "reload", RELOAD,
            "postMessage", POST_MESSAGE,
            "load", LOAD
        );
    }

    @Override
    public void receiveCommand (CrosswalkWebView view, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case GO_BACK:
                view.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
                break;
            case GO_FORWARD:
                view.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.FORWARD, 1);
                break;
            case RELOAD:
                view.reload(XWalkView.RELOAD_NORMAL);
                break;
            case LOAD:
                view.load(args.getString(0),null);
                break;
            case POST_MESSAGE:
                try {
                    JSONObject eventInitDict = new JSONObject();
                    eventInitDict.put("data", args.getString(0));
                    view.evaluateJavascript("document.dispatchEvent(new MessageEvent('message', " + eventInitDict.toString() + "))", null);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    @Override
    public Map getExportedCustomDirectEventTypeConstants () {
        return MapBuilder.of(
            NavigationStateChangeEvent.EVENT_NAME,
            MapBuilder.of("registrationName", "onCrosswalkWebViewNavigationStateChange"),
            ErrorEvent.EVENT_NAME,
            MapBuilder.of("registrationName", "onCrosswalkWebViewError"),
            LoadFinishedEvent.EVENT_NAME,
            MapBuilder.of("registrationName", "onCrosswalkWebViewLoadFinished"),
            ProgressEvent.EVENT_NAME,
            MapBuilder.of("registrationName", "onCrosswalkWebViewProgress"),
            TopMessageEvent.EVENT_NAME,
            MapBuilder.of("registrationName", "onMessage")
        );
    }

    protected class XWalkActivityEventListener extends BaseActivityEventListener {
        private CrosswalkWebView crosswalkWebView;

        public XWalkActivityEventListener(CrosswalkWebView _crosswalkWebView) {
            crosswalkWebView = _crosswalkWebView;
        }

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            crosswalkWebView.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            crosswalkWebView.onActivityResult(requestCode, resultCode, data);
        }
    }
}
