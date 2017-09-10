package dub.photoview;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import utlis.DubFiles;

public class MyApplication extends Application {
    public static Context context;
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        instance = this;
        initImageLoaderCongruation();
    }

    public static MyApplication getInstance() {
        return instance;
    }
    private void initImageLoaderCongruation() {
        String processName = DubFiles.getProcessName(this, android.os.Process.myPid());
        if (!TextUtils.isEmpty(processName) && processName.equals(getPackageName())) {
            //ExceptionHandlerHelper.getInstance().init(this,StephenConfig.LogFileName);//打印logcat
            ImageLoaderConfiguration config = new ImageLoaderConfiguration
                    .Builder(this)
                    .memoryCacheExtraOptions(480, 800) //max width, max height，即保存的每个缓存文件的最大长宽
                    .threadPoolSize(3)//线程池内加载的数量
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                    .memoryCacheSize(2 * 1024 * 1024)
                    .discCacheSize(50 * 1024 * 1024)
                    .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .discCacheFileCount(100) //缓存的文件数量
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                    .writeDebugLogs() // Remove for release app
                    .build();//开始构建
            ImageLoader.getInstance().init(config);//全局初始化此配置
        }
    }

}
