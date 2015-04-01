package com.forktech.bgorle.countrycodes;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    private Spinner mCountrySpinner;

    private TextView mTextSpinner;
    private EditText metCountryCode;

    private ArrayList<Country> mCountryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCountrySpinner = (Spinner) findViewById(R.id.spinner);
        mTextSpinner = (TextView) findViewById(R.id.text_spinner);
        metCountryCode = (EditText) findViewById(R.id.et_country_code);

        fillCoutryInfo();

        mTextSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountriesDialog();
            }

        });

        metCountryCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    String text = ((TextView) v).getText().toString();
                    if (text != null && text.length() > 1)
                        return false;
                    else
                        return true;
                }
                return false;
            }
        });


        metCountryCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() <= 1) {
                    mTextSpinner.setText(R.string.select_country);
                    return;
                }
                int length = mCountryList.size();
                for (int i = 0; i < length; i++) {
                    if (mCountryList.get(i).getCountryPhoneCode().equalsIgnoreCase(s.subSequence(1, s.length()).toString())) {
                        mTextSpinner.setText(mCountryList.get(i).getCountryName());
                        break;
                    } else {
                        mTextSpinner.setText("Wrong country code");
                    }
                }
            }
        });

        String country = getUserCountry();

        metCountryCode.setText("+" + country);
        metCountryCode.setSelection(metCountryCode.getText().length());
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not
     * available)
     *
     * @return country code or null
     */
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

    private void showCountriesDialog() {
        fillCoutryInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_country);
        builder.setIcon(R.mipmap.ic_launcher);
        View v = getLayoutInflater().inflate(R.layout.country_list, null);
        final ListView listview = (ListView) v.findViewById(android.R.id.list);

        final CustomArrayAdapter adapter = new CustomArrayAdapter(this, R.layout.simple_list_item_horizontal, android.R.id.text1, mCountryList);

        EditText searchView = (EditText) v.findViewById(R.id.editText2);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
            }
        });


        listview.setAdapter(adapter);
        builder.setView(v);

        final AlertDialog dialog = builder.create();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                Country country = adapter.getItem(position);
                metCountryCode.setText("+" + country.getCountryPhoneCode());
                mTextSpinner.setText(country.getCountryName());
            }
        });

        dialog.show();
    }

    class CustomArrayAdapter extends ArrayAdapter<Country> {
        private List<Country> mList;
        private ArrayList<Country> mtempList;

        public CustomArrayAdapter(Context context, int layout, int textViewResouceId, List<Country> list) {
            super(context, layout, textViewResouceId, list);
            mList = list;
            mtempList = new ArrayList<Country>();
            mtempList.addAll(list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView text1 = (TextView) view.findViewById(android.R.id.text1);
            TextView text2 = (TextView) view.findViewById(android.R.id.text2);

            text1.setText(mList.get(position).getCountryName());
            text2.setText(mList.get(position).getCountryPhoneCode());
            return view;
        }

        @Override
        public Country getItem(int position) {
            return mList.get(position);
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            mList.clear();
            if (charText.isEmpty()) {
                mList.addAll(mtempList);
            } else {

                for (int i = 0; i < mtempList.size(); i++) {

                    if (mtempList.get(i).getCountryName().toLowerCase()
                            .contains(charText.toLowerCase())) {
                        mList.add(mtempList.get(i));
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
