package beer.unaccpetable.brewzilla.Adapters;

import beer.unaccpetable.brewzilla.Ingredients.Malt;

/**
 * Created by zak on 11/16/2016.
 */

public class MaltAdapter extends Adapter {
    public MaltAdapter(int iLayout) {
        super(iLayout);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtHeader.setText(m_Dataset.get(position).Name);

        if (OnlyEmptyIngredientExists()) return;
        Malt item = (Malt)m_Dataset.get(position);
        holder.txtFooter.setText("Weight: " + item.Weight + " lbs");
    }
}
