package com.example.q.project1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.q.project1.R.id.galleryGridView;

public class Tab2Gallery extends Fragment {

    GridView gv;
    GalleryGridAdapter gAdapter;

    FloatingActionButton FABAddImg;
    SeekBar seekBar;
    TextView seekText;
    ArrayList<String> storedImgPath = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2gallery, container, false);

        loadImageFromStorage();

        FABAddImg = rootView.findViewById(R.id.fab_add);
        gv = (GridView) rootView.findViewById(galleryGridView);
        gAdapter = new GalleryGridAdapter(getContext());
        gv.setAdapter(gAdapter);
        seekBar = rootView.findViewById(R.id.gall_seekbar);
        seekText = rootView.findViewById(R.id.gall_seekcnt);


        FABAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Integer seek_cnt = seekBar.getProgress() + 1;
                String seek_text = String.valueOf(seek_cnt) + " in a row";
                seekText.setText(seek_text);
                gv.setNumColumns(seek_cnt);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri uri = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(uri, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            cursor.close();
            storeImage(imagePath);
            gAdapter.notifyDataSetChanged();
        }


    }

    /* Create a File for saving an image or video */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getContext().getApplicationContext().getPackageName()
                + "/Files/tabB");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        File mediaFile;
        String mImageName = "ChanRong_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void storeImage(String imagePath) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("TAG", "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            Bitmap image = BitmapFactory.decodeFile(imagePath);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            storedImgPath.add(imagePath);
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    public void loadImageFromStorage() {
        String storagePath = Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getContext().getApplicationContext().getPackageName()
                + "/Files/tabB";
        File[] ImgFiles = (new File(storagePath).listFiles());
        if (ImgFiles != null) {
            for (int i = 0; i < ImgFiles.length; i++) {
                if (!ImgFiles[i].isDirectory()) {
                    storedImgPath.add(String.valueOf(ImgFiles[i]));
                }
            }
        }
    }

    public class GalleryGridAdapter extends BaseAdapter {
        Context context;

        public GalleryGridAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return storedImgPath.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LinearLayout linear = new LinearLayout(context);
            linear.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, 500));
            linear.setPadding(10, 10, 10, 10);

            ImageView imageview = new ImageView(context);
            imageview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Bitmap bitmap = BitmapFactory.decodeFile(storedImgPath.get(position));

            imageview.setImageBitmap(bitmap);

            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String[] actions = new String[]{"Show Img", "Delete Img"};
                    AlertDialog.Builder selectAct = new AlertDialog.Builder(getContext());
                    selectAct.setTitle("Select Action");
                    selectAct.setNegativeButton("Cancel", null);
                    selectAct.setItems(actions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {
                                Intent showIntent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(new File(storedImgPath.get(position))));
                                startActivity(showIntent);
                            } else {
                                storedImgPath.remove(position);
                                gAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    selectAct.show();

                }
            });

            linear.addView(imageview);
            return linear;
        }
    }
}
