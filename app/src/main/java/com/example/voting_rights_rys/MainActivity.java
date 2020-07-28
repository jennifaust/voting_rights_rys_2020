package com.example.voting_rights_rys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
<<<<<<< HEAD
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
=======
import android.widget.TextView;
>>>>>>> 49d38f8f070c2bbe2edc45abc38de11a5e46633f

import com.google.android.gms.gcm.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "homeChannelID";
    public static final int NOTIFICATION_ID1 = 12345678;
    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        //Get the Intent which started this activity (address string from Settings)
        Intent intent = getIntent();
        //Using the key to get the bundle (address)
        String pollplace = intent.getStringExtra(Elections.pollingLocation);
        System.out.println("pollplace: "+ pollplace);
        if (pollplace != null) {
            //TEST THAT ADDRESS IS RECEIVED FROM SETTINGS
            TextView polltext = findViewById(R.id.pollingplace);
            polltext.setText(pollplace);
        }

        //Initialize bottom nav bar and select "Home"
        BottomNavigationView bottomNavView = findViewById(R.id.bot_nav);
        bottomNavView.setSelectedItemId(R.id.home);

        //Start a new activity when a nav bar item is selected
        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.elections:
                        startActivity(new Intent(getApplicationContext(), Elections.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.resources:
                        startActivity(new Intent(getApplicationContext(), Resources.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }

        });

<<<<<<< HEAD
        //Use to send notification
        //notificationManager = NotificationManagerCompat.from(this);
        //editTextTitle = findViewById(R.id.edit_notification_title);
        //editTextMessage = findViewById(R.id.edit_notification_message);


        /*//Test the app with a dummy button
        Button b1 = (Button) findViewById(R.id.button7);
        b1.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendNotification();
            }
        });*/
    }
    //create and send notification
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification (View v){
        System.out.println("Inside sendNotification button");

        //When notification is pressed, direct to Elections class
        Intent intent = new Intent(this, Elections.class);

        //Create a back stack to preserve user navigation experience
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Create the notification
        Notification.Builder notificationTest = new Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_small_icon)
                .setContentTitle("Test notification")
                .setContentText("test test test")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                //Pass pendingIntent to the app
                .setContentIntent(pendingIntent) //set up the intent to launch when user taps app
                .setAutoCancel(true); //automatically removes app when user taps it

        //Build notification and display notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID1, notificationTest.build());
    }

    //Initializing the notification
    /*@RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification(){
        System.out.println("Inside app button");
        //When app is pressed, direct to Elections class
        Intent intent = new Intent(this, Elections.class);
        //Build PendingIntent with a back stack to preserve user navigation experience
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //have to change these flag configurations to match the intent
        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addNextIntentWithParentStack(intent);
        //Gets the PendingIntent containing back stack
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        //PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build the app
        //Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID)
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_notification_small_icon)
                .setContentTitle("Testing app")
                .setContentText("Notification test test test")
                .setPriority(Notification.PRIORITY_DEFAULT)
                //Pass pendingIntent to the app
                .setContentIntent(pendingIntent) //set up the intent to launch when user taps app
                .setAutoCancel(true); //automatically removes app when user taps it

        //Show app
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID1, builder.build());

        //NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        //notificationManagerCompat.notify(NOTIFICATION_ID1, builder.build());
    }*/

    /*//Create the app channel - should call this code repeatedly
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        //Create app channel
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        //set importance to determine how to interrupt user with notifications from this channel
        NotificationChannel channel_1 = new NotificationChannel(CHANNEL_ID, name, importance);
        channel_1.setDescription(description);

        //Register the channel with the system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel_1);


    }*/
=======
    }

>>>>>>> 49d38f8f070c2bbe2edc45abc38de11a5e46633f
}