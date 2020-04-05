package edu.sharif.sharif_dev.weather.secondPage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * class for converting DarkSky.net response to Java Objects
 */

public class ForecastResponse {

    @SerializedName("latitude")
    @Expose
    public Double latitude;
    @SerializedName("longitude")
    @Expose
    public Double longitude;
    @SerializedName("timezone")
    @Expose
    public String timezone;
    @SerializedName("currently")
    @Expose
    public Currently currently;
    @SerializedName("minutely")
    @Expose
    public Minutely minutely;
    @SerializedName("hourly")
    @Expose
    public Hourly hourly;
    @SerializedName("daily")
    @Expose
    public Daily daily;
    @SerializedName("flags")
    @Expose
    public Flags flags;
    @SerializedName("offset")
    @Expose
    public double offset;

}

class Currently {

    @SerializedName("time")
    @Expose
    public int time;
    @SerializedName("summary")
    @Expose
    public String summary;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("nearestStormDistance")
    @Expose
    public double nearestStormDistance;
    @SerializedName("precipIntensity")
    @Expose
    public double precipIntensity;
    @SerializedName("precipIntensityError")
    @Expose
    public double precipIntensityError;
    @SerializedName("precipProbability")
    @Expose
    public double precipProbability;
    @SerializedName("precipType")
    @Expose
    public String precipType;
    @SerializedName("temperature")
    @Expose
    public double temperature;
    @SerializedName("apparentTemperature")
    @Expose
    public double apparentTemperature;
    @SerializedName("dewPoint")
    @Expose
    public double dewPoint;
    @SerializedName("humidity")
    @Expose
    public double humidity;
    @SerializedName("pressure")
    @Expose
    public double pressure;
    @SerializedName("windSpeed")
    @Expose
    public double windSpeed;
    @SerializedName("windGust")
    @Expose
    public double windGust;
    @SerializedName("windBearing")
    @Expose
    public int windBearing;
    @SerializedName("cloudCover")
    @Expose
    public double cloudCover;
    @SerializedName("uvIndex")
    @Expose
    public double uvIndex;
    @SerializedName("visibility")
    @Expose
    public double visibility;
    @SerializedName("ozone")
    @Expose
    public double ozone;

}


class Daily {

    @SerializedName("summary")
    @Expose
    public String summary;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("data")
    @Expose
    public List<DailyData> data = null;

}


class MinutelyData {

    @SerializedName("time")
    @Expose
    public int time;
    @SerializedName("precipIntensity")
    @Expose
    public double precipIntensity;
    @SerializedName("precipIntensityError")
    @Expose
    public double precipIntensityError;
    @SerializedName("precipProbability")
    @Expose
    public double precipProbability;
    @SerializedName("precipType")
    @Expose
    public String precipType;

}


class HourlyData {

    @SerializedName("time")
    @Expose
    public int time;
    @SerializedName("summary")
    @Expose
    public String summary;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("precipIntensity")
    @Expose
    public double precipIntensity;
    @SerializedName("precipProbability")
    @Expose
    public double precipProbability;
    @SerializedName("precipType")
    @Expose
    public String precipType;
    @SerializedName("temperature")
    @Expose
    public double temperature;
    @SerializedName("apparentTemperature")
    @Expose
    public double apparentTemperature;
    @SerializedName("dewPoint")
    @Expose
    public double dewPoint;
    @SerializedName("humidity")
    @Expose
    public double humidity;
    @SerializedName("pressure")
    @Expose
    public double pressure;
    @SerializedName("windSpeed")
    @Expose
    public double windSpeed;
    @SerializedName("windGust")
    @Expose
    public double windGust;
    @SerializedName("windBearing")
    @Expose
    public int windBearing;
    @SerializedName("cloudCover")
    @Expose
    public double cloudCover;
    @SerializedName("uvIndex")
    @Expose
    public double uvIndex;
    @SerializedName("visibility")
    @Expose
    public double visibility;
    @SerializedName("ozone")
    @Expose
    public double ozone;

}


class DailyData {

    @SerializedName("time")
    @Expose
    public int time;
    @SerializedName("summary")
    @Expose
    public String summary;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("sunriseTime")
    @Expose
    public int sunriseTime;
    @SerializedName("sunsetTime")
    @Expose
    public int sunsetTime;
    @SerializedName("moonPhase")
    @Expose
    public double moonPhase;
    @SerializedName("precipIntensity")
    @Expose
    public double precipIntensity;
    @SerializedName("precipIntensityMax")
    @Expose
    public double precipIntensityMax;
    @SerializedName("precipIntensityMaxTime")
    @Expose
    public int precipIntensityMaxTime;
    @SerializedName("precipProbability")
    @Expose
    public double precipProbability;
    @SerializedName("precipType")
    @Expose
    public String precipType;
    @SerializedName("temperatureHigh")
    @Expose
    public double temperatureHigh;
    @SerializedName("temperatureHighTime")
    @Expose
    public int temperatureHighTime;
    @SerializedName("temperatureLow")
    @Expose
    public double temperatureLow;
    @SerializedName("temperatureLowTime")
    @Expose
    public int temperatureLowTime;
    @SerializedName("apparentTemperatureHigh")
    @Expose
    public double apparentTemperatureHigh;
    @SerializedName("apparentTemperatureHighTime")
    @Expose
    public int apparentTemperatureHighTime;
    @SerializedName("apparentTemperatureLow")
    @Expose
    public double apparentTemperatureLow;
    @SerializedName("apparentTemperatureLowTime")
    @Expose
    public int apparentTemperatureLowTime;
    @SerializedName("dewPoint")
    @Expose
    public double dewPoint;
    @SerializedName("humidity")
    @Expose
    public double humidity;
    @SerializedName("pressure")
    @Expose
    public double pressure;
    @SerializedName("windSpeed")
    @Expose
    public double windSpeed;
    @SerializedName("windGust")
    @Expose
    public double windGust;
    @SerializedName("windGustTime")
    @Expose
    public int windGustTime;
    @SerializedName("windBearing")
    @Expose
    public double windBearing;
    @SerializedName("cloudCover")
    @Expose
    public double cloudCover;
    @SerializedName("uvIndex")
    @Expose
    public double uvIndex;
    @SerializedName("uvIndexTime")
    @Expose
    public int uvIndexTime;
    @SerializedName("visibility")
    @Expose
    public double visibility;
    @SerializedName("ozone")
    @Expose
    public double ozone;
    @SerializedName("temperatureMin")
    @Expose
    public double temperatureMin;
    @SerializedName("temperatureMinTime")
    @Expose
    public int temperatureMinTime;
    @SerializedName("temperatureMax")
    @Expose
    public double temperatureMax;
    @SerializedName("temperatureMaxTime")
    @Expose
    public int temperatureMaxTime;
    @SerializedName("apparentTemperatureMin")
    @Expose
    public double apparentTemperatureMin;
    @SerializedName("apparentTemperatureMinTime")
    @Expose
    public int apparentTemperatureMinTime;
    @SerializedName("apparentTemperatureMax")
    @Expose
    public double apparentTemperatureMax;
    @SerializedName("apparentTemperatureMaxTime")
    @Expose
    public int apparentTemperatureMaxTime;

}




class Flags {

    @SerializedName("sources")
    @Expose
    public List<String> sources = null;
    @SerializedName("nearest-station")
    @Expose
    public double nearestStation;
    @SerializedName("units")
    @Expose
    public String units;

}

class Hourly {

    @SerializedName("summary")
    @Expose
    public String summary;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("data")
    @Expose
    public List<HourlyData> data = null;

}


class Minutely {

    @SerializedName("summary")
    @Expose
    public String summary;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("data")
    @Expose
    public List<MinutelyData> data = null;

}
