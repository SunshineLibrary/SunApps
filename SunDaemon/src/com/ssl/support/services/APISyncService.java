package com.ssl.support.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.ssl.support.sync.APISyncTask;

public class APISyncService extends Service {

	private boolean syncInProgress;
	private APISyncTask syncTask;
	private static final String IP = "ssl-mock.herokuapp.com";
	private static final long MIN_DELAY = 1200000;
	private ConnectivityManager cm;
    private PowerManager.WakeLock wakeLock;
    private static final int DONE = 1;

    private static Runnable wakeUpRunner;
    private static Handler handler;
    private Handler mhandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
if(msg.what == DONE){
	System.out.println("Liu:更新数据成功");
	Toast.makeText(getApplicationContext(), "成功更新数据", 0).show();
}
stopSelf();
		}
    	
    };

    @Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!syncInProgress) {
            Log.v(getClass().getName(), "Starting APISyncTask...");
            acquireLock();
			syncTask.execute();
			syncInProgress = true;
		}
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		syncInProgress = false;
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
		syncTask = new APISyncTask(this) {
			@Override
			protected void onPostExecute(Integer result) {
				if (result.intValue() == SUCCESS) {
                    Log.v(getClass().getName(), "API sync completed successfully.");
                    //hereLiu:
                    mhandler.sendEmptyMessage(DONE);
System.out.println("发送消息出去");
				} else {
                    Log.v(getClass().getName(), "API sync failed.");
                }
				/*if(isSuccessful){
					stopSelf();
				}*/
			}
		};
	}

	@Override
	public void onDestroy() {
		if (isConnected()) {
            long delay = (long) Math.floor(Math.random() * MIN_DELAY + MIN_DELAY);
            getHandler().postDelayed(getWakeUpRunner(), delay);
		}
        releaseLock();
	}

    private static Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    private Runnable getWakeUpRunner() {
        if (wakeUpRunner == null) {
            wakeUpRunner = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplication(), APISyncService.class);
                    getBaseContext().startService(intent);
                }
            };
        } else {
            getHandler().removeCallbacks(wakeUpRunner);
        }
        return wakeUpRunner;
    }

	public boolean isConnected() {
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            return cm.getActiveNetworkInfo().isConnected();
        } else {
            Log.v(getClass().getName(), "Device is not connected...");
            return false;
        }
	}

    public void acquireLock() {
        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }

    public void releaseLock() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}
