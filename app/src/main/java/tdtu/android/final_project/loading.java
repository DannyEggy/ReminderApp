package tdtu.android.final_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import java.util.logging.Handler;

public class loading {
    static Activity activity;
    static AlertDialog dialog;

    loading(Activity activity){
        this.activity=activity;
    }
    public static void startLoading(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading,null));
        builder.setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }
    public static void dismiss(){

        dialog.dismiss();
    }
}