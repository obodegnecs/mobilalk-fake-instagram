package com.example.inspigram;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = HomeActivity.class.getName();
    public static final int RESULT_TAG = 69;
    public static final int GRID_NUMBER = 1;
    public static final int PERMISSION_CODE = 69;

    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private ArrayList<Content> contentList;
    private ContentAdapter contentAdapter;

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference contents;

    private NotificationHandler notificationHandler;

    private int queryLimit = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseFirestore = FirebaseFirestore.getInstance();
        contents = firebaseFirestore.collection("Contents");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            Log.d(TAG, "Authenticated user!");
            //Toast.makeText(HomeActivity.this, "Üdvözlünk " + firebaseUser.getProviderData() + "!", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Not authenticated loser!");
            finish();
        }

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, GRID_NUMBER));
        contentList = new ArrayList<>();
        contentAdapter = new ContentAdapter(this, contentList);
        recyclerView.setAdapter(contentAdapter);



        queryData();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(broadcastReceiver, intentFilter);

        notificationHandler = new NotificationHandler(this);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action == null) {
                return;
            }

            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    queryLimit = 5;
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    queryLimit = 2;
                    break;
            }

            queryData();
        }
    };

    @SuppressLint("NotifyDataSetChanged")
    private void queryData() {
        contentList.clear();

        //READ
        contents.limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                Content content = queryDocumentSnapshot.toObject(Content.class);
                content.setId(queryDocumentSnapshot.getId());
                contentList.add(content);
            }

            if (contentList.size() == 0) {
                initializeContents();
                queryData();
            }

            contentAdapter.notifyDataSetChanged();
        });

    }

    private void initializeContents() {
        String[] userNames = getResources().getStringArray(R.array.userNames);
        String[] comments = getResources().getStringArray(R.array.comments);
        TypedArray profilePictures = getResources().obtainTypedArray(R.array.profilePictures);
        TypedArray contentImages = getResources().obtainTypedArray(R.array.contentImages);

        Log.i(TAG, "initializeContents: ");

        for (int i = 0; i < userNames.length; i++) {
            //CREATE
            contents.add(new Content(
                    profilePictures.getResourceId(i, 0),
                    userNames[i],
                    "0",
                    contentImages.getResourceId(i, 0),
                    comments[i]));
            Log.i(TAG, "initializeContents: " + userNames[i]);
        }

        profilePictures.recycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Log.d(TAG, "Home button clicked!");
                return true;
            case R.id.newContent:
                Log.d(TAG, "newContent button clicked!");
                Intent intent = new Intent(this, NewContentActivity.class);
                startActivity(intent);
                return true;
            case R.id.delete:
                deleteCurrentUser();
                Log.d(TAG, "Delete button clicked!");
                return true;
            case R.id.settings:
                Log.d(TAG, "Settings button clicked!");
                return true;
            case R.id.logOut:
                Log.d(TAG, "Logout button clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void liked (Boolean buttonOn, Content currentContent) {
        DocumentReference documentReference = contents.document(currentContent._getId());
        String likes;

        if (buttonOn) {
            likes = String.valueOf(Integer.parseInt(currentContent.getLikes()) + 1);
            notificationHandler.send(currentContent.getUserName());
        } else {
            likes = String.valueOf(Integer.parseInt(currentContent.getLikes()) - 1);
        }

        //UPDATE
        documentReference.update("likes", likes)
                .addOnFailureListener(failure -> {
                    Toast.makeText(HomeActivity.this, "A " + currentContent._getId() + ". kontent nem lájkolható!", Toast.LENGTH_SHORT).show();
                });

        queryData();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    /*public void newContent(Integer bitmap) {
        TypedArray profilePictures = getResources().obtainTypedArray(R.array.profilePictures);

        contents.add(new Content(
                profilePictures.getResourceId(0, 0),
                firebaseUser.getDisplayName(),
                "0",
                bitmap,
                null));
    }*/

    public void deleteCurrentUser() {
        //DELETE
        firebaseUser.delete();
        FirebaseAuth.getInstance().signOut();
        finish();
    }

}