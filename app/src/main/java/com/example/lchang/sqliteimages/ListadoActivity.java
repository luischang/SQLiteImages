package com.example.lchang.sqliteimages;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListadoActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<Plato> list;
    PlatoAdapter adapter = null;
    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
        setTitle("Listado de Platos");
        dbManager = new DBManager(this);
        gridView = (GridView) findViewById(R.id.gridView);
        this.updateFoodList();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Actualizar", "Eliminar"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(ListadoActivity.this);

                dialog.setTitle("Elegir una acción");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        ArrayList<Integer> arrID = dbManager.getData("SELECT codigo FROM PLATO");
                        if (item == 0) {
                            // Ver dialog Update
                            showDialogUpdate(ListadoActivity.this, arrID.get(position));

                        } else {
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    ImageView imgMPlato;
    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.actualiza_plato);
        dialog.setTitle("Actualizar");

        imgMPlato = (ImageView) dialog.findViewById(R.id.imgMPlato);
        final EditText txtMNombre = (EditText) dialog.findViewById(R.id.txtMNombre);
        final EditText txtMPrecio = (EditText) dialog.findViewById(R.id.txtMPrecio);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnMActualizar);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imgMPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        ListadoActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbManager = new DBManager(getApplicationContext());
                    dbManager.update(position,
                            txtMNombre.getText().toString().trim(),
                            Double.parseDouble(txtMPrecio.getText().toString().trim()),
                            RegistroActivity.imageViewToByte(imgMPlato)
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Actualización exitosa!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Error Update", error.getMessage());
                }
                updateFoodList();
            }
        });
    }

    private void showDialogDelete(final int idFood){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ListadoActivity.this);
        dialogDelete.setTitle("Advertencia!!");
        dialogDelete.setMessage("Esta seguro de eliminar el registro?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dbManager = new DBManager(getApplicationContext());
                    dbManager.delete(idFood);
                    Toast.makeText(getApplicationContext(), "Eliminación exitosa!!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateFoodList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateFoodList(){
        list = new ArrayList<>();
        list = dbManager.fetch();
        adapter = new PlatoAdapter(this, R.layout.item_plato, list);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "No cuenta con permiso para acceder a la galería!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgMPlato.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}