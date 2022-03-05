package com.gh.plugin;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.timespeed.lib.TimeSpeed;

import org.cocos2dx.lua.JniHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PluginMgr {

    static {
        System.loadLibrary("hook");
        System.loadLibrary("substrate");
        System.loadLibrary("timespeed");


    }

    Activity activity;
    Resources resources;

    PopupWindow pop;

    public PluginMgr(Activity activity, Resources resources) {
        this.activity = activity;
        this.resources = resources;


    }
    String TAG =  PluginMgr.class.getSimpleName();
    DragFloatView dragFloatView;
    View dialog_main;
    boolean isTimeSpeed = true;
    public  void show()  {

        try {

            Log.i(TAG,"---------------------------第一步骤");
            if (pop == null){

                View dialog_icon = LayoutInflaterManager.getInstance().getLayoutInflater(activity) .inflate(resources.getLayout(R.layout.dialog_icon),null);
                {
                    Log.i(TAG,"---------------------------第二步骤");
                    ImageView iv_icon = dialog_icon.findViewById(R.id.iv_icon);
                    ImageView iv_normal_reddot = dialog_icon.findViewById(R.id.iv_normal_reddot);
                    ImageView iv_fringe_left = dialog_icon.findViewById(R.id.iv_fringe_left);
                    ImageView iv_fringe_left_reddot = dialog_icon.findViewById(R.id.iv_fringe_left_reddot);
                    iv_icon.setImageDrawable(resources.getDrawable(R.mipmap.ic_launcher));
                    iv_normal_reddot.setImageDrawable(resources.getDrawable(R.mipmap.icon_reddot_normal));
                 //   iv_fringe_left.setImageDrawable(resources.getDrawable(R.mipmap.icon_fringe_left));
                    iv_fringe_left_reddot.setImageDrawable(resources.getDrawable(R.mipmap.icon_reddot_fringe));
                }
                Log.i(TAG,"---------------------------第三步骤");
                ViewGroup contentView = activity . findViewById(android.R.id.content);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dragFloatView = new DragFloatView(activity);
                dragFloatView.addView(dialog_icon);
                contentView. addView(dragFloatView,layoutParams);

                dialog_main = LayoutInflaterManager.getInstance().getLayoutInflater(activity) .inflate(resources.getLayout(R.layout.dialog_main) , null);
                final RelativeLayout relativeLayout = dialog_main.findViewById(R.id.root_viewpager);



                final TextView textView2 = new TextView(activity);

                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                textView2.setLayoutParams(layoutParams2);
                textView2.setPadding(20, 0, 20, 0);
                textView2.setText( "测试界面22222222");
                textView2.setTextColor(Color.parseColor("#03DAC5"));
                textView2.setTextSize(20);
                Log.i(TAG,"---------------------------第四步骤");

//                setImageDrawable (dialog_main , R.id.iv_bottombar_tab_icon_1,R.mipmap.function_icon_turbo_activity);
//                setImageDrawable (dialog_main , R.id.iv_bottombar_tab_icon_2,R.mipmap.function_icon_clicker_activity);
//                setImageDrawable (dialog_main , R.id.iv_bottombar_tab_icon_3,R.mipmap.function_icon_gift_activity);
//                setImageDrawable (dialog_main , R.id.iv_bottombar_tab_icon_4,R.mipmap.function_icon_area_activity);
//
//                setImageDrawable (dialog_main , R.id.iv_bottombar_tab_red_dot_1,R.drawable.main_bottombar_red_dot_indicator_background);
//                setImageDrawable (dialog_main , R.id.iv_bottombar_tab_red_dot_2,R.drawable.main_bottombar_red_dot_indicator_background);
//                setImageDrawable (dialog_main , R.id.iv_bottombar_tab_red_dot_3,R.drawable.main_bottombar_red_dot_indicator_background);
//                setImageDrawable (dialog_main , R.id.iv_bottombar_tab_red_dot_4,R.drawable.main_bottombar_red_dot_indicator_background);
                Log.i(TAG,"---------------------------第五步骤");

                pop = new PopupWindow(dialog_main ,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        false);
                pop.setBackgroundDrawable(new ColorDrawable(0xffffff));//支持点击Back虚拟键退出

               final RelativeLayout  main_page_accv2 = (RelativeLayout) LayoutInflaterManager.getInstance().getLayoutInflater(activity) .inflate(resources.getLayout(R.layout.main_page_accv2) , null);
               RadioGroup radioGroup = main_page_accv2.findViewById(R.id.acc_radio_group);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.acc_cocos2d){
                                isTimeSpeed = false;
                            }else {
                                isTimeSpeed = true;
                            }
                    }
                });
                setImageDrawable (main_page_accv2 , R.id.iv_acc_activated_state_switch_btn_icon,R.mipmap.main_acc_control_inactivated_state);
                setImageDrawable (main_page_accv2 , R.id.iv_acc_control_min_btn,R.mipmap.main_acc_control_min);
                setImageDrawable (main_page_accv2 , R.id.iv_acc_control_max_btn,R.mipmap.main_acc_control_max);

                final TextView textView = main_page_accv2.findViewById(R.id.tv_acc_scale);

                relativeLayout.removeAllViews();
                relativeLayout.addView(main_page_accv2);
                Log.i(TAG,"---------------------------第六步骤");

                final SeekBar seekBar = main_page_accv2.findViewById(R.id.acc_seekbar);


                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        switch (progress/10){
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                if (isTimeSpeed){
                                    TimeSpeed.speedUTCTime(activity,0.2f);
                                TimeSpeed.speedClockTime(activity,0.2f);
                                }else {
                                    JniHelper.setTimeScale(1.0f);
                                }


                                textView.setText("X0.2");

                                break;
                            case 5:

                                if (isTimeSpeed){
                                    TimeSpeed.speedUTCTime(activity,1.0f);
                                    TimeSpeed.speedClockTime(activity,1.0f);
                                }else {
                                    JniHelper.setTimeScale(5.0f);
                                }
                                textView.setText("X1.0");
                                break;
                            case 6:
                                if (isTimeSpeed){
                                    TimeSpeed.speedUTCTime(activity,2.0f);
                                    TimeSpeed.speedClockTime(activity,2.0f);
                                }else {
                                    JniHelper.setTimeScale(10.0f);
                                }


                                textView.setText("X2.0");
                                break;
                            case 7:
                                if (isTimeSpeed){
                                    TimeSpeed.speedUTCTime(activity,3.0f);
                                    TimeSpeed.speedClockTime(activity,3.0f);
                                }else {
                                    JniHelper.setTimeScale(10.0f);
                                }


                                textView.setText("X3.0");
                                break;
                            case 8:
                                if (isTimeSpeed){
                                    TimeSpeed.speedUTCTime(activity,4.0f);
                                    TimeSpeed.speedClockTime(activity,4.0f);
                                }else {
                                    JniHelper.setTimeScale(10.0f);
                                }


                                textView.setText("X4.0");
                                break;
                            case 9:
                                if (isTimeSpeed){
                                    TimeSpeed.speedUTCTime(activity,5.0f);
                                    TimeSpeed.speedClockTime(activity,5.0f);
                                }else {
                                    JniHelper.setTimeScale(10.0f);
                                }


                                textView.setText("X5.0");
                                break;
                            case 10:
                                if (isTimeSpeed){
                                    TimeSpeed.speedUTCTime(activity,6.0f);
                                    TimeSpeed.speedClockTime(activity,6.0f);
                                }else {
                                    JniHelper.setTimeScale(10.0f);
                                }


                                textView.setText("X10.0");
                                break;
                            default:
                                if (isTimeSpeed){
                                    TimeSpeed.speedUTCTime(activity,1.0f);
                                    TimeSpeed.speedClockTime(activity,1.0f);
                                }else {
                                    JniHelper.setTimeScale(5.0f);
                                }
                                textView.setText("X1.0");
                                break;
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                main_page_accv2.findViewById(R.id.rl_acc_control_min_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (seekBar.getProgress()>0){
                        seekBar.setProgress(seekBar.getProgress()-10);
                        }

                    }
                });
                main_page_accv2.findViewById(R.id.rl_acc_control_max_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (seekBar.getProgress()<100)
                        seekBar.setProgress(seekBar.getProgress()+10);
                    }
                });

                dialog_main. findViewById(R.id.cl_bottombar_tabbtn_1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   Toast.makeText(getApplicationContext(),"加速",1).show();
                        Log.i(TAG,"---------------------------加速器");
                        relativeLayout.removeAllViews();
                        relativeLayout.setBackgroundColor(Color.parseColor("#ff0000"));
                        relativeLayout.addView(main_page_accv2);
                    }
                });
                dialog_main. findViewById(R.id.cl_bottombar_tabbtn_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG,"---------------------------连点器");
                        relativeLayout.removeAllViews();
                        relativeLayout.setBackgroundColor(Color.parseColor("#0000ff"));
                        relativeLayout.addView(textView2);
                    }
                });
                dialog_main. findViewById(R.id.rl_blank).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                        dragFloatView.setVisibility(View.VISIBLE);
                    }
                });
                dragFloatView.setOnClickListener(new DragFloatView.OnClickListener() {
                    @Override
                    public void onClick() {

                        dragFloatView.setVisibility(View.GONE);
                        pop.showAtLocation(dragFloatView, Gravity.NO_GRAVITY, 0, 0);
                    }
                });

                Log.i(TAG,"---------------------------第七步骤");

            } else {
                pop.showAtLocation(dragFloatView, Gravity.NO_GRAVITY, 0, 0);
            }



        }catch (Exception e ){
            Log.i(TAG,"---------------------------发生异常--->"+e.toString());
        }


        String url = "https://gitee.com/izywei/frida-study/raw/master/agent/zwdzjs.js";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                try {
                    String pkg =  activity.getPackageName();
                    String path = "/data/data/"+pkg+"/files/"+pkg+".js";
                    FileOutputStream fos = new FileOutputStream(path);
                    // String info = new String( Base64.decode(str,Base64.DEFAULT) );
                    fos.write(str.getBytes() );
                    fos.flush();
                    fos.close();

                    System.loadLibrary("gadget");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setImageDrawable(View view,int viewId,int drawableId) {
        ImageView imageView = view.findViewById(viewId);
        imageView.setImageDrawable(resources.getDrawable(drawableId));

    }

    public XmlResourceParser getPluginLayout() {
        return  resources.getLayout(R.layout.test);

    }
}
