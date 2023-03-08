package com.jc.farmfinder;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.time.Instant;

public class ReportScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_screen);

        Button createReportButton = (Button)findViewById(R.id.CreateReportBtn);
        createReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {CreateReport();}
        });

        Button addImgButton = (Button)findViewById(R.id.addImgBtn);
        addImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {UploadImage();}
        });
    }

    private void CreateReport()
    {
        //get all the fields required
        EditText animalDesc = (EditText)findViewById(R.id.animalDescriptionTxt);
        EditText tagNumber = (EditText)findViewById(R.id.TagNumberTxt);
        EditText additionalInfo = (EditText)findViewById(R.id.additionalInfoTxt);
        ImageView thumbnail = (ImageView) findViewById(R.id.ImageThumbnail);

        //this field is required
        if(animalDesc.getText().toString() == "")
        {
            //create a toast informing the user that this field is required
        }
        else{
            //use the details provided to create the report


            //when report is created, return the user to the home screen
            finish();
        }
    }

    private void UploadImage()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        imagePickerActivityResult.launch(galleryIntent);
    }

    ActivityResultLauncher<Intent> imagePickerActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null) {
                        Uri imageUri = result.getData().getData();
                        ImageView thumbnail = (ImageView) findViewById(R.id.ImageThumbnail);
                        thumbnail.setImageURI(imageUri);
                    }
                }
            }
    );
}