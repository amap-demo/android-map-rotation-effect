package com.amap.android_map_rotation_effect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements AMap.OnMapLoadedListener, AMap.OnMapTouchListener {
    private MapView mapView;
    private AMap aMap;
    public static final LatLng CENTER = new LatLng(39.90403, 116.407525);// 地图旋转中心点，可以为当前位置
    private final float TOUCH_SCALE_FACTOR = 180.0f / 640;
    private float mPreviousX;
    private float mPreviousY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        // 设置所有手势是否可用，禁止手势
        aMap.getUiSettings().setAllGesturesEnabled(false);
        //隐藏3D楼块
        aMap.showBuildings(false);
        //隐藏地图上文字
        aMap.showMapText(false);
        //地图加载完成回调监听
        aMap.setOnMapLoadedListener(this);
        //监听地图手势
        aMap.setOnMapTouchListener(this);
        //设置默认的缩放按钮不显示
        aMap.getUiSettings().setZoomControlsEnabled(false);

    }

    @Override
    public void onMapLoaded() {
        //设置地图中心点为屏幕的1/2和3/4位置
        aMap.setPointToCenter(mapView.getWidth()/2, mapView.getHeight()/4*3);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(CENTER, 18, 60, 0)));
        //添加Marker
        Marker marker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark)));
        marker.setPositionByPixels(mapView.getWidth()/2, mapView.getHeight()/4*3);
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > mapView.getHeight() /4*3) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < mapView.getWidth() / 2) {
                    dy = dy * -1 ;
                }
                CameraPosition cameraPosition = aMap.getCameraPosition();
                cameraPosition = new CameraPosition(CENTER, 18, 60, (cameraPosition.bearing - ((dx + dy) * TOUCH_SCALE_FACTOR)));
                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
        mPreviousX = x;
        mPreviousY = y;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
