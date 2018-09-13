package com.jordansexton.react.crosswalk.webview;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

public class LoadFinishedEvent extends Event<LoadFinishedEvent> {
    public static final String EVENT_NAME = "loadFinished";
    protected LoadFinishedEvent(int viewTag, long timestampMs) {
        super(viewTag);
    }
    @Override
    public String getEventName () {
        return EVENT_NAME;
    }
    @Override
    public void dispatch (RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }
    private WritableMap serializeEventData () {
        WritableMap eventData = Arguments.createMap();
        return eventData;
    }
}
