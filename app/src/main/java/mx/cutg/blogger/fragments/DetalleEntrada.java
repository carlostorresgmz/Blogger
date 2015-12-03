package mx.cutg.blogger.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mx.cutg.blogger.R;
import mx.cutg.blogger.interfaces.FragmentPassParameters;
import mx.cutg.blogger.objetos.PostEntrada;
import mx.cutg.blogger.utils.Utils;

public class DetalleEntrada extends Fragment
{
    public static DetalleEntrada newInstance(Bundle bun)

    {
        DetalleEntrada fm = new DetalleEntrada();
        fm.setArguments(bun);
        return fm;
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        log("onAttach");
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        log("onCreate");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_detalle, container, false);
        initUI(v, v.getContext(), savedInstanceState);
        return v;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        log("onActivityCreated");
    }


    @Override
    public void onStart()
    {
        super.onStart();

        log("onStart");
    }


    @Override
    public void onResume()
    {
        super.onResume();

        log("onResume");
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        log("onSaveInstanceState");
    }


    @Override
    public void onPause()
    {
        super.onPause();

        log("onPause");
    }


    @Override
    public void onStop() {
        super.onStop();

        log("onStop");
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        log("onDestroyView");
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        log("onDestroy");
    }


    @Override
    public void onDetach()
    {
        super.onDetach();

        log("onDetach");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_action_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        log("onConfigurationChanged");
    }


    //METODO QUE INICIALIZA LA INTERFAZ DEL FRAGMENT
    @SuppressWarnings("ConstantConditions")
    private void initUI(View view, Context ctx, Bundle savedInstanceState)
    {
        TextView autor = (TextView)view.findViewById(R.id.tv_detalle_entrada_autor);
        TextView fecha = (TextView)view.findViewById(R.id.tv_detalle_entrada_fecha);
        TextView titulo = (TextView)view.findViewById(R.id.tv_detalle_entrada_titulo);
        TextView descripcion = (TextView)view.findViewById(R.id.tv_detalle_entrada_desc);

        Typeface tf = Utils.getFont(ctx, 2);

        autor.setTypeface(Utils.getFont(ctx, 3));
        fecha.setTypeface(tf);
        titulo.setTypeface(tf);
        descripcion.setTypeface(tf);

        Bundle bun = getArguments();

        if(bun != null)
        {
            PostEntrada entrada = bun.getParcelable("entrada");

            autor.setText(entrada.getNb_autor());
            fecha.setText(entrada.getFh_publicacion());
            titulo.setText(entrada.getNb_titulo());
            descripcion.setText(entrada.getDe_contenido());

            getArguments().clear();
        }
    }


    private void log(String str)
    {
        //Log.d("PRUEBA", "fragment detalleEntrada " + str);
    }

}