package mx.cutg.blogger.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import mx.cutg.blogger.R;
import mx.cutg.blogger.activities.Blogger;
import mx.cutg.blogger.adapters.AdaptadorListado;
import mx.cutg.blogger.interfaces.FragmentPassParameters;
import mx.cutg.blogger.interfaces.verDetalle;
import mx.cutg.blogger.objetos.PostEntrada;
import mx.cutg.blogger.utils.ManejadorDB;
import mx.cutg.blogger.utils.Utils;

public class ListadoEntradas extends Fragment implements FragmentPassParameters,
        OnQueryTextListener, verDetalle
{
    private loadEntradas Task;
    private SwipeRefreshLayout refreshLayout;
    private AdaptadorListado adapterEntradas;
    private RecyclerView recycler;
    //private SearchView searchView;
    private boolean isLoad = true;

    public static ListadoEntradas newInstance() {
        return new ListadoEntradas();
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        adapterEntradas = new AdaptadorListado(getContext(), this);
        adapterEntradas.initAdapter();

       /* if(savedInstanceState != null && savedInstanceState.size() > 0)
        {
            ArrayList<PostEntrada> listado = savedInstanceState.getParcelableArrayList("adapterDataSet");
            adapterEntradas.setDataSet(listado);
            adapterEntradas.notifyDataSetChanged();
        }*/
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_listado, container, false);
        initUI(v, v.getContext());
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
        super.onSaveInstanceState(outState);

        if(outState != null && adapterEntradas != null)
        {
            ArrayList<PostEntrada> arr = new ArrayList<>();
            arr.addAll(adapterEntradas.getDataSet());
            outState.putParcelableArrayList("adapterDataSet", arr);
        }
    }


    @Override
    public void onPause()
    {
        super.onPause();
    }


    @Override
    public void onStop()
    {
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
        if(Task != null && Task.isAlive())
        {
            Task.interrupt();
        }
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
        super.onPrepareOptionsMenu(menu);

        MenuItem search = menu.findItem(R.id.ab_search);
        search.setVisible(true);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
       return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }


    //METODO QUE INICIALIZA LA INTERFAZ DEL FRAGMENT
    private void initUI(View view, Context ctx)
    {
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.srl_listado_update);
        recycler = (RecyclerView) view.findViewById(R.id.rv_listado);
        FloatingActionButton agregar = (FloatingActionButton) view.findViewById(R.id.btn_listado_agregar);

        refreshLayout.setColorSchemeResources(R.color.rf1, R.color.rf2, R.color.rf3, R.color.rf4);

        LinearLayoutManager lManager = new LinearLayoutManager(ctx);
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapterEntradas);

        //EVENTOS
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                startTask();
            }
        });

        agregar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String backstackname = ListadoEntradas.this.getClass().getName();
                ((Blogger) getActivity()).replaceFragment(AgregarEntrada.newInstance(ListadoEntradas.this),
                        getString(R.string.txt_title_agregar),
                        backstackname);
            }
        });

        if(isLoad)
        {
            isLoad = false;
            refreshLayout.post(new Runnable()
            {
                @Override
                public void run()
                {
                    refreshLayout.setRefreshing(true);
                    startTask();
                }
            });
        }
    }


    private void startTask()
    {
        Task = new loadEntradas();
        Task.start();
    }


    //INTERFACE QUE AGREGA UNA NUEVA ENTRADA AL ADAPTADOR
    @SuppressWarnings("ConstantConditions")
    /*private void changeOrentation()
    {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        ViewGroup viewGroup = (ViewGroup) getView();
        viewGroup.removeAllViewsInLayout();
        View subview = inflater.inflate(R.layout.fragment_listado, viewGroup);
        initUI(subview, subview.getContext());
    }*/


    @Override
    public void fragmentPutValues(PostEntrada entrada)
    {
        adapterEntradas.setEntrada(entrada);
        adapterEntradas.notifyDataSetChanged();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText)
    {
        adapterEntradas.getFilter().filter(newText);
        recycler.scrollToPosition(0);
        return false;
    }


    @Override
    public void DetalleLoad(PostEntrada entrada)
    {
        Bundle bun = new Bundle();
        bun.putParcelable("entrada", entrada);
        String backstackname = ListadoEntradas.this.getClass().getName();
        ((Blogger) getActivity()).replaceFragment(DetalleEntrada.newInstance(bun),
                getString(R.string.txt_title_detalle),
                backstackname);
    }


    //METODO QUE PROCESA EL RESULTADOM EN EL HILO PRINCIPAL
    private void postRun(final Bundle bun)
    {
        refreshLayout.setRefreshing(false);

        if(bun.getBoolean("success"))
        {
            ArrayList<PostEntrada> listado = bun.getParcelableArrayList("arreglo");
            adapterEntradas.setDataSet(listado);
            adapterEntradas.notifyDataSetChanged();
            recycler.setAdapter(adapterEntradas);
        }
        else
        {
            Toast.makeText(getContext(), bun.getString("msje"), Toast.LENGTH_SHORT).show();
        }

        if(!Task.isInterrupted()){
            Task.interrupt();
        }
    }


    //SUBCLASE QUE LISTA LAS ENTRADAS DEL SERVIDOR
    private class loadEntradas extends Thread
    {
        private boolean isRunning = true;

        public loadEntradas() {}


        @Override
        public synchronized void start()
        {
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
                    HttpClient httpclient = new DefaultHttpClient();
                    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                    HttpGet get = new HttpGet(getString(R.string.txt_url)+"method="+getString(R.string.txt_getEntradas));

                    get.setHeader("content-type", "application/json");

                    HttpResponse response = httpclient.execute(get);

                    String str = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    JSONObject json = new JSONObject(str);

                    if(json.getBoolean("SUCCESS"))
                    {
                        ArrayList<PostEntrada> listado = new ArrayList<>();

                        JSONArray entradas = json.getJSONArray("ENTRADAS");

                        StringBuilder query = new StringBuilder();
                        query.append("DELETE FROM Entradas");

                        db.AbrirDB(true);
                        db.transaccionStart();
                        db.transaccion(query);

                        for(int i=0; i<entradas.length(); i++)
                        {
                            query.setLength(0);
                            JSONObject tupla = entradas.getJSONObject(i);

                            query.append("INSERT INTO Entradas(id_Entrada, fh_Entrada, nb_Autor, nb_Titulo, de_Contenido) VALUES")
                                    .append("(").append(tupla.getInt("id_Entrada")).append(", '")
                                    .append(tupla.getString("fh_Entrada")).append("', '")
                                    .append(tupla.getString("nb_Autor")).append("', '")
                                    .append(tupla.getString("nb_Titulo")).append("', '")
                                    .append(tupla.getString("de_Contenido")).append("')");

                            db.transaccion(query);

                            listado.add(new PostEntrada(tupla.getString("nb_Autor"), tupla.getString("nb_Titulo"),
                                    tupla.getString("de_Contenido"), tupla.getString("fh_Entrada")));
                        }

                        bun.putParcelableArrayList("arreglo", listado);
                        db.transaccionCommit();

                        bun.putBoolean("success", true);
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

                if(isRunning)
                {
                    Message msge = new Message();
                    msge.setData(bun);

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
            isRunning = false;
            super.interrupt();
        }
    }

}