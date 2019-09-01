package com.example.claimfast2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class clientMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mlastLocation;
    LocationRequest mLocationRequest;
    private Button bLogout, bRequest,tdistance;
    private LatLng accidentLocation;
    private SupportMapFragment mapFragment;
    private boolean requestBol = false;
    Marker mAgentMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(clientMap.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }else{
            mapFragment.getMapAsync(this);
        }


        bRequest = (Button) findViewById(R.id.request);
        bLogout =  (Button) findViewById(R.id.logout);
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(clientMap.this, selectUser.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        bRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(requestBol){
                            requestBol = false;
                            geoQuery.removeAllListeners();
                            AgentLocationRef.removeEventListener(AgentLocationRefListener);

                            if(agentFoundId != null){
                                DatabaseReference agentRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Agents").child(agentFoundId);
                                agentRef.setValue(true);
                                agentFoundId=null;
                            }
                            agentFound=false;
                            radius=3;
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("clientRequest");
                            GeoFire geoFire = new GeoFire(ref);
                            geoFire.removeLocation(userId);

                            if(mAgentMarker != null){
                                mAgentMarker.remove();
                            }
                            bRequest.setText("Request another agent");
                }else{
                    requestBol = true;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("clientRequest");
                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.setLocation(userId, new GeoLocation(mlastLocation.getLatitude(),mlastLocation.getLongitude()));
                    accidentLocation = new LatLng(mlastLocation.getLatitude(),mlastLocation.getLongitude());
                    mAgentMarker = mMap.addMarker(new MarkerOptions().position(accidentLocation).title("Accident here"));
                    bRequest.setText("Getting your agent......");

                    getClosestAgent();
                }
            }
        });

    }

    private int radius = 1;
    private boolean agentFound = false;
    private String agentFoundId;
    GeoQuery geoQuery;
    public void getClosestAgent(){
        DatabaseReference agentLocation = FirebaseDatabase.getInstance().getReference().child("agentAvailable");
        GeoFire geoFire = new GeoFire(agentLocation);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(accidentLocation.latitude,accidentLocation.longitude),radius); //Selecting a agent within a radius of 1km area
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!agentFound && requestBol){
                    agentFound= true;
                    agentFoundId = key;


                    DatabaseReference agentRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Agents").child(agentFoundId);
                    String clientId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map = new HashMap();
                    map.put("clientAgentId",clientId);
                    agentRef.updateChildren(map);

                    getAgentLocation();
                    bRequest.setText("Looking for agent location");

                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if(!agentFound){
                    radius++;
                    getClosestAgent();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    DatabaseReference AgentLocationRef;
    private ValueEventListener AgentLocationRefListener;
    private void getAgentLocation(){
            AgentLocationRef = FirebaseDatabase.getInstance().getReference().child("AgentWorking").child(agentFoundId).child("l"); //In firebase l is the node that stores the lagnitude and longtude in the database
            AgentLocationRefListener = AgentLocationRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()&& requestBol){
                        List<Object> map = (List<Object>) dataSnapshot.getValue();
                        double locationLat = 0;
                        double locationlng = 0;
                        if(map.get(0) != null){
                            locationLat = Double.parseDouble(map.get(0).toString());
                        }
                        if(map.get(1) != null){
                            locationlng = Double.parseDouble(map.get(1).toString());
                        }
                        LatLng agentLatLng = new LatLng(locationLat,locationlng);
                        if(mAgentMarker != null){
                            mAgentMarker.remove();
                        }
                        Location locl = new Location("");
                        locl.setLatitude(accidentLocation.latitude);
                        locl.setLongitude(accidentLocation.longitude);

                        Location loc2 = new Location("");
                        loc2.setLatitude(agentLatLng.latitude);
                        loc2.setLongitude(agentLatLng.longitude);

                        float distance = locl.distanceTo(loc2);

                        tdistance = (Button) findViewById(R.id.tdistance);

                        tdistance.setText("hey hey");

                        if(distance<100){
                            tdistance.setText("Agent is here");
                        }else{
                            tdistance.setText("Agent Found: "+ String.valueOf(distance));
                        }

                        mAgentMarker = mMap.addMarker(new MarkerOptions().position(agentLatLng).title("Your Agent"));


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (getApplicationContext()!=null){

            mlastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //Camera moves with the location
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15)); //Vary from 1 to 21
    }}

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //High accuracy means battery drains more.Choose other options for energy saving if needed.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(clientMap.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    final int LOCATION_REQUEST_CODE = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case LOCATION_REQUEST_CODE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mapFragment.getMapAsync(this);
                }else{
                    Toast.makeText(getApplicationContext(),"Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
    //When a driver gets out from a activity zoom out
    @Override
    protected void onStop() {
        super.onStop();

    }

}
