package edu.sharif.sharif_dev.weather;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;



public class CustomHandler extends Handler {
    Context context ;

    public CustomHandler(Context context){
        this.context = context;
    }

    public void sendIntMessage(int number){
        Message message = new Message();
        message.what = number;
        message.arg1 = 1;
        handleMessage(message);
    }

    @Override
    public void handleMessage(Message msg){
        super.handleMessage(msg);
        if(msg.what == R.string.search_input_error){
            Toast toast = Toast.makeText(context, R.string.search_input_error, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            toast.show();
        }
        if(msg.what == R.string.mapbox_error){
            Toast toast = Toast.makeText(context, R.string.mapbox_error, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            toast.show();
        }

    }
}
