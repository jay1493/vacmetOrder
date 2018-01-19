package com.imagesoftware.anubhav.vacmet;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.imagesoftware.anubhav.vacmet.adapters.RecyclerviewAdapter;
import com.imagesoftware.anubhav.vacmet.database.VacmetDatabase;
import com.imagesoftware.anubhav.vacmet.database.daos.DatabaseRequestsDao;
import com.imagesoftware.anubhav.vacmet.database.entities.ItemEntity;
import com.imagesoftware.anubhav.vacmet.database.entities.OrderEntity;
import com.imagesoftware.anubhav.vacmet.database.translators.ItemTranslator;
import com.imagesoftware.anubhav.vacmet.database.translators.OrderTranslator;
import com.imagesoftware.anubhav.vacmet.interfaces.ItemClickListener;
import com.imagesoftware.anubhav.vacmet.model.AdminModel;
import com.imagesoftware.anubhav.vacmet.model.InvoiceTo;
import com.imagesoftware.anubhav.vacmet.model.ItemModel;
import com.imagesoftware.anubhav.vacmet.model.LogisticsModel;
import com.imagesoftware.anubhav.vacmet.model.OrderContainer;
import com.imagesoftware.anubhav.vacmet.model.OrderModel;
import com.imagesoftware.anubhav.vacmet.services.RefereshNetworkService;
import com.imagesoftware.anubhav.vacmet.services.UserAccessJobService;
import com.imagesoftware.anubhav.vacmet.services.VacmetOverlayService;
import com.imagesoftware.anubhav.vacmet.utils.CircleTransform;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.theartofdev.edmodo.cropper.CropImage;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by anubhav on 23/1/17.
 */

public class OrderStatus extends AppCompatActivity implements View.OnClickListener,RecyclerviewAdapter.OpenPdfClicked,RecyclerviewAdapter.DeliveryDateChanged,RecyclerviewAdapter.RemarkEntered,
        RecyclerviewAdapter.UploadInvoiceListener,RecyclerviewAdapter.LogisticsDetailsListener, RecyclerviewAdapter.ShowPrevModifiedDatesListener{


    private static final int MY_PERMISSIONS_REQUEST_SYSTEM_ALERT_WINDOW = 9090;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 9092;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE_RESULT = 1;
    private static final String SERVER_IP = "http://18.217.26.130";
    private static final String URL_SAVE_INVOICE = "/Springs_Chat/chatServlet/saveInvoice";
    private static final String URL_GET_INVOICE = "/Springs_Chat/chatServlet/getInvoice/";
    private static int mImageCounter = 0;
    public static final String VBELN = "VBELN";
    public static final String SALES_ORDER_NO = "SALES_ORDER_NO";
    public static final String STATUS = "STATUS";
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
    private static final String ADMIN_ACCESS = "ADMIN_ACCESS";
    private ShimmerRecyclerView recyclerView;
    private ArrayList<OrderModel> orderModelList;
    private RecyclerviewAdapter recyclerViewAdapter;
    private NestedScrollView noSearchResultFound;
    private Toolbar toolbar;
    private ImageView ivCollapsingtoolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private CoordinatorLayout coordinatorLayout;
    private HashMap<Integer, OrderModel> deletedOrders;
    private ArrayList<OrderModel> searchList;
    private FloatingActionButton floatingActionButton, floatingLogOut;
    private ItemTouchHelper itemTouchHelper;
    private SharedPreferences sharedPreferences, logingSharePrefs;
    private final String LoginPrefs = "LoginPrefs";
    private final String SapId = "SapId";
    private final String OrderIdPrefs = "OrderIdPrefs";
    private final String ClientorServer = "ClientorServer";
    private final String LoggedInUser = "LoggedInUser";
    private final String LoggedInUserName = "LoggedInUserName";
    private final String LoggedInUserPassword = "LoggedInUserPassword";
    private String urlForOrders1 = "http://122.160.221.107:8020/sap/bc/";
    private String urlForOrders2 = "?sap-client=500&";
    private ProgressDialog progressDialog;
    private final String Main_Table_Xml_Tag = "ZBAPI_HDR_SOSTATUS";
    private final String Secondary_Table_Xml_Tag = "ZBAPI_SOSTATUS";
    private final String Secondary_Table_Invoice_Xml_Tag = "ZBAPI_SISTATUS";
    private DrawerLayout drawerLayout;
    private LinearLayout mainDrawerView;
    private EditText etSapId;
    private ImageView imgEditSapId;
    private TextView employeeName, employeeDesig;
    private ImageView employeePic;
    private ActionBarDrawerToggle drawerToggle;
    private SharedPreferences orderIdPrefs;
    private String DefaultSapId = "1400056";
    private Button btnUpdateService;
    private RadioGroup radioGroup;
    private RadioButton radioClient, radioServer;
    private LinearLayout adminConsole;
    private RadioGroup sortOrdersGroup;
    private RadioButton openOrdersRadio, closedOrdersRadio;
    private ArrayList<OrderContainer> orderContainerList;
    private Button uploadInvoices;
    private ArrayList<String> cameraSelectedImagesUris;
    private ArrayList<String> croppedImagesUri;
    private String filenameToSaveInDb;
    private Context activity;
    private File fileToSave;
    private Gson customGson;
    private File fileFromDb;
    private VacmetDatabase vacmetDatabase;
    private DatabaseRequestsDao databaseRequestsDao;
    private LocalOrderRefereshBroadcastReceiver localBroadcast;
    private IntentFilter intentFilter;
    private FirebaseJobDispatcher firebaseJobDispatcher;
    private final String jobExtraKey = "user_email";
    private OrderTranslator orderTranslator;
    private ItemTranslator itemTranslator;
    private View offlineClickedView;
    private String offlineOrderType;
    private final String GET_PENDING_CODE = "get_pendingord";
    private final String GET_DISPATCH_CODE = "get_dispatch";
    private final String SAP_CODE = "sap_key";
    private final String CLIENT_SERVER_CODE = "client_server_key";
    private static final String LOG_IN_MODE_IS_EXISTING_USER = "LOG_IN_MODE_IS_EXISTING_USER";
    private boolean replaceJobSchedulers;
    private boolean isUserAdmin;
    private DatabaseReference mReadDatabase;
    private ValueEventListener valueEventListenerForOrders;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mWriteDatabase;
    private FirebaseDatabase db;
    private AlertDialog.Builder alertBuilderForRemarksInput;
    private SwipeRefreshLayout swipeRefereshLayout;
    private HashMap<String, OrderEntity> orderEntityMap;
    private EditText logisticsBillNo;
    private EditText logisticsVesselNo;
    private EditText logisticsContainerNo;
    private EditText logisticsETA;
    private AlertDialog.Builder builderForLogistics;
    private StringBuilder logisticsStringBuilder;
    private DatabaseReference mWriteDatabaseForLogistics;
    private ValueEventListener valueEventListenerForLogistics;
    private DatabaseReference mReadDatabaseForLogistics;
    private List<OrderEntity> anySavedOrders;



    @Override
    protected void onStart() {
        super.onStart();
        if (getSupportActionBar() != null) {
            setSupportActionBar(toolbar);
            ActionBar bar = getSupportActionBar();
            bar.setDisplayHomeAsUpEnabled(true);
        }
        if(mReadDatabase !=null){
            mReadDatabase.addValueEventListener(valueEventListenerForOrders);
        }
        if(mReadDatabaseForLogistics !=null){
            mReadDatabaseForLogistics.addValueEventListener(valueEventListenerForLogistics);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mReadDatabaseForLogistics !=null){
            mReadDatabaseForLogistics.removeEventListener(valueEventListenerForLogistics);
        }
        if(mReadDatabase !=null){
            mReadDatabase.removeEventListener(valueEventListenerForOrders);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_nav_drawer);
        init();
        logingSharePrefs = getSharedPreferences(LoginPrefs, MODE_PRIVATE);
        orderIdPrefs = getSharedPreferences(OrderIdPrefs, MODE_PRIVATE);
        if (orderIdPrefs.getString(SapId, null) == null) {
            SharedPreferences.Editor editor = orderIdPrefs.edit();
            editor.putString(SapId, DefaultSapId);
            editor.apply();
        } else {
            DefaultSapId = orderIdPrefs.getString(SapId, null);
        }
        etSapId.setText(orderIdPrefs.getString(SapId, null));
        if (logingSharePrefs.getString(LoggedInUserName, null) != null) {
            employeeName.setText(logingSharePrefs.getString(LoggedInUserName, null));
        }
//        feedDummyData();
        if (orderIdPrefs.getString(ClientorServer, null) == null) {
            SharedPreferences.Editor editor = orderIdPrefs.edit();
            editor.putString(ClientorServer, "c");
            editor.apply();
        }

        vacmetDatabase =  Room.databaseBuilder(OrderStatus.this,VacmetDatabase.class,"vacmet_db").build();
       databaseRequestsDao = vacmetDatabase.getDatabaseRequestDao();

        if(!logingSharePrefs.getBoolean(LOG_IN_MODE_IS_EXISTING_USER,false)){
            new CustomDeleteOfflineTables().execute();
            replaceJobSchedulers = true;
        }
        initializeJobDispatcherService();
        setUpAuthenticationForWritingData();
        setUpAuthenticationForWritingDataForLogistics();

        hitOrdersService(orderIdPrefs.getString(ClientorServer, null), DefaultSapId, "get_pendingord");

        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {

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
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN
                , ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                updateList(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                recyclerViewAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deletedOrders.put(new Integer(viewHolder.getAdapterPosition()), orderModelList.get(viewHolder.getAdapterPosition()));
                orderModelList.remove(viewHolder.getAdapterPosition());
                dispalySnackBarForAdvanceRecylerViewItemRemoved(viewHolder.getAdapterPosition());
                recyclerViewAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        if(logingSharePrefs.getBoolean(ADMIN_ACCESS,false)){
            isUserAdmin = true;
        }
        if(!isUserAdmin){
            adminConsole.setVisibility(View.GONE);
        }else{
            adminConsole.setVisibility(View.VISIBLE);
        }


    }

    private void setUpAuthenticationForWritingData() {
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth!=null){
            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseDatabase db = getFirebaseDatabaseInstance();
                        if(db!=null) {
                            mWriteDatabase = db.getReference("vacmet-44d24").child("orders");
                        }
                    }
                }
            });
        }

    }
    private void setUpAuthenticationForWritingDataForLogistics() {
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth!=null){
            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseDatabase db = getFirebaseDatabaseInstance();
                        if(db!=null) {
                            mWriteDatabaseForLogistics = db.getReference("vacmet-44d24").child("dispatch-logistics");
                        }
                    }
                }
            });
        }

    }

    private void setUpListenerForOrdersData() {
        FirebaseDatabase db = getFirebaseDatabaseInstance();
        if(db!=null) {
            mReadDatabase = db.getReference("vacmet-44d24").child("orders");
            if (mReadDatabase != null) {
                valueEventListenerForOrders = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        new RefreshDatesInOrders().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                       dispalyGeneralizedSnackBar(getResources().getString(R.string.data_cant_be_saved_in_firebase));
                    }
                };
            }
        }
    }

    private void setUpListenerForLogisticsData() {
        FirebaseDatabase db = getFirebaseDatabaseInstance();
        if(db!=null) {
            mReadDatabaseForLogistics = db.getReference("vacmet-44d24").child("dispatch-logistics");
            if (mReadDatabaseForLogistics != null) {
                valueEventListenerForLogistics = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        new RefreshDatesInOrders().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dispalyGeneralizedSnackBar(getResources().getString(R.string.data_cant_be_saved_in_firebase));
                    }
                };
            }
        }
    }


    private FirebaseDatabase getFirebaseDatabaseInstance() {
        if(db == null) {
            db = FirebaseDatabase.getInstance();
        }
        return db;
    }

    @Override
    public void onRemarkEnteredFromRecylerItem(View view, int pos) {
       initializeRemarkDialog(view,pos);
    }

    @Override
    public void onUploadInvoicePdfClicked(View view, int pos) {
        filenameToSaveInDb = orderModelList.get(pos).getInvoiceNo();
        startAddingImages();
    }

    @Override
    public void onLogisticsDetailsInput(View view, int pos) {
      initializeLogisticsDialog(view,pos);
    }

    @Override
    public void showPreviousDates(View view, int pos) {
       initiatePreviousDatesDialog(view,pos);
    }

    class RefreshDatesInOrders extends AsyncTask<DataSnapshot,Void,Void>{

        @Override
        protected Void doInBackground(DataSnapshot... dataSnapshots) {
            if(dataSnapshots[0]!=null) {
                DataSnapshot matchedData = null;

                        if(orderModelList!=null && orderModelList.size()>0){
                            OrderModel matchedOrderModel = null;
                            for (OrderModel orderModel : orderModelList) {
                                for (DataSnapshot dataSnapshot : dataSnapshots[0].getChildren()) {
                                    if(openOrdersRadio.isChecked()) {
                                        if (dataSnapshot.getKey().equalsIgnoreCase(orderModel.getOrderNo())) {
                                            matchedData = dataSnapshot;
                                            matchedOrderModel = orderModel;
                                            if (matchedData != null) {
                                                AdminModel fetchedAdminModel = matchedData.getValue(AdminModel.class);
                                                String dt = fetchedAdminModel.getDeliveryDate();
                                                if(dt.contains(getResources().getString(R.string.date_modification_split_key))){
                                                    List<String> modifiedDatesList = new ArrayList<>();
                                                    String[] splitDts = dt.split(Pattern.quote(getResources().getString(R.string.date_modification_split_key)));
                                                    for (int i=0;i<splitDts.length-1;i++) {
                                                        modifiedDatesList.add(splitDts[i]);
                                                    }
                                                     matchedOrderModel.setOldModifiedDates(new ArrayList<String>(modifiedDatesList));
                                                     String fetchNewDt = dt.substring(dt.lastIndexOf(getResources().getString(R.string.date_modification_split_key))+(getResources().getString(R.string.date_modification_split_key).length()),dt.length());
                                                     matchedOrderModel.setDeliveryDate(fetchNewDt);
                                                }else {
                                                    matchedOrderModel.setDeliveryDate(fetchedAdminModel.getDeliveryDate());
                                                }
                                                matchedOrderModel.setAdminNotes(fetchedAdminModel.getAdminNote());
                                                OrderEntity orderEntityFromSavedDb = databaseRequestsDao.getOrderForOrderNo(orderIdPrefs.getString(SapId, null), 1, matchedOrderModel.getOrderNo());
                                                orderEntityFromSavedDb.setDeliveryDate(matchedOrderModel.getDeliveryDate());
                                                orderEntityFromSavedDb.setAdminNotes(matchedOrderModel.getAdminNotes());
                                                orderEntityFromSavedDb.setOldModifiedDates(matchedOrderModel.getOldModifiedDates());
                                                databaseRequestsDao.updateOrders(orderEntityFromSavedDb);
                                            }
                                        }
                                    }else if(closedOrdersRadio.isChecked()){
                                        if (dataSnapshot.getKey().equalsIgnoreCase(orderModel.getInvoiceNo())) {
                                            matchedData = dataSnapshot;
                                            matchedOrderModel = orderModel;
                                            if (matchedData != null) {
                                                LogisticsModel fetchedAdminModel = matchedData.getValue(LogisticsModel.class);
                                                LogisticsModel logisticsModel = null;
                                                if(matchedOrderModel.getLogisticsModel() == null){
                                                    logisticsModel = new LogisticsModel();
                                                }else{
                                                    logisticsModel = matchedOrderModel.getLogisticsModel();
                                                }
                                                logisticsModel.setBlNo(fetchedAdminModel.getBlNo());
                                                logisticsModel.setContainerNo(fetchedAdminModel.getContainerNo());
                                                logisticsModel.setVesselNo(fetchedAdminModel.getVesselNo());
                                                logisticsModel.setEta(fetchedAdminModel.getEta());
                                                matchedOrderModel.setLogisticsModel(logisticsModel);
                                                OrderEntity orderEntityFromSavedDb = databaseRequestsDao.getOrderForInvoiceNo(orderIdPrefs.getString(SapId, null), 0, matchedOrderModel.getInvoiceNo());
                                                orderEntityFromSavedDb.setLogisticsModel(matchedOrderModel.getLogisticsModel());
                                                databaseRequestsDao.updateOrders(orderEntityFromSavedDb);
                                            }
                                        }
                                    }
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerViewAdapter = new RecyclerviewAdapter(OrderStatus.this, orderModelList, new ItemClickListener() {
                                        @Override
                                        public void onClick(View view, int position, View clickedView) {
                                            if(!connectionIsOnline() || (anySavedOrders!=null && anySavedOrders.size()>0)){
                                                //Fill Items for order at pos, in background
                                                fillOfflineItemsForOrder(position,closedOrdersRadio.isChecked()?GET_DISPATCH_CODE:GET_PENDING_CODE,clickedView);
                                            }else if(connectionIsOnline() && (anySavedOrders == null || (anySavedOrders!=null && anySavedOrders.size() ==0))){
                                                Intent intent = new Intent(OrderStatus.this, OrderInformation.class);
                                                intent.putExtra("OrderInfo", orderModelList.get(position));
                                                intent.putExtra("TransitionName", ViewCompat.getTransitionName(clickedView));
                                                intent.putExtra("isDispatched",closedOrdersRadio.isChecked()?true:false);
                                                startActivity(intent);
                                            }

                                        }


                                    },closedOrdersRadio.isChecked()?true:false,OrderStatus.this,OrderStatus.this,isUserAdmin,OrderStatus.this,OrderStatus.this,OrderStatus.this,OrderStatus.this);
                                    recyclerView.setAdapter(recyclerViewAdapter);
                                }
                            });

                        }

            }
            return null;
        }
    }

    @Override
    public void onDateClickListenerCall(final View viewHolder, final int pos) {
        /**
         * Change view date,refresh list, and update order.
         */
        Calendar cal = Calendar.getInstance();
        String initialDespDt = orderModelList.get(pos).getDeliveryDate();
        int yr = cal.get(Calendar.YEAR);
        int mnth = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if(!TextUtils.isEmpty(orderModelList.get(pos).getDeliveryDate())) {
            String[] dt = initialDespDt.split(Pattern.quote("-"));
            yr = Integer.parseInt(dt[0]);
            mnth = Integer.parseInt(dt[1]) - 1;
            day = Integer.parseInt(dt[2]);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.MONTH, mnth);
            cal.set(Calendar.YEAR, yr);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,android.R.style.Theme_Holo_Dialog_NoActionBar ,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                 StringBuilder stringBuilder = new StringBuilder();
                 stringBuilder.append(String.valueOf(year));
                 stringBuilder.append("-");
                 String m = String.valueOf(month+1);
                 if(m.length() == 1) {
                     stringBuilder.append("0");
                     stringBuilder.append(m);
                 }else{
                     stringBuilder.append(m);
                 }
                stringBuilder.append("-");
                stringBuilder.append(String.valueOf(dayOfMonth));
                orderModelList.get(pos).setDeliveryDate(stringBuilder.toString());
                TextView delDt = (TextView)(viewHolder.findViewById(R.id.deliveryDate));
                delDt.setText(stringBuilder.toString());
                updateOrder(pos);

            }
        },yr,mnth,day);
        datePickerDialog.show();
    }

    private void updateOrder(int pos) {
        new UpdateOrderDueToDeliveryDate().execute(pos);
    }
    class UpdateOrderDueToDeliveryDate extends AsyncTask<Integer,Void,Void>{


        @Override
        protected Void doInBackground(Integer... integers) {
            final int pos = integers[0];
            OrderEntity orderEntity = orderTranslator.translateModelToEntity(orderModelList.get(pos));
            orderEntity.setOrderId(orderModelList.get(pos).getOrderId());
            final OrderEntity dummyOrder = orderEntity;
            databaseRequestsDao.updateOrders(orderEntity);

                if(openOrdersRadio.isChecked()) {
                    if(mWriteDatabase!=null) {
                        mWriteDatabase.child(orderEntity.getOrderNo()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                AdminModel fetchOldModel = dataSnapshot.getValue(AdminModel.class);
                                if(fetchOldModel!=null && !TextUtils.isEmpty(fetchOldModel.getDeliveryDate())){
                                    List<String> modifiedDatesList = new ArrayList<>();
                                    //Retrieve old value, and append new date at the end
                                    String dt = fetchOldModel.getDeliveryDate();
                                    StringBuilder dateBuilder = new StringBuilder();
                                    if(dt.contains(getResources().getString(R.string.date_modification_split_key))) {
                                        String[] splitDts = fetchOldModel.getDeliveryDate().split(Pattern.quote(getResources().getString(R.string.date_modification_split_key)));
                                        for (int i=0;i<splitDts.length-1;i++) {
                                            modifiedDatesList.add(splitDts[i]);
                                        }
                                        dateBuilder.append(dt);
                                        dateBuilder.append(getResources().getString(R.string.date_modification_split_key));
                                        dateBuilder.append(dummyOrder.getDeliveryDate());
                                        AdminModel adminModel = new AdminModel();
                                        adminModel.setDeliveryDate(dateBuilder.toString());
                                        adminModel.setAdminNote(dummyOrder.getAdminNotes());
                                        mWriteDatabase.child(dummyOrder.getOrderNo()).setValue(adminModel);

                                    }else{
                                        //Consists of one date only
                                        modifiedDatesList.add(dt);
                                        dateBuilder.append(dt);
                                        dateBuilder.append(getResources().getString(R.string.date_modification_split_key));
                                        dateBuilder.append(dummyOrder.getDeliveryDate());
                                        AdminModel adminModel = new AdminModel();
                                        adminModel.setDeliveryDate(dateBuilder.toString());
                                        adminModel.setAdminNote(dummyOrder.getAdminNotes());
                                        mWriteDatabase.child(dummyOrder.getOrderNo()).setValue(adminModel);
                                    }
                                   if(modifiedDatesList!=null && modifiedDatesList.size()>0){
                                        orderModelList.get(pos).setOldModifiedDates(new ArrayList<String>(modifiedDatesList));
                                        final List<String> finalList = modifiedDatesList;
                                        Thread thread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                OrderEntity entity = databaseRequestsDao.getOrderForOrderNo(orderIdPrefs.getString(SapId, null), 1, orderModelList.get(pos).getOrderNo());
                                                entity.setOldModifiedDates(new ArrayList<String>(finalList));
                                                databaseRequestsDao.updateOrders(entity);
                                            }
                                        });
                                        thread.start();

                                   }

                                }else{
                                    //Just Set Value
                                    AdminModel adminModel = new AdminModel();
                                    adminModel.setDeliveryDate(dummyOrder.getDeliveryDate());
                                    adminModel.setAdminNote(dummyOrder.getAdminNotes());
                                    mWriteDatabase.child(dummyOrder.getOrderNo()).setValue(adminModel);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                dispalyGeneralizedSnackBar(getResources().getString(R.string.data_cant_be_saved_in_firebase));
                            }
                        });
                    }
                }else if(closedOrdersRadio.isChecked()){
                    if(mWriteDatabaseForLogistics!=null) {
                        mWriteDatabaseForLogistics.child(orderEntity.getInvoiceNo()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                LogisticsModel logisticsModel = new LogisticsModel();
                                logisticsModel.setEta(dummyOrder.getLogisticsModel().getEta());
                                logisticsModel.setVesselNo(dummyOrder.getLogisticsModel().getVesselNo());
                                logisticsModel.setContainerNo(dummyOrder.getLogisticsModel().getContainerNo());
                                logisticsModel.setBlNo(dummyOrder.getLogisticsModel().getBlNo());
                                mWriteDatabaseForLogistics.child(dummyOrder.getInvoiceNo()).setValue(logisticsModel);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                dispalyGeneralizedSnackBar(getResources().getString(R.string.data_cant_be_saved_in_firebase));
                            }
                        });
                    }
                }

            return null;
        }
    }

    class CustomDeleteOfflineTables extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            databaseRequestsDao.deleteOrders();
            databaseRequestsDao.deleteItems();
            return null;
        }
    }

    private void initializeJobDispatcherService() {
/**
 * setReplaceCurrent is necessary as it replaces old user creds in extras, sent to service.
 */
            firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
            Bundle jobExtras = new Bundle();
            jobExtras.putString(jobExtraKey,logingSharePrefs.getString(LoggedInUser, null));
            Job makeJob = firebaseJobDispatcher.newJobBuilder().setTag(getString(R.string.user_access_job)).setService(UserAccessJobService.class)
                     .setExtras(jobExtras).setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL).setRecurring(true).setLifetime(Lifetime.FOREVER)
                     .setReplaceCurrent(replaceJobSchedulers).setTrigger(Trigger.executionWindow(7200,8400)).setConstraints(Constraint.ON_ANY_NETWORK).build();
            Bundle networkExtras = new Bundle();
            networkExtras.putString(SAP_CODE,orderIdPrefs.getString(SapId, null));
            networkExtras.putString(CLIENT_SERVER_CODE,orderIdPrefs.getString(ClientorServer, null));
            Job networkJob = firebaseJobDispatcher.newJobBuilder().setTag(getString(R.string.network_refersh_job)).setService(RefereshNetworkService.class)
                    .setExtras(networkExtras).setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL).setRecurring(true).setLifetime(Lifetime.FOREVER)
                    .setReplaceCurrent(replaceJobSchedulers).setTrigger(Trigger.executionWindow(86400,87000)).setConstraints(Constraint.ON_ANY_NETWORK).build();
            firebaseJobDispatcher.mustSchedule(makeJob);
            firebaseJobDispatcher.mustSchedule(networkJob);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
        if (orderIdPrefs.getString(ClientorServer, null).equalsIgnoreCase("c")) {
            employeeDesig.setText(getResources().getString(R.string.client));
            radioClient.setChecked(true);
        } else if (orderIdPrefs.getString(ClientorServer, null).equalsIgnoreCase("s")) {
            employeeDesig.setText(getResources().getString(R.string.Sales_executive));
            radioServer.setChecked(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpTransitionEffects();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpTransitionEffects() {
        getWindow().setExitTransition(new Fade(Fade.IN));
        getWindow().setEnterTransition(new Fade(Fade.OUT));
        TransitionSet sharedExitTransition = new TransitionSet();
        sharedExitTransition.addTransition(new ChangeBounds());
        sharedExitTransition.addTransition(new ChangeTransform());
        sharedExitTransition.addTransition(new ChangeImageTransform());
        getWindow().setSharedElementExitTransition(sharedExitTransition);
        TransitionSet sharedEnterTransition = new TransitionSet();
        sharedEnterTransition.addTransition(new ChangeImageTransform());
        sharedEnterTransition.addTransition(new ChangeTransform());
        sharedEnterTransition.addTransition(new ChangeBounds());
        getWindow().setSharedElementEnterTransition(sharedEnterTransition);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);

    }

    class LocalOrderRefereshBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null && intent.getAction()!=null && (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") ||
                    intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED"))){
                if(connectionIsOnline()){
                    setUpAuthenticationForWritingData();
                    setUpAuthenticationForWritingDataForLogistics();
                    hitOrdersService(orderIdPrefs.getString(ClientorServer, null), DefaultSapId, openOrdersRadio.isChecked() ? "get_pendingord" : "get_dispatch");
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void hitOrdersService(String client, String id, String orderType) {

        new CustomAsyncTaskForRestOrderService().execute(client, id, orderType);
    }

    private void init() {
        orderTranslator = new OrderTranslator();
        itemTranslator = new ItemTranslator();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        localBroadcast = new LocalOrderRefereshBroadcastReceiver();


        customGson = new GsonBuilder().registerTypeHierarchyAdapter(byte[].class,
                                new ByteArrayToBase64TypeAdapter()).create();
        activity = this;
        cameraSelectedImagesUris = new ArrayList<>();
        croppedImagesUri = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        adminConsole = (LinearLayout) findViewById(R.id.admin_drawer);
        sortOrdersGroup = (RadioGroup) findViewById(R.id.sort_orders_by_group);

        openOrdersRadio = (RadioButton) findViewById(R.id.radioOpenOrders);
        closedOrdersRadio = (RadioButton) findViewById(R.id.radioClosedOrders);

        openOrdersRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                if (isChecked) {
                    /*progressDialog.setMessage("Fetching open orders...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    final ArrayList<OrderModel> openOrders = new ArrayList<OrderModel>();
                    for(OrderModel o: orderModelList){
                        if(o.getStatus().equalsIgnoreCase(getString(R.string.open))){
                            openOrders.add(o);
                        }
                    }

                    if(openOrders.size()>0){
                        recyclerView.setVisibility(View.VISIBLE);
                        noSearchResultFound.setVisibility(View.GONE);
                        recyclerViewAdapter = new RecyclerviewAdapter(OrderStatus.this,openOrders,new ItemClickListener(){
                            @Override
                            public void onClick(View view, int position, View clickedView) {
                                Intent intent = new Intent(OrderStatus.this,OrderInformation.class);
                                intent.putExtra("OrderInfo",openOrders.get(position));
                                intent.putExtra("TransitionName",ViewCompat.getTransitionName(clickedView));
                                *//*LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.ll_orderStatus);
                                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(OrderStatus.this,clickedView, ViewCompat.getTransitionName(clickedView));
                                startActivity(intent,activityOptionsCompat.toBundle());*//*
                                startActivity(intent);
                            }


                        });
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }else{
                        noSearchResultFound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }*/
                    setCollapsingToolbarTitle(getResources().getString(R.string.order_status));
                    hitOrdersService(orderIdPrefs.getString(ClientorServer, null), DefaultSapId, "get_pendingord");
                }
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawers();
                }

            }
        });
        closedOrdersRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                if (isChecked) {
              /*      progressDialog.setMessage("Fetching closed orders...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    final ArrayList<OrderModel> closedOrders = new ArrayList<OrderModel>();
                    for(OrderModel o: orderModelList){
                        if(o.getStatus().equalsIgnoreCase(getString(R.string.closed))){
                            closedOrders.add(o);
                        }
                    }

                    if(closedOrders.size()>0){
                        recyclerView.setVisibility(View.VISIBLE);
                        noSearchResultFound.setVisibility(View.GONE);
                        recyclerViewAdapter = new RecyclerviewAdapter(OrderStatus.this,closedOrders,new ItemClickListener(){
                            @Override
                            public void onClick(View view, int position, View clickedView) {
                                Intent intent = new Intent(OrderStatus.this,OrderInformation.class);
                                intent.putExtra("OrderInfo",closedOrders.get(position));
                                intent.putExtra("TransitionName",ViewCompat.getTransitionName(clickedView));
                               *//* LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.ll_orderStatus);
                                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(OrderStatus.this,clickedView, ViewCompat.getTransitionName(clickedView));
                                startActivity(intent,activityOptionsCompat.toBundle());*//*
                                startActivity(intent);
                            }


                        });
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }else{
                        noSearchResultFound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }*/
                    setCollapsingToolbarTitle(getResources().getString(R.string.delivery_status));
                    hitOrdersService(orderIdPrefs.getString(ClientorServer, null), DefaultSapId, "get_dispatch");
                }
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawers();
                }

            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioClient = (RadioButton) findViewById(R.id.radioYes);
        radioServer = (RadioButton) findViewById(R.id.radioNo);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_order_status);
        uploadInvoices = (Button) findViewById(R.id.btn_uploadInvoices);
        uploadInvoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddingImages();
            }
        });
        mainDrawerView = (LinearLayout) findViewById(R.id.mainDrawerView);
        etSapId = (EditText) findViewById(R.id.et_sap_id);
        etSapId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSapId.isEnabled()) {
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
                if (radioClient.isChecked()) {
                    isClientorServer = "c";
                    employeeDesig.setText(getResources().getString(R.string.client));
                } else if (radioServer.isChecked()) {
                    isClientorServer = "s";
                    employeeDesig.setText(getResources().getString(R.string.Sales_executive));
                }
                String orderType = "";
                if (openOrdersRadio.isChecked()) {
                    orderType = "get_pendingord";
                } else if (closedOrdersRadio.isChecked()) {
                    orderType = "get_dispatch";
                } else {
                    orderType = "get_pendingord";
                }
                DefaultSapId = etSapId.getText().toString().trim();
                SharedPreferences.Editor editor = orderIdPrefs.edit();
                editor.putString(SapId, etSapId.getText().toString().trim());
                editor.putString(ClientorServer, isClientorServer);
                editor.apply();
                hitOrdersService(orderIdPrefs.getString(ClientorServer, null), etSapId.getText().toString().trim(), orderType);
            }
        });
        employeeName = (TextView) findViewById(R.id.name);
        employeePic = (ImageView) findViewById(R.id.img_profile);
        employeeDesig = (TextView) findViewById(R.id.designation);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingEdit);
        floatingLogOut = (FloatingActionButton) findViewById(R.id.floatingLogout);
        floatingActionButton.setBackgroundColor(Color.WHITE);
        sharedPreferences = getSharedPreferences("GooglePic", MODE_PRIVATE);
        if (sharedPreferences.getString("PhotoUrl", null) != null) {
            Glide.with(this).load(sharedPreferences.getString("PhotoUrl", null))
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(employeePic);
        } else {
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
        recyclerView = (ShimmerRecyclerView) findViewById(R.id.orderStatusList);
        swipeRefereshLayout = (SwipeRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        orderModelList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerviewAdapter(this, orderModelList, new ItemClickListener() {
            @Override
            public void onClick(View view, int position, View clickedView) {
                Intent intent = new Intent(OrderStatus.this, OrderInformation.class);
                intent.putExtra("OrderInfo", orderModelList.get(position));
                intent.putExtra("TransitionName", ViewCompat.getTransitionName(clickedView));
                intent.putExtra("isDispatched",false);

                /*LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.ll_orderStatus);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(OrderStatus.this,clickedView, ViewCompat.getTransitionName(clickedView));
                startActivity(intent,activityOptionsCompat.toBundle());*/
                startActivity(intent);
            }


        },false, this,this,isUserAdmin,this,this,this,this);
        noSearchResultFound = (NestedScrollView) findViewById(R.id.noSearchFound);
        searchList = new ArrayList<>();
    }

    private void initializeRemarkDialog(final View view,final int pos) {
        alertBuilderForRemarksInput = new AlertDialog.Builder(this);
        final EditText editForDialog = new EditText(this);
        editForDialog.setTextColor(getResources().getColor(R.color.black));
        editForDialog.setHint(R.string.remark_hint);
        alertBuilderForRemarksInput.setView(editForDialog);
        alertBuilderForRemarksInput.setCancelable(false)
                .setIcon(getResources().getDrawable(R.drawable.vac_small)).setMessage(R.string.input_remarks).
                setPositiveButton(R.string.save_remark, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        orderModelList.get(pos).setAdminNotes(editForDialog.getText().toString().trim());
                        TextView delDt = (TextView)(view.findViewById(R.id.tv_adminNotes));
                        if(!TextUtils.isEmpty(editForDialog.getText().toString().trim())) {
                            delDt.setText(editForDialog.getText().toString().trim());
                        }else{
                            delDt.setText("Enter Remarks ");
                        }
                        updateOrder(pos);
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("Dismiss Remark", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertBuilderForRemarksInput.show();
    }
    private void initializeLogisticsDialog(final View view,final int pos) {
        AlertDialog logisticsAlert = null;
        builderForLogistics = new AlertDialog.Builder(this);
        final View logisticsView = LayoutInflater.from(this).inflate(R.layout.logistics_dialog_layout,null);
        builderForLogistics.setView(logisticsView);
        builderForLogistics.setCancelable(false);
        logisticsStringBuilder = new StringBuilder();
        logisticsBillNo = (EditText) logisticsView.findViewById(R.id.logisticsAlert_billNo);
        logisticsVesselNo = (EditText) logisticsView.findViewById(R.id.logisticsAlert_vessel);
        logisticsContainerNo = (EditText) logisticsView.findViewById(R.id.logisticsAlert_container);
        logisticsETA = (EditText) logisticsView.findViewById(R.id.logisticsAlert_eta);
        Button positiveBtn = (Button) findViewById(R.id.logisticsAlert_positive);
        Button negativeBtn = (Button) findViewById(R.id.logisticsAlert_negative);
        if(orderModelList.get(pos).getLogisticsModel()!=null) {
            if (!TextUtils.isEmpty(orderModelList.get(pos).getLogisticsModel().getBlNo())) {
                logisticsBillNo.setText(orderModelList.get(pos).getLogisticsModel().getBlNo());
            }

            if (!TextUtils.isEmpty(orderModelList.get(pos).getLogisticsModel().getVesselNo())) {
                logisticsVesselNo.setText(orderModelList.get(pos).getLogisticsModel().getVesselNo());
            }

            if (!TextUtils.isEmpty(orderModelList.get(pos).getLogisticsModel().getContainerNo())) {
                logisticsContainerNo.setText(orderModelList.get(pos).getLogisticsModel().getContainerNo());
            }

            if (!TextUtils.isEmpty(orderModelList.get(pos).getLogisticsModel().getEta())) {
                logisticsETA.setText(orderModelList.get(pos).getLogisticsModel().getEta());
            }
        }
        /*builderForLogistics.setCancelable(false).setPositiveButton(R.string.save_logistics_data, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
        }).setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
            }
        });*/
        logisticsAlert = builderForLogistics.create();

        logisticsAlert.show();

        final AlertDialog finalLogisticsAlert = logisticsAlert;
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    LogisticsModel logisticsModel = new LogisticsModel();
                    if(!TextUtils.isEmpty(logisticsBillNo.getText().toString().trim())){
                        logisticsModel.setBlNo(logisticsBillNo.getText().toString().trim());
                        logisticsStringBuilder.append("Bl No: "+logisticsBillNo.getText().toString().trim());
                    }else{
                        logisticsModel.setBlNo(null);
                    }
                    if(!TextUtils.isEmpty(logisticsContainerNo.getText().toString().trim())){
                        logisticsModel.setContainerNo(logisticsContainerNo.getText().toString().trim());
                        if(logisticsStringBuilder.length()>0){
                            logisticsStringBuilder.append(", Container No: "+logisticsContainerNo.getText().toString().trim());
                        }else{
                            logisticsStringBuilder.append("Container No: "+logisticsContainerNo.getText().toString().trim());
                        }

                    }else{
                        logisticsModel.setContainerNo(null);
                    }
                    if(!TextUtils.isEmpty(logisticsVesselNo.getText().toString().trim())){
                        logisticsModel.setVesselNo(logisticsVesselNo.getText().toString().trim());
                        if(logisticsStringBuilder.length()>0){
                            logisticsStringBuilder.append(", Vessel No: "+logisticsVesselNo.getText().toString().trim());
                        }else{
                            logisticsStringBuilder.append("Vessel No: "+logisticsVesselNo.getText().toString().trim());
                        }
                    }else{
                        logisticsModel.setVesselNo(null);
                    }
                    if(!TextUtils.isEmpty(logisticsETA.getText().toString().trim())){
                        logisticsModel.setEta(logisticsETA.getText().toString().trim());
                        if(logisticsStringBuilder.length()>0){
                            logisticsStringBuilder.append(", E.T.A: "+logisticsETA.getText().toString().trim());
                        }else{
                            logisticsStringBuilder.append("E.T.A: "+logisticsETA.getText().toString().trim());
                        }
                    }else{
                        logisticsModel.setEta(null);
                    }
                    orderModelList.get(pos).setLogisticsModel(logisticsModel);
                    TextView logistics = (TextView) view.findViewById(R.id.tv_logistics);
                    logistics.setText(convertStringInSpanColors(logisticsStringBuilder.toString(),new String[]{"Bl No:","Container No:","Vessel No:","E.T.A:"}));
                    updateOrder(pos);
                    TextView logisticsHeader = (TextView) view.findViewById(R.id.tv_headerLogistics);
                    if(logisticsStringBuilder.length()>0){
                        logisticsHeader.setText(getResources().getString(R.string.logistics_details));
                    }else {
                        logisticsHeader.setText(getResources().getString(R.string.input_invoice_logistics_details));
                    }

                    finalLogisticsAlert.dismiss();
                }
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              finalLogisticsAlert.dismiss();
            }
        });



    }

    private void initiatePreviousDatesDialog(final View view,final int pos) {
       AlertDialog.Builder prevDatesDialog = new AlertDialog.Builder(this);
       if(orderModelList.get(pos).getOldModifiedDates()!=null && orderModelList.get(pos).getOldModifiedDates().size()>0){
           View inflateView = LayoutInflater.from(OrderStatus.this).inflate(R.layout.single_textview_alert,null);
           prevDatesDialog.setView(inflateView);
           ListView lv = (ListView) inflateView.findViewById(R.id.listView1);
           ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,orderModelList.get(pos).getOldModifiedDates());
           lv.setAdapter(adapter);
           prevDatesDialog.show();
       }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        handleSearch(getIntent());
//        setCollapsingToolbarTitle(getResources().getString(R.string.order_status));
        if(openOrdersRadio.isChecked()){
            setCollapsingToolbarTitle(getResources().getString(R.string.order_status));
        }else if(closedOrdersRadio.isChecked()){
            setCollapsingToolbarTitle(getResources().getString(R.string.delivery_status));
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadcast,intentFilter);
    }

    private void setCollapsingToolbarTitle(String s) {
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setTitle(s);
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
            */

    /**
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
        if (!query.equalsIgnoreCase("")) {
            orderModelList.clear();
            for (int i = 0; i < searchList.size(); i++) {
                if(openOrdersRadio.isChecked()) {
                    if (searchList.get(i).getPartyName().contains(query) || searchList.get(i).getOrderNo().equalsIgnoreCase(query)) {
                        orderModelList.add(searchList.get(i));
                        foundResult = true;
                    }
                }else if(closedOrdersRadio.isChecked()){
                    if (searchList.get(i).getPartyName().contains(query) || searchList.get(i).getInvoiceNo().equalsIgnoreCase(query)) {
                        orderModelList.add(searchList.get(i));
                        foundResult = true;
                    }
                }
            }
            if (!foundResult) {
                //No result found
                recyclerView.setVisibility(View.GONE);
                noSearchResultFound.setVisibility(View.VISIBLE);
            } else {
                noSearchResultFound.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        } else {
            orderModelList.clear();
            for (int i = 0; i < searchList.size(); i++) {
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

    private boolean dispalySnackBarForAdvanceRecylerViewItemRemoved(final int position) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Undo Removed Order", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderModelList.add(position, deletedOrders.get(new Integer(position)));
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
    private void dispalyGeneralizedSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        View sBarView = snackbar.getView();
        TextView textView = (TextView) sBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.white));
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        ((TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHint(getResources().getString(R.string.search_hint));
        ((TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.white));
        ((ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn)).setImageDrawable(getResources().getDrawable(R.drawable.search_24dp));
        if (!searchView.isIconified()) {
            collapsingToolbar.setTitle("");
        } else {
            if(openOrdersRadio.isChecked()) {
                collapsingToolbar.setTitle(getResources().getString(R.string.order_status));
            }else if(closedOrdersRadio.isChecked()){
                collapsingToolbar.setTitle(getResources().getString(R.string.delivery_status));
            }
        }
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), OrderStatus.class)));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                orderModelList.clear();
                for (int i = 0; i < searchList.size(); i++) {
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
                if(openOrdersRadio.isChecked()) {
                    collapsingToolbar.setTitle(getResources().getString(R.string.order_status) + " " + query);
                }else if(closedOrdersRadio.isChecked()){
                    collapsingToolbar.setTitle(getResources().getString(R.string.delivery_status) + " " + query);
                }
                handleSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equalsIgnoreCase("")) {
                    if(openOrdersRadio.isChecked()) {
                        collapsingToolbar.setTitle(getResources().getString(R.string.order_status));
                    }else if(closedOrdersRadio.isChecked()){
                        collapsingToolbar.setTitle(getResources().getString(R.string.delivery_status));
                    }
                    orderModelList.clear();
                    for (int i = 0; i < searchList.size(); i++) {
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == android.R.id.home) {




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
        if (checkSelfPermission(android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            } else {
                requestPermissions(new String[]{android.Manifest.permission.SYSTEM_ALERT_WINDOW},
                        MY_PERMISSIONS_REQUEST_SYSTEM_ALERT_WINDOW);
            }
        } else {
            Intent intent = new Intent(OrderStatus.this, VacmetOverlayService.class);
            intent.putExtra("OrderList", orderModelList);
//          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("User", logingSharePrefs.getString(LoggedInUserName, null));
            this.finish();
            startService(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SYSTEM_ALERT_WINDOW:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(OrderStatus.this, VacmetOverlayService.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("OrderList", orderModelList);
                    intent.putExtra("User", logingSharePrefs.getString(LoggedInUserName, null));
                    this.finish();
                    startService(intent);
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                }
                break;
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE_RESULT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImages();
                    Toast.makeText(this, R.string.toast_permissions_given, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.toast_insufficient_permissions, Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(OrderStatus.this, VacmetOverlayService.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("OrderList", orderModelList);
                intent.putExtra("User", logingSharePrefs.getString(LoggedInUserName, null));
                if (LoginActivity.url != null) {
                    if (sharedPreferences.getString("PhotoUrl", null) == null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("PhotoUrl", LoginActivity.url);
                        editor.apply();
                    }
                    intent.putExtra("PhotoUrl", LoginActivity.url);
                } else if (sharedPreferences.getString("PhotoUrl", null) != null) {
                    intent.putExtra("PhotoUrl", sharedPreferences.getString("PhotoUrl", null));
                }
                this.finish();
                startService(intent);
            }
        }else if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK) {

            cameraSelectedImagesUris.clear();

            ArrayList<Uri> imageUris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            for (int i = 0; i < imageUris.size(); i++) {
                cameraSelectedImagesUris.add(imageUris.get(i).getPath());
            }
            Toast.makeText(this, R.string.toast_images_added, Toast.LENGTH_LONG).show();
            nextImageToCrop();
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                croppedImagesUri.add(resultUri.getPath());
                Toast.makeText(this, R.string.toast_imagecropped, Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, R.string.toast_error_getCropped, Toast.LENGTH_LONG).show();
                croppedImagesUri.add(cameraSelectedImagesUris.get(mImageCounter));
                error.printStackTrace();
            } else {
                croppedImagesUri.add(cameraSelectedImagesUris.get(mImageCounter));
            }

            mImageCounter++;
            nextImageToCrop();
            if(mImageCounter == cameraSelectedImagesUris.size()){
                createPdf();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onBackPressed() {
        /**
         * start service here
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            Intent intent = new Intent(OrderStatus.this, VacmetOverlayService.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("OrderList", orderModelList);
            intent.putExtra("User", logingSharePrefs.getString(LoggedInUserName, null));
            if (LoginActivity.url != null) {
                if (sharedPreferences.getString("PhotoUrl", null) == null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("PhotoUrl", LoginActivity.url);
                    editor.apply();
                }
                intent.putExtra("PhotoUrl", LoginActivity.url);
            } else if (sharedPreferences.getString("PhotoUrl", null) != null) {
                intent.putExtra("PhotoUrl", sharedPreferences.getString("PhotoUrl", null));
            }
            this.finish();
            startService(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floatingEdit:
                if (floatingActionButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.edit_done).getConstantState()) {
                    floatingActionButton.setImageResource(R.drawable.editing);
                    itemTouchHelper.attachToRecyclerView(recyclerView);

                } else if (floatingActionButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.editing).getConstantState()) {
                    floatingActionButton.setImageResource(R.drawable.edit_done);
                    itemTouchHelper.attachToRecyclerView(null);

                }
                break;
            case R.id.floatingLogout:
                SharedPreferences.Editor editor = logingSharePrefs.edit();
                editor.putString(LoggedInUser, null);
                editor.putString(LoggedInUserPassword, null);
                editor.apply();
                Intent intent = new Intent(OrderStatus.this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    @Override
    public void onPdfClick(int pos) {
        String invoiveNo = orderModelList.get(pos)!=null? orderModelList.get(pos).getInvoiceNo():null;
        /**
         * HIT Service only if we dont have that file in cache.
         */
        if(fileFromDb!=null && invoiveNo.equalsIgnoreCase(fileFromDb.getName())){
            //Cache File
            openPdf();
        }else{
            //Hit Service
            new CustomServiceAsync().execute("Open",null,invoiveNo);
        }
    }

    private class CustomAsyncTaskForRestOrderService extends AsyncTask<String, Void, List<OrderModel>> {
        String orderType = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            swipeRefereshLayout.setEnabled(false);
            if(swipeRefereshLayout.isRefreshing()){
                swipeRefereshLayout.setRefreshing(false);
            }
            if(noSearchResultFound.getVisibility() == View.VISIBLE){
                recyclerView.setVisibility(View.VISIBLE);
                noSearchResultFound.setVisibility(View.GONE);
            }
            recyclerView.showShimmerAdapter();
            /*if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if(connectionIsOnline()) {
                progressDialog.setMessage(getResources().getString(R.string.fetching_orders));
            }else{
                progressDialog.setMessage(getResources().getString(R.string.fetching_orders_locally));
            }
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();*/
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
            anySavedOrders = databaseRequestsDao.getOrdersForSapIdAndOrderType(orderIdPrefs.getString(SapId, null),orderType.equalsIgnoreCase(GET_PENDING_CODE)?1:0);
            if(connectionIsOnline() && (anySavedOrders == null || (anySavedOrders!=null && anySavedOrders.size() == 0))) {
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
                                            /**
                                             * Currently not saving this date from service, we will be notified by firebase
                                             */
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
                                            itemModel.setContainerNo(xmlPullParser.nextText());
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
                                                orderModel.setIsPending(openOrdersRadio.isChecked()?true:false);
                                                orderModel.setSapId(orderIdPrefs.getString(SapId, null));
                                                orderModelList.add(orderModel);
                                            }
                                        }

                                        break;
                                    case MAIN_INVOICE:
                                        if (orderModel != null) {
                                            orderModel.setIsPending(openOrdersRadio.isChecked()?true:false);
                                            orderModel.setSapId(orderIdPrefs.getString(SapId, null));
                                            orderModelList.add(orderModel);
                                        }
                                        break;
                                    case Secondary_Table_Xml_Tag:
                                        if (itemModel != null) {
                                            if(orderType.equalsIgnoreCase("get_dispatch")){
                                                itemModel.setSelectedOrderNo(orderModel.getInvoiceNo());
                                            }else{
                                                itemModel.setSelectedOrderNo(orderModel.getOrderNo());
                                            }

                                            orderModel.addItemInOrder(itemModel, orderType.equalsIgnoreCase("get_dispatch") ? true : false);
                                        }
                                        saveItemInOrder = false;
                                        break;
                                    case Secondary_Table_Invoice_Xml_Tag:
                                        if (itemModel != null) {
                                            if(orderType.equalsIgnoreCase("get_dispatch")){
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
                      saveInVacmetDatabase(orderModelList);
                  }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(OrderStatus.this, "Oops!! Something Went Wrong, Check Your Connection!!", Toast.LENGTH_SHORT).show();
                    orderModelList.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                    orderModelList.clear();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    Toast.makeText(OrderStatus.this, "Oops!! Something Went Wrong...", Toast.LENGTH_SHORT).show();
                    orderModelList.clear();
                }
            }else{
                List<OrderEntity> orderEntityList = databaseRequestsDao.getOrdersForSapIdAndOrderType(orderIdPrefs.getString(SapId, null),openOrdersRadio.isChecked()?1:0);
                List<OrderModel> localOrderModelList = new ArrayList<>();
                for(OrderEntity orderEntity : orderEntityList){
                    localOrderModelList.add(orderTranslator.translateEntityToModel(orderEntity));
                }
                orderModelList =new ArrayList<>(localOrderModelList);
            }
            return orderModelList;
        }

        @Override
        protected void onPostExecute(List<OrderModel> s) {
            super.onPostExecute(s);
//            progressDialog.dismiss();
            if(openOrdersRadio.isChecked()) {
                setUpListenerForOrdersData();
            }else if(closedOrdersRadio.isChecked()) {
                setUpListenerForLogisticsData();
            }
            recyclerView.hideShimmerAdapter();
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            if (s.size() > 0) {
                sortList(orderModelList,orderType.equalsIgnoreCase("get_dispatch")?true:false);
                for (OrderModel o : orderModelList) {
                    searchList.add(o);
                }

                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawers();
                }
                recyclerViewAdapter = new RecyclerviewAdapter(OrderStatus.this, orderModelList, new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, View clickedView) {
                        if(!connectionIsOnline() || (anySavedOrders!=null && anySavedOrders.size()>0)){
                            //Fill Items for order at pos, in background
                            fillOfflineItemsForOrder(position,orderType,clickedView);
                        }else if(connectionIsOnline() && (anySavedOrders == null || (anySavedOrders!=null && anySavedOrders.size() ==0))){
                            Intent intent = new Intent(OrderStatus.this, OrderInformation.class);
                            intent.putExtra("OrderInfo", orderModelList.get(position));
                            intent.putExtra("TransitionName", ViewCompat.getTransitionName(clickedView));
                            intent.putExtra("isDispatched",orderType.equalsIgnoreCase("get_dispatch")?true:false);
                            startActivity(intent);
                        }

                    /*LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.ll_orderStatus);
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(OrderStatus.this,clickedView, ViewCompat.getTransitionName(clickedView));
                    ActivityCompat.startActivity(OrderStatus.this,intent,activityOptionsCompat.toBundle());*/


                    }


                },orderType.equalsIgnoreCase("get_dispatch")?true:false,OrderStatus.this,OrderStatus.this,isUserAdmin,OrderStatus.this,OrderStatus.this,OrderStatus.this,OrderStatus.this);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                noSearchResultFound.setVisibility(View.GONE);
                swipeRefereshLayout.setOnRefreshListener(null);
            } else {
                recyclerView.setVisibility(View.GONE);
                noSearchResultFound.setVisibility(View.VISIBLE);
                swipeRefereshLayout.setEnabled(true);
                swipeRefereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        hitOrdersService(orderIdPrefs.getString(ClientorServer, null), DefaultSapId, openOrdersRadio.isChecked() ? "get_pendingord" : "get_dispatch");

                    }
                });
            }

        }
    }

    private void fillOfflineItemsForOrder(int position, String orderType, View clickedView) {
        offlineOrderType = orderType;
        offlineClickedView = clickedView;
        new ItemAsynTask().execute(position);
    }

    class ItemAsynTask extends AsyncTask<Integer,Void,List<ItemModel>>{

        int selectedPos = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog.setMessage(getResources().getString(R.string.fetching_items_in_order));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected List<ItemModel> doInBackground(Integer... strings) {
            selectedPos = strings[0];
            List<ItemModel> itemsList = new ArrayList<>();
            for(ItemEntity itemEntity : databaseRequestsDao.getItemsForOrderId(openOrdersRadio.isChecked()?orderModelList.get(selectedPos).getOrderNo():orderModelList.get(selectedPos).getInvoiceNo())){
                itemsList.add(itemTranslator.translateModelFromEntity(itemEntity));
            }
            return itemsList;
        }

        @Override
        protected void onPostExecute(List<ItemModel> itemModels) {
            super.onPostExecute(itemModels);
            progressDialog.dismiss();
            orderModelList.get(selectedPos).setItemList(new ArrayList<ItemModel>(itemModels));
            Intent intent = new Intent(OrderStatus.this, OrderInformation.class);
            intent.putExtra("OrderInfo", orderModelList.get(selectedPos));
            intent.putExtra("TransitionName", ViewCompat.getTransitionName(offlineClickedView));
            intent.putExtra("isDispatched",offlineOrderType.equalsIgnoreCase("get_dispatch")?true:false);
            startActivity(intent);
        }
    }

    private boolean connectionIsOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }else{
            return false;
        }

    }

    private void saveInVacmetDatabase(ArrayList<OrderModel> orderModelList) {

        List<OrderEntity> orderEntityList = new ArrayList<>();
         for(OrderModel orderModel : orderModelList){
             orderEntityList.add(orderTranslator.translateModelToEntity(orderModel));
         }
         databaseRequestsDao.insertOrders(orderEntityList);
         for(OrderEntity orderEntity : orderEntityList){
             if(orderEntity.getItemModelArrayList()!=null && orderEntity.getItemModelArrayList().size()>0) {
                 databaseRequestsDao.insertItems(orderEntity.getItemModelArrayList());
             }
         }

    }

    private void sortList(ArrayList<OrderModel> orderModelList, final boolean isDispatched) {
            Collections.sort(orderModelList, new Comparator<OrderModel>() {
                @Override
                public int compare(OrderModel o1, OrderModel o2) {
                    if (o1.getPartyName().compareTo(o2.getPartyName()) == 0) {
                        //Do Comparison on order no.
                        if(!isDispatched) {
                            Long order1 = Long.parseLong(o1.getOrderNo());
                            Long order2 = Long.parseLong(o2.getOrderNo());
                            return order2.compareTo(order1);
                        }else{
                            Long order1 = Long.parseLong(o1.getInvoiceNo());
                            Long order2 = Long.parseLong(o2.getInvoiceNo());
                            return order2.compareTo(order1);
                        }
                    } else {
                        //Do comparison on Party name
                        return o1.getPartyName().compareTo(o2.getPartyName());
                    }
                }
            });
        }
    void startAddingImages() {
        // Check if permissions are granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE_RESULT);
            } else {
                selectImages();
            }
        } else {
            selectImages();
        }
    }
    public void selectImages() {
        Intent intent = new Intent(this, ImagePickerActivity.class);

        //add to intent the URIs of the already selected images
        //first they are converted to Uri objects
        ArrayList<Uri> uris = new ArrayList<>(cameraSelectedImagesUris.size());
        for (String stringUri : cameraSelectedImagesUris) {
            uris.add(Uri.fromFile(new File(stringUri)));
        }
        // add them to the intent
        intent.putExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, uris);

        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }



    void nextImageToCrop() {
        if (mImageCounter != cameraSelectedImagesUris.size()) {
            CropImage.activity(Uri.fromFile(new File(cameraSelectedImagesUris.get(mImageCounter))))
                    .setActivityMenuIconColor(getResources().getColor(R.color.colorPrimary))
                    .setInitialCropWindowPaddingRatio(0)
                    .setAllowRotation(true)
                    .setActivityTitle(getString(R.string.cropImage_activityTitle) + (mImageCounter + 1))
                    .start(this);
        }
    }

    void createPdf() {
        if (croppedImagesUri.size() == 0) {
            if (cameraSelectedImagesUris.size() == 0) {
                Toast.makeText(this, R.string.toast_no_images, Toast.LENGTH_LONG).show();
                return;
            } else {
                croppedImagesUri = (ArrayList<String>) cameraSelectedImagesUris.clone();
            }
        }
        if(!TextUtils.isEmpty(filenameToSaveInDb)){
            new CreatingPdf().execute("Save", null, null);
        }else {
            new MaterialDialog.Builder(this)
                    .title(R.string.creating_pdf).inputType(InputType.TYPE_CLASS_NUMBER)
                    .content(R.string.enter_file_name).itemsColor(getResources().getColor(R.color.black)).theme(Theme.LIGHT)
                    .input(getString(R.string.example), null, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            if (input == null || input.toString().trim().equals("")) {
                                Toast.makeText(OrderStatus.this, R.string.toast_name_not_blank, Toast.LENGTH_LONG).show();
                            } else {
                                filenameToSaveInDb = input.toString();

                                new CreatingPdf().execute("Save", null, null);


                            }
                        }
                    })
                    .show();
        }
    }

    public class CreatingPdf extends AsyncTask<String, String, String> {

        // Progress dialog
        MaterialDialog.Builder builder = new MaterialDialog.Builder(OrderStatus.this)
                .title(R.string.please_wait)
                .content(R.string.preparing_pdf)
                .cancelable(false).theme(Theme.LIGHT)
                .progress(true, 0).itemsColor(getResources().getColor(R.color.black));
        MaterialDialog dialog = builder.build();
        private Image image;
        private byte[] pdfBytes;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String saveOrOpen = params[0];
            byte[] bytes = new byte[0];
            if(params[1]!=null) {
                bytes = Base64.decode(params[1],Base64.DEFAULT);
            }
            String invoiceNo = null;
            if(params[2]!=null){
                invoiceNo = params[2];
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 38, 38, 50, 38);
            Rectangle documentRect = document.getPageSize();
            if(saveOrOpen.equalsIgnoreCase("Save")) {
                try {
                    File sdcard = Environment.getExternalStorageDirectory();
                    File filedir = new File(Environment.getExternalStorageDirectory()+File.separator+ "Vacmet");
//                    File filedir = getDir("Vacmet",MODE_PRIVATE);
                    if(!filedir.exists() || !filedir.isDirectory()){
                        filedir.mkdirs();
                    }
                    if(TextUtils.isEmpty(filenameToSaveInDb)){
                        invoiceNo = "blank";
                    }
                    fileToSave = new File(filedir,"Blank"+".pdf");
                    if(!fileToSave.getParentFile().exists()){
                        fileToSave.getParentFile().mkdirs();
                    }
                    if(fileToSave.exists()) {
                        fileToSave.delete();
                    }

                    fileToSave.createNewFile();
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
                    document.open();
                    for (int i = 0; i < croppedImagesUri.size(); i++) {

                        Bitmap bmp = MediaStore.Images.Media.getBitmap(
                                activity.getContentResolver(), Uri.fromFile(new File(croppedImagesUri.get(i))));
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 70, stream);

                        image = Image.getInstance(croppedImagesUri.get(i));



                        if (bmp.getWidth() > documentRect.getWidth()
                                || bmp.getHeight() > documentRect.getHeight()) {
                            //bitmap is larger than page,so set bitmap's size similar to the whole page
                            image.scaleAbsolute(documentRect.getWidth(), documentRect.getHeight());
                        } else {
                            //bitmap is smaller than page, so add bitmap simply.
                            //[note: if you want to fill page by stretching image,
                            // you may set size similar to page as above]
                            image.scaleAbsolute(bmp.getWidth(), bmp.getHeight());
                        }

                        image.setAbsolutePosition(
                                (documentRect.getWidth() - image.getScaledWidth()) / 2,
                                (documentRect.getHeight() - image.getScaledHeight()) / 2);
                        image.setBorder(Image.BOX);
                        image.setBorderWidth(15);
                        document.add(image);
                        document.newPage();
                    }

                    document.close();
                    convertPdfToBytes(byteArrayOutputStream);

                    croppedImagesUri.clear();
                    cameraSelectedImagesUris.clear();
                    mImageCounter = 0;

                    pdfBytes = byteArrayOutputStream.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                    saveOrOpen = "-1";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Error! while encoding Pdf", Toast.LENGTH_SHORT).show();
                        }
                    });
                    document.close();
                }
            }else if(saveOrOpen.equalsIgnoreCase("Open")){
                File sdcard = Environment.getExternalStorageDirectory();
                File filedir = new File(Environment.getExternalStorageDirectory()+File.separator+ "Vacmet");
//                    File filedir = getDir("Vacmet",MODE_PRIVATE);
                if(!filedir.exists() || !filedir.isDirectory()){

                    filedir.mkdirs();
                }
                for(File fileToDelete : filedir.listFiles()){
                    if(!fileToDelete.isDirectory()){
                        fileToDelete.delete();
                    }
                }
                if(TextUtils.isEmpty(invoiceNo)){
                    invoiceNo = "blank";
                }
                fileFromDb = new File(filedir,invoiceNo+".pdf");

                if(!fileFromDb.getParentFile().exists()){
                    fileFromDb.getParentFile().mkdirs();
                }
                if(fileFromDb.exists()) {
                    fileFromDb.delete();
                }

                try {
                    fileFromDb.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(fileFromDb);
                    fileOutputStream.write(bytes);
                    fileOutputStream.close();
                    fileOutputStream.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                    saveOrOpen = "-1";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Error! while encoding Pdf", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            return saveOrOpen;
        }

        @Override
        protected void onPostExecute(String saveOrOpen) {
            super.onPostExecute(saveOrOpen);
            dialog.dismiss();
            if(saveOrOpen.equalsIgnoreCase("Save")) {
                if (pdfBytes != null && pdfBytes.length > 0) {
                    if (!TextUtils.isEmpty(filenameToSaveInDb)) {
                        /**
                         * HIT Service
                         */
                        prepareObjectAndHitService("Save",pdfBytes,filenameToSaveInDb);
                    }
                }
            }else if(saveOrOpen.equalsIgnoreCase("Open")){
                /**
                 * Open Pdf
                 */
                openPdf();
            }else{
                Toast.makeText(activity, "Problem occurred with Pdf!!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void convertPdfToBytes(ByteArrayOutputStream byteArrayOutputStream) {
        if(fileToSave!=null){
            try {
                FileInputStream fileInputStream = new FileInputStream(fileToSave);
                byte[] buf = new byte[1024];
                for (int readNum; (readNum = fileInputStream.read(buf)) != -1;) {
                    byteArrayOutputStream.write(buf, 0, readNum);
                }
                } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareObjectAndHitService(String saveOrOpen, byte[] pdfBytes, String filenameToSaveInDb) {
        InvoiceTo invoiceTo = new InvoiceTo();
        invoiceTo.setInvoice(pdfBytes);
        invoiceTo.setInvoiceNo(filenameToSaveInDb);
        Gson gson = new Gson();
        String jsonReq = gson.toJson(invoiceTo);
        new CustomServiceAsync().execute(saveOrOpen,jsonReq,null);
    }

    private void openPdf() {
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(fileFromDb),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        Intent intent = Intent.createChooser(target,"Open Invoice");
        startActivity(intent);
    }

    public class CustomServiceAsync extends AsyncTask<String,String,String>{

        // Progress dialog
        MaterialDialog.Builder builder = new MaterialDialog.Builder(OrderStatus.this)
                .title(R.string.please_wait).theme(Theme.LIGHT)
                .content(R.string.securely_connecting_to_db)
                .cancelable(false)
                .progress(true, 0).itemsColor(getResources().getColor(R.color.black));
        MaterialDialog dialog = builder.build();
        private String responseStr = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            Handler handler = new Handler(activity.getMainLooper());

            responseStr = params[0];
            String invoice = params[2];
            if(params[0].equalsIgnoreCase("Save")) {
                String response = null;

                    if (TextUtils.isEmpty(params[1])) {
                        return "-1";
                    } else {
                      try {
                        URL url = new URL(SERVER_IP + URL_SAVE_INVOICE);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setRequestProperty("Content-Type", "application/json");
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setDoOutput(true);
                          disableSSLCertificateChecking();
                        /*  final TrustManager[] trustAllCerts = new TrustManager[]{
                                  new X509TrustManager() {
                                      @Override
                                      public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                      }

                                      @Override
                                      public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                      }

                                      @Override
                                      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                          return null;
                                      }
                                  }
                          };

                          // Install the all-trusting trust manager
                          final SSLContext sslContext = SSLContext.getInstance("SSL");
                          sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                          // Create an ssl socket factory with our all-trusting manager
                          final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                          httpURLConnection.se*/


                          /*OutputStream os = httpURLConnection.getOutputStream();
                          os.write(params[1].getBytes());
                          os.flush();*/
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
                        bufferedWriter.write(params[1]);
                        bufferedWriter.close();
                        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            response = "Success";
                        }
                      } catch(MalformedURLException e){
                          e.printStackTrace();
                          handler.post(new Runnable() {
                              @Override
                              public void run() {
                                  Toast.makeText(activity, "Error! while saving Invoice", Toast.LENGTH_SHORT).show();

                              }
                          });
                    } catch(IOException e){
                        e.printStackTrace();
                          handler.post(new Runnable() {
                              @Override
                              public void run() {
                                  Toast.makeText(activity, "Error! while saving Invoice", Toast.LENGTH_SHORT).show();

                              }
                          });
                    }

                        return response;
                }
            }else if(params[0].equalsIgnoreCase("Open")){

                if(TextUtils.isEmpty(invoice)){
                    return "-1";
                }else{

                    try {
                        URL url = new URL(SERVER_IP + URL_GET_INVOICE + invoice );
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        //httpURLConnection.setRequestProperty("Content-Type", "application/json");
//                        httpURLConnection.setDoInput(true);
//                        httpURLConnection.setDoOutput(true);
                        disableSSLCertificateChecking();
                        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                            String response = "";
                            String line ="";
                            while((line = bufferedReader.readLine())!=null){
                                response += line;
                            }
                            return response;
                        }
                    } catch(MalformedURLException e){
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Error! while saving Invoice", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } catch(IOException e){
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Error! while saving Invoice", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            }
            return "-1";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(!"-1".equalsIgnoreCase(s)) {
                if(responseStr.equalsIgnoreCase("Save")) {
                    if (!TextUtils.isEmpty(s)) {
                        Toast.makeText(activity, "Invoice saved successfully in database", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Invoice can't be saved, try again later!", Toast.LENGTH_SHORT).show();
                    }
                }else if(responseStr.equalsIgnoreCase("Open")){
                    if(!TextUtils.isEmpty(s)){
//                        Gson gson = new Gson();



                        InvoiceTo invoiceTo = customGson.fromJson(s,InvoiceTo.class);
                        try {

                            new CreatingPdf().execute("Open",Base64.encodeToString(invoiceTo.getInvoice(),Base64.DEFAULT),invoiceTo.getInvoiceNo());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "Encoding exception occurred! Contact admin", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity, "Invoice not found in the database.Please Upload first!!", Toast.LENGTH_SHORT).show();

                    }

                }
            }else{
                Toast.makeText(activity, "Invoice not found in the database.Please Upload first!!", Toast.LENGTH_SHORT).show();
            }
        }

        private void disableSSLCertificateChecking() {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    // Not implemented
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    // Not implemented
                }
            } };

            try {
                SSLContext sc = SSLContext.getInstance("TLS");

                sc.init(null, trustAllCerts, new java.security.SecureRandom());

                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

    }

    class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.decode(json.getAsString(), Base64.NO_WRAP);
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcast);
        super.onDestroy();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability gApi = GoogleApiAvailability.getInstance();
        int resultCode = gApi.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(gApi.isUserResolvableError(resultCode)){
                gApi.getErrorDialog(this, resultCode, 1).show();
            }else{
                Toast.makeText(this, getResources().getString(R.string.toast_playservices_unrecoverable), Toast.LENGTH_LONG).show();
                finish();
            } return false;
        } return true;
    }

    /**This Function Does'nt handle duplicate Arguments, and should be unique.*/
    public static SpannableString convertStringInSpanColors(String str,String[] colorArgs){
        SpannableString spannableString = new SpannableString(str);
        for(int i=0; i<colorArgs.length; i++){
            if(str.contains(colorArgs[i])) {
                spannableString.setSpan(new ForegroundColorSpan(Color.DKGRAY), str.indexOf(colorArgs[i]),
                        str.indexOf(colorArgs[i])+colorArgs[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }


        return spannableString;
    }

}
