package com.imagesoftware.anubhav.vacmet.services;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imagesoftware.anubhav.vacmet.OrderInformation;
import com.imagesoftware.anubhav.vacmet.OrderStatus;
import com.imagesoftware.anubhav.vacmet.R;
import com.imagesoftware.anubhav.vacmet.adapters.RecyclerviewAdapter;
import com.imagesoftware.anubhav.vacmet.database.VacmetDatabase;
import com.imagesoftware.anubhav.vacmet.database.daos.DatabaseRequestsDao;
import com.imagesoftware.anubhav.vacmet.database.entities.OrderEntity;
import com.imagesoftware.anubhav.vacmet.database.translators.OrderTranslator;
import com.imagesoftware.anubhav.vacmet.interfaces.ItemClickListener;
import com.imagesoftware.anubhav.vacmet.model.ItemModel;
import com.imagesoftware.anubhav.vacmet.model.OrderContainer;
import com.imagesoftware.anubhav.vacmet.model.OrderModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Anubhav-Singh on 11-01-2018.
 */

public class RefereshNetworkService extends JobService {

    private CustomAsyncTaskForRestOrderService customAsynTaskForJob;
    public static final String VBELN = "VBELN";
    public static final String SALES_ORDER_NO = "SALES_ORDER_NO";
    public static final String STATUS = "STATUS";
    public static final String CUSTOMER_PO_DATE = "CUSTOMER_PO_DATE";
    public static final String CUSTOMER_PO_NO = "CUSTOMER_PO_NO";
    public static final String OPEN_QTY = "OPEN_QTY";
    public static final String DESP_QTY = "DESP_QTY";
    public static final String STOCK_QTY = "STOCK_QTY";
    public static final String ORDERED_QTY = "ORDERED_QTY";
    public static final String DOC_DATE = "DOC_DATE";
    public static final String DEL_DATE = "DEL_DATE";
    public static final String CUST_NM = "CUST_NM";
    private static final String MATNR = "MATNR";
    private static final String MATERIAL_NO = "MATERIAL_NO";
    private static final String MAIN_INVOICE = "ZBAPI_HDR_SISTATUS";
    private static final String INVOICE_NO = "INVOICE_NO";
    private static final String INVOICE_DATE = "INVOICE_DATE";
    private static final String ITEM_STATUS_ = "STATUS";
    private static final String ITEM_ORDERED_QTY_ = "ORDERED_QTY";
    private static final String ITEM_OPEN_QTY_ = "OPEN_QTY";
    private static final String ITEM_DESP_QTY_ = "DESP_QTY";
    private static final String ITEM_STOCK_QTY_ = "STOCK_QTY";
    private static final String ITEM_ORDER_DATE_ = "DOC_DATE";
    private static final String ITEM_DEL_DATE_ = "DEL_DATE";
    private static final String ITEM_MATERIAL_NM_ = "MATERIAL_NM";
    private static final String ITEM_TOTAL_QTY_ = "TOTAL_QTY";
    private static final String ITEM_CONTAINER_NO_ = "CONTAINER_NO";
    private static final String ITEM_BILL_NO_ = "BL_NO";
    private static final String ITEM_BL_DATE_ = "BL_DATE";
    private static final String ITEM_LENGTH_ = "LENGTH";
    private static final String ITEM_WIDTH_ = "WIDTH";
    private static final String ITEM_TREATMENT1_ = "TREATMENT1";
    private static final String ITEM_TREATMENT2_ = "TREATMENT2";
    private static final String ITEM_SHADES_ = "SHADES";
    private ArrayList<OrderModel> orderModelList;
    private ArrayList<OrderModel> orderContainerList;
    private String urlForOrders1 = "http://122.160.221.107:8020/sap/bc/";
    private String urlForOrders2 = "?sap-client=500&";
    private final String Main_Table_Xml_Tag = "ZBAPI_HDR_SOSTATUS";
    private final String Secondary_Table_Xml_Tag = "ZBAPI_SOSTATUS";
    private final String Secondary_Table_Invoice_Xml_Tag = "ZBAPI_SISTATUS";
    private String sapKey;
    private final String SAP_CODE = "sap_key";
    private final String CLIENT_SERVER_CODE = "client_server_key";
    private final String GET_PENDING_CODE = "get_pendingord";
    private final String GET_DISPATCH_CODE = "get_dispatch";
    private String clientServerKey;
    private int counter = 0;
    private JobParameters initialJob;
    private VacmetDatabase vacmetDatabase;
    private DatabaseRequestsDao databaseRequestsDao;
    private OrderTranslator orderTranslator;


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public boolean onStartJob(JobParameters job) {
        /**
         * This method operates on UI Thread.
         * We send the job to another thread, and immediately tells the function, that there is ongoing Job in worker thread,
         * hence onJobFinished would be required to call from the worker thread.
         *
         */
        vacmetDatabase =  Room.databaseBuilder(this,VacmetDatabase.class,"vacmet_db").build();
        databaseRequestsDao = vacmetDatabase.getDatabaseRequestDao();
        orderTranslator = new OrderTranslator();
        if(job!=null && job.getExtras()!=null && job.getExtras().getString(SAP_CODE)!=null && job.getExtras().getString(CLIENT_SERVER_CODE)!=null) {
            initialJob = job;
            sapKey = job.getExtras().getString(SAP_CODE);
            clientServerKey = job.getExtras().getString(CLIENT_SERVER_CODE);
            customAsynTaskForJob = (CustomAsyncTaskForRestOrderService) new CustomAsyncTaskForRestOrderService().execute(clientServerKey,sapKey,GET_PENDING_CODE);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        //Only called when job is finished midway, hence clear resources here, and return true, to indicate to reschedule job
        if(customAsynTaskForJob!=null){
            customAsynTaskForJob.cancel(true);
            counter = 0;
        }
        return true;
    }

    private class CustomAsyncTaskForRestOrderService extends AsyncTask<String, Void, List<OrderModel>> {
        String orderType = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<OrderModel> doInBackground(String... params) {

            String appendedParamInUrl = "";
            String id = params[1];
            orderType = params[2];
            if (params[0].equalsIgnoreCase("c")) {
                appendedParamInUrl = "C=" + id;
            } else if (params[0].equalsIgnoreCase("s")) {
                appendedParamInUrl = "S=" + id;
            }

                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(urlForOrders1 + orderType + urlForOrders2 + appendedParamInUrl).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Authorization", "Basic QkFQSToxMjM0NTY=");
                    httpURLConnection.setRequestProperty("Content-Type", "application/xml");
                    InputStream inputStream = httpURLConnection.getInputStream();
                    String line = "";
                    String response = "";
               /* BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }*/
                    Log.d("", "doInBackground: " + httpURLConnection.getResponseCode());
                    Log.d("", "doInBackground: " + response);
                    XmlPullParser xmlPullParser = Xml.newPullParser();
                    xmlPullParser.setInput(inputStream, null);
                    int eventType = xmlPullParser.getEventType();
                    OrderModel orderModel = null;
                    ItemModel itemModel = null;
                    OrderContainer orderContainer = null;
                    orderModelList = new ArrayList<>();
                    orderContainerList = new ArrayList<>();
                    boolean saveItemInOrder = false;
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                switch (xmlPullParser.getName()) {
                                    case Secondary_Table_Xml_Tag:
                                        saveItemInOrder = true;
                                        break;
                                    case Secondary_Table_Invoice_Xml_Tag:
                                        saveItemInOrder = true;
                                        break;
                                    case MAIN_INVOICE:
                                        orderModel = new OrderModel();

                                        break;
                                    case Main_Table_Xml_Tag:
                                        //Main Tag
                                        orderModel = new OrderModel();

                                        break;
                                    case INVOICE_NO:
                                        String invoiceNo = xmlPullParser.nextText();
                                        if (orderModel != null && !saveItemInOrder) {
                                            orderModel.setInvoiceNo(invoiceNo);
                                        } else {
                                            for (OrderModel orderModelFromList : orderModelList) {
                                                if (orderModelFromList.getInvoiceNo().equalsIgnoreCase(invoiceNo)) {
                                                    orderModel = orderModelFromList;
                                                }
                                            }
                                        }
                                        break;
                                    case INVOICE_DATE:
                                        if (orderModel != null && !saveItemInOrder) {
                                            orderModel.setInvoiceDate(xmlPullParser.nextText());
                                        } else if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setInvoiceDate(xmlPullParser.nextText());
                                        }
                                        break;
                                    case MATNR:
                                        itemModel = new ItemModel();
                                        itemModel.setMaterialNo(xmlPullParser.nextText());
                                        break;
                                    case MATERIAL_NO:
                                        itemModel = new ItemModel();
                                        itemModel.setMaterialNo(xmlPullParser.nextText());
                                        break;
                                    case VBELN:
                                        String orderNo = xmlPullParser.nextText();
                                        if (orderModel != null && !saveItemInOrder) {
                                            orderModel.setOrderNo(orderNo);
                                        } else {
                                            for (OrderModel orderModelFromList : orderModelList) {
                                                if (orderModelFromList.getOrderNo().equalsIgnoreCase(orderNo)) {
                                                    orderModel = orderModelFromList;
                                                }
                                            }
                                        }
                                        break;
                                    case SALES_ORDER_NO:
                                        if (itemModel != null) {
                                            itemModel.setContainedOrderNo(xmlPullParser.nextText());
                                        }
                                        break;
                                    case ORDERED_QTY:
                                        String qty = xmlPullParser.nextText();
                                        if (orderModel != null && !saveItemInOrder && !TextUtils.isEmpty(qty)) {
                                            orderModel.setOrderQty(qty);
                                        } else if (orderModel != null && saveItemInOrder && itemModel != null && !TextUtils.isEmpty(qty)) {
                                            itemModel.setOrderedQty(qty);
                                        }
                                        break;
                                    case STATUS:
                                        if (orderModel != null && !saveItemInOrder) {
                                            orderModel.setStatus(xmlPullParser.nextText());
                                        } else if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setStatus(xmlPullParser.nextText());
                                        }
                                        break;
                                    case CUSTOMER_PO_NO:
                                        /**
                                         * Here overriding for item would occur, as it was told that p.i details would remain same for all items.
                                         */
                                        if(orderModel!=null && saveItemInOrder){
                                            orderModel.setCustomerPONo(xmlPullParser.nextText().trim());
                                        }
                                        break;
                                    case CUSTOMER_PO_DATE:
                                        /**
                                         * Here overriding for item would occur, as it was told that p.i details would remain same for all items.
                                         */
                                        if(orderModel!=null && saveItemInOrder){
                                            orderModel.setCustomerPODate(xmlPullParser.nextText().trim());
                                        }
                                        break;
                                    case OPEN_QTY:
                                        String qty1 = xmlPullParser.nextText();
                                        if (orderModel != null && !saveItemInOrder && !TextUtils.isEmpty(qty1)) {
                                            orderModel.setInProdQty(qty1);
                                        } else if (orderModel != null && saveItemInOrder && itemModel != null && !TextUtils.isEmpty(qty1)) {
                                            itemModel.setInProdQty(qty1);
                                        }
                                        break;
                                    case DESP_QTY:
                                        String qty2 = xmlPullParser.nextText();
                                        if (orderModel != null && !saveItemInOrder && !TextUtils.isEmpty(qty2)) {
                                            orderModel.setDespQty(qty2);
                                        } else if (orderModel != null && saveItemInOrder && itemModel != null && !TextUtils.isEmpty(qty2)) {
                                            itemModel.setDespQty(qty2);
                                        }
                                        break;
                                    case STOCK_QTY:
                                        String qty3 = xmlPullParser.nextText();
                                        if (orderModel != null && !saveItemInOrder && !TextUtils.isEmpty(qty3)) {
                                            orderModel.setStockQty(qty3);
                                        } else if (orderModel != null && saveItemInOrder && itemModel != null && !TextUtils.isEmpty(qty3)) {
                                            itemModel.setStockQty(qty3);
                                        }
                                        break;
                                    case DOC_DATE:
                                        if (orderModel != null && !saveItemInOrder) {
//                                            orderModel.setDeliveryDate(xmlPullParser.nextText());
                                        } else if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setDeliveryDate(xmlPullParser.nextText());
                                        }
                                        break;
                                    case DEL_DATE:
                                        if (orderModel != null && !saveItemInOrder) {
                                            orderModel.setOrderDate(xmlPullParser.nextText());
                                        } else if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setOrderDate(xmlPullParser.nextText());
                                        }
                                        break;
                                    case CUST_NM:
                                        if (orderModel != null) {
                                            orderModel.setPartyName(xmlPullParser.nextText());
                                        }
                                        break;
                                    case ITEM_MATERIAL_NM_:
                                        if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setItemName(xmlPullParser.nextText());
                                        }
                                        break;
                                    case ITEM_TOTAL_QTY_:
                                        if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setTotalQty(xmlPullParser.nextText());
                                        }
                                        break;
                                    case ITEM_CONTAINER_NO_:
                                        if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            String contStr = xmlPullParser.nextText();
                                            itemModel.setContainerNo(contStr);
                                            orderModel.addContainerNo(contStr);
                                        }
                                        break;
                                    case ITEM_BILL_NO_:
                                        if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setBillNo(xmlPullParser.nextText());
                                        }
                                        break;
                                    case ITEM_BL_DATE_:
                                        if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setBillDate(xmlPullParser.nextText());
                                        }
                                        break;
                                    case ITEM_LENGTH_:
                                        if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setLength(xmlPullParser.nextText());
                                        }
                                        break;
                                    case ITEM_WIDTH_:
                                        if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setWidth(xmlPullParser.nextText());
                                        }
                                        break;
                                    case ITEM_TREATMENT1_:
                                        if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setTreatment1(xmlPullParser.nextText());
                                        }
                                        break;
                                    case ITEM_TREATMENT2_:
                                        if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setTreatment2(xmlPullParser.nextText());
                                        }
                                        break;
                                    case ITEM_SHADES_:
                                        if (orderModel != null && saveItemInOrder && itemModel != null) {
                                            itemModel.setShades(xmlPullParser.nextText());
                                        }
                                        break;

                                }
                                break;
                            case XmlPullParser.END_TAG:
                                switch (xmlPullParser.getName()) {
                                    case Main_Table_Xml_Tag:
                                        //Main Tag
                                        if (orderModel != null) {
                                            if (!orderModelList.contains(orderModel)) {
                                                orderModel.setIsPending(params[2].equalsIgnoreCase(GET_PENDING_CODE));
                                                orderModel.setSapId(sapKey);
                                                orderModelList.add(orderModel);
                                            }
                                        }

                                        break;
                                    case MAIN_INVOICE:
                                        if (orderModel != null) {
                                            orderModel.setIsPending(params[2].equalsIgnoreCase(GET_PENDING_CODE));
                                            orderModel.setSapId(sapKey);
                                            orderModelList.add(orderModel);
                                        }
                                        break;
                                    case Secondary_Table_Xml_Tag:
                                        if (itemModel != null) {
                                            if(orderType.equalsIgnoreCase(GET_DISPATCH_CODE)){
                                                itemModel.setSelectedOrderNo(orderModel.getInvoiceNo());
                                            }else{
                                                itemModel.setSelectedOrderNo(orderModel.getOrderNo());
                                            }

                                            orderModel.addItemInOrder(itemModel, orderType.equalsIgnoreCase(GET_DISPATCH_CODE) ? true : false);
                                        }
                                        saveItemInOrder = false;
                                        break;
                                    case Secondary_Table_Invoice_Xml_Tag:
                                        if (itemModel != null) {
                                            if(orderType.equalsIgnoreCase(GET_DISPATCH_CODE)){
                                                itemModel.setSelectedOrderNo(orderModel.getInvoiceNo());
                                            }else{
                                                itemModel.setSelectedOrderNo(orderModel.getOrderNo());
                                            }
                                            orderModel.addItemInOrder(itemModel, orderType.equalsIgnoreCase("get_dispatch") ? true : false);
                                        }
                                        saveItemInOrder = false;
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
                                    case Main_Table_Xml_Tag:

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
                    if(orderModelList!=null && orderModelList.size()>0){
                        saveInVacmetDatabase(orderModelList,orderType);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    orderModelList.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                    orderModelList.clear();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    orderModelList.clear();
                }

            return orderModelList;
        }

        @Override
        protected void onPostExecute(List<OrderModel> s) {
            super.onPostExecute(s);
            counter = counter+1;
            if(s!=null && s.size()>0 && counter < 2){
                new CustomAsyncTaskForRestOrderService().execute(clientServerKey,sapKey,GET_DISPATCH_CODE);
            }else if(s!=null && s.size()>0){
                jobFinished(initialJob,false);
            }

        }
    }
    private void saveInVacmetDatabase(ArrayList<OrderModel> orderModelList,String orderType) {
/**
 * Retrieve old data and put in new one if order matches
 */

        List<OrderEntity> orderEntityList = new ArrayList<>();
        for(OrderModel orderModel : orderModelList){
            orderEntityList.add(orderTranslator.translateModelToEntity(orderModel));
        }
        if(orderType.equalsIgnoreCase(GET_PENDING_CODE)) {
            for (OrderEntity orderEntity : databaseRequestsDao.getOrdersForSapIdAndOrderType(sapKey, 1)){
                for(OrderEntity newOrderEntity : orderEntityList){
                    if(newOrderEntity.getOrderNo().equalsIgnoreCase(orderEntity.getOrderNo())){
                        newOrderEntity.setDeliveryDate(orderEntity.getDeliveryDate());
                    }
                }
            }
        }
        databaseRequestsDao.insertOrders(orderEntityList);
        for(OrderEntity orderEntity : orderEntityList){
            if(orderEntity.getItemModelArrayList()!=null && orderEntity.getItemModelArrayList().size()>0) {
                databaseRequestsDao.insertItems(orderEntity.getItemModelArrayList());
            }
        }

    }


}
