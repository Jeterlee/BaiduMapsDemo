package cn.jeterlee.baidumaps;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

public class MainActivity extends AppCompatActivity {
    private static final String LTAG = MainActivity.class.getSimpleName();
    private SDKReceiver mSDKReceiver;
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mMapView = (MapView) findViewById(R.id.bmapView);

        // setContentView(R.layout.activity_main);
        // TextView text = (TextView) findViewById(R.id.text_Info);
        // text.setTextColor(Color.YELLOW);
        // text.setText(String.format("欢迎使用百度地图Android SDK v%s", VersionInfo.getApiVersion()));
        // ListView mListView = (ListView) findViewById(R.id.listView);
        // // 添加ListItem，设置事件响应
        // mListView.setAdapter(new DemoListAdapter());
        // mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //     @Override
        //     public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //         onListItemClick(i);
        //     }
        // });

        // 注册 SDK 广播监听者
        IntentFilter filter = new IntentFilter();
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        mSDKReceiver = new SDKReceiver();
        registerReceiver(mSDKReceiver, filter);
    }


    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.d(LTAG, "action: " + s);
            // TextView text = (TextView) findViewById(R.id.text_Info);
            // text.setTextColor(Color.RED);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                // text.setText("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
                Toast.makeText(MainActivity.this, "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置",
                        Toast.LENGTH_SHORT).show();
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                // text.setText("网络出错");
                Toast.makeText(MainActivity.this, "网络出错!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void onListItemClick(int i) {
        Intent intent = new Intent(MainActivity.this, demos[i].clazz);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        // 取消监听 SDK 广播
        unregisterReceiver(mSDKReceiver);
    }

    // 适配器
    private class DemoListAdapter extends BaseAdapter {

        public DemoListAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return demos.length;
        }

        @Override
        public Object getItem(int i) {
            return demos[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            convertView = View.inflate(MainActivity.this, R.layout.demo_info_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView desc = (TextView) convertView.findViewById(R.id.desc);
            title.setText(demos[i].title);
            desc.setText(demos[i].desc);
            if (i >= 16) {
                title.setTextColor(Color.YELLOW);
            }
            return convertView;
        }
    }

    // 适配对象
    private static final DemoInfo[] demos = {};

    // 适配对象的格式信息
    private static class DemoInfo {
        private final int title;
        private final int desc;
        private final Class<? extends Activity> clazz;

        public DemoInfo(int title, int desc, Class<? extends Activity> clazz) {
            this.title = title;
            this.desc = desc;
            this.clazz = clazz;
        }
    }
}
