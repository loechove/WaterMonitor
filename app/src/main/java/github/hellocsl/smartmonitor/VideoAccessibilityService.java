package github.hellocsl.smartmonitor;


import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import github.hellocsl.smartmonitor.state.IMonitorService;
import github.hellocsl.smartmonitor.state.Impl.IdleState;
import github.hellocsl.smartmonitor.state.MonitorState;
import github.hellocsl.smartmonitor.utils.Constant;

import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOWS_CHANGED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;


/**
 * Created by chensuilun on 16-10-8.
 */
public class VideoAccessibilityService extends AccessibilityService implements IMonitorService {
    private static final String TAG = "VideoAccessibilityServi";
    public static boolean sIsEnable = false;

    private MonitorState mCurState;

    @Override
    public void setState(MonitorState state) {
        mCurState = state;
    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onServiceConnected: ");
        }
        sIsEnable = true;
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = TYPE_WINDOW_CONTENT_CHANGED | TYPE_WINDOWS_CHANGED | TYPE_WINDOW_STATE_CHANGED;

        // If you only want this service to work with specific applications, set their
        // package names here.  Otherwise, when the service is activated, it will listen
        // to events from all applications.
        info.packageNames = new String[]{Constant.QQ_PKG, Constant.DIALER, Constant.MEIZU_IN_CALL_PKG};

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
//        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.notificationTimeout = 100;

        this.setServiceInfo(info);
        setState(new IdleState(this));
        // FIXME: 16-10-9 for qqChat
//        setState(new QQChatState(this));
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onUnbind: ");
        }
        sIsEnable = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAccessibilityEvent: event:" + accessibilityEvent.getEventType());
        }
        if (mCurState != null) {
            mCurState.handle(accessibilityEvent);
        }
    }


    @Override
    public void onInterrupt() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onInterrupt: ");
        }
    }


    @Override
    public AccessibilityNodeInfo getWindowNode() {
        return getRootInActiveWindow();
    }
}
