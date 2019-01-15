package com.example.lchang.sqliteimages;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.Toast;

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

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new PlatoAdapter(this, R.layout.item_plato, list);

        gridView.setAdapter(adapter);

        while (cursor.moveToNext()) {
            int codigo = cursor.getInt(0);
            String nombre = cursor.getString(1);
            Double precio = cursor.getDouble(2);
            byte[] imagen = cursor.getBlob(3);

            list.add(new Plato(codigo,nombre,precio,imagen));
        }
        adapter.notifyDataSetChanged();

        dbManager.close();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Actualizar", "Eliminar"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(ListadoActivity.this);

                dialog.setTitle("Elegir una acción");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            //Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM FOOD");
                            Cursor c = DBManager.getData("SELECT codigo FROM PLATO");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            // show dialog update at here
                            showDialogUpdate(ListadoActivity.this, arrID.get(position));

                        } else {
                            // delete
                            Cursor c = DBManager.getData("SELECT codigo FROM PLATO");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });



    }

    ImageView imageViewFood;
    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.actualiza_plato);
        dialog.setTitle("Actualizar");

        imageViewFood = (ImageView) dialog.findViewById(R.id.imageViewFood);
        final EditText edtName = (EditText) dialog.findViewById(R.id.edtName);
        final EditText edtPrice = (EditText) dialog.findViewById(R.id.edtPrice);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewFood.setOnClickListener(new View.OnClickListener() {
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
                    dbManager.open();
                    DBManager.update(position,
                            edtName.getText().toString().trim(),
                            Double.parseDouble(edtPrice.getText().toString().trim()),
                            RegistroActivity.imageViewToByte(imageViewFood)
                    );
                    dbManager.close();
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
                    dbManager.open();
                   DBManager.delete(idFood);
                    dbManager.close();
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
        // get all data from sqlite
        Cursor cursor = DBManager.getData("SELECT * FROM Plato");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            byte[] image = cursor.getBlob(3);

            list.add(new Plato(id, name, Double.parseDouble(price), image));
        }
        adapter.notifyDataSetChanged();
    }

}
