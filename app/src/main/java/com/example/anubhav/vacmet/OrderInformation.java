package com.example.anubhav.vacmet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Base64;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anubhav.vacmet.adapters.ListOrderInformation;
import com.example.anubhav.vacmet.interfaces.OrderDetailsClickListener;
import com.example.anubhav.vacmet.model.ItemModel;
import com.example.anubhav.vacmet.model.OrderModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anubhav on 27/1/17.
 */

public class OrderInformation extends AppCompatActivity implements OrderDetailsClickListener{
    public static final String soapUrlForItemsForAOrder = "http://122.160.221.107:8020/sap/bc/srt/rfc/sap/zget_so_status/500/zget_sosta/zget_so_sta";
    private static final String usernameForService = "BAPI";
    private static final String passwordForService = "123456";
    private static final String soapEnvRequest1 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <urn:ZBAPI_GET_SOSTATUS>\n" +
            "         <RET_TABLE>\n" +
            "            <!--Zero or more repetitions:-->\n" +
            "            <item>\n" +
            "            \n" +
            "            </item>\n" +
            "         </RET_TABLE>\n" +
            "         <SONBR>";
    private static final String soapEnvRequest2 = "</SONBR>\n" +
            "      </urn:ZBAPI_GET_SOSTATUS>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";
    private static final String MATNR = "MATNR";
    private static final String STATUS = "STATUS";
    private static final String OPEN_QTY = "OPEN_QTY";
    private static final String DESP_QTY = "DESP_QTY";
    private static final String STOCK_QTY = "STOCK_QTY";
    private static final String MATERIAL_NM = "MATERIAL_NM";
    private static final String ITEM = "item";
    private OrderModel orderModel;
    private TextView partyName,orderNo,orderDate,orderQty,requestDispatchDate,
    despQty,prodQty;
    private ArrayList<ItemModel> itemList;
    private ListView itemsListView;
    private ListOrderInformation listAdapter;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private LinearLayout llParent;
    private TextView stockQty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back_24dp));
        supportPostponeEnterTransition();
        if(getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().getSerializable("OrderInfo")!=null){
            orderModel = (OrderModel) getIntent().getExtras().getSerializable("OrderInfo");
            if(getIntent().getExtras().getString("TransitionName")!=null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    orderNo.setTransitionName(getIntent().getExtras().getString("TransitionName"));
                }
            }

            partyName.setText(orderModel.getPartyName());
            orderNo.setText(orderModel.getOrderNo());
            orderDate.setText(orderModel.getOrderDate());
            requestDispatchDate.setText(orderModel.getDeliveryDate());
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            orderQty.setText(orderModel.getOrderQty());
            stockQty.setText(orderModel.getStockQty());
            despQty.setText(orderModel.getDespQty());
            prodQty.setText(orderModel.getInProdQty());
//            itemList = orderModel.getItemModelArrayList();
            getSupportActionBar().setWindowTitle(orderModel.getPartyName());
            getSupportActionBar().setTitle(orderModel.getPartyName());
            toolbar.setTitle(orderModel.getPartyName());
            hitSOAPItemService(orderModel);
        }else{
            partyName.setText("");
            orderNo.setText("");
            orderDate.setText("");
            requestDispatchDate.setText("");
            orderQty.setText("");
            despQty.setText("");
            prodQty.setText("");
            itemList = null;
        }


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpTransitions();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpTransitions() {

     /*   getWindow().setEnterTransition(new Fade(Fade.OUT));
        TransitionSet sharedExitTransition = new TransitionSet();
        sharedExitTransition.addTransition(new ChangeBounds());
        sharedExitTransition.addTransition(new ChangeTransform());
        sharedExitTransition.addTransition(new ChangeImageTransform());
        getWindow().setSharedElementEnterTransition(sharedExitTransition);
        TransitionSet sharedEnterTransition = new TransitionSet();
        sharedEnterTransition.addTransition(new ChangeImageTransform());
        sharedEnterTransition.addTransition(new ChangeTransform());
        sharedEnterTransition.addTransition(new ChangeBounds());
        getWindow().setSharedElementEnterTransition(sharedEnterTransition);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);*/
    }

    private void hitSOAPItemService(OrderModel order) {
        String orderNo = order.getOrderNo();
//        new CustomAsyncTask().execute(orderNo);
        if(order.getItemList()!=null && order.getItemList().size()>0){
            listAdapter = new ListOrderInformation(OrderInformation.this,order.getItemList(),this);
            itemsListView.setAdapter(listAdapter);
        }
        supportStartPostponedEnterTransition();
    }

    private void init() {
        llParent = (LinearLayout) findViewById(R.id.ll_orderDetailsParent);
        partyName = (TextView) findViewById(R.id.orderInfo_partyName);
        orderNo = (TextView) findViewById(R.id.orderInfo_orderNo);
        orderDate = (TextView) findViewById(R.id.orderInfo_orderDate);
        requestDispatchDate = (TextView) findViewById(R.id.orderInfo_rdd);
        orderQty = (TextView) findViewById(R.id.orderInfo_orderQty);
        despQty = (TextView) findViewById(R.id.orderInfo_despQty);
        stockQty = (TextView) findViewById(R.id.orderInfo_stockQty);
        prodQty = (TextView) findViewById(R.id.orderInfo_prodQty);
        itemsListView = (ListView) findViewById(R.id.list_orderInformation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
//                supportFinishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view, int position) {
        LinearLayout expandedDetails = (LinearLayout) view.findViewById(R.id.expanded_item_details);
        if(expandedDetails.getVisibility() == View.VISIBLE){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionSet transitionSet = new TransitionSet();
                transitionSet.setDuration(1500);
                transitionSet.addTransition(new Fade(Fade.IN));
                transitionSet.addTransition(new ChangeBounds());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    transitionSet.addTransition(new ChangeTransform());
                }
                TransitionManager.beginDelayedTransition(expandedDetails,transitionSet);
            }
            expandedDetails.setVisibility(View.GONE);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionSet transitionSet = new TransitionSet();
                transitionSet.setDuration(1500);
                transitionSet.addTransition(new Fade(Fade.OUT));
                transitionSet.addTransition(new ChangeBounds());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    transitionSet.addTransition(new ChangeTransform());
                }
                TransitionManager.beginDelayedTransition(expandedDetails,transitionSet);
            }
            expandedDetails.setVisibility(View.VISIBLE);
        }
    }

    private class CustomAsyncTask extends AsyncTask<String,Void,List<ItemModel>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getResources().getString(R.string.fetching_items_in_order));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected List<ItemModel> doInBackground(String... params) {
            String orderNo = params[0];
            try {
                URL url = new URL(soapUrlForItemsForAOrder);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                String userPass = usernameForService +":"+ passwordForService;
                httpURLConnection.setRequestProperty("Content-Type","text/xml;charset=UTF-8");
                httpURLConnection.setRequestProperty("Content-Length","443");
                httpURLConnection.setRequestProperty("Authorization","Basic "+new String(Base64.encode(userPass.getBytes(),Base64.DEFAULT)));
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
                writer.write(soapEnvRequest1+orderNo+soapEnvRequest2);
                writer.flush();
                writer.close();
                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    itemList = new ArrayList<>();
                    ItemModel itemModel = null;
                    InputStream inputStream = httpURLConnection.getInputStream();
                    XmlPullParser xmlPullParser = Xml.newPullParser();
                    xmlPullParser.setInput(inputStream,null);
                    int eventType = xmlPullParser.getEventType();
                    boolean isItemHasName = false;
                    while(eventType != XmlPullParser.END_DOCUMENT){

                        switch (eventType){
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                switch (xmlPullParser.getName()){
                                    case ITEM:
                                        itemModel = new ItemModel();
                                        break;
                                    case MATNR:
                                        if(itemModel!=null){
                                            itemModel.setMaterialNo(xmlPullParser.nextText());
                                        }
                                        break;
                                    case STATUS:
                                        if(itemModel!=null){
                                            itemModel.setStatus(xmlPullParser.nextText());
                                        }
                                        break;
                                    case OPEN_QTY:
                                         if(itemModel!=null){
                                             itemModel.setInProdQty(xmlPullParser.nextText());
                                         }
                                        break;
                                    case DESP_QTY:
                                        if(itemModel!=null){
                                            itemModel.setDespQty(xmlPullParser.nextText());
                                        }
                                        break;
                                    case STOCK_QTY:
                                        //Todo
                                        break;
                                    case MATERIAL_NM:
                                        if(itemModel!=null){
                                            String nextItem = xmlPullParser.nextText();
                                            if(!TextUtils.isEmpty(nextItem)) {
                                                itemModel.setItemName(nextItem);
                                                isItemHasName = true;
                                            }
                                        }
                                        break;

                                }
                                break;
                            case XmlPullParser.END_TAG:
                                switch (xmlPullParser.getName()){
                                    case ITEM:
                                        if(itemModel!=null && isItemHasName){
                                            itemList.add(itemModel);
                                            isItemHasName = false;
                                        }
                                        break;
                                }
                                break;
                        }
                        eventType = xmlPullParser.next();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(OrderInformation.this, "Oops!! Something Went Wrong, Check Your Connection!!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Toast.makeText(OrderInformation.this, "Oops!! Something Went Wrong...", Toast.LENGTH_SHORT).show();
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<ItemModel> itemModels) {
            super.onPostExecute(itemModels);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(itemModels!=null){
                listAdapter = new ListOrderInformation(OrderInformation.this,itemModels,OrderInformation.this);
                itemsListView.setAdapter(listAdapter);
            }
        }
    }
}
