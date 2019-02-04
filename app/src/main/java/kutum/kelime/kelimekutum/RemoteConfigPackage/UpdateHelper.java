package kutum.kelime.kelimekutum.RemoteConfigPackage;

import android.content.Context;
import android.text.TextUtils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by deniz on 27.2.2018.
 */

public class UpdateHelper {
    public static final String KEY_UPDATE_STATE="is_update";
    public static final String KEY_UPDATE_VERSION="version";
    public static final String KEY_UPDATE_URL="update_url";
    public static final String KEY_MESSAGE="message";

    public interface OnUpdateCheckListener{
        void onUpdateCheckListener(String urlApp,String message);
    }

    public static Builder with(Context context){
        return new Builder(context);
    }

    private Context context;
    private OnUpdateCheckListener onUpdateCheckListener;

    public UpdateHelper(Context context, OnUpdateCheckListener onUpdateCheckListener) {
        this.context = context;
        this.onUpdateCheckListener = onUpdateCheckListener;
    }

    public void check(){
        FirebaseRemoteConfig remoteConfig=FirebaseRemoteConfig.getInstance();
        if(remoteConfig.getBoolean(KEY_UPDATE_STATE)){
            String currentVersion=remoteConfig.getString(KEY_UPDATE_VERSION);
            String appVersion=getAppVersion(context);
            String updateURL=remoteConfig.getString(KEY_UPDATE_URL);
            String message=remoteConfig.getString(KEY_MESSAGE);
            if(!TextUtils.equals(currentVersion,appVersion) && onUpdateCheckListener!=null)
                onUpdateCheckListener.onUpdateCheckListener(updateURL,message);
        }
    }

    private String getAppVersion(Context context) {
        String result= "";
        try{
            result=context.getPackageManager().getPackageInfo(context.getPackageName(),0)
                    .versionName;
            result=result.replaceAll("[a-zA-Z] |-","");
        }catch (Exception e){

        }
        return result;
    }

    public static class Builder{
        private Context context;
        private OnUpdateCheckListener onUpdateCheckListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateCheck(OnUpdateCheckListener onUpdateCheckListener){
            this.onUpdateCheckListener=onUpdateCheckListener;
            return this;
        }

        public UpdateHelper build(){
            return new UpdateHelper(context,onUpdateCheckListener);
        }

        public UpdateHelper check(){
            UpdateHelper updateHelper=build();
            updateHelper.check();

            return updateHelper;
        }
    }
}
