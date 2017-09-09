package com.example.anubhav.vacmet;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.anubhav.vacmet.adapters.RecyclerviewAdapter;
import com.example.anubhav.vacmet.interfaces.ItemClickListener;
import com.example.anubhav.vacmet.model.ItemModel;
import com.example.anubhav.vacmet.model.OrderModel;
import com.example.anubhav.vacmet.services.VacmetOverlayService;
import com.example.anubhav.vacmet.utils.CircleTransform;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anubhav on 23/1/17.
 */

public class OrderStatus extends AppCompatActivity implements View.OnClickListener {


    private static final int MY_PERMISSIONS_REQUEST_SYSTEM_ALERT_WINDOW = 9090;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 9092;
    public static final String VBELN = "VBELN";
    public static final String STATUS = "STATUS";
    public static final String OPEN_QTY = "OPEN_QTY";
    public static final String DESP_QTY = "DESP_QTY";
    public static final String STOCK_QTY = "STOCK_QTY";
    public static final String DOC_DATE = "DOC_DATE";
    public static final String DEL_DATE = "DEL_DATE";
    public static final String CUST_NM = "CUST_NM";
    private RecyclerView recyclerView;
    private ArrayList<OrderModel> orderModelList;
    private RecyclerviewAdapter recyclerViewAdapter;
    private NestedScrollView noSearchResultFound;
    private Toolbar toolbar;
    private ImageView ivCollapsingtoolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private CoordinatorLayout coordinatorLayout;
    private HashMap<Integer,OrderModel> deletedOrders;
    private ArrayList<OrderModel> searchList;
    private FloatingActionButton floatingActionButton,floatingLogOut;
    private ItemTouchHelper itemTouchHelper;
    private SharedPreferences sharedPreferences,logingSharePrefs;
    private final String LoginPrefs = "LoginPrefs";
    private final String SapId = "SapId";
    private final String OrderIdPrefs = "OrderIdPrefs";
    private final String ClientorServer = "ClientorServer";
    private final String LoggedInUser = "LoggedInUser";
    private final String LoggedInUserName = "LoggedInUserName";
    private final String LoggedInUserPassword = "LoggedInUserPassword";
    private String urlForOrders = "http://122.160.221.107:8020/sap/bc/get_pending?sap-client=500&";
    private ProgressDialog progressDialog;
    private final String Main_Xml_Tag = "ZBAPI_SOSTATUS";
    private DrawerLayout drawerLayout;
    private LinearLayout mainDrawerView;
    private EditText etSapId;
    private ImageView imgEditSapId;
    private TextView employeeName,employeeDesig;
    private ImageView employeePic;
    private ActionBarDrawerToggle drawerToggle;
    private SharedPreferences orderIdPrefs;
    private final String DefaultSapId = "1400056";
    private Button btnUpdateService;
    private RadioGroup radioGroup;
    private RadioButton radioClient,radioServer;

    @Override
    protected void onStart() {
        super.onStart();
        if(getSupportActionBar()!=null) {
            setSupportActionBar(toolbar);
            ActionBar bar = getSupportActionBar();
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_nav_drawer);
        init();
        logingSharePrefs = getSharedPreferences(LoginPrefs,MODE_APPEND);
        orderIdPrefs = getSharedPreferences(OrderIdPrefs,MODE_PRIVATE);
        if(orderIdPrefs.getString(SapId,null)==null){
            SharedPreferences.Editor editor = orderIdPrefs.edit();
            editor.putString(SapId,DefaultSapId);
            editor.apply();
        }
        etSapId.setText(orderIdPrefs.getString(SapId,null));
        if(logingSharePrefs.getString(LoggedInUserName,null)!=null){
            employeeName.setText(logingSharePrefs.getString(LoggedInUserName,null));
        }
//        feedDummyData();
        if(orderIdPrefs.getString(ClientorServer,null)==null){
            SharedPreferences.Editor editor = orderIdPrefs.edit();
            editor.putString(ClientorServer,"c");
            editor.apply();
        }

        hitOrdersService(orderIdPrefs.getString(ClientorServer,null),DefaultSapId);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.back_24dp);
        //Todo: Back functionality only via hardware button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
   /*     toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OrderStatus.this, "Clicked!", Toast.LENGTH_SHORT).show();
                Log.e("", "onClick: Toolbar" );
                finish();
            }
        });*/

        floatingActionButton.setOnClickListener(this);
        floatingActionButton.setBackgroundColor(getResources().getColor(R.color.white));
        floatingLogOut.setBackgroundColor(getResources().getColor(R.color.white));
        floatingLogOut.setOnClickListener(this);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.closeDrawers();
         itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN
                ,ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                updateList(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                recyclerViewAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deletedOrders.put(new Integer(viewHolder.getAdapterPosition()),orderModelList.get(viewHolder.getAdapterPosition()));
                orderModelList.remove(viewHolder.getAdapterPosition());
                dispalySnackBar(viewHolder.getAdapterPosition());
                recyclerViewAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
        if(orderIdPrefs.getString(ClientorServer,null).equalsIgnoreCase("c")){
            employeeDesig.setText(getResources().getString(R.string.client));
        }else if(orderIdPrefs.getString(ClientorServer,null).equalsIgnoreCase("s")){
            employeeDesig.setText(getResources().getString(R.string.party));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void hitOrdersService(String client , String id) {
        //Todo: Pass SAP No., here to fetch total orders for corresponding SAP id.
        new CustomAsyncTaskForRestOrderService().execute(client,id);
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioClient = (RadioButton) findViewById(R.id.radioYes);
        radioServer = (RadioButton) findViewById(R.id.radioNo);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_order_status);
        mainDrawerView = (LinearLayout) findViewById(R.id.mainDrawerView);
        etSapId = (EditText) findViewById(R.id.et_sap_id);
        etSapId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSapId.isEnabled()){
                    etSapId.requestFocus();

                }
            }
        });
        imgEditSapId = (ImageView) findViewById(R.id.edit_sap_id);
        imgEditSapId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSapId.setEnabled(true);
                etSapId.setFocusable(true);
                etSapId.setFocusableInTouchMode(true);
                radioGroup.setClickable(true);
                radioClient.setEnabled(true);
                radioClient.setClickable(true);
                radioServer.setEnabled(true);
                radioServer.setClickable(true);
            }
        });
        btnUpdateService = (Button) findViewById(R.id.btn_hitService);
        btnUpdateService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSapId.setEnabled(false);
                etSapId.setFocusable(false);
                etSapId.setFocusableInTouchMode(true);
                radioGroup.setClickable(false);
                radioServer.setEnabled(false);
                radioServer.setClickable(false);
                radioClient.setEnabled(false);
                radioClient.setClickable(false);
                String isClientorServer = null;
                if(radioClient.isChecked()){
                    isClientorServer = "c";
                    employeeDesig.setText(getResources().getString(R.string.client));
                }else if(radioServer.isChecked()){
                    isClientorServer = "s";
                    employeeDesig.setText(getResources().getString(R.string.party));
                }
                SharedPreferences.Editor editor = orderIdPrefs.edit();
                editor.putString(SapId,etSapId.getText().toString().trim());
                editor.putString(ClientorServer,isClientorServer);
                editor.apply();
                hitOrdersService(orderIdPrefs.getString(ClientorServer,null),etSapId.getText().toString().trim());
            }
        });
        employeeName = (TextView) findViewById(R.id.name);
        employeePic = (ImageView) findViewById(R.id.img_profile);
        employeeDesig = (TextView) findViewById(R.id.designation);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingEdit);
        floatingLogOut = (FloatingActionButton) findViewById(R.id.floatingLogout);
        floatingActionButton.setBackgroundColor(Color.WHITE);
        /**Todo : Problem for MODE_WORLD_READABLE LOOK==============================================**/
        sharedPreferences = getSharedPreferences("GooglePic",MODE_APPEND);
        if(sharedPreferences.getString("PhotoUrl",null)!=null) {
            Glide.with(this).load(sharedPreferences.getString("PhotoUrl",null))
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(employeePic);
        }else{
            Glide.with(this).load(LoginActivity.url)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(employeePic);
        }
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        deletedOrders = new HashMap<>();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        ivCollapsingtoolbar = (ImageView) findViewById(R.id.ivCollapsingtoolbar);
        recyclerView = (RecyclerView) findViewById(R.id.orderStatusList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        orderModelList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerviewAdapter(this,orderModelList,new ItemClickListener(){
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(OrderStatus.this,OrderInformation.class);
                intent.putExtra("OrderInfo",orderModelList.get(position));
                startActivity(intent);
            }
        });
        noSearchResultFound = (NestedScrollView) findViewById(R.id.noSearchFound);
        searchList = new ArrayList<>();
    }
    private void feedDummyData() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<ItemModel> party1 = new ArrayList<>();
        ItemModel itemModel1 = new ItemModel("Polyester Film(Grade Printing)","10","30");
        ItemModel itemModel2 = new ItemModel("BOPP","20","50");
        party1.add(itemModel1);
        party1.add(itemModel2);
        OrderModel o1 = new OrderModel("Varna Limited, Sri Lanka","0179","130","14/12/2016","35","28/01/2017","30",party1);
        ArrayList<ItemModel> party2 = new ArrayList<>();
        ItemModel itemModel21 = new ItemModel("Polyester Film(Grade Printing)","10","10");
        ItemModel itemModel22 = new ItemModel("BOPP","2","5");
        party2.add(itemModel21);
        party2.add(itemModel22);
        OrderModel o2 = new OrderModel("Roto Packing Material Industries Co. UAE","0012","30","15/12/2016","10","25/12/2016","15",party2);
        ArrayList<ItemModel> party3 = new ArrayList<>();
        ItemModel itemModel31 = new ItemModel("Polyester Film(Grade Printing)","25","25");
        ItemModel itemModel32 = new ItemModel("BOPP","25","30");
        party3.add(itemModel31);
        party3.add(itemModel32);
        OrderModel o3 = new OrderModel("Amcor Flexibiles Durban,South Africa","0157","50","19/12/2016","25","25/01/2017","25",party3);
        ArrayList<ItemModel> party4 = new ArrayList<>();
        ItemModel itemModel41 = new ItemModel("Polyester Film(Grade Printing)","2","25");
        ItemModel itemModel42 = new ItemModel("BOPP Film","25","50");
        party4.add(itemModel41);
        party4.add(itemModel42);
        OrderModel o4 = new OrderModel("Party C","12114","200",simpleDateFormat.format(date),"100",simpleDateFormat.format(date),"105",party4);
        ArrayList<ItemModel> party5 = new ArrayList<>();
        ItemModel itemModel51 = new ItemModel("Polyester Film(Grade Printing)","0","25");
        ItemModel itemModel52 = new ItemModel("BOPP Film","125","250");
        party5.add(itemModel51);
        party5.add(itemModel52);
        OrderModel o5 = new OrderModel("Party A","12115","200",simpleDateFormat.format(date),"100",simpleDateFormat.format(date),"500",party5);
        ArrayList<ItemModel> party6 = new ArrayList<>();
        ItemModel itemModel61 = new ItemModel("Polyester Film(Grade Printing)","0","25");
        ItemModel itemModel62 = new ItemModel("BOPP Film","12","25");
        party6.add(itemModel61);
        party6.add(itemModel62);
        OrderModel o6 = new OrderModel("Party X","12116","200",simpleDateFormat.format(date),"100",simpleDateFormat.format(date),"70",party6);
        orderModelList.add(o1);
        orderModelList.add(o2);
        orderModelList.add(o3);
        orderModelList.add(o4);
        orderModelList.add(o5);
        orderModelList.add(o6);
        for(OrderModel o: orderModelList){
            searchList.add(o);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        handleSearch(getIntent());
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setTitle(getResources().getString(R.string.order_status));
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.white));

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        handleSearch(getIntent());
    }

   /* private void handleSearch(Intent intent) {
   // USE THIS FUNCTION IF A SEARCHABLE ACTIVITY IS DEFINED WITH <intentfilter = ACTION_SEARCH>
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            *//**
             * Write logic here to update recycler list, and make nestedscrollview layout visible and reclerview
             * gone - if no results found...
             *//*

             boolean foundResult = false;
            if(!query.equalsIgnoreCase("")){
                orderModelList.clear();
                for(int i=0;i<searchList.size();i++){
                    if(searchList.get(i).getPartyName().equalsIgnoreCase(query)){
                        orderModelList.add(searchList.get(i));
                        foundResult = true;
                    }
                }
                if(!foundResult){
                    //No result found
                    recyclerView.setVisibility(View.GONE);
                    noSearchResultFound.setVisibility(View.VISIBLE);
                }else{
                    noSearchResultFound.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }else{
                orderModelList.clear();
                for(int i =0;i<searchList.size();i++){
                    orderModelList.add(searchList.get(i));
                }
                noSearchResultFound.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewAdapter.notifyDataSetChanged();
            }



        }
    }*/
    private void handleSearch(String query) {
            /**
             * Write logic here to update recycler list, and make nestedscrollview layout visible and reclerview
             * gone - if no results found...
             */

             boolean foundResult = false;
            if(!query.equalsIgnoreCase("")){
                orderModelList.clear();
                for(int i=0;i<searchList.size();i++){
                    if(searchList.get(i).getPartyName().equalsIgnoreCase(query) || searchList.get(i).getOrderNo().equalsIgnoreCase(query)){
                        orderModelList.add(searchList.get(i));
                        foundResult = true;
                    }
                }
                if(!foundResult){
                    //No result found
                    recyclerView.setVisibility(View.GONE);
                    noSearchResultFound.setVisibility(View.VISIBLE);
                }else{
                    noSearchResultFound.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }else{
                orderModelList.clear();
                for(int i =0;i<searchList.size();i++){
                    orderModelList.add(searchList.get(i));
                }
                noSearchResultFound.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewAdapter.notifyDataSetChanged();
            }

    }

    private void updateList(int initialPosition, int finalPosition) {
        if (initialPosition < finalPosition) {
            for (int i = initialPosition; i < finalPosition; i++) {
                Collections.swap(orderModelList, i, i + 1);
            }
        } else {
            for (int i = initialPosition; i > finalPosition; i--) {
                Collections.swap(orderModelList, i, i - 1);
            }
        }
    }
    private boolean dispalySnackBar(final int position) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout,"Undo Removed Order",Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderModelList.add(position,deletedOrders.get(new Integer(position)));
                recyclerViewAdapter.notifyItemInserted(position);
                recyclerView.scrollToPosition(position);
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        View sBarView = snackbar.getView();
        TextView textView = (TextView) sBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.white));
        snackbar.show();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search,menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        ((TextView)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHint(getResources().getString(R.string.search_hint));
        ((TextView)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.white));
        ((ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn)).setImageDrawable(getResources().getDrawable(R.drawable.search_24dp));
        if(!searchView.isIconified()){
            collapsingToolbar.setTitle("");
        }else{
            collapsingToolbar.setTitle(getResources().getString(R.string.order_status));
        }
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(getApplicationContext(),OrderStatus.class)));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                orderModelList.clear();
                for(int i =0;i<searchList.size();i++){
                    orderModelList.add(searchList.get(i));
                }
                noSearchResultFound.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewAdapter.notifyDataSetChanged();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                collapsingToolbar.setTitle(getResources().getString(R.string.order_status)+" "+query);
                handleSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equalsIgnoreCase("")){
                    collapsingToolbar.setTitle(getResources().getString(R.string.order_status));
                    orderModelList.clear();
                    for(int i =0;i<searchList.size();i++){
                        orderModelList.add(searchList.get(i));
                    }
                    noSearchResultFound.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/

        if(item.getItemId() == android.R.id.home){




           /* *//**
             * Call on back click,start service here.
             *//*
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            }else{
                Intent intent = new Intent(OrderStatus.this, VacmetOverlayService.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("OrderList",orderModelList);
                intent.putExtra("User",logingSharePrefs.getString(LoggedInUserName,null));
                if(LoginActivity.url!=null){
                    if(sharedPreferences.getString("PhotoUrl",null)==null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("PhotoUrl", LoginActivity.url);
                        editor.apply();
                    }
                    intent.putExtra("PhotoUrl",LoginActivity.url);
                }else if(sharedPreferences.getString("PhotoUrl",null)!=null){
                    intent.putExtra("PhotoUrl",sharedPreferences.getString("PhotoUrl",null));
                }
                (OrderStatus.this).finish();
                startService(intent);
            }
            return true;*/
        }
        return super.onOptionsItemSelected(item);
    }
    @TargetApi(23)
    private void permissionsCheck() {
      if(checkSelfPermission(android.Manifest.permission.SYSTEM_ALERT_WINDOW)!= PackageManager.PERMISSION_GRANTED){
          if (shouldShowRequestPermissionRationale(android.Manifest.permission.SYSTEM_ALERT_WINDOW)) {
          } else {
              requestPermissions(new String[]{android.Manifest.permission.SYSTEM_ALERT_WINDOW},
                      MY_PERMISSIONS_REQUEST_SYSTEM_ALERT_WINDOW);
          }
      }else{
          Intent intent = new Intent(OrderStatus.this, VacmetOverlayService.class);
          intent.putExtra("OrderList",orderModelList);
//          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.putExtra("User",logingSharePrefs.getString(LoggedInUserName,null));
          this.finish();
          startService(intent);
      }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_SYSTEM_ALERT_WINDOW:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(OrderStatus.this, VacmetOverlayService.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("OrderList",orderModelList);
                    intent.putExtra("User",logingSharePrefs.getString(LoggedInUserName,null));
                    this.finish();
                    startService(intent);
                }else{
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION){
            if(resultCode == RESULT_OK){
                Intent intent = new Intent(OrderStatus.this, VacmetOverlayService.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("OrderList",orderModelList);
                intent.putExtra("User",logingSharePrefs.getString(LoggedInUserName,null));
                if(LoginActivity.url!=null){
                    if(sharedPreferences.getString("PhotoUrl",null)==null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("PhotoUrl", LoginActivity.url);
                        editor.apply();
                    }
                    intent.putExtra("PhotoUrl",LoginActivity.url);
                }else if(sharedPreferences.getString("PhotoUrl",null)!=null){
                    intent.putExtra("PhotoUrl",sharedPreferences.getString("PhotoUrl",null));
                }
                this.finish();
                startService(intent);
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onBackPressed() {
        /**
         * start service here
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&& !Settings.canDrawOverlays(this)){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }else{
            Intent intent = new Intent(OrderStatus.this, VacmetOverlayService.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("OrderList",orderModelList);
            intent.putExtra("User",logingSharePrefs.getString(LoggedInUserName,null));
            if(LoginActivity.url!=null){
                if(sharedPreferences.getString("PhotoUrl",null)==null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("PhotoUrl", LoginActivity.url);
                    editor.apply();
                }
                intent.putExtra("PhotoUrl",LoginActivity.url);
            }else if(sharedPreferences.getString("PhotoUrl",null)!=null){
                intent.putExtra("PhotoUrl",sharedPreferences.getString("PhotoUrl",null));
            }
            this.finish();
            startService(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.floatingEdit:
                if(floatingActionButton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.edit_done).getConstantState()){
                    floatingActionButton.setImageResource(R.drawable.editing);
                    itemTouchHelper.attachToRecyclerView(recyclerView);

                }else if(floatingActionButton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.editing).getConstantState()){
                    floatingActionButton.setImageResource(R.drawable.edit_done);
                    itemTouchHelper.attachToRecyclerView(null);

                }
                break;
            case R.id.floatingLogout:
                SharedPreferences.Editor editor = logingSharePrefs.edit();
                editor.putString(LoggedInUser,null);
                editor.putString(LoggedInUserPassword,null);
                editor.apply();
                Intent intent = new Intent(OrderStatus.this,LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    private class CustomAsyncTaskForRestOrderService extends AsyncTask<String,Void,List<OrderModel>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog!=null && progressDialog.isShowing()){
              progressDialog.dismiss();
            }
            progressDialog.setMessage(getResources().getString(R.string.fetching_orders));
            progressDialog.show();
        }

        @Override
        protected List<OrderModel> doInBackground(String... params) {
            String appendedParamInUrl = "";
            String id = params[1];
            if(params[0].equalsIgnoreCase("c")) {
                appendedParamInUrl = "C="+id;
            }else if(params[0].equalsIgnoreCase("s")){
                appendedParamInUrl = "S="+id;
            }
            try {
                InputStream inputStream = new URL(urlForOrders+appendedParamInUrl).openConnection().getInputStream();
                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setInput(inputStream,null);
                int eventType = xmlPullParser.getEventType();
                OrderModel orderModel = null;
                orderModelList = new ArrayList<>();
                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            switch (xmlPullParser.getName()){
                                case Main_Xml_Tag:
                                    //Main Tag
                                    orderModel = new OrderModel();

                                    break;
                                case VBELN:
                                    if(orderModel!=null){
                                        orderModel.setOrderNo(xmlPullParser.nextText());
                                    }
                                    break;
                                case STATUS:
                                    if(orderModel!=null){
                                        orderModel.setStatus(xmlPullParser.nextText());
                                    }
                                    break;
                                case OPEN_QTY:
                                    if(orderModel!=null){
                                        orderModel.setInProdQty(xmlPullParser.nextText());
                                    }
                                    break;
                                case DESP_QTY:
                                    if(orderModel!=null){
                                        orderModel.setDespQty(xmlPullParser.nextText());
                                    }
                                    break;
                                case STOCK_QTY:
                                    //Todo
                                    break;
                                case DOC_DATE:
                                    if(orderModel!=null){
                                        orderModel.setDeliveryDate(xmlPullParser.nextText());
                                    }
                                    break;
                                case DEL_DATE:
                                    if(orderModel!=null){
                                        orderModel.setOrderDate(xmlPullParser.nextText());
                                    }
                                    break;
                                case CUST_NM:
                                    if(orderModel!=null){
                                        orderModel.setPartyName(xmlPullParser.nextText());
                                    }
                                    break;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            switch (xmlPullParser.getName()){
                                case Main_Xml_Tag:
                                    //Main Tag
                                    if(orderModel!=null){
                                        if(!orderModelList.contains(orderModel)) {
                                            orderModelList.add(orderModel);
                                        }
                                    }

                                    break;

                            }
                            break;

                    }
                    eventType = xmlPullParser.next();
                }





               /* URL url = new URL(urlForOrders+appendedParamInUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line = "";
                    while(reader.readLine()!=null){
                        line += reader.readLine();
                    }
                    XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                    xmlPullParserFactory.setNamespaceAware(true);
                    XmlPullParser pullParser = xmlPullParserFactory.newPullParser();
                    pullParser.setInput(new StringReader(line));
                    int event = pullParser.getEventType();
                    while(event != XmlPullParser.END_DOCUMENT){
                        switch (pullParser.getEventType()){
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                switch (pullParser.getName()){
                                    case Main_Xml_Tag:

                                        break;
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                break;
                            case XmlPullParser.TEXT:
                                break;
                        }
                        event = pullParser.next();
                    }


                }*/

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(OrderStatus.this, "Oops!! Something Went Wrong, Check Your Connection!!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Toast.makeText(OrderStatus.this, "Oops!! Something Went Wrong...", Toast.LENGTH_SHORT).show();
            }
            return orderModelList;
        }

        @Override
        protected void onPostExecute(List<OrderModel> s) {
            super.onPostExecute(s);
            for(OrderModel o: orderModelList){
                searchList.add(o);
            }
            progressDialog.dismiss();
            if(drawerLayout.isDrawerOpen(Gravity.START)){
                drawerLayout.closeDrawers();
            }
            recyclerViewAdapter = new RecyclerviewAdapter(OrderStatus.this,orderModelList,new ItemClickListener(){
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(OrderStatus.this,OrderInformation.class);
                    intent.putExtra("OrderInfo",orderModelList.get(position));
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }
}
