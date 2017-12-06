//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.agora.tracker.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer {
    private SensorManager sensorManager = null;
    private boolean hasStarted = false;
    private static Accelerometer.CLOCKWISE_ANGLE rotation;
    private SensorEventListener accListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent arg0) {
            if(arg0.sensor.getType() == 1) {
                float x = arg0.values[0];
                float y = arg0.values[1];
                float z = arg0.values[2];
                if(Math.abs(x) > 3.0F || Math.abs(y) > 3.0F) {
                    if(Math.abs(x) > Math.abs(y)) {
                        if(x > 0.0F) {
                            Accelerometer.rotation = Accelerometer.CLOCKWISE_ANGLE.Deg0;
                        } else {
                            Accelerometer.rotation = Accelerometer.CLOCKWISE_ANGLE.Deg180;
                        }
                    } else if(y > 0.0F) {
                        Accelerometer.rotation = Accelerometer.CLOCKWISE_ANGLE.Deg90;
                    } else {
                        Accelerometer.rotation = Accelerometer.CLOCKWISE_ANGLE.Deg270;
                    }
                }
            }

        }
    };

    public Accelerometer(Context ctx) {
        this.sensorManager = (SensorManager)ctx.getSystemService("sensor");
        rotation = Accelerometer.CLOCKWISE_ANGLE.Deg0;
    }

    public void start() {
        if(!this.hasStarted) {
            this.hasStarted = true;
            rotation = Accelerometer.CLOCKWISE_ANGLE.Deg0;
            this.sensorManager.registerListener(this.accListener, this.sensorManager.getDefaultSensor(1), 3);
        }
    }

    public void stop() {
        if(this.hasStarted) {
            this.hasStarted = false;
            this.sensorManager.unregisterListener(this.accListener);
        }
    }

    public static int getDirection() {
        return rotation.getValue();
    }

    public static enum CLOCKWISE_ANGLE {
        Deg0(0),
        Deg90(1),
        Deg180(2),
        Deg270(3);

        private int value;

        private CLOCKWISE_ANGLE(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
