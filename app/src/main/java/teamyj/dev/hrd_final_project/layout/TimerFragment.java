package teamyj.dev.hrd_final_project.layout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
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

import java.util.concurrent.TimeUnit;

import teamyj.dev.hrd_final_project.R;

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
        // 뷰 초기화
        initViews(view);
        // 리스너 초기화
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
        int id = view.getId();
        if (id == R.id.imageViewReset) {
            reset();
        } else if (id == R.id.imageViewStartStop) {
            startStop();
        }
    }

    private void reset() {
        stopCountDownTimer();
        // 타이머를 리셋할 때 초기 값을 다시 설정합니다.
        timeCountInMilliSeconds = initialTimeCountInMilliSeconds;
        setProgressBarValues();
        editTextTime.setText(hmsTimeFormatter(timeCountInMilliSeconds)); // 시간을 초기 값으로 복원
        startCountDownTimer();
    }

    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {
            setTimerValues(); // 타이머 시작 전 시간을 설정
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
            if (timeParts.length == 3) { // HH:MM:SS 형식인지 확인
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);
                timeCountInMilliSeconds = (hours * 3600 + minutes * 60 + seconds) * 1000;
                initialTimeCountInMilliSeconds = timeCountInMilliSeconds; // 초기 타이머 값을 저장
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "시간 형식이 올바르지 않습니다", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "시간을 설정해주세요", Toast.LENGTH_LONG).show();
        }
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeCountInMilliSeconds = millisUntilFinished;
                editTextTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                editTextTime.setText(hmsTimeFormatter(0)); // 타이머가 끝나면 00:00:00으로 설정
                setProgressBarValues();
                imageViewReset.setVisibility(View.GONE);
                imageViewStartStop.setImageResource(R.drawable.ic_baseline_play_circle_24);
                editTextTime.setEnabled(true);
                timerStatus = TimerStatus.STOPPED; // 타이머 상태를 STOPPED로 업데이트
            }
        }.start();
    }

    private void stopCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void setProgressBarValues() {
        progressBarCircle.setMax((int) (initialTimeCountInMilliSeconds / 1000));
        progressBarCircle.setProgress((int) (timeCountInMilliSeconds / 1000));
    }

    private String hmsTimeFormatter(long milliSeconds) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
    }
}
