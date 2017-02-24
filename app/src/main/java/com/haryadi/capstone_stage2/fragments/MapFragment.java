package com.haryadi.capstone_stage2.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haryadi.capstone_stage2.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


//Fragment to display map

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapLongClickListener{

    @BindView(R.id.mapView) MapView mMapView;
    @BindView(R.id.coord)  CoordinatorLayout layout;
    @BindView(R.id.toolbarText) EditText search;

    @BindView(R.id.wifi_enable) FloatingActionButton wifiEnable;
    @BindView(R.id.bluetooth_enable) FloatingActionButton bluetoothEnable;
    @BindView(R.id.location_enable) FloatingActionButton locationEnable;
    @BindView(R.id.floatingActionMenu) FloatingActionMenu floatingActionMenu;
    private GoogleMap map;
    GoogleApiClient mGoogleApiClient;

    Location mLastLocation;

    ArrayList<MarkerOptions> markers = new ArrayList<>();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    public static final String SAVE_MAP_STATE = "mapview";
    public static final String SAVE_MARKER = "marker";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).
                build();

        floatingActionMenu.setClosedOnTouchOutside(true);
        wifiEnable.setOnClickListener(getOnClick(floatingActionMenu));
        bluetoothEnable.setOnClickListener(getOnClick(floatingActionMenu));
        locationEnable.setOnClickListener(getOnClick(floatingActionMenu));

        return rootView;
    }

    public View.OnClickListener getOnClick(final FloatingActionMenu fm) {
        FloatingActionButton b;
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.wifi_enable) {
                    fm.close(true);
                    showWifiEditDialog("WIFI");
                   // checkIfWifiEnabled(getActivity());
                } else if (v.getId() == R.id.bluetooth_enable) {
                    fm.close(true);
                    showBluetoothEditDialog("BLUETOOTH");
                    //checkIfBluetoothEnabled(getActivity());

                } else if (v.getId() == R.id.location_enable) {
                    Toast t = Toast.makeText(getActivity(), getString(R.string.location_msg), Toast.LENGTH_SHORT);
                    t.show();
                    fm.close(true);
                }
            }
        };
    }

    private void showWifiEditDialog(String title) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateWifiFragment editNameDialogFragment =  CreateWifiFragment.newInstance(title, false, null);
        editNameDialogFragment.show(fm, title);
    }

    private void showBluetoothEditDialog(String title) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateBluetoothFragment editNameDialogFragment =  CreateBluetoothFragment.newInstance(title, false, null);
        editNameDialogFragment.show(fm, title);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState==null) {
            mMapView.onCreate(null);
            mMapView.getMapAsync(this);
        }
        else{
            mMapView.onCreate(savedInstanceState.getBundle(SAVE_MAP_STATE));
            mMapView.getMapAsync(this);
        }
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(SAVE_MARKER)){
                markers = savedInstanceState.getParcelableArrayList(SAVE_MARKER);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.map_style));

            if (!success) {
                Log.e("Style", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Style", "Can't find style. Error: ", e);
        }
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        map.setPadding(5, 480, 5, 5);
        map.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
        map.setOnMapLongClickListener(this);
        if (markers.size() > 0) {
            Log.v("ActivityCreate", String.valueOf(markers.size()));
            for (int i = 0; i < markers.size(); i++) {
                Log.v("hhh", String.valueOf(markers.size()));
                MarkerOptions latLng = markers.get(i);
                BitmapDescriptor defaultMarker =
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                Marker marker = map.addMarker(latLng);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                map.setMyLocationEnabled(true);
                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        latLng, 14));
            }
        }
    }

    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(layout, "Permission reqd to access current location",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    })
                    .show();
        } else {

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.v("InsideReq", String.valueOf(requestCode));
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (mLastLocation != null) {
                            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    latLng, 12));
                        }
                        return;
                    }

                } else {
                    requestPermission();
                }
                break;
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }
}


