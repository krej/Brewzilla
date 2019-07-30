package beer.unaccpetable.brewzilla.Screens.MainScreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Adapters.BaseAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.R;
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.RecipeEditor;

public class RecipeListAdapterViewControl extends BaseAdapterViewControl {

    public RecipeListAdapterViewControl(Activity a) {
        m_Activity = a;
    }

    @Override
    public void SetupDialog(View root, ListableObject i) {

    }

    @Override
    public void SetupViewInList(NewAdapter.ViewHolder view, ListableObject i) {
        Recipe r = (Recipe)i;

        TextView textView = view.view.findViewById(R.id.recipeName);
        TextView tvStyle = view.view.findViewById(R.id.recipeStyle);
        TextView tvABV = view.view.findViewById(R.id.recipeABV);
        Tools.SetText(textView, i.name);


        if (r.style != null)
            Tools.SetText(tvStyle, r.style.name);
        if (r.recipeStats != null)
            Tools.SetText(tvABV, r.recipeStats.getFormattedABV(true));
    }

    @Override
    public void onItemClick(View v, ListableObject i) {
        Intent intent = new Intent(m_Activity.getApplicationContext(), RecipeEditor.class);
        intent.putExtra("RecipeID", i.idString);
        //m_Activity.startActivity(intent);
        m_Activity.startActivityForResult(intent, MainScreen.EDIT_RECIPE);
    }

    @Override
    public void onItemLongPress(View v, ListableObject i) {

    }

    @Override
    public boolean onDialogOkClicked(Dialog d, ListableObject i) {
        return false;
    }
}
