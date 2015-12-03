package mx.cutg.blogger.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mx.cutg.blogger.R;
import mx.cutg.blogger.interfaces.FragmentPassParameters;
import mx.cutg.blogger.objetos.PostEntrada;
import mx.cutg.blogger.utils.ManejadorDB;
import mx.cutg.blogger.utils.Utils;

public class AgregarEntrada extends Fragment implements OnFocusChangeListener
{
    private TextInputLayout inputAutor;
    private TextInputLayout inputTitulo;
    private TextInputLayout inputContenido;

    private EditText nbAutor;
    private EditText nbTitulo;
    private EditText de_Contenido;

    private View viewFocusable;
    private static FragmentPassParameters listener;

    public static AgregarEntrada newInstance(FragmentPassParameters myinterface)
    {
        listener = myinterface;
        return new AgregarEntrada();
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_agregar, container, false);
        initUI(v, v.getContext(), savedInstanceState);
        return v;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onStart()
    {
        super.onStart();
    }


    @Override
    public void onResume()
    {
        super.onResume();
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putString("nbAutor", nbAutor.getText().toString().trim());
        outState.putString("nbTitulo", nbTitulo.getText().toString().trim());
        outState.putString("deContenido", de_Contenido.getText().toString().trim());

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onPause()
    {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    @Override
    public void onDetach()
    {
        super.onDetach();
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
        MenuItem add = menu.findItem(R.id.ab_agregar);
        add.setVisible(true);

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

            case R.id.ab_agregar:
                validacionesFormulario();
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }


    //METODO QUE INICIALIZA LA INTERFAZ DEL FRAGMENT
    @SuppressWarnings("ConstantConditions")
    private void initUI(View view, Context ctx, Bundle savedInstanceState)
    {
        Typeface tf1 = Utils.getFont(ctx, 2);

        inputAutor = (TextInputLayout) view.findViewById(R.id.edt_agregar_autor_input_layout);
        inputTitulo = (TextInputLayout) view.findViewById(R.id.edt_agregar_titulo_input_layout);
        inputContenido = (TextInputLayout) view.findViewById(R.id.edt_agregar_desc_input_layout);
        viewFocusable = view.findViewById(R.id.view_agregar_focus_add);

        nbAutor = inputAutor.getEditText();
        nbTitulo = inputTitulo.getEditText();
        de_Contenido = inputContenido.getEditText();

        nbAutor.setTypeface(tf1);
        nbTitulo.setTypeface(tf1);
        de_Contenido.setTypeface(tf1);

        if(savedInstanceState != null && savedInstanceState.size() > 0)
        {
            nbAutor.setText(savedInstanceState.getString("nbAutor"));
            nbTitulo.setText(savedInstanceState.getString("nbTitulo"));
            de_Contenido.setText(savedInstanceState.getString("deContenido"));
        }

        //EVENTOS
        nbAutor.setOnFocusChangeListener(this);
        nbTitulo.setOnFocusChangeListener(this);
        de_Contenido.setOnFocusChangeListener(this);
    }


    //METODO QUE VALIDA EL FORMULARIO
    private void validacionesFormulario()
    {
        hideKeyboard();

        String autor = nbAutor.getText().toString().trim();
        String titulo = nbTitulo.getText().toString().trim();
        String contenido = de_Contenido.getText().toString().trim();

        if(autor.equals(""))
        {
            viewFocusable.requestFocus();
            inputAutor.setErrorEnabled(true);
            inputAutor.setError(getString(R.string.txt_agregar_error_autor));
        }
        else if(titulo.equals(""))
        {
            viewFocusable.requestFocus();
            inputTitulo.setErrorEnabled(true);
            inputTitulo.setError(getString(R.string.txt_agregar_error_titulo));
        }
        else if(contenido.equals(""))
        {
            viewFocusable.requestFocus();
            inputContenido.setErrorEnabled(true);
            inputContenido.setError(getString(R.string.txt_agregar_error_contenido));
        }
        else
        {
            viewFocusable.requestFocus();

            publicarEntreda Task = new publicarEntreda();
            Task.start();
        }
    }


    //METODO QUE OCULTA EL TECLADO EN CASO DE SER VISIBLE
    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewFocusable.getWindowToken(), 0);
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus)
    {
        switch(view.getId())
        {
            case R.id.edt_agregar_autor:
                if (hasFocus)
                {
                    inputAutor.setError(null);
                    inputAutor.setErrorEnabled(false);
                }
                break;

            case R.id.edt_agregar_titulo:
                if (hasFocus)
                {
                    inputTitulo.setError(null);
                    inputTitulo.setErrorEnabled(false);
                }
                break;

            case R.id.edt_agregar_desc:
                if (hasFocus)
                {
                    inputContenido.setError(null);
                    inputContenido.setErrorEnabled(false);
                }
                break;


            default: break;
        }
    }


    private class publicarEntreda extends Thread
    {
        private ProgressDialog pd;
        private boolean isInterrupted;

        public publicarEntreda() {
        }


        @Override
        public synchronized void start()
        {
            pd = new ProgressDialog(getActivity());
            pd.setTitle(getString(R.string.txt_pd_title));
            pd.setMessage(getString(R.string.txt_pd_msje));
            pd.setCancelable(false);
            pd.show();

            super.start();
        }


        @SuppressWarnings({"deprecation", "TryWithIdenticalCatches"})
        @Override
        public void run()
        {
            super.run();

            Activity act = getActivity();
            ManejadorDB db = new ManejadorDB(act, getString(R.string.NB_DB));
            final Bundle bun = new Bundle();

            try
            {
                if(Utils.hadInternet(act))
                {
                    Thread.sleep(1000);
                    String autor = nbAutor.getText().toString();
                    String title = nbTitulo.getText().toString();
                    String desc = de_Contenido.getText().toString();

                    List<NameValuePair> parametros = new ArrayList<>(4);
                    parametros.add(new BasicNameValuePair("method", getString(R.string.txt_agregarEntrada)));
                    parametros.add(new BasicNameValuePair("nb_autor", autor));
                    parametros.add(new BasicNameValuePair("nb_titulo", title));
                    parametros.add(new BasicNameValuePair("de_contenido", desc));

                    HttpClient httpclient = new DefaultHttpClient();
                    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                    HttpPost post = new HttpPost(getString(R.string.txt_url) );

                    post.setHeader("content-type", "application/json");
                    post.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    UrlEncodedFormEntity form = new UrlEncodedFormEntity(parametros, "UTF-8");
                    post.setEntity(form);

                    HttpResponse response = httpclient.execute(post);

                    String str = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    JSONObject json = new JSONObject(str);

                    if(json.getBoolean("SUCCESS"))
                    {
                        PostEntrada entrada = new PostEntrada(autor, title, desc, json.getString("fh_Entrada"));

                        StringBuilder query = new StringBuilder();
                        query.append("INSERT INTO Entradas(id_Entrada, fh_Entrada, nb_Autor, nb_Titulo, de_Contenido) VALUES")
                            .append("(").append(json.getInt("id_Entrada"))
                            .append(", '").append(json.getString("fh_Entrada"))
                            .append("', '").append(autor)
                            .append("', '").append(title)
                            .append("', '").append(desc).append("')");

                        db.AbrirDB(true);
                        db.transaccionStart();
                        db.transaccion(query);
                        db.transaccionCommit();

                        bun.putBoolean("success", true);
                        bun.putString("msje", getString(R.string.txt_success));
                        bun.putParcelable("entrada", entrada);
                    }
                    else
                    {
                        bun.putBoolean("success", false);
                        bun.putString("msje", json.getString("ERROR"));
                    }
                }
                else
                {
                    bun.putBoolean("success", false);
                    bun.putString("msje", getString(R.string.txt_error_internet));
                }
            }
            catch(SQLiteException e)
            {
                bun.putBoolean("success", false);
                bun.putString("msje", e.toString());
            }
            catch(ClientProtocolException e)
            {
                bun.putBoolean("success", false);
                bun.putString("msje", e.toString());
            }
            catch(UnsupportedEncodingException e)
            {
                bun.putBoolean("success", false);
                bun.putString("msje", e.toString());
            }
            catch(IOException e)
            {
                bun.putBoolean("success", false);
                bun.putString("msje", e.toString());
            }
            catch(JSONException e)
            {
                bun.putBoolean("success", false);
                bun.putString("msje", e.toString());
            }
            catch(Exception e)
            {
                bun.putBoolean("success", false);
                bun.putString("msje", e.toString());
            }
            finally
            {
                db.transaccionClose();
                db.CerrarBD();

                if(!isInterrupted)
                {
                    /*Message msge = new Message();
                    msge.setData(bun);*/

                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            postRun(bun);
                        }
                    });
                }
            }
        }


        @Override
        public void interrupt()
        {
            this.isInterrupted = true;
            super.interrupt();
            pd.dismiss();
        }


        //METODO QUE PROCESA EL RESULTADOM EN EL HILO PRINCIPAL
        private void postRun(Bundle bun)
        {
            pd.dismiss();

            Toast.makeText(getContext(), bun.getString("msje"), Toast.LENGTH_SHORT).show();

            if(bun.getBoolean("success"))
            {
                listener.fragmentPutValues((PostEntrada) bun.getParcelable("entrada"));
                getActivity().onBackPressed();
            }
        }
    }

}
