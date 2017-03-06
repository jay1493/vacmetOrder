package com.example.anubhav.vacmet.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anubhav.vacmet.OrderStatus;
import com.example.anubhav.vacmet.R;
import com.example.anubhav.vacmet.model.OrderModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by anubhav on 24/1/17.
 */

public class VacmetOverlayService extends Service {

    private Context context;
    private WindowManager windowManager;
    private View floatingView;
    private View collapsedView;
    private View expandedView;
    private ImageView closeExpandedView;
    private ImageView expandOrderClass;
    private TextView partyName;
    private TextView deliveryDate;
    private TextView marqueeInfo;

    private ArrayList<OrderModel> orderModelList;
    private RelativeLayout relativeLayout;
    private boolean showProgressBar = false;
    private Intent persisttedIntent;
    private ImageView logo;
    private String photoUrl = null;

    public VacmetOverlayService() {
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
        if(orderModelList == null) {
            if(intent!=null && intent.getSerializableExtra("OrderList") !=null) {
                Log.e("", "onStartCommand: 1");
                if(intent.getStringExtra("PhotoUrl")!=null){
                    photoUrl = intent.getStringExtra("PhotoUrl");
                    if(photoUrl != null){
                        Log.e("", "onCreate: url Found:== "+photoUrl );
                        Glide.with(getApplicationContext()).load(photoUrl).override(100,100).fitCenter().into(logo);
                    }else{
                        logo.setImageDrawable(getResources().getDrawable(R.drawable.male_user));
                    }
                }
                persisttedIntent = intent;
                orderModelList = (ArrayList<OrderModel>) intent.getSerializableExtra("OrderList");
                feedData();
                showProgressBar = false;
            }else{
                showProgressBar = true;
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        floatingView = LayoutInflater.from(this).inflate(R.layout.activity_window_manager,null,false);
    final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP|Gravity.LEFT;
        layoutParams.x = 0;
        layoutParams.y = 100;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView,layoutParams);
        logo = (ImageView) floatingView.findViewById(R.id.logo);
        collapsedView = floatingView.findViewById(R.id.collapsed_view);
        expandedView = floatingView.findViewById(R.id.expanded_view);
        closeExpandedView = (ImageView) floatingView.findViewById(R.id.close_expanded_card);
        partyName = (TextView) floatingView.findViewById(R.id.party_name);
        deliveryDate = (TextView) floatingView.findViewById(R.id.delivery_date);
        marqueeInfo = (TextView) floatingView.findViewById(R.id.marquee_info);
        relativeLayout = (RelativeLayout) floatingView.findViewById(R.id.rootContainer);
        closeExpandedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });
        expandOrderClass = (ImageView) floatingView.findViewById(R.id.expand_class);
        expandOrderClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacmetOverlayService.this, OrderStatus.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                stopSelf();
            }
        });
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            private int intialX;
            private int intialY;
            private float intialTouchX;
            private float intialTouchY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        intialX = layoutParams.x;
                        Log.e("", "onTouch: X:"+intialX );
                        intialY = layoutParams.y;
                        Log.e("", "onTouch: Y:"+intialY );
                        intialTouchX = motionEvent.getRawX();
                        Log.e("", "onTouch: TouchX:"+intialTouchX );
                        intialTouchY = motionEvent.getRawY();
                        Log.e("", "onTouch: TouchY:"+intialTouchY );
                        return true;
                    case MotionEvent.ACTION_UP:
                        int xDiff = (int)(motionEvent.getRawX() - intialTouchX);
                        int yDiff = (int)(motionEvent.getRawY() - intialTouchY);
                        if(xDiff < 10 && yDiff < 10){
                            //click happened
                            if(isViewCollapsed()){
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }else{
                                collapsedView.setVisibility(View.VISIBLE);
                                expandedView.setVisibility(View.GONE);
                            }
                        }
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.x = intialX + (int)(motionEvent.getRawX() - intialTouchX);
                        Log.e("", "onTouch: NewLayoutX:"+layoutParams.x );
                        layoutParams.y = intialY + (int)(motionEvent.getRawY() - intialTouchY);
                        Log.e("", "onTouch: NewLayoutY:"+layoutParams.y );
                        windowManager.updateViewLayout(floatingView,layoutParams);
                        return true;
                }
             return false;
            }
        });



    }

    private boolean isViewCollapsed() {
        return collapsedView.getVisibility() == View.VISIBLE;

    }

    private void feedData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = new Date(simpleDateFormat.format(new Date()));
        HashMap<Long,Integer> deliveryDiff = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<orderModelList.size();i++){
            Date d = null;
            try {
                d = simpleDateFormat.parse(orderModelList.get(i).getDeliveryDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            deliveryDiff.put(new Long(d1.getTime()-d.getTime()),new Integer(i));
            stringBuilder.append(orderModelList.get(i).getPartyName()+"  "+orderModelList.get(i).getDeliveryDate()+" :: ");
        }
        /**
         * Doubt about tree map sorting
         */
        TreeMap<Long,Integer> treeMap = new TreeMap<>(deliveryDiff);
        partyName.setText(String.valueOf(orderModelList.get((int)treeMap.firstEntry().getValue()).getPartyName()));
        deliveryDate.setText(String.valueOf(orderModelList.get((int)treeMap.firstEntry().getValue()).getDeliveryDate()));
        marqueeInfo.setText(stringBuilder.toString());
        marqueeInfo.setSelected(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(floatingView!=null){
            windowManager.removeView(floatingView);
        }
        Log.e("", "onDestroy: " );
    }
}
