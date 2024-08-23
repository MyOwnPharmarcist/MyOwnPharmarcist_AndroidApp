package teamyj.dev.hrd_final_project.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import teamyj.dev.hrd_final_project.R;

public class AlarmFragment extends Fragment {

    private Ringtone ringtone;
    private Vibrator vibrator;
    private Uri alarmUri;

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        Context context = getContext();
        if (context != null) {
            ringtone = RingtoneManager.getRingtone(context, alarmUri);
            if (ringtone != null && !ringtone.isPlaying()) {
                ringtone.play();
            }

            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                long[] vibrationPattern = {0, 1000, 1000}; // 진동 패턴
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 0)); // 반복
                } else {
                    vibrator.vibrate(vibrationPattern, 0); // 반복
                }
            }
        }

        Button stopAlarmButton = view.findViewById(R.id.button_ok);
        stopAlarmButton.setOnClickListener(v -> stopAlarm());

        return view;
    }

    private void stopAlarm() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }

        // TimerFragment로 돌아가기 위해 액티비티를 종료
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
