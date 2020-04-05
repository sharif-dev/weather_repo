package edu.sharif.sharif_dev.weather.secondPage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *  error in DarkSky.net Response
 */

public class ForecastResponseError {
    @SerializedName("code")
    @Expose
    private int error_code;
    @SerializedName("error")
    @Expose
    private String message;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
