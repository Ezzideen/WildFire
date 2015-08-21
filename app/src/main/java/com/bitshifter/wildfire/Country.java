package com.bitshifter.wildfire;

/**
 * Created by rohit on 20/8/15.
 */
public class Country{
    private int id;
    private String name;
    private String fireNumber;
    private String policeNumber;
    private String ambulanceNumber;

    public Country(int id, String name, String fireNumber, String policeNumber, String ambulanceNumber) {
        this.id = id;
        this.name = name;
        this.fireNumber = fireNumber;
        this.policeNumber = policeNumber;
        this.ambulanceNumber = ambulanceNumber;
    }

    public String getFireNumber() {
        return fireNumber;
    }

    public String getPoliceNumber() {
        return policeNumber;
    }

    public String getAmbulanceNumber() {
        return ambulanceNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
