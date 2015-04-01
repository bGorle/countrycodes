# countrycodes


> **Getting the country code from android devices without asking the user.**

When we are trying to create an android application sometimes we need to get the user mobile number for registration. User needs to enter their mobile number and country code. Here in this article i would like to show how to get the user country code with out asking the user.

In android we are having an API to get the ISO country codes. Here is the method to get the ISO code from android devices. By using this method we will get the ISO country codes. First this method will try get the code from SIM by calling android API method `getSimCountryIso()`. This method will return ISO code if user is having the SIM card in mobile phone other  wise null. But this method won't work if we are not having the SIM card,  to get over from this android providing another API method getting the ISO code from Network with one  `getNetworkCountryIso`.

    public String getUserCountry() {
        try {
            final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) {
                return getCountryCodeFromISO(simCountry);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) {
                    return getCountryCodeFromISO(networkCountry);
                }
            }

        } catch (Exception e) {
        }
        return "";
    }

We got the ISO country code by using the above method, now we need to get the phone code from this ISO code. For that we at Fork Technologies  collected the list of countries with their ISO and Phone codes. We placed this list in strings.xml file.

> Reading the country list from xml file and placing the data in java object.

	 private List mCountryList; 
     private void fillCoutryInfo() {
        mCountryList = new ArrayList<Country>();

        String[] rl = getResources().getStringArray(
                R.array.countries_phone_iso);

        int countryListLength = rl.length;
        for (int i = 0; i < countryListLength; i++) {
            String[] g = rl[i].split(",");
            mCountryList.add(new Country(g[0], g[1], g[2]));
        }
    }

----------

> Country.java 

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


----------


We are comparing the ISO code with these country list by using this method it will give us the Country phone code thats all we can place this code in our `EditText`.
					       
    private String getCountryCodeFromISO(String isoCode) {
        String CountryZipCode = "";

        int size = mCountryList.size();
        for (int i = 0; i < size; i++) {
            if (mCountryList.get(i).getCountryIsoCode().trim().equalsIgnoreCase(isoCode.trim())) {
                CountryZipCode = mCountryList.get(i).getCountryPhoneCode();
                break;
            }
        }
        return CountryZipCode;
    }

Here is the complete code in [github](https://github.com/bGorle/countrycodes.git).

