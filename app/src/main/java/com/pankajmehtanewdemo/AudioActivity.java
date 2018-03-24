package com.pankajmehtanewdemo;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pankajmehtanewdemo.Adapter.ComplainPopupAdapter;
import com.pankajmehtanewdemo.Adapter.ComplainRegisterAdapter;
import com.pankajmehtanewdemo.Appcontroler.Constants;
import com.pankajmehtanewdemo.Model.ComplainPopupModel;
import com.pankajmehtanewdemo.Model.Complainmodel;
import com.pankajmehtanewdemo.Model.Complaintype;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AudioActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fabplace_audio;
    private static final int SELECT_AUDIO = 2;
    String selectedPath = "";
    File file;
    private int serverResponseCode = 0;
    RecyclerView rv_audio_mycomplain;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter listAdapter;
    List<Complainmodel> listuser;
    String codeid, hDate;
    int Year, Month, Day;
    Button btnPopupWindow;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    List<ComplainPopupModel> listuser1;


    List<Complaintype> listcomplaintype;
    ArrayList<String> comlintypearray = new ArrayList<>();
    Spinner sp_complain;

    String complainid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udio);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fabplace_audio = (FloatingActionButton) findViewById(R.id.fabplace_audio);
        rv_audio_mycomplain = (RecyclerView) findViewById(R.id.rv_audio_mycomplain);
        fabplace_audio.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        listuser = new ArrayList<>();
        listcomplaintype = new ArrayList<>();
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        Calendar calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        int mnth = Month + 1;
        String date = Day + "-" + mnth + "-" + Year;
        hDate = Year + "-" + mnth + "-" + Day;
        listuser1 = new ArrayList<>();


    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (Constants.user_mob != null) {
            Audiocomplainload(Constants.user_mob);
            subcategoryload();
           //  complaintypeload();
        }
    }


    public void openGalleryAudio() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Audio "), SELECT_AUDIO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_AUDIO) {
                System.out.println("SELECT_AUDIO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPathFromURI(AudioActivity.this, selectedImageUri);
                //
                if (selectedPath != null) {
                    dialog.show();
                    file = new File(selectedPath);
                    uploadAudio(selectedPath);
                } else {
                    Toast.makeText(AudioActivity.this, "No Audio Selected", Toast.LENGTH_SHORT).show();
                }
                System.out.println("SELECT_AUDIO Path : " + selectedPath);
            }
        }
    }


    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public void uploadAudio(String desc) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();
        if (isNetworkAvailable()) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            params.put("contact", Constants.user_mob);
            params.put("date", hDate);
            params.put("tt_id", complainid);
            params.add("type", "3");
            try {
                params.put("file", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            client.post(Constants.uri + "complaint_audio.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }


                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString != null) {
                        Log.d("res", responseString);
                        JSONObject jObj = null;
                        try {
                            jObj = new JSONObject(responseString);
                            String res = jObj.getString("success");

                            if (res.equals("true")) {
                                JSONArray resultsarray = jObj.getJSONArray("response");
                                for (int i = 0; i < resultsarray.length(); i++) {
                                    codeid = resultsarray.getJSONObject(i).getString("uniq_id");
                                    startActivity(new Intent(AudioActivity.this, CmpleteRequestActivity.class).putExtra("unid", codeid));
                                    dialog.dismiss();
                                    finish();
                                }

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
        } else {
            Toast.makeText(AudioActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void Audiocomplainload(String mobno) {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            params.put("contact", mobno);
            params.add("type", "3");
            client.post(Constants.uri + "view_complaint.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString != null) {
                        listuser.clear();
                        try {
                            Log.d("res", responseString);
                            //Toast.makeText(getContext().getApplicationContext(), res.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObj = new JSONObject(responseString);
                            JSONArray resultsarray = jsonObj.getJSONArray("response");

                            // looping through All Contacts
                            for (int i = 0; i < resultsarray.length(); i++) {
                                Complainmodel temp = new Complainmodel();
                                temp.setCode(resultsarray.getJSONObject(i).getString("uniq_id"));
                                temp.setDate(resultsarray.getJSONObject(i).getString("date"));
                                temp.setStatus(resultsarray.getJSONObject(i).getString("status"));
                                listuser.add(temp);
                                dialog.dismiss();

                            }

                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                    if (listuser.size() > 0) {
                        rv_audio_mycomplain.setVisibility(View.VISIBLE);
                    }
                    rv_audio_mycomplain.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(AudioActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_audio_mycomplain.setLayoutManager(mLayoutManager);
                    listAdapter = new ComplainRegisterAdapter(AudioActivity.this, listuser);
                    rv_audio_mycomplain.setAdapter(listAdapter);


                }
            });
        } else {
            Toast.makeText(AudioActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    /*public void Showcomplaindialogue() {
        LayoutInflater li = LayoutInflater.from(AudioActivity.this);
        View vi = li.inflate(R.layout.complain_typedialogue, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
        alertDialogBuilder.setView(vi);
        alertDialogBuilder.setMessage("Select Complain Type");
        alertDialogBuilder.setCancelable(false);


        sp_complain = (Spinner) vi.findViewById(R.id.sp_complain);


        if (comlintypearray != null && comlintypearray.size() > 0) {
            spinnerArrayAdapter = new Spinneradapter(AudioActivity.this, listcomplaintype);
            sp_complain.setAdapter(spinnerArrayAdapter);
            sp_complain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    for (int l = 0; l < listcomplaintype.size(); l++) {
                        complainid = listcomplaintype.get(position).getTt_id();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }


        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                openGalleryAudio();
            }
        });


        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }*/


    /*public void complaintypeload() {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            client.post(Constants.uri + "type.php", new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString != null) {
                        listcomplaintype.clear();

                        try {
                            Log.d("res", responseString);
                            //Toast.makeText(getContext().getApplicationContext(), res.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObj = new JSONObject(responseString);
                            JSONArray resultsarray = jsonObj.getJSONArray("response");
                            comlintypearray.clear();
                            // looping through All Contacts
                            for (int i = 0; i < resultsarray.length(); i++) {
                                Complaintype temp = new Complaintype();
                                temp.setTt_id(resultsarray.getJSONObject(i).getString("tt_id"));
                                temp.setTtname(resultsarray.getJSONObject(i).getString("tt_name"));
                                temp.setIconset(Constants.imageuri + resultsarray.getJSONObject(i).getString("icon"));
                                listcomplaintype.add(temp);

                                comlintypearray.add(resultsarray.getJSONObject(i).getString("tt_name"));

                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                        Log.d("comty", String.valueOf(comlintypearray.size()));

                    }
                }
            });
        } else {
            Toast.makeText(AudioActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }*/


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabplace_audio:
                //  Showcomplaindialogue();
                popupWindow();
                break;
        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




/*    public int uploadFile(final String sourceFileUri) {

        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 3 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        long fileSizeInBytes = sourceFile.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInKB / 1024;



        if (fileSizeInMB > maxBufferSize){
            Toast.makeText(AudioActivity.this, "Audio selected less than 3 mb", Toast.LENGTH_SHORT).show();
        }else {
            if (!sourceFile.isFile()) {

                dialog.dismiss();

                // Log.e("uploadFile", "Source File not exist :" + filepath);

                runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("Source File not exist :" + filepath);
                    }
                });

                return 0;

            } else {
                try {
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(Constants.staticuri+"complaint_audio.php");

                    Map<String,Object> params = new LinkedHashMap<>();
                    params.put("date", "Freddie the Fish");
                    params.put("contact", "fishie@seamail.example.com");
                    params.put("tt_id", "1");

                    StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String,Object> param : params.entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    byte[] postDataBytes = postData.toString().getBytes("UTF-8");


                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("file", String.valueOf(sourceFile));



                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                            + fileName + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);


                    String date="2017-07-28";
                    dos.writeBytes("Content-Disposition: form-data; date=\"date\"" + lineEnd);

                    dos.writeBytes(lineEnd);
                    dos.writeBytes(date); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);


                    String contact="9956565656";
                    dos.writeBytes("Content-Disposition: form-data; contact=\"contact\"" + lineEnd);

                    dos.writeBytes(lineEnd);
                    dos.writeBytes(contact); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    String ttid="1";
                    dos.writeBytes("Content-Disposition: form-data; tt_id=\"ttid\"" + lineEnd);

                    dos.writeBytes(lineEnd);
                    dos.writeBytes(ttid); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);

                    if (serverResponseCode == 200) {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(AudioActivity.this,
                                        "File Upload Complete.", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }

                    // close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {

                    dialog.dismiss();
                    ex.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {
                      *//*  messageText
                                .setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(MainActivity.this,
                                "MalformedURLException", Toast.LENGTH_SHORT)
                                .show();*//*
                        }
                    });

                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {

                    dialog.dismiss();
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AudioActivity.this,
                                    "Got Exception : see logcat ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                dialog.dismiss();

            }
        }
        return serverResponseCode;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private void popupWindow() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popupwindow_layout, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setIgnoreCheekPress();

        Button btnOk = (Button) popupView.findViewById(R.id.btnOk);
        ImageView btnCancel = (ImageView) popupView.findViewById(R.id.btnCancel);
        recyclerView = (RecyclerView) popupView.findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        ComplainPopupAdapter customAdapter = new ComplainPopupAdapter(AudioActivity.this, listuser1);
        recyclerView.setAdapter(customAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(AudioActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //  Constants.sub_id = listuser.get(position).getSubid();
                complainid = listuser1.get(position).getSubid();
                openGalleryAudio();



            }
        }));

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
/*
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
    }*/


    public void subcategoryload() {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            //params.add("cat_id", Constants.cat_id);
            params.add("cat_id", complainid);
            client.post(Constants.uri + "type.php", params, new TextHttpResponseHandler() {


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString != null) {
                        listuser1.clear();

                        try {
                            Log.d("res", responseString);
                            //Toast.makeText(getContext().getApplicationContext(), res.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObj = new JSONObject(responseString);
                            JSONArray resultsarray = jsonObj.getJSONArray("response");
                            // looping through All Contacts
                            for (int i = 0; i < resultsarray.length(); i++) {
                                ComplainPopupModel temp = new ComplainPopupModel();
                                temp.setSubid(resultsarray.getJSONObject(i).getString("tt_id"));
                                temp.setSubname(resultsarray.getJSONObject(i).getString("tt_name"));
                                temp.setImg(Constants.imageuri + resultsarray.getJSONObject(i).getString("icon"));
                                listuser1.add(temp);
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }



                    }
                }
            });
        } else {
            Toast.makeText(AudioActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

}
