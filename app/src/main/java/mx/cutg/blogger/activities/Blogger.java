package mx.cutg.blogger.activities;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;

import mx.cutg.blogger.R;
import mx.cutg.blogger.fragments.AgregarEntrada;
import mx.cutg.blogger.fragments.ListadoEntradas;
import mx.cutg.blogger.utils.CustomTypefaceSpan;
import mx.cutg.blogger.utils.Utils;

public class Blogger extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogger);

        initUI(savedInstanceState);
    }


    @Override
    protected void onStart()
    {
        super.onStart();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onPause()
    {
        super.onPause();
    }


    @Override
    protected void onStop()
    {
        super.onStop();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBackPressed()
    {
        FragmentManager manager = getSupportFragmentManager();

        if(manager.getBackStackEntryCount() > 0){
           backFragments(manager);
        }
        else{
            super.onBackPressed();
        }
    }


    //METODO QUE INICIALIZA LA INTERFAZ
    private void initUI(Bundle savedInstanceState)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        String title;
        if(savedInstanceState == null)
        {
            title = getString(R.string.txt_title);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fm_contenedor, ListadoEntradas.newInstance(), title);
            transaction.commit();
        }
        else
        {
            FragmentManager manager = getSupportFragmentManager();
            if(manager.getBackStackEntryCount() > 0)
            {
                if(manager.findFragmentByTag(getString(R.string.txt_title_agregar)) instanceof AgregarEntrada){
                    title = getString(R.string.txt_title_agregar);
                }
                else{
                    title = getString(R.string.txt_title_detalle);
                }
                enabledArrow(true);
            }
            else
            {
                title = getString(R.string.txt_title);
            }
        }

        applyFontToMenuItem(title);
    }


    //METODO QUE ASIGNA UN FONT AL TITLE
    @SuppressWarnings("ConstantConditions")
    private void applyFontToMenuItem(CharSequence title)
    {
        Typeface font = Utils.getFont(getApplicationContext(), 2);
        SpannableString mNewTitle = new SpannableString(title);
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        getSupportActionBar().setTitle(mNewTitle);
    }


    //METODO QUE REMPLAZA UN FRAGMENT CON OTRO
    @SuppressWarnings("ConstantConditions")
    public void replaceFragment(Fragment fragment, String title, String backStateName)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fm_contenedor, fragment, title);
        ft.addToBackStack(backStateName);
        ft.commit();

        enabledArrow(true);
        applyFontToMenuItem(title);
    }


    //METODO QUE ACTUALIZA PARAMETROS DE LA INTERFAZ PRINCIPAL, CUANDO UN FRAGMENT HIJO REGRESA A UNO PADRE
    private void backFragments(FragmentManager manager)
    {
        enabledArrow(false);
        applyFontToMenuItem(getString(R.string.txt_title));
        manager.popBackStack();
    }


    //METODO QUE MUESTRA U OCULTA LA FLECHA BACK
    @SuppressWarnings("ConstantConditions")
    private void enabledArrow(boolean show)
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        getSupportActionBar().setHomeButtonEnabled(show);
    }

}
