package beer.unaccpetable.brewzilla.Adapters;

import beer.unaccpetable.brewzilla.Ingredients.Yeast;

/**
 * Created by zak on 11/16/2016.
 */

public class YeastAdapter extends Adapter {
    public YeastAdapter(int iLayout) {
        super(iLayout);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtHeader.setText(m_Dataset.get(position).Name);

        if (OnlyEmptyIngredientExists()) return;
        Yeast item = (Yeast) m_Dataset.get(position);
        holder.txtFooter.setText("Lab: " + item.Lab);
        holder.txtThirdLine.setText("Attenuation: " + item.Attenuation);
    }
}
