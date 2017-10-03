package beer.unaccpetable.brewzilla.Adapters;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.Recipe;
import beer.unaccpetable.brewzilla.NewRecipe;
import beer.unaccpetable.brewzilla.R;

/**
 * Created by zak on 1/8/2017.
 */

public class RecipeAdapter extends Adapter {

    public RecipeAdapter(int iLayout, int iDialogLayout) {
        super(iLayout, iDialogLayout);
    }

    public void RecipeAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(m_iLayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolderR vh = new ViewHolderR(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtHeader.setText(m_Dataset.get(position).name);

        if (OnlyEmptyIngredientExists()) return;

    }

    @Override
    protected boolean AddItem(Dialog d, boolean bExisting) {
        return false;
    }


    public class ViewHolderR extends Adapter.ViewHolder {
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView txtThirdLine, txtFourthLine;

        public ViewHolderR(View v) {
            super(v);

            /*txtHeader = (TextView) v.findViewById(R.id.firstLine);
            try {
                txtFooter = (TextView) v.findViewById(R.id.secondLine);

                txtThirdLine = (TextView) v.findViewById(R.id.thirdLine);
                txtFourthLine = (TextView) v.findViewById(R.id.fourthLine);
            } finally {

            }*/
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //This toast event works... I think I'll need to move the pop up Add/Edit window into this class
                    //and then possibly see if I can do a RaiseEvent thing to tell the NewRecipe class to refresh the stats
                    //Toast.makeText(v.getContext(), "Test" + getLayoutPosition(), Toast.LENGTH_LONG).show();
                    m_iClickedItem = getLayoutPosition();
                    LoadRecipe((Recipe)m_Dataset.get(m_iClickedItem), v);

                    //AddItem(v.getContext(), m_Dataset.get(m_iClickedItem));
                }
            });
        }
    }

    private void LoadRecipe(Recipe r, View v) {
        Intent intent = new Intent(v.getContext(), NewRecipe.class);
        intent.putExtra("RecipeID", r.id);
        v.getContext().startActivity(intent);
    }
}
