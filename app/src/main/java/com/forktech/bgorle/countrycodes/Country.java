package com.forktech.bgorle.countrycodes;

/**
 * Created by bGorle on 1/4/15.
 */
public class Country {

    private String countryName;
    private String countryIsoCode;
    private String countryPhoneCode;

    public Country(String countryName, String countryIsoCode, String countryPhoneCode) {
        this.countryName = countryName;
        this.countryIsoCode = countryIsoCode;
        this.countryPhoneCode = countryPhoneCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryName='" + countryName + '\'' +
                ", countryIsoCode='" + countryIsoCode + '\'' +
                ", countryPhoneCode='" + countryPhoneCode + '\'' +
                '}';
    }
}
