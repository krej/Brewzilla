package beer.unaccpetable.brewzilla.Fragments.MashSetup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.unacceptable.unacceptablelibrary.Controls.CustomOnItemSelectedListener;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Models.RecipeParameters;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.R;

public class MashFragment extends Fragment implements MashSetupController.View {
    private MashSetupController m_Controller;

    //initial mash
    Spinner m_spGristRatio;
    EditText m_txtInitialMashTemp;
    TextView m_lblInitialStrikeTemp;
    TextView m_lblInitialStrikeVolume;
    TextView m_txtTargetMashTemp;
    //mash infusion
    EditText m_txtCurrentTempOfMash, m_txtTargetMashTempInfusion, m_txtTotalWaterInMash, m_txtHLTTemp;
    TextView m_lblWaterToAdd;
    LinearLayout m_llMashInfusionWaterToAdd;
    
    public static MashFragment newInstance() {
        MashFragment fragmentFirst = new MashFragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setController(MashSetupController controller) {
        m_Controller = controller;
        m_Controller.attachView(this);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mash_setup, container, false);

        FindUIElements(view);


        SetupRecipeParamaterListeners();

        return view;
    }

    private void FindUIElements(View view) {
        m_spGristRatio = view.findViewById(R.id.spinGristRatio);
        m_txtInitialMashTemp = view.findViewById(R.id.txtInitialMashTemp);
        m_lblInitialStrikeTemp = view.findViewById(R.id.mashStrikeTemp);
        m_lblInitialStrikeVolume = view.findViewById(R.id.mashStrikeVolume);
        m_txtTargetMashTemp = view.findViewById(R.id.txtTargetMashTemp);

        m_txtCurrentTempOfMash = view.findViewById(R.id.txtCurrentTempOfMash);
        m_txtTargetMashTempInfusion = view.findViewById(R.id.txtMashTargetTempInfusion);
        m_txtTotalWaterInMash = view.findViewById(R.id.txtTotalWaterInMash);
        m_txtHLTTemp = view.findViewById(R.id.txtHltTemp);
        m_lblWaterToAdd = view.findViewById(R.id.waterToAdd);
        m_llMashInfusionWaterToAdd = view.findViewById(R.id.llMashInfusionWaterToAdd);
    }

   private void SetupRecipeParamaterListeners() {
        //TODO: These text change events cannot save the recipe, they need to just recalc stats

        m_spGristRatio.setOnItemSelectedListener(new CustomOnItemSelectedListener(new CustomOnItemSelectedListener.IMyAdapterViewOnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String[] gristValues = getResources().getStringArray(R.array.gristRatioValues);
                double dValue = Tools.ParseDouble(gristValues[position]);

                m_Controller.setGristRatio(dValue);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));

        m_txtInitialMashTemp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double dTemp = Tools.ParseDouble(s.toString());
                m_Controller.SetInitialMashTemp(dTemp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        m_txtTargetMashTemp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double dTemp = Tools.ParseDouble(s.toString());
                m_Controller.SetTargetMashTemp(dTemp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextWatcher twMashInfusion = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sCurrentTemp = m_txtCurrentTempOfMash.getText().toString();
                String sTargetMashTemp = m_txtTargetMashTempInfusion.getText().toString();
                String sTotalWaterInMash = m_txtTotalWaterInMash.getText().toString();
                String sHLTTemp = m_txtHLTTemp.getText().toString();

                m_Controller.CalcMashInfusion(sCurrentTemp, sTargetMashTemp, sTotalWaterInMash, sHLTTemp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        m_txtCurrentTempOfMash.addTextChangedListener(twMashInfusion);
        m_txtTargetMashTempInfusion.addTextChangedListener(twMashInfusion);
        m_txtTotalWaterInMash.addTextChangedListener(twMashInfusion);
        m_txtHLTTemp.addTextChangedListener(twMashInfusion);
    }

    @Override
    public void PopulateParameters(RecipeParameters recipeParameters) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.gristRatioValues, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //m_spGristRatio.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(String.valueOf(recipeParameters.gristRatio));
        m_spGristRatio.setSelection(spinnerPosition);
        m_txtInitialMashTemp.setText(String.valueOf(recipeParameters.initialMashTemp));
        m_txtTargetMashTemp.setText(String.valueOf(recipeParameters.targetMashTemp));

        //mash infusion
        m_txtCurrentTempOfMash.setText(String.valueOf(recipeParameters.targetMashTemp));
        m_txtTargetMashTempInfusion.setText(String.valueOf(recipeParameters.targetMashTemp));
        //m_txtTotalWaterInMash.setText
        //m_txtHLTTemp.setText();
    }

    @Override
    public void MashInfusionShowWaterToAdd(String sVolume) {
        m_llMashInfusionWaterToAdd.setVisibility(View.VISIBLE);
        m_lblWaterToAdd.setText(sVolume);
    }

    @Override
    public void PopulateMashStats(RecipeStatistics stats) {
        m_lblInitialStrikeTemp.setText(stats.getFormattedStrikeWaterTemp());
        m_lblInitialStrikeVolume.setText(stats.getFormattedStrikeWaterVolume() + " quarts");

        m_txtTotalWaterInMash.setText(stats.getFormattedStrikeWaterVolume());
        m_txtHLTTemp.setText(stats.getFormattedStrikeWaterTemp());
    }

    @Override
    public void setReadOnly(boolean bReadOnly) {
        m_spGristRatio.setEnabled(!bReadOnly);
        m_txtInitialMashTemp.setEnabled(!bReadOnly);
        m_lblInitialStrikeTemp.setEnabled(!bReadOnly);
        m_lblInitialStrikeVolume.setEnabled(!bReadOnly);
        m_txtTargetMashTemp.setEnabled(!bReadOnly);

        m_txtCurrentTempOfMash.setEnabled(!bReadOnly);
        m_txtTargetMashTempInfusion.setEnabled(!bReadOnly);
        m_txtTotalWaterInMash.setEnabled(!bReadOnly);
        m_txtHLTTemp.setEnabled(!bReadOnly);
    }
}
