package beer.unaccpetable.brewzilla.Adapters;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Adapters.BaseAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.IAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.R;

public class SearchDialogAdapterViewControl extends BaseAdapterViewControl {
    public interface IItemSelected {
        void itemSelected(ListableObject i);
    }

    private IItemSelected m_ItemSelectedListener;

    public void setItemSelectedListener(IItemSelected listener) {
        m_ItemSelectedListener = listener;
    }

    @Override
    public void SetupDialog(View root, ListableObject i) {

    }

    @Override
    public void SetupViewInList(NewAdapter.ViewHolder view, ListableObject i) {
        TextView textView = view.view.findViewById(R.id.firstLine);
        Tools.SetText(textView, i.toString());
    }

    @Override
    public void onItemClick(View v, ListableObject i) {
        if (m_ItemSelectedListener != null)
            m_ItemSelectedListener.itemSelected(i);


    }

    @Override
    public void onItemLongPress(View v, ListableObject i) {

    }

    @Override
    public boolean onDialogOkClicked(Dialog d, ListableObject i) {
        return false;
    }
}
