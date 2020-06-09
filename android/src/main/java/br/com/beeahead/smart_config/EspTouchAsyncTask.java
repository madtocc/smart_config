package br.com.beeahead.smart_config;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.com.beeahead.smart_config.esptouch.EsptouchTask;
import br.com.beeahead.smart_config.esptouch.IEsptouchResult;
import br.com.beeahead.smart_config.esptouch.IEsptouchTask;
import io.flutter.plugin.common.MethodChannel;

public class EspTouchAsyncTask extends AsyncTask<byte[], Void, List<IEsptouchResult>> {
    private Context context;
    private MethodChannel channel;
    private final Object mLock = new Object();
    private IEsptouchTask mEsptouchTask;
    EspTouchAsyncTask(Context context, MethodChannel channel) {
        this.context = context;
        this.channel = channel;
    }

    void cancelTask(){
        cancel(true);
        mEsptouchTask.interrupt();
    }

    @Override
    protected List<IEsptouchResult> doInBackground(byte[]... bytes) {
        int taskResultCount;
        synchronized (mLock) {
            byte[] apSsid = bytes[0];
            byte[] apBssid = bytes[1];
            byte[] apPassword = bytes[2];
            byte[] deviceCountData = bytes[3];
//            byte[] broadcastData = bytes[4];
            taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));
            mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, context);
        }
        return mEsptouchTask.executeForResults(taskResultCount);
    }
    @Override
    protected void onPostExecute(List<IEsptouchResult> result) {
        if (result == null) {
            // Log.i("OI","Create Esptouch task failed, the esptouch port could be used by other thread");
            return;
        }
        IEsptouchResult firstResult = result.get(0);
        // check whether the task is cancelled and no results received
        if (!firstResult.isCancelled()) {
            if (firstResult.isSuc()) {
                StringBuilder sb = new StringBuilder();
                for (IEsptouchResult resultInList : result) {
                    sb.append(resultInList.getBssid())
                            .append("-")
                            .append(resultInList.getInetAddress().getHostAddress());
                }
                // Log.i("OI",sb.toString());
                channel.invokeMethod("showESPs",sb.toString());
            } else {

                // Log.i("OI","FAIL");
                channel.invokeMethod("showESPs","fail");
            }
        }
    }

}
