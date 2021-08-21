package cloud.mminf.myapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 0;
    private final String TAG = "Main Activity";
    private ImageView mImage;
    private Uri mImageUri;
    private Button choosePhoto;
    private Button reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);

        mImage = (ImageView) findViewById(R.id.lbl_user_photo);

        choosePhoto = (Button) findViewById(R.id.lbl_btn_photo);
        choosePhoto.setOnClickListener(this);

        reset = (Button) findViewById(R.id.lbl_btn_reset);
        reset.setOnClickListener(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mImageUri = preferences.getString("image", null);

        if (mImageUri != null) {
            mImage.setImageURI(Uri.parse(mImageUri));
        } else {
            mImage.setImageResource(R.drawable.ic_launcher);
        }







    }

    public void imageSelect() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("CONFERMA OPERAZIONE");
        builder.setMessage("Sei sicuro che vuoi cambiare immagine?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                permissionsCheck();
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


                // Do nothing, but close the dialog
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();








    }


    public void permissionsCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
    }


    public void clearData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        finish();
        startActivity(getIntent());
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lbl_btn_photo:
                // get image
                imageSelect();
                break;
            case R.id.lbl_btn_reset:
                // increase by 1
                clearData();
                break;
            default:
                break;
        }
    }



    @SuppressLint("MissingSuperCall")
    @Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to




        if (requestCode == PICK_IMAGE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a image.
                // The Intent's data Uri identifies which item was selected.
                if (data != null) {

                    // This is the key line item, URI specifies the name of the data
                    mImageUri = data.getData();

                    // Saves image URI as string to Default Shared Preferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("image", String.valueOf(mImageUri));
                    editor.commit();

                    // Sets the ImageView with the Image URI
                    mImage.setImageURI(mImageUri);
                    mImage.invalidate();
                }
            }
        }
    }




}