package teamyj.dev.hrd_final_project.Interface;

import android.content.res.AssetManager;

import java.util.concurrent.ExecutorService;

public interface ApplicationGettable {
    ExecutorService getExecutor();
    AssetManager getAssetManager();
}
