package teamyj.dev.hrd_final_project.layout;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.concurrent.TimeUnit;

import teamyj.dev.hrd_final_project.R;
import teamyj.dev.hrd_final_project.main_system.AlarmReceiver;

public class TimerFragment extends Fragment implements View.OnClickListener {

    private long timeCountInMilliSeconds = 30 * 60000; // 기본값: 30분
    private long initialTimeCountInMilliSeconds = timeCountInMilliSeconds; // 리셋을 위한 초기 타이머 값

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private EditText editTextTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        initViews(view);
        initListeners();
        return view;
    }

    private void initViews(View view) {
        progressBarCircle = view.findViewById(R.id.progressBarCircle);
        editTextTime = view.findViewById(R.id.editTextTime);
        imageViewReset = view.findViewById(R.id.imageViewReset);
        imageViewStartStop = view.findViewById(R.id.imageViewStartStop);
    }

    private void initListeners() {
        imageViewReset.setOnClickListener(this);
        imageViewStartStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageViewReset) {
            reset();
        } else if (view.getId() == R.id.imageViewStartStop) {
            startStop();
        }
    }

    private void reset() {
        stopCountDownTimer();
        timeCountInMilliSeconds = initialTimeCountInMilliSeconds;
        setProgressBarValues();
        editTextTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
        startCountDownTimer();
    }

    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {
            setTimerValues();
            setProgressBarValues();
            imageViewReset.setVisibility(View.VISIBLE);
            imageViewStartStop.setImageResource(R.drawable.ic_baseline_stop_circle_24);
            editTextTime.setEnabled(false);
            timerStatus = TimerStatus.STARTED;
            startCountDownTimer();
        } else {
            imageViewReset.setVisibility(View.GONE);
            imageViewStartStop.setImageResource(R.drawable.ic_baseline_play_circle_24);
            editTextTime.setEnabled(true);
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }
    }

    private void setTimerValues() {
        String timeString = editTextTime.getText().toString().trim();
        if (!TextUtils.isEmpty(timeString)) {
            String[] timeParts = timeString.split(":");
            if (timeParts.length == 3) {
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);
                timeCountInMilliSeconds = (hours * 3600 + minutes * 60 + seconds) * 1000;
                initialTimeCountInMilliSeconds = timeCountInMilliSeconds; // 초기 타이머 값 업데이트
            } else if (timeParts.length == 2) {
                int minutes = Integer.parseInt(timeParts[0]);
                int seconds = Integer.parseInt(timeParts[1]);
                timeCountInMilliSeconds = (minutes * 60 + seconds) * 1000;
                initialTimeCountInMilliSeconds = timeCountInMilliSeconds; // 초기 타이머 값 업데이트
            } else if (timeParts.length == 1) {
                int minutes = Integer.parseInt(timeParts[0]);
                timeCountInMilliSeconds = minutes * 60000;
                initialTimeCountInMilliSeconds = timeCountInMilliSeconds; // 초기 타이머 값 업데이트
            }
        } else {
            Toast.makeText(getActivity(), "시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                editTextTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                editTextTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                setProgressBarValues();
                imageViewReset.setVisibility(View.GONE);
                imageViewStartStop.setImageResource(R.drawable.ic_baseline_play_circle_24);
                editTextTime.setEnabled(true);
                timerStatus = TimerStatus.STOPPED;
                startAlarm();
            }
        }.start();
        countDownTimer.start();
    }

    private void stopCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void setProgressBarValues() {
        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }

    private String hmsTimeFormatter(long milliSeconds) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) % TimeUnit.MINUTES.toSeconds(1));
    }

    private void startAlarm() {
        // 알람 시간 설정
        long triggerAtMillis = System.currentTimeMillis() + timeCountInMilliSeconds;

        Context context = getContext();
        if (context != null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }
        Log.d("TimerFragment", "Alarm set for: " + triggerAtMillis);
    }
}
