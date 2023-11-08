package com.confidenceb.spamsmsdetector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Messages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Messages extends Fragment {

    TextView smsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);

        smsTextView = findViewById(R.id.readsms);

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

    }

    public void readSMSBtn(View view){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);

        if(permissionCheck==PackageManager.PERMISSION_GRANTED){
            readSMS();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, 1);
        }
    }

    private void readSMS() {
        // Open the default SMS app
        Intent smsIntent = new Intent(Intent.ACTION_MAIN);
        smsIntent.addCategory(Intent.CATEGORY_APP_MESSAGING);
        PackageManager packageManager = getPackageManager();
        ResolveInfo resolveInfo = packageManager.resolveActivity(smsIntent, PackageManager.MATCH_DEFAULT_ONLY);


        if (resolveInfo != null) {
            Intent appIntent = packageManager.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName);
            startActivity(appIntent);
        }

        // Read SMS messages
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            StringBuilder smsBuilder = new StringBuilder();

            int addressIndex = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
            int bodyIndex = cursor.getColumnIndex(Telephony.Sms.BODY);

            while (cursor.moveToNext()) {
                String address = cursor.getString(addressIndex);
                String body = cursor.getString(bodyIndex);

                if (addressIndex != -1 && bodyIndex != -1) {
                    smsBuilder.append("From: ").append(address).append("\n");
                    smsBuilder.append("Message: ").append(body).append("\n\n");
                }
            }

            cursor.close();
            smsTextView.setText(smsBuilder.toString());
        }
    }
}