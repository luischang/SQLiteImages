package com.example.lchang.sqliteimages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlatoAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Plato> listaPlato;

    public PlatoAdapter(Context context, int layout, ArrayList<Plato> listaPlato) {
        this.context = context;
        this.layout = layout;
        this.listaPlato = listaPlato;
    }

    @Override
    public int getCount() {
        return listaPlato.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPlato.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imgPlato;
        TextView txtNombre, txtPrecio;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtNombre = (TextView) row.findViewById(R.id.txtNombre);
            holder.txtPrecio = (TextView) row.findViewById(R.id.txtPrecio);
            holder.imgPlato = (ImageView) row.findViewById(R.id.imgPlato);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Plato plato = listaPlato.get(position);

        holder.txtNombre.setText(plato.getNombre());
        holder.txtPrecio.setText(plato.getPrecio().toString());

        byte[] imagenPlato = plato.getImagen();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagenPlato, 0, imagenPlato.length);
        holder.imgPlato.setImageBitmap(bitmap);

        return row;
    }
}
