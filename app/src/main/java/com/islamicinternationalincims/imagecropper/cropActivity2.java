package com.islamicinternationalincims.imagecropper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.islamicinternationalincims.imagecropper.databinding.ActivityCrop2Binding;
import com.islamicinternationalincims.imagecropper.databinding.ActivityMainBinding;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class cropActivity2 extends AppCompatActivity {
    String result;
    Uri finalUri;
    ActivityCrop2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrop2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        readIntent();

        UCrop.Options options=new UCrop.Options();
        options.setCircleDimmedLayer(true);

        String destinationUri=new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();
        UCrop.of(finalUri,Uri.fromFile(new File(getCacheDir(),destinationUri)))
                .withOptions(options)
                //.withAspectRatio(0,0)
                .start(cropActivity2.this);
    }

    private void readIntent() {
        Intent intent=getIntent();
        if(intent.getExtras()!=null){
            result=intent.getStringExtra("DATA");
            finalUri= Uri.parse(result);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Intent intent=new Intent();
            intent.putExtra("RESULT",resultUri+"");
            setResult(-1,intent);
            finish();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
}