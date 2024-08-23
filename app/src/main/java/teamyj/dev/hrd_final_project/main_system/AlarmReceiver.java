package teamyj.dev.hrd_final_project.main_system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import teamyj.dev.hrd_final_project.layout.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
}