package com.example.anubhav.trial3;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anubhav.trial3.adapters.RecyclerviewAdapter;
import com.example.anubhav.trial3.model.OrderModel;
import com.example.anubhav.trial3.services.VacmetOverlayService;

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
        setContentView(R.layout.activity_order_status);
        init();
        feedDummyData();
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_24dp);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
   /*     toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OrderStatus.this, "Clicked!", Toast.LENGTH_SHORT).show();
                Log.e("", "onClick: Toolbar" );
                finish();
            }
        });*/

        floatingActionButton.setOnClickListener(this);
        floatingLogOut.setOnClickListener(this);
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

        recyclerView.setAdapter(recyclerViewAdapter);
    }
    private void init() {
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingEdit);
        floatingLogOut = (FloatingActionButton) findViewById(R.id.floatingLogout);
        floatingActionButton.setBackgroundColor(Color.WHITE);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        deletedOrders = new HashMap<>();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        ivCollapsingtoolbar = (ImageView) findViewById(R.id.ivCollapsingtoolbar);
        recyclerView = (RecyclerView) findViewById(R.id.orderStatusList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        orderModelList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerviewAdapter(this,orderModelList);
        noSearchResultFound = (NestedScrollView) findViewById(R.id.noSearchFound);
        searchList = new ArrayList<>();
    }
    private void feedDummyData() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        OrderModel o1 = new OrderModel("Party X","12111","200",simpleDateFormat.format(date),"100",simpleDateFormat.format(date));
        OrderModel o2 = new OrderModel("Party A","12112","200",simpleDateFormat.format(date),"100",simpleDateFormat.format(date));
        OrderModel o3 = new OrderModel("Party B","12113","200",simpleDateFormat.format(date),"100",simpleDateFormat.format(date));
        OrderModel o4 = new OrderModel("Party C","12114","200",simpleDateFormat.format(date),"100",simpleDateFormat.format(date));
        OrderModel o5 = new OrderModel("Party A","12115","200",simpleDateFormat.format(date),"100",simpleDateFormat.format(date));
        OrderModel o6 = new OrderModel("Party X","12116","200",simpleDateFormat.format(date),"100",simpleDateFormat.format(date));
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
        if(item.getItemId() == android.R.id.home){
            /**
             * Call on back click,start service here.
             */
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            }else{
                Intent intent = new Intent(OrderStatus.this, VacmetOverlayService.class);
                intent.putExtra("OrderList",orderModelList);
                startService(intent);
                finish();
            }
            return true;
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
          startService(intent);
          finish();
      }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_SYSTEM_ALERT_WINDOW:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(OrderStatus.this, VacmetOverlayService.class);
                    intent.putExtra("OrderList",orderModelList);
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
                intent.putExtra("OrderList",orderModelList);
                startService(intent);
                finish();
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
            intent.putExtra("OrderList",orderModelList);
            startService(intent);
            finish();
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
                Intent intent = new Intent(OrderStatus.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
