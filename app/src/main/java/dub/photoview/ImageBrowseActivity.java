package dub.photoview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 *图片查看
 */
public class ImageBrowseActivity extends Activity {
    private ViewPager vp;
    private TextView tvCount;
    private List<String> imageList;
    private List<View> viewList;
    private MyPageAdapter adapter;
    private int p;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_borwse);
        iv_back = ((ImageView) findViewById(R.id.iv_back));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vp = (ViewPager) findViewById(R.id.act_imagebrowse_viewpager);
        tvCount = (TextView) findViewById(R.id.act_imagebrowse_tvcontent);

        imageList = getIntent().getStringArrayListExtra("imagelist");
        p = getIntent().getIntExtra("p", 0);
        if (imageList != null) {
            viewList = new ArrayList<>();
            for (int i = 0; i < imageList.size(); i++) {
                PhotoView photoView = new PhotoView(this);
                photoView.enable();
                photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                photoView.setMaxScale(2);
                photoView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
                photoView.setImageResource(R.color.atlas_bgc);
                setImageResource(imageList.get(i),photoView);
                viewList.add(photoView);
                //图片的长按事件
                final int ii = i;
            }
            vp.setAdapter(adapter = new MyPageAdapter(viewList));
            vp.setCurrentItem(p);
            tvCount.setText(vp.getCurrentItem() + 1 + "/" + imageList.size());

            vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
                @Override
                public void onPageSelected(int position) {
                    tvCount.setText(vp.getCurrentItem() + 1 + "/" + imageList.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }


    class MyPageAdapter extends PagerAdapter {

        private List<View> listViews;// content

        private int size;// 页数

        public MyPageAdapter(List<View> listViews) {// 构造函数
            // 初始化viewpager的时候给的一个页面
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        @Override
        public int getCount() {// 返回数量
            return size;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
            ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {// 返回view对象
            try {
                ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);
            } catch (Exception e) {
                Log.e("zhou", "exception：" + e.getMessage());
            }
            return listViews.get(arg1 % size);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //CommonUtils.recycleImageView(photoView);
    }

    public void setImageResource(String path, ImageView riv) {
        ImageLoader.getInstance().displayImage("file:///"+path, riv);
    }
}
