package beer.unaccpetable.brewzilla.Adapters;

import beer.unaccpetable.brewzilla.Ingredients.Hop;

/**
 * Created by zak on 11/16/2016.
 */

public class HopAdapter extends Adapter {

    public HopAdapter(int iLayout) {
        super(iLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtHeader.setText(m_Dataset.get(position).Name);

        if (OnlyEmptyIngredientExists()) return;

        Hop item = (Hop)m_Dataset.get(position);
        holder.txtFooter.setText("Time: " + item.Time + " min");
    }
}
