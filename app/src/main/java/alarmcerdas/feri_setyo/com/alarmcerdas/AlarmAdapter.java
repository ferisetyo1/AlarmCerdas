package alarmcerdas.feri_setyo.com.alarmcerdas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmAdapter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("AlarmUp"));
    }

}
