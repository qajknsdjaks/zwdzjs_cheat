package com.hostapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPlugin();

    }







    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            extractAssets(newBase, "plugin-release-unsigned.apk");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public void initPlugin() {
        final File extractFile = this.getFileStreamPath("plugin-release-unsigned.apk");
        dexPath = extractFile.getPath();
        //apk文件地址
        String dexpath = extractFile.getPath();

        //解压释放目录
        File fileRelease = getDir("dex", 0); //0 表示Context.MODE_PRIVATE

        Log.d("DEMO", "dexpath:" + dexpath);
        Log.d("DEMO", "fileRelease.getAbsolutePath():" + fileRelease.getAbsolutePath());

        classLoader = new DexClassLoader(dexpath, fileRelease.getAbsolutePath(), null, getClassLoader());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    loadResources();

                    Class  mLoadClassBean = classLoader.loadClass("com.gh.plugina.PluginMgr");

                    Constructor<?> cons = mLoadClassBean.getConstructor(Activity.class,Resources.class );
                    pluginMgr = cons.newInstance(MainActivity.this ,mResources);
                    Method show = mLoadClassBean.getMethod("show");
                    show.setAccessible(true);
                    show.invoke(pluginMgr);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },2000);
        //普通调用，反射的方式

    }

    Object pluginMgr;
    DexClassLoader classLoader = null;


    private AssetManager mAssetManager;
    private Resources mResources;
    private String dexPath = null;    //apk文件地址
    private Resources.Theme mTheme;

    protected void loadResources() {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        superRes.getDisplayMetrics();
        superRes.getConfiguration();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }
    /**
     * 把Assets里面得文件复制到 /data/data/files 目录下
     *
     * @param context
     * @param sourceName
     */
    public static void extractAssets(Context context, String sourceName) {
        AssetManager am = context.getAssets();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = am.open(sourceName);
            File extractFile = context.getFileStreamPath(sourceName);
            fos = new FileOutputStream(extractFile);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSilently(is);
            closeSilently(fos);
        }
    }

    // --------------------------------------------------------------------------
    private static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}