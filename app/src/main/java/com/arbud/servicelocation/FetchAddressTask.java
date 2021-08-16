package com.arbud.servicelocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.LocaleList;
import android.text.TextUtils;

import com.arbud.servicelocation.FetchAddressTask.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressTask extends AsyncTask<Location, Void, String > {
    private Context mcontext;
    private onTaskCompleted mlistener;
    public FetchAddressTask(Context context, onTaskCompleted listener) {
        mcontext = context;
        mlistener = listener;
    }
    @Override
    protected String doInBackground(Location... locations) {
        Geocoder geocoder = new Geocoder(mcontext, Locale.getDefault());
        Location location = locations[0];
        List<Address> addresses = null;
        String resultMsg = "";
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses==null || addresses.size()==0) {
            resultMsg = "No Address Found";
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressPart = new ArrayList<>();
            for (int i = 0; i <= address.getMaxAddressLineIndex();i++) {
                addressPart.add(address.getAddressLine(i));
            }
            resultMsg = TextUtils.join("\n",addressPart);
        }

        return resultMsg;
    }

    @Override
    protected void onPostExecute(String s) {
        mlistener.onTaskCompleted(s);
        super.onPostExecute(s);
    }
    interface onTaskCompleted{
        void onTaskCompleted(String result);
    }
}
