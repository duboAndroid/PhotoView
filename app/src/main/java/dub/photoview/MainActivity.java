package dub.photoview;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import adapter.SubscribePicAdapter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static dub.photoview.MyApplication.context;

public class MainActivity extends AppCompatActivity {
    public static final String AddPicFlag = "addPicFlag";
    public ArrayList<String> picList = new ArrayList<>();
    private SubscribePicAdapter subPicAdapter;
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    public ArrayList<String> uploadList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView morePicRecycler = (RecyclerView) findViewById(R.id.more_pic);
        morePicRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        picList.add(AddPicFlag);
        subPicAdapter = new SubscribePicAdapter(R.layout.item_subscribe_pic, picList);
        morePicRecycler.setAdapter(subPicAdapter);
        subPicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (position == 0) {
                    if (picList.size() >= 7) {
                        Toast.makeText(MainActivity.this,"最多上传6张图片",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    choicePhotoWrapper();
                }else {
                    Intent intent = new Intent(context, ImageBrowseActivity.class);
                    intent.putStringArrayListExtra("imagelist", uploadList);
                    intent.putExtra("p", position-1);
                    startActivity(intent);
                }

            }
        });
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, perms)) {
            Intent intent = new Intent(MainActivity.this, PhotoPickerActivity.class);
            intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
            intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 7 - picList.size());
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_PHOTO);
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            // File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "swjPhoto");
            //    startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mPhotosSnpl.getMaxItemCount(), mPhotosSnpl.getData(), true), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
            picList.addAll(result);
            uploadList.addAll(result);
            subPicAdapter.notifyDataSetChanged();  //图片返回来了但是没展示出来，adapter 的conver方法只走了一次

        }
    }

}
