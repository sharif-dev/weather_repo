package edu.sharif.sharif_dev.weather;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;


public class CustomHandler extends Handler {
    Context context;

    public CustomHandler(Context context) {
        this.context = context;
    }

    public void sendIntMessage(int number) {
        Message message = new Message();
        message.what = number;
        message.arg1 = 1;
        handleMessage(message);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case R.string.search_input_error:
                makeToast(R.string.search_input_error);
                return;
            case R.string.mapbox_error:
                makeToast(R.string.mapbox_error);
                return;
            case R.string.token_error:
                makeToast(R.string.token_error);
                return;
            case R.string.forbidden:
                makeToast(R.string.forbidden);
                return;
            case R.string.not_found:
                makeToast(R.string.not_found);
                return;
            case R.string.query_length_error:
                makeToast(R.string.query_length_error);
                return;
            case R.string.rate_error:
                makeToast(R.string.rate_error);
                return;
        }

    }

    private void makeToast(int stringCode) {
        Toast toast = Toast.makeText(context, stringCode, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        toast.show();
    }
}
