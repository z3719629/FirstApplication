package com.crm.firstapplication;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import io.reactivex.functions.Consumer;

@ContentView(R.layout.activity_main_1)
public class ShareResiveActivity extends AppCompatActivity {

    private static final String TAG = "RxPermissionTest";

    @ViewInject(R.id.imageView)
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        // 申请权限
        requestPermissions();
    }

    private void showImage() {
        Intent intent=getIntent();
        String action=intent.getAction();
        String type=intent.getType();
        if(action.equals(Intent.ACTION_SEND)&&type.equals("image/*")){
            Uri uri=intent.getParcelableExtra(Intent.EXTRA_STREAM);
            //接收多张图片
            //ArrayList<Uri> uris=intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            if(uri!=null ){
                try {
                    FileInputStream fileInputStream = new FileInputStream(uri.getPath());
                    Bitmap bitmap= BitmapFactory.decodeStream(fileInputStream);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(ShareResiveActivity.this);
        rxPermission
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                            showImage();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(TAG, permission.name + " is denied.");
                        }
                    }
                });

    }

}
