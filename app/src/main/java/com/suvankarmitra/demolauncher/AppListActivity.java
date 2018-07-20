package com.suvankarmitra.demolauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppListActivity extends AppCompatActivity {

    private PackageManager manager;
    private List<Item> apps;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        loadApps();
        loadListView();
        addClickListener();
    }

    private void loadApps() {
        manager = getPackageManager();
        apps = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities =  manager.queryIntentActivities(i, 0);
        for (ResolveInfo r : availableActivities) {
            Item app = new Item();
            app.label = r.activityInfo.packageName; // package name
            app.name = r.loadLabel(manager); // app name
            app.icon = r.loadIcon(manager); // app icon
            Log.d("PackageName", "loadApps: "+app.name + "::" + app.label);
            apps.add(app);
        }
        Collections.sort(apps);
    }

    private void loadListView() {
        list = findViewById(R.id.list);
        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, R.layout.item, apps) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item, null);
                }

                ImageView icon = convertView.findViewById(R.id.icon);
                icon.setImageDrawable(apps.get(position).icon);

                TextView name = convertView.findViewById(R.id.name);
                name.setText(apps.get(position).name);

                return convertView;
            }
        };

        list.setAdapter(adapter);
    }

    private void addClickListener() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = manager.getLaunchIntentForPackage(apps.get(position).label.toString());
                startActivity(i);
            }
        });
    }
}
