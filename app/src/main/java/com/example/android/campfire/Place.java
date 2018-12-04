package com.example.android.campfire;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Place {
    private Integer Id;
    private String Name;
    private LatLng Position;
    private String Creator;
    private List<Feature> Features;
    private String Image;

    public Place(String name, LatLng position, String creator, List<Feature> features, String image) {
        Name = name;
        Position = position;
        Creator = creator;
        Features = features;
        Image = image;
    }

    public Integer getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public LatLng getPosition() {
        return Position;
    }

    public String getCreator() {
        return Creator;
    }

    public List<Feature> getFeatures() {
        return Features;
    }

    public String getImage() {
        return Image;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPosition(LatLng position) {
        Position = position;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public void setImage(String image) {
        Image = image;
    }

}
