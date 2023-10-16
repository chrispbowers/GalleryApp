package com.example.galleryapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Uri imageUri;
    ImageAdapter imageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView galleryGridView = findViewById(R.id.galleryGridView);
        imageAdapter = new ImageAdapter();
        galleryGridView.setAdapter(imageAdapter);

    }

    //Handles the camera button click
    public void onCameraClick(View view) {

        Log.d("GalleryApp", "onCameraClick");

        //Create a unique file name to store the image using the current time
        String imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg";

        //Create a new file in the application folder
        File imageFile = new File(getFilesDir(), imageFileName);

        //A Uri used to retrieve the image taken by teh camera application
        imageUri = FileProvider.getUriForFile(this, ".fileprovider", imageFile);

        //Create an Intent to capture an image
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Set the output of that intent to be the image URi.
        //This ensure that the camera application records the image to the set location
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        //Launch the intent with the result handler
        cameraActivityLauncher.launch(takePictureIntent);

    }

    public class ImageAdapter extends BaseAdapter {

        //A list of Uris of images to be displayed in the gallery
        List<Uri> imageUris;

        // Adapter constructor
        public ImageAdapter() {
            imageUris = new ArrayList<>();
        }



        //The number of images in the gallery
        @Override
        public int getCount() {
            return imageUris.size();
        }

        //Get a specific image Uri
        @Override
        public Object getItem(int i) {
            return imageUris.get(i);
        }


        //Get an ID for the item. Just use its location in the list for this
        @Override
        public long getItemId(int i) {
            return i;
        }

        // Return an image to be placed in the gallery based on the
        // specific Uri for the list at this location provided.
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ImageView imageView = new ImageView(viewGroup.getContext());
            imageView.setImageURI(imageUris.get(i));
            return imageView;

        }

        //Add new image uris
        public void addImageUri(Uri imageUri) {
            imageUris.add(imageUri);
            notifyDataSetChanged();
        }

    }

    ActivityResultLauncher<Intent> cameraActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {

                    Log.d("GalleryApp", "image Uri: " + imageUri);

                    //Add new image Uri the adapter
                    imageAdapter.addImageUri(imageUri);

                }
    });


}