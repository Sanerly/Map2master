package com.vison.base_map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 罗盘(方向 0 ` 360)
 * Created by xiaoshu on 2017-7.
 */
public class CompassUtils {
    private SensorManager mSensorManager;
    private CompassLister compassLister;
    private float mRotation = 0;
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mRotation = event.values[SensorManager.DATA_X];
            if (compassLister != null) {
                compassLister.onOrientationChange(mRotation);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public interface CompassLister {
        void onOrientationChange(float orientation);
    }

    public CompassUtils(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // 方向传感器（过时了）
        Sensor orientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (orientation != null) {
            //注册监听
            boolean boo = mSensorManager.registerListener(mSensorEventListener, orientation, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void setCompassLister(CompassLister compassLister) {
        this.compassLister = compassLister;
    }

    public void unbind() {
        mSensorManager.unregisterListener(mSensorEventListener);
    }
}
