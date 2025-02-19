package com.tourch.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Ahmet Ertugrul OZCAN
 * Cihazin konum bilgisini goruntuler
 */
public class GpsTracker extends Service implements LocationListener
{
    private final Context mContext;

    // Cihazda gps acik mi?
    boolean isGPSEnabled = false;

    // Cihazda veri baglantisi aktif mi?
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    // Konum
    Location location;
    // Enlem
    double latitude;
    // Boylam
    double longitude;

    // Konum guncellemesi gerektirecek minimum degisim miktari
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // metre

    // Konum guncellemesi gerektirecek minimum sure miktari
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // dakika

    // LocationManager nesnesi
    protected LocationManager locationManager;

    //
    // Kurucu Metod - Constructor
    //
    public GpsTracker(Context context)
    {
        this.mContext = context;
        getLocation();
    }

    //
    // Konum bilgisini dondurur
    //
    public Location getLocation()
    {
        try
        {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // GPS acik mi?
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Internet acik mi?
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled)
            {

            }
            else
            {
                this.canGetLocation = true;

                // Once internetten alinan konum bilgisi kayitlanir
                if (isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // GPS'ten alinan konum bilgisi;
                if (isGPSEnabled)
                {
                    if (location == null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return location;
    }

    // Enlem bilgisini dondurur
    public double getLatitude()
    {
        if(location != null)
        {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    // Boylam bilgisini dondurur
    public double getLongitude()
    {
        if(location != null)
        {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    @Override
    public void onLocationChanged(Location location)
    {
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }




}
