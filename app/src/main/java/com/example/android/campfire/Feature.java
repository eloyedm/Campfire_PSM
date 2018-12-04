package com.example.android.campfire;

public class Feature {
    private String Name;
    private Integer Id;

    public Feature(String name, Integer id) {
        Name = name;
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public Integer getId() {
        return Id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setId(Integer id) {
        Id = id;
    }
}
