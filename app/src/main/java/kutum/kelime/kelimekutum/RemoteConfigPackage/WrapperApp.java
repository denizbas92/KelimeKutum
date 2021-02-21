package kutum.kelime.kelimekutum.RemoteConfigPackage;

import android.app.Application;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deniz on 27.2.2018.
 */

public class WrapperApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final FirebaseRemoteConfig remoteConfig=FirebaseRemoteConfig.getInstance();

        Map<String ,Object> defaultValue=new HashMap<>();
        defaultValue.put(UpdateHelper.KEY_UPDATE_STATE,false);
        defaultValue.put(UpdateHelper.KEY_UPDATE_VERSION,"1.0");
        defaultValue.put(UpdateHelper.KEY_UPDATE_URL,"my app url");
        defaultValue.put(UpdateHelper.KEY_MESSAGE,"message");
        remoteConfig.setDefaults(defaultValue);
        remoteConfig.fetch(30)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            remoteConfig.activateFetched();
                        }
                    }
                });
    }
}
