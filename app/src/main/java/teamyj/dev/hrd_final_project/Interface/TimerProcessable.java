package teamyj.dev.hrd_final_project.Interface;

public interface TimerProcessable {
    void onAlarm(long timer);
    void setTimer(long time);
    long getTimer();
}
