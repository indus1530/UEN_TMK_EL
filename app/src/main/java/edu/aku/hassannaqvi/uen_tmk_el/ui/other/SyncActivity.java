package edu.aku.hassannaqvi.uen_tmk_el.ui.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.aku.hassannaqvi.uen_tmk_el.CONSTANTS;
import edu.aku.hassannaqvi.uen_tmk_el.R;
import edu.aku.hassannaqvi.uen_tmk_el.adapter.SyncListAdapter;
import edu.aku.hassannaqvi.uen_tmk_el.contracts.FamilyMembersContract;
import edu.aku.hassannaqvi.uen_tmk_el.contracts.FormsContract;
import edu.aku.hassannaqvi.uen_tmk_el.core.DatabaseHelper;
import edu.aku.hassannaqvi.uen_tmk_el.core.MainApp;
import edu.aku.hassannaqvi.uen_tmk_el.databinding.ActivitySyncBinding;
import edu.aku.hassannaqvi.uen_tmk_el.models.Death;
import edu.aku.hassannaqvi.uen_tmk_el.models.Form;
import edu.aku.hassannaqvi.uen_tmk_el.models.MWRA_CHILD;
import edu.aku.hassannaqvi.uen_tmk_el.models.SyncModel;
import edu.aku.hassannaqvi.uen_tmk_el.sync.GetAllData;
import edu.aku.hassannaqvi.uen_tmk_el.sync.SyncAllData;
import edu.aku.hassannaqvi.uen_tmk_el.sync.SyncAllPhotos;
import edu.aku.hassannaqvi.uen_tmk_el.sync.SyncDevice;

import static edu.aku.hassannaqvi.uen_tmk_el.utils.AndroidUtilityKt.dbBackup;
import static edu.aku.hassannaqvi.uen_tmk_el.utils.CreateTable.PROJECT_NAME;

public class SyncActivity extends AppCompatActivity implements SyncDevice.SyncDevicInterface {
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    String DirectoryName;
    DatabaseHelper db;
    SyncListAdapter syncListAdapter;
    ActivitySyncBinding bi;
    SyncModel model;
    SyncModel uploadmodel;
    List<SyncModel> list;
    List<SyncModel> uploadlist;
    Boolean listActivityCreated;
    Boolean uploadlistActivityCreated;
    boolean sync_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_sync);
        bi.setCallback(this);
        list = new ArrayList<>();
        uploadlist = new ArrayList<>();
        //bi.noItem.setVisibility(View.VISIBLE);
        bi.noDataItem.setVisibility(View.VISIBLE);
        listActivityCreated = true;
        uploadlistActivityCreated = true;
        sharedPref = getSharedPreferences("src", MODE_PRIVATE);
        editor = sharedPref.edit();
        db = new DatabaseHelper(this);
        dbBackup(this);

        sync_flag = getIntent().getBooleanExtra(CONSTANTS.SYNC_LOGIN, false);

        bi.btnSync.setOnClickListener(v -> onSyncDataClick());
        bi.btnUpload.setOnClickListener(v -> syncServer());
        // setAdapter();
        setUploadAdapter();
    }

    public void onSyncDataClick() {

        bi.activityTitle.setText(getString(R.string.btnSync));
        // Require permissions INTERNET & ACCESS_NETWORK_STATE
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new SyncDevice(this, true).execute();
            new SyncData(this, MainApp.UC_ID).execute(sync_flag);
        } else {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }
    }

    void setUploadAdapter() {
        syncListAdapter = new SyncListAdapter(uploadlist);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        bi.rvUploadList.setLayoutManager(mLayoutManager2);
        bi.rvUploadList.setItemAnimator(new DefaultItemAnimator());
        bi.rvUploadList.setAdapter(syncListAdapter);
        syncListAdapter.notifyDataSetChanged();
        if (syncListAdapter.getItemCount() > 0) {
            bi.noDataItem.setVisibility(View.GONE);
        } else {
            bi.noDataItem.setVisibility(View.VISIBLE);
        }
    }

    public void syncServer() {
        bi.activityTitle.setText(getString(R.string.btnUpload));
        // Require permissions INTERNET & ACCESS_NETWORK_STATE
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            DatabaseHelper db = new DatabaseHelper(this);

            bi.noDataItem.setVisibility(View.GONE);

            new SyncDevice(this, false).execute();
//  *******************************************************Forms*********************************
            /*String[] syncValues = new String[]{CONSTANTS.FORM_MP, CONSTANTS.FORM_MF};
            for (int i = 0; i < syncValues.length; i++) {
                Toast.makeText(getApplicationContext(), String.format("Syncing Forms %s", syncValues[i]), Toast.LENGTH_SHORT).show();
                if (uploadlistActivityCreated) {
                    uploadmodel = new SyncModel();
                    uploadmodel.setstatusID(0);
                    uploadlist.add(uploadmodel);
                }
                new SyncAllData(
                        this,
                        String.format("Forms - %s", syncValues[i]),
                        "updateSyncedForms",
                        Form.class,
                        MainApp._HOST_URL + MainApp._SERVER_URL,
                        FormsContract.FormsTable.TABLE_NAME + syncValues[i],
                        db.getUnsyncedForms(syncValues[i]), i, syncListAdapter, uploadlist
                ).execute();
            }*/

            Toast.makeText(getApplicationContext(), "Syncing Forms", Toast.LENGTH_SHORT).show();
            if (uploadlistActivityCreated) {
                uploadmodel = new SyncModel();
                uploadmodel.setstatusID(0);
                uploadlist.add(uploadmodel);
            }
            new SyncAllData(
                    this,
                    "Forms",
                    "updateSyncedForms",
                    Form.class,
                    MainApp._HOST_URL + MainApp._SERVER_URL,
                    FormsContract.FormsTable.TABLE_NAME,
                    db.getUnsyncedForms(), 0, syncListAdapter, uploadlist
            ).execute();

            Toast.makeText(getApplicationContext(), "Syncing FamilyMembers", Toast.LENGTH_SHORT).show();
            if (uploadlistActivityCreated) {
                uploadmodel = new SyncModel();
                uploadmodel.setstatusID(0);
                uploadlist.add(uploadmodel);
            }
            new SyncAllData(
                    this,
                    "FamilyMembers",
                    "updateSyncedFamilyMemForms",
                    FamilyMembersContract.class,
                    MainApp._HOST_URL + MainApp._SERVER_URL,
                    FamilyMembersContract.MemberTable.TABLE_NAME,
                    db.getUnsyncedFamilyMembers(), 1, syncListAdapter, uploadlist
            ).execute();

            Toast.makeText(getApplicationContext(), "Syncing Forms Death", Toast.LENGTH_SHORT).show();
            if (uploadlistActivityCreated) {
                uploadmodel = new SyncModel();
                uploadmodel.setstatusID(0);
                uploadlist.add(uploadmodel);
            }
            new SyncAllData(
                    this,
                    "Forms - Death",
                    "updateSyncedDeath",
                    Death.class,
                    MainApp._HOST_URL + MainApp._SERVER_URL,
                    "Death",
                    db.getUnsyncedDeaths(CONSTANTS.CHILD_DEATH_TYPE, CONSTANTS.MOTHER_DEATH_TYPE), 2, syncListAdapter, uploadlist
            ).execute();

            String[][] syncValues = new String[][]{{"MWRAs", CONSTANTS.MWRA_TYPE}, {"Immunization", CONSTANTS.CHILD_TYPE}, {"Anthro", CONSTANTS.CHILD_ANTHRO_TYPE + "-" + CONSTANTS.MWRA_ANTHRO_TYPE}};
            for (int i = 3; i <= 5; i++) {
                int k = i - 3;
                Toast.makeText(getApplicationContext(), String.format("Syncing Forms %s", syncValues[k][0]), Toast.LENGTH_SHORT).show();
                if (uploadlistActivityCreated) {
                    uploadmodel = new SyncModel();
                    uploadmodel.setstatusID(0);
                    uploadlist.add(uploadmodel);
                }
                new SyncAllData(
                        this,
                        String.format("Forms - %s", syncValues[k][0]),
                        "updateSyncedMWRACHILD",
                        MWRA_CHILD.class,
                        MainApp._HOST_URL + MainApp._SERVER_URL,
                        syncValues[k][0],
                        syncValues[k][1].contains("-") ? db.getUnsyncedMWRAChild(syncValues[k][1].split("-")) : db.getUnsyncedMWRAChild(syncValues[k][1]), i, syncListAdapter, uploadlist
                ).execute();
            }

            uploadlistActivityCreated = false;

            SharedPreferences syncPref = getSharedPreferences("SyncInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = syncPref.edit();

            editor.putString("LastDataUpload", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
            editor.apply();

        } else {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void processFinish(boolean flag) {
        if (flag) {
            MainApp.appInfo.updateTagName(this);
//            new SyncData(SyncActivity.this, MainApp.DIST_ID).execute(sync_flag);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }

    public void btnUploadPhotos(View view) {

        File sdDir;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
            // Android 9+
            sdDir = this.getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES);
        else
            // Android 5 - 8
            sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        Log.d("Files", "Path: " + sdDir);
        File directory = new File(String.valueOf(sdDir), PROJECT_NAME);
        Log.d("Directory", "uploadPhotos: " + directory);
        if (directory.exists()) {
            File[] files = directory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return (file.getPath().endsWith(".jpg") || file.getPath().endsWith(".jpeg"));
                }
            });


            Log.d("Files", "Count: " + files.length);
            if (files.length > 0) {
                for (File file : files) {
                    Log.d("Files", "FileName:" + file.getName());
                    SyncAllPhotos syncAllPhotos = new SyncAllPhotos(file.getName(), this);
                    syncAllPhotos.execute();

                    try {
                        //3000 ms delay to process upload of next file.
                        Thread.sleep(3000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                editor.putString("LastPhotoUpload", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
                editor.apply();
            } else {
                Toast.makeText(this, "No photos to upload.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No photos were taken", Toast.LENGTH_SHORT).show();
        }

    }

    private class SyncData extends AsyncTask<Boolean, String, String> {

        private final Context mContext;
        private final String distID;

        private SyncData(Context mContext, String districtId) {
            this.mContext = mContext;
            this.distID = districtId;
        }

        @Override
        protected String doInBackground(Boolean... booleans) {
            runOnUiThread(() -> {
                String[] syncItems;
                if (booleans[0])
                    syncItems = new String[]{"User", "VersionApp", "Villages", "UCs"};
                else syncItems = new String[]{"BLRandom"};
                for (String syncItem : syncItems) {
                    if (listActivityCreated) {
                        model = new SyncModel();
                        model.setstatusID(0);
                        list.add(model);
                    }
                    new GetAllData(mContext, syncItem, syncListAdapter, list).execute();
                }

                listActivityCreated = false;
            });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(() -> {
                editor.putString("LastDataDownload", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
                editor.apply();
                editor.putBoolean("flag", true);
                editor.commit();

                dbBackup(mContext);

            }, 1200);
        }
    }
}
