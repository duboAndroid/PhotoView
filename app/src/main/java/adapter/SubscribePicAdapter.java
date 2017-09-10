package adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import dub.photoview.MainActivity;
import dub.photoview.R;

/**
 * 删除照片
 */
public class SubscribePicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private List<String> picList;

    public SubscribePicAdapter(int layoutResId, List picList) {
        super(layoutResId,picList);
        this.picList = picList;
    }

    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        ImageView iv_del=(ImageView)helper.getView(R.id.iv_del);
        if (item.equals(MainActivity.AddPicFlag)) {
            helper.setImageResource(R.id.subscribe_pic, R.drawable.test_add_pic);
            iv_del.setVisibility(View.GONE);
        } else {
//            helper.setImageBitmap(R.id.subscribe_pic, BitmapFactory.decodeFile(item));//添加照片      此处有问题
            iv_del.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage("file:///"+item,(ImageView) helper.getView(R.id.subscribe_pic));
        }

        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picList.remove(item);
                notifyDataSetChanged();
            }
        });
    }
}
