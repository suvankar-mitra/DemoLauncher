package com.suvankarmitra.demolauncher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Date time;
    private RelativeLayout mainRL;
    private TextView quote;
    private TextView author;
    private TextView clock;
    private TextView date;
    private ImageButton appDrawer;
    private TextView searchBar;

    private final String TAG = "MainActivity";
    private Thread timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRL = findViewById(R.id.main_activity);
        quote = findViewById(R.id.quote);
        author = findViewById(R.id.author);
        appDrawer = findViewById(R.id.button);
        clock = findViewById(R.id.clock);
        date = findViewById(R.id.date);
        searchBar = findViewById(R.id.search_bar);

        // To get user permissions
        getUserPermissions();
        setClickListeners();
        //getRandomQuote();

    }

    private void getUserPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG, "onRequestPermissionsResult: Got permission");
                    getUnreadSMSCount();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read SMS", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onRequestPermissionsResult: permission denied");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
        getSupportFragmentManager().popBackStack();
    }

    private void setTime(TextView clock, TextView date) {
        time = Calendar.getInstance().getTime();
        SpannableString ss1=  new SpannableString(android.text.format.DateFormat.format("h:mma", time));
        ss1.setSpan(new RelativeSizeSpan(0.2f), ss1.length()-2,ss1.length(), 0);
        clock.setText(ss1);
        SpannableString ss2=  new SpannableString(android.text.format.DateFormat.format("EE MMMM dd yyyy", time));
        ss2.setSpan(new RelativeSizeSpan(0.7f), ss2.length()-4,ss2.length(), 0);
        ss2.setSpan(new RelativeSizeSpan(0.7f), 0,3, 0);
        date.setText(ss2);
    }

    private void setClickListeners() {

        appDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
                startActivity(intent);
            }
        });

        ImageButton dialer = findViewById(R.id.button_1);
        dialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.dialer");
                if (i != null) {
                    startActivity(i); //null pointer check in case package name was not found
                }
            }
        });
        dialer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "Dialer was long pressed", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageButton message = findViewById(R.id.button_2);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.messaging");
                if (i != null) {
                    startActivity(i); //null pointer check in case package name was not found
                }
            }
        });

        ImageButton button3 = findViewById(R.id.button_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
                if (i != null) {
                    startActivity(i); //null pointer check in case package name was not found
                }
            }
        });

        ImageButton button4 = findViewById(R.id.button_4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getPackageManager().getLaunchIntentForPackage("com.android.camera");
                if (i != null) {
                    startActivity(i); //null pointer check in case package name was not found
                }
            }
        });

        TextView clock = findViewById(R.id.clock);
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.deskclock");
                if (i != null) {
                    startActivity(i); //null pointer check in case package name was not found
                }
            }
        });

        mainRL = findViewById(R.id.main_activity);
        mainRL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("MainActivity", "onLongClick: I was long pressed");
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .addToBackStack(MainActivity.class.getCanonicalName())
                        .setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)
                        .add(android.R.id.content, new LauncherSettingsFragment())
                        .commit();
                return true;
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.googlequicksearchbox");
                if (i != null) {
                    startActivity(i); //null pointer check in case package name was not found
                }
            }
        });
    }

    private void getRandomQuote() {
        new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String url = "https://quotesondesign.com/wp-json/posts?filter[orderby]=rand&filter[posts_per_page]=1";

                                StringRequest stringRequest = new StringRequest
                                        (Request.Method.GET, url, new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {
                                                Log.d("MainActivity", "Response: " + response.toString());
                                                try {
                                                    JSONArray jsonArray = new JSONArray(response);
                                                    String a = "- " + jsonArray.getJSONObject(0).getString("title").trim();
                                                    String q = jsonArray.getJSONObject(0).getString("content");
                                                    q = String.valueOf(Html.fromHtml(q)).trim();
                                                    quote.setText("\"" + q + "\"");
                                                    author.setText(a);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // TODO: Handle error
                                            }
                                        });

                                //creating a request queue
                                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                                //adding the string request to request queue
                                requestQueue.add(stringRequest);
                            }
                        });
                        Thread.sleep(1000 * 60 * 30);
                    }
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }

    private void getUnreadSMSCount() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            return;
        }

        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        Cursor c = getContentResolver().query(SMS_INBOX, null, "read = 0", null, null);
        int unreadMessagesCount = c.getCount();
        c.close();
        Log.d(TAG, "getUnreadSMSCount: " + unreadMessagesCount);
        ImageButton msg = findViewById(R.id.button_2);
        if (unreadMessagesCount > 0) {
            msg.setImageResource(R.drawable.ic_message_text_badge);
        } else {
            msg.setImageResource(R.drawable.ic_chat_black_24dp);
        }

        String[] projection = {CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE};
        String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE +" AND NEW = 1";
        Cursor c1 = getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, where, null, null);
        c1.moveToFirst();
        int missedCallCount = c1.getCount();
        c1.close();
        Log.d("CALL", ""+missedCallCount);
        ImageButton call = findViewById(R.id.button_1);
        if(missedCallCount > 0) {
            call.setImageResource(R.drawable.ic_phone_badge);
        } else {
            call.setImageResource(R.drawable.ic_call_black_24dp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: CALLED");

        timer = new Thread(){
            public void run() {
                try {
                    while (!isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setTime(clock, date);
                            }
                        });
                        // Get the count and update
                        getUnreadSMSCount();
                        // sleep
                        Thread.sleep(1000);
                    }
                    Log.d(TAG, "run: Thread killed");
                } catch (InterruptedException e) {
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer!=null && timer.isAlive() && !timer.isInterrupted()) {
            timer.interrupt();
        }
    }
}
