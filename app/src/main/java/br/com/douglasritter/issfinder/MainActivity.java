package br.com.douglasritter.issfinder;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;


public class MainActivity extends ActionBarActivity {

    private GoogleMap mMap;
    private Location mCurrentLocation;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        getISSNow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public interface ISSService {
        @GET("/{directory}")
        void listObjects(@Path("directory") String directory, Callback<ISSNow> issCallback);
    }

    RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://api.open-notify.org")
            .build();

    public void getISSNow(){
        ISSService service = restAdapter.create(ISSService.class);
        service.listObjects("iss-now.json", new Callback<ISSNow>() {
            @Override
            public void success(ISSNow issNow, Response response) {
                Log.e("RESPONSE: ", issNow.toString());
                setPositionOnTheMap(issNow.getIssPosition().getLatitude(),
                        issNow.getIssPosition().getLongitude());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("FAIL: ", error.getMessage());
            }
        });
    }

    public void setPositionOnTheMap(String latitude, String longitude){

        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        /*Marker iss = mMap.addMarker(
                new MarkerOptions().position(latLng)
                        .title("ISS"));*/

        if(mCurrentLocation != null){

            LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            Marker user = mMap.addMarker(
                    new MarkerOptions().position(currentLatLng)
                            .title("Me"));
        }

        Marker iss = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("ISS")
                .snippet("International Space Station")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_iss)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 2));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(2), 2000, null);

    }




    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            mCurrentLocation = location;
            getISSNow();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };





}
