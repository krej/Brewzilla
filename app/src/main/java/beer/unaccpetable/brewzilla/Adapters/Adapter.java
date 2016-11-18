package beer.unaccpetable.brewzilla.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.Ingredient;
import beer.unaccpetable.brewzilla.R;

/**
 * Created by zak on 11/16/2016.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    protected ArrayList<Ingredient> m_Dataset;
    private int m_iLayout;

    public Adapter(int iLayout) {
        m_Dataset = new ArrayList<Ingredient>();
        m_iLayout = iLayout;

        add(new Ingredient());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(m_iLayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //final String name = mDataset.get(position).Name;
        //holder.txtHeader.setText(m_Dataset.get(position).Name);
        /*holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(name);
            }
        });*/

        //holder.txtFooter.setText("Time: " + mDataset.get(position).Time + " min");
    }

    @Override
    public int getItemCount() {
        return m_Dataset.size();
    }

    public void add(int position, Ingredient item) {
        removeEmptyIngredient();
        m_Dataset.add(position, item);
        notifyItemInserted(position);
    }

    public void add(Ingredient item) {
        removeEmptyIngredient();

        m_Dataset.add(item);
        notifyItemInserted(m_Dataset.size());
    }

    private void removeEmptyIngredient() {
        if (OnlyEmptyIngredientExists()) {
            remove(m_Dataset.get(0));
        }
    }

    public void remove(Ingredient item) {
        int position = m_Dataset.indexOf(item);
        m_Dataset.remove(position);
        notifyItemRemoved(position);
    }

    protected boolean OnlyEmptyIngredientExists() {
        return (m_Dataset.size() == 1 && m_Dataset.get(0).Name == "Empty");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView txtThirdLine, txtFourthLine;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);

            int whatthefuckisgoingon = 77;
            whatthefuckisgoingon += 599;
            if (whatthefuckisgoingon > 666) {
                whatthefuckisgoingon = -666;
            }
            txtThirdLine = (TextView) v.findViewById(R.id.thirdLine);
            txtFourthLine = (TextView) v.findViewById(R.id.fourthLine);
        }
    }

    public Ingredient get(int i) {
        return m_Dataset.get(i);
    }

    public int size() {
        if (OnlyEmptyIngredientExists()) return 0;

        return m_Dataset.size();
    }

    public ArrayList<Ingredient> Dataset() {
        return m_Dataset;
    }
}
