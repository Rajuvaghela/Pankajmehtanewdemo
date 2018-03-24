package com.pankajmehtanewdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fabplace;
    Bitmap photo = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String pankajmehtabjp;
    String RootDir;
    ImageView imgset;
    EditText edt_text_review;
    TextView textset_cmp;
    LinearLayout disshow;
    File file;
    String formattedDate;
    String codeid, hDate;
    int Year, Month, Day;
    RecyclerView rv_mycomplain;


    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter listAdapter;
    List<Complainmodel> listuser;
    List<Complaintype> listcomplaintype;
    ArrayList<String> comlintypearray = new ArrayList<>();
    Spinner sp_complain;

    String complainid;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    List<ComplainPopupModel> listuser1;
    View popupView;
     PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        listuser = new ArrayList<>();
        listcomplaintype = new ArrayList<>();
        fabplace = (FloatingActionButton) findViewById(R.id.fabplace);
        disshow = (LinearLayout) findViewById(R.id.disshow);
        // imgset=(ImageView)findViewById(R.id.imgset);
        textset_cmp = (TextView) findViewById(R.id.textset_cmp);
        rv_mycomplain = (RecyclerView) findViewById(R.id.rv_mycomplain);
        fabplace.setOnClickListener(this);

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
            complainload(Constants.user_mob);
            subcategoryload();
         //   complaintypeload();
        }
    }

    public void complainload(String mobno) {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            params.put("contact", mobno);
            params.add("type", "1");
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
                        rv_mycomplain.setVisibility(View.VISIBLE);
                    }
                    rv_mycomplain.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(PhotoActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_mycomplain.setLayoutManager(mLayoutManager);
                    listAdapter = new ComplainRegisterAdapter(PhotoActivity.this, listuser);
                    rv_mycomplain.setAdapter(listAdapter);


                }
            });
        } else {
            Toast.makeText(PhotoActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    void openImageChooser() {
        final CharSequence[] items = {"Upload Photo", "Upload from gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Upload Photo")) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                } else if (items[item].equals("Upload from gallery")) {
                    Intent intent = new Intent();
                    if (Build.VERSION.SDK_INT >= 19) {
                        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    } else {
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                    }
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    void saveImage(Bitmap img) {
        RootDir = Environment.getExternalStorageDirectory()
                + File.separator + "pankajmehta";
        File myDir = new File(RootDir);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        pankajmehtabjp = "Image-" + n + ".jpg";
        file = new File(myDir, pankajmehtabjp);
        long length = file.length();
        Log.e("length",""+length);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            img.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Toast.makeText(MainActivity.this, "Image saved to 'txt_imgs' folder", Toast.LENGTH_LONG).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                photo = (Bitmap) data.getExtras().get("data");
                saveImage(photo);
                Showdialogue();
            } else if (requestCode == SELECT_FILE) {
                photo = null;
                if (data != null) {
                    try {
                        //   new File(data.getData().toString());
                        //  items_image.add(String.valueOf(imgs));
                        // Toast.makeText(getApplicationContext(),String.valueOf(imgs),Toast.LENGTH_LONG).show();
                        photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        saveImage(photo);
                        Showdialogue();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //  imgset.setImageBitmap(photo);

            //setimage.setImageBitmap(photo);
            // disshow.setVisibility(View.VISIBLE);
        }
    }


    /*public void Showcomplaindialogue() {
        LayoutInflater li = LayoutInflater.from(PhotoActivity.this);
        View vi = li.inflate(R.layout.complain_typedialogue, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PhotoActivity.this);
        alertDialogBuilder.setView(vi);
        alertDialogBuilder.setMessage("Select Complain Type");
        alertDialogBuilder.setCancelable(false);


        sp_complain = (Spinner) vi.findViewById(R.id.sp_complain);


        if (comlintypearray != null && comlintypearray.size() > 0) {
            spinnerArrayAdapter = new Spinneradapter(PhotoActivity.this, listcomplaintype);
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
                openImageChooser();
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


    public void complaintypeload() {
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
            Toast.makeText(PhotoActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void uploadphoto(String desc) {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            params.put("description", desc);
            params.put("date", hDate);
            params.put("contact", Constants.user_mob);
            long length = file.length();
            Log.e("length",""+length);
            //   params.put("contact", Constants.user_mob);
            params.put("tt_id", complainid);
            try {
                params.put("path", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            client.post(Constants.uri + "complaint_ins.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
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
                                dialog.dismiss();

                                JSONArray resultsarray = jObj.getJSONArray("response");
                                for (int i = 0; i < resultsarray.length(); i++) {
                                    codeid = resultsarray.getJSONObject(i).getString("uniq_id");
                                }
                                startActivity(new Intent(PhotoActivity.this, CmpleteRequestActivity.class).putExtra("unid", codeid));
                                finish();
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
        } else {
            dialog.dismiss();
            Toast.makeText(PhotoActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void Showdialogue() {
        LayoutInflater li = LayoutInflater.from(PhotoActivity.this);
        View vi = li.inflate(R.layout.complain_text, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PhotoActivity.this);
        alertDialogBuilder.setView(vi);
        alertDialogBuilder.setMessage("Message");
        alertDialogBuilder.setCancelable(false);


        edt_text_review = (EditText) vi.findViewById(R.id.edt_text_review);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                if (reviewvalidate()) {
                    uploadphoto(edt_text_review.getText().toString());
                    dialog.dismiss();
                    // reviewsubmit(Categoryname, homemenucategoryname, review_mobno.getText().toString(), edt_text_review.getText().toString());
                }

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
        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
    }


    public boolean reviewvalidate() {
        if (isEmpty(edt_text_review)) {
            Toast.makeText(getApplicationContext(), "Enter your complaion", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }


    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabplace:
                //  Showcomplaindialogue();
                popupWindow();
                break;
        }
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
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
         popupView = layoutInflater.inflate(R.layout.popupwindow_layout, null,false);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setIgnoreCheekPress();

        Button btnOk = (Button) popupView.findViewById(R.id.btnOk);
        ImageView btnCancel = (ImageView) popupView.findViewById(R.id.btnCancel);
        recyclerView = (RecyclerView) popupView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        ComplainPopupAdapter customAdapter = new ComplainPopupAdapter(PhotoActivity.this, listuser1);
        recyclerView.setAdapter(customAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(PhotoActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //  Constants.sub_id = listuser.get(position).getSubid();


                 complainid = listuser1.get(position).getSubid();
                openImageChooser();

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
            Toast.makeText(PhotoActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

}
