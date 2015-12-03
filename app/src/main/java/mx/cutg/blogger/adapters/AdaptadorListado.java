package mx.cutg.blogger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mx.cutg.blogger.R;
import mx.cutg.blogger.objetos.PostEntrada;
import mx.cutg.blogger.utils.Utils;
import mx.cutg.blogger.interfaces.verDetalle;

public class AdaptadorListado extends RecyclerView.Adapter<AdaptadorListado.PostEntradaHolder> implements Filterable
{
    private List<PostEntrada> visibleItems;
    private List<PostEntrada> allItems;
    private Context ctx;
    private verDetalle listener = null;


    public AdaptadorListado(Context context, verDetalle myinterface)
    {
        super();
        this.ctx = context;
        this.listener = myinterface;
    }


    public void initAdapter()
    {
        visibleItems = new ArrayList<>();
        allItems = new ArrayList<>();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public PostEntradaHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entrada, parent, false);
        return new PostEntradaHolder(v);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(PostEntradaHolder entrada, final int position)
    {
        entrada.autor.setText(visibleItems.get(position).getNb_autor());
        entrada.fecha.setText(ctx.getString(R.string.txt_label_item_fh) + visibleItems.get(position).getFh_publicacion());
        entrada.titulo.setText(visibleItems.get(position).getNb_titulo());

        String contenido = visibleItems.get(position).getDe_contenido();

        if(contenido.length() > 67){
            entrada.descripcion.setText(contenido.substring(0, 67) + "...");
        }
        else{
            entrada.descripcion.setText(contenido + "...");
        }

        entrada.detalle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(listener != null){
                    listener.DetalleLoad(visibleItems.get(position));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return visibleItems.size();
    }



    @SuppressWarnings("unused")
    //METODO QUE REGRESA EL DATA SET DEL ADAPTADOR
    public List<PostEntrada> getDataSet()
    {
        return this.visibleItems;
    }


    @SuppressWarnings("unused")
    //METODO QUE ASIGNA UN DATA SET DEL ADAPTADOR
    public void setDataSet(ArrayList<PostEntrada> data)
    {
        this.allItems = data;
        this.visibleItems = data;
    }


    @SuppressWarnings("unused")
    //METODO QUE ASIGNA UNA NUEVA ENTRADA
    public void setEntrada(PostEntrada art)
    {
        //allItems.add(art);
        visibleItems.add(art);
    }



    @Override
    public Filter getFilter()
    {
        return new EntradasFilter(this, allItems);
    }


    //SUBCLASE QUE NOS SIRVE COMO VISTA DE CAD ITEM
    public static class PostEntradaHolder extends RecyclerView.ViewHolder
    {
        TextView autor;
        TextView fecha;
        TextView titulo;
        TextView descripcion;
        Button detalle;

        public PostEntradaHolder(View itemView)
        {
            super(itemView);

            autor  = (TextView)itemView.findViewById(R.id.tv_item_entrada_autor);
            fecha  = (TextView)itemView.findViewById(R.id.tv_item_entrada_fecha);
            titulo = (TextView)itemView.findViewById(R.id.tv_item_entrada_titulo);
            descripcion = (TextView)itemView.findViewById(R.id.tv_item_entrada_desc);
            detalle = (Button)itemView.findViewById(R.id.btn_item_entrada_detalle);

            Typeface tf = Utils.getFont(itemView.getContext(), 2);

            autor.setTypeface(Utils.getFont(itemView.getContext(), 3));
            fecha.setTypeface(tf);
            titulo.setTypeface(tf);
            descripcion.setTypeface(tf);
        }

    }


    //SUBCLASE QUE FILTRA EL ADAPTER
    private static class EntradasFilter extends Filter
    {
        private final AdaptadorListado adapter;
        private final List<PostEntrada> originalList;
        private final List<PostEntrada> filteredList;

        private EntradasFilter(AdaptadorListado adp, List<PostEntrada> originalList)
        {
            super();

            this.adapter = adp;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(originalList);
            }
            else
            {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for(final PostEntrada it : originalList)
                {
                    if(it.getNb_autor().toLowerCase().contains(filterPattern)){
                        filteredList.add(it);
                    }
                    else if(it.getNb_titulo().toLowerCase().contains(filterPattern)){
                        filteredList.add(it);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            adapter.allItems = originalList;
            adapter.visibleItems.clear();
            adapter.visibleItems.addAll((ArrayList<PostEntrada>) results.values);
            adapter.notifyDataSetChanged();
        }

    }

}