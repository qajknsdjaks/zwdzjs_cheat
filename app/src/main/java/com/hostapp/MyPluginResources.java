package com.hostapp;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Method;

public class MyPluginResources extends Resources {

    public MyPluginResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }

    /**
     * 自定义返回插件的资源文件的Resource方法
     * @param resources
     * @param assets
     * @return
     */
    public static MyPluginResources getPluginResources(Resources resources,AssetManager assets){
        MyPluginResources pluginResources = new MyPluginResources(assets, resources.getDisplayMetrics(), resources.getConfiguration());
        return pluginResources;
    } 

    //自己定义加载插件APK的AssetsManager
    public static AssetManager getPluginAssetsManager(File apkFile, Resources resources) throws ClassNotFoundException{
        // 由于系统没有提供AssetManager的实例化方法，因此我们使用反射
        Class<?> forName = Class.forName("android.content.res.AssetManager");
        Method[] declaredMethods = forName.getDeclaredMethods();
        for(Method method :declaredMethods){
            if(method.getName().equals("addAssetPath")){
                try {
                    AssetManager assetManager = AssetManager.class.newInstance();
                    // 调用addAssetPath方法，参数为我们插件APK的路径
                    method.invoke(assetManager, apkFile.getAbsolutePath());
                    return assetManager;
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }
        }
        return null;
    }
}
 