package com.dongnao.alvin.alipayplugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2018/3/28.
 */

public class PluginManager {
    private PackageInfo packageInfo;
    private Resources resources;
    private Context context;
    private DexClassLoader dexClassLoader;
    private static final PluginManager ourInstance = new PluginManager();
    private final String TAG = "app_PluginManager";
    public static PluginManager getInstance() {
        return ourInstance;
    }

    private PluginManager() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void loadPath(Context context) {

        File filesDir = context.getDir("plugin", Context.MODE_PRIVATE);
        String name = "pluginb.apk";
        String path = new File(filesDir, name).getAbsolutePath();

        //1 加载Manifest中注册的Activity
        PackageManager packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);


//        activity 名字
        File dexOutFile = context.getDir("dex", Context.MODE_PRIVATE);
        //2 生成DexClassLoader，用来加载插件的Dex文件
        dexClassLoader = new DexClassLoader(path, dexOutFile.getAbsolutePath()
                , null, context.getClassLoader());
//        resource

        try {
            //3 找到插件的资源文件resouce
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, path);
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Resources getResources() {
        return resources;
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }
}
