package beer.unaccpetable.brewzilla.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.Ingredient;
import beer.unaccpetable.brewzilla.NewRecipe;
import beer.unaccpetable.brewzilla.R;

/**
 * Created by zak on 11/16/2016.
 */

public abstract class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>
implements View.OnClickListener
{

    protected ArrayList<Ingredient> m_Dataset;
    private int m_iLayout;
    protected int m_iDialogLayout;
    protected int m_iClickedItem;
    protected LayoutInflater inflater;

    public Adapter(int iLayout, int iDialogLayout) {
        m_Dataset = new ArrayList<Ingredient>();
        m_iLayout = iLayout;
        m_iDialogLayout = iDialogLayout;
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

    protected abstract boolean AddItem(Dialog d, boolean bExisting);

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

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //This toast event works... I think I'll need to move the pop up Add/Edit window into this class
                    //and then possibly see if I can do a RaiseEvent thing to tell the NewRecipe class to refresh the stats
                    //Toast.makeText(v.getContext(), "Test" + getLayoutPosition(), Toast.LENGTH_LONG).show();
                    m_iClickedItem = getLayoutPosition();
                    AddItem(v.getContext(), m_Dataset.get(m_iClickedItem));
                }
            });
        }
    }

    public int clickedPosition() {
        return m_iClickedItem;
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

    @Override
    public void onClick(View v) {
        //Toast.makeText(context, String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        //m_iClickedItem =
    }

    public void AddItem(final Context c, Ingredient i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        //builder.setView(m_iDialogLayout);
        final boolean bExisting = i != null;

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });


        builder.setView(SetupDialog(c, i));
        final AlertDialog dialog = builder.create();


        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!AddItem(dialog, bExisting)) return;
                NewRecipe n = (NewRecipe)c;
                n.RefreshStats();
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

    protected View SetupDialog(Context c, Ingredient i) {

        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(m_iDialogLayout, null);
        return root;
    }

    protected void InfoMissing(Context c) {
        CharSequence text = "Info Missing";
        Toast t = Toast.makeText(c, text, Toast.LENGTH_SHORT);
        t.show();
    }

    protected Ingredient GetClickedItem() {
        return m_Dataset.get(m_iClickedItem);
    }
}
