package com.islamicinternationalincims.imagecropper;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.islamicinternationalincims.imagecropper.databinding.ActivityMainBinding;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;

    ActivityResultLauncher<String> mGetcontent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
/*

        mGetcontent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                    Intent intent=new Intent(MainActivity.this,cropActivity2.class);
                    intent.putExtra("DATA",result.toString());
                    startActivityForResult(intent,101);
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetcontent.launch("image/*");
            }
        });
*/


        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapDrawable=(BitmapDrawable) binding.imageView.getDrawable();
                bitmap=bitmapDrawable.getBitmap();
                FileOutputStream fileOutputStream=null;
                File sdCard= Environment.getExternalStorageDirectory();
                File Diractory=new File(sdCard.getAbsolutePath()+"/Download");
                Diractory.mkdir();
                String filename=String.format("%d.jpg",System.currentTimeMillis());
                File outFile=new File(Diractory,filename);
                Toast.makeText(MainActivity.this, "image save", Toast.LENGTH_SHORT).show();
                try {
                    fileOutputStream=new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));
                    sendBroadcast(intent);

                }catch (Exception e){

                }
            }
        });
        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable mDrawable =binding.imageView.getDrawable();
                Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
                //String aaa= aa;
                // Toast.makeText(TractorDetailActivity2.this, ""+aaa, Toast.LENGTH_SHORT).show();
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "image cropper", null);
                Uri uri = Uri.parse(path);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
              //  shareIntent.putExtra(Intent.EXTRA_TEXT, name+aaa);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ImagePicker.with(MainActivity.this)
                        .crop()
                        // Crop image(Optional), Check Customization for more option



                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                       .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
             Uri uri=data.getData();



            File file = new File(uri.getPath());
            long length = file.length() ;
            Bitmap bitmapOrg = BitmapFactory.decodeFile(uri.getPath());

            Bitmap bitmap = bitmapOrg;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            long lengthbmp = imageInByte.length;
            Toast.makeText(this, ""+lengthbmp/1024, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, ""+length/1024, Toast.LENGTH_SHORT).show();

                    // Use Uri object instead of File to avoid storage permissions
                    binding.imageView.setImageURI(uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }



    }


    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==-1 && requestCode==101){
            String result=data.getStringExtra("RESULT");
            Uri resultUri=null;
            if (result!=null){
                resultUri= Uri.parse(result);
            }
            binding.imageView.setImageURI(resultUri);
        }
    }

 */
