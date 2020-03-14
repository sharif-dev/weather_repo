package edu.sharif.sharif_dev.weather;

import java.util.ArrayList;

class MapClass {
    private String type ;
    private Object query ;
    private ArrayList<CityClass> features;
    private String attribution;

    class CityClass{
        private String id;
        private String type;
        private Object place_type;
        private int relevance;
        private Object properties;
        private String text;
        private String place_name;
        private Object bbox;
        private ArrayList center;
        private Object geometry;
        private Object context;

        public ArrayList getCenter() {
            return center;
        }

        public String getPlace_name() {
            return place_name;
        }
    }

    public ArrayList<CityClass> getFeatures() {
        return features;
    }
}
