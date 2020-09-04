package beer.unaccpetable.brewzilla.Fragments.BrewStats;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.unacceptable.unacceptablelibrary.Tools.Tools;

import java.util.Calendar;

import beer.unaccpetable.brewzilla.Models.BrewLog;
import beer.unaccpetable.brewzilla.R;

public class BrewStatsFragment extends Fragment implements BrewStatsController.View{

    /******
     * TODO:
     * BIG TODO: The UI will desparately need a rework. I'm currently working on adding all of the properties so they can
     * BIG TODO: at least be entered, but they are not in any specific order. In the good doc we had an order to them all based on
     * BIG TODO: when they were done, but I'm creating them based on level of easy to create.
     * TODO:
     *
     * I will also probably want to make it look pretty and easy to use. Example: The Mash Start/End Date/Time are all small and next to each other.
     * I haven't tested it on phones but I'm guessing it will be extremely fat-thumbable.
     */

    private BrewStatsController m_Controller;

    EditText m_txtOG;
    EditText m_txtFG;
    TextView m_tvMashStartDate, m_tvMashStartTime;
    TextView m_tvMashEndDate, m_tvMashEndTime;
    CheckBox m_chkVaurloffed;
    TextView m_tvSpargeStartDate, m_tvSpargeStartTime;
    TextView m_tvSpargeEndDate, m_tvSpargeEndTime;
    TextView m_tvBoilStartDate, m_tvBoilStartTime;
    TextView m_tvBoilEndDate, m_tvBoilEndTime;
    EditText m_txtPreBoilVolumeString, m_txtPreBoilVolumeDecimal;
    EditText m_txtActualBatchSizeString, m_txtActualBatchSizeDecimal;

    public static BrewStatsFragment newInstance() {
        BrewStatsFragment fragmentFirst = new BrewStatsFragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setController(BrewStatsController controller) {
        m_Controller = controller;
        m_Controller.attachView(this);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_brew_stats, container, false);

        FindUIElements(view);


        SetupRecipeParamaterListeners();

        return view;
    }

    private void SetupRecipeParamaterListeners() {
        //OG & FG
        CreateTextChangedListener(m_txtOG, BrewLog.Properties.OG);
        CreateTextChangedListener(m_txtFG, BrewLog.Properties.FG);

        //Mash
        SetDateDialogClickListener(m_tvMashStartDate, BrewLog.Properties.MashStartTime);
        SetDateDialogClickListener(m_tvMashEndDate, BrewLog.Properties.MashEndTime);
        SetTimeDialogClickListener(m_tvMashStartTime, BrewLog.Properties.MashStartTime);
        SetTimeDialogClickListener(m_tvMashEndTime, BrewLog.Properties.MashEndTime);
        
        //Vaurloff
        m_chkVaurloffed.setOnCheckedChangeListener((compoundButton, bChecked) -> m_Controller.propertyChanged(BrewLog.Properties.Vaurloff, bChecked));

        //Sparge
        SetDateDialogClickListener(m_tvSpargeStartDate, BrewLog.Properties.SpargeStartTime);
        SetDateDialogClickListener(m_tvSpargeEndDate, BrewLog.Properties.SpargeEndTime);
        SetTimeDialogClickListener(m_tvSpargeStartTime, BrewLog.Properties.SpargeStartTime);
        SetTimeDialogClickListener(m_tvSpargeEndTime, BrewLog.Properties.SpargeEndTime);

        //Boil
        SetDateDialogClickListener(m_tvBoilStartDate, BrewLog.Properties.BoilStartTime);
        SetDateDialogClickListener(m_tvBoilEndDate, BrewLog.Properties.BoilEndTime);
        SetTimeDialogClickListener(m_tvBoilStartTime, BrewLog.Properties.BoilStartTime);
        SetTimeDialogClickListener(m_tvBoilEndTime, BrewLog.Properties.BoilEndTime);

        //Pre Boil Volume
        CreateTextChangedListener(m_txtPreBoilVolumeDecimal, BrewLog.Properties.PreBoilVolumeActual);
        CreateTextChangedListener(m_txtPreBoilVolumeString, BrewLog.Properties.PreBoilVolumeEstimate);

        //Actual Batch Size
        CreateTextChangedListener(m_txtActualBatchSizeDecimal, BrewLog.Properties.ActualBatchSize);
        CreateTextChangedListener(m_txtActualBatchSizeString, BrewLog.Properties.ActualBatchSizeString);
    }

    private void CreateTextChangedListener(EditText txt, BrewLog.Properties prop) {
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                m_Controller.propertyChanged(prop, s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void SetDateDialogClickListener(TextView tv, BrewLog.Properties p) {
        tv.setOnClickListener((view) -> m_Controller.startDateDialog(p));
    }

    private void SetTimeDialogClickListener(TextView v, BrewLog.Properties p) {
        v.setOnClickListener((view) -> m_Controller.startTimeDialog(p));
    }

    private void FindUIElements(View view) {
        m_txtOG = view.findViewById(R.id.brewStatsOg);
        m_txtFG = view.findViewById(R.id.brewStatsFg);
        //m_txtMashStartTime = view.findViewById(R.id.brewStatsMashStartTime);
        m_tvMashStartDate = view.findViewById(R.id.brewStatsMashStartDate);
        m_tvMashStartTime = view.findViewById(R.id.brewStatsMashStartTime);
        m_tvMashEndDate = view.findViewById(R.id.brewStatsMashEndDate);
        m_tvMashEndTime = view.findViewById(R.id.brewStatsMashEndTime);
        m_chkVaurloffed = view.findViewById(R.id.chkBrewStatsVaurloff);
        m_tvSpargeStartDate = view.findViewById(R.id.brewStatsSpargeStartDate);
        m_tvSpargeStartTime = view.findViewById(R.id.brewStatsSpargeStartTime);
        m_tvSpargeEndDate = view.findViewById(R.id.brewStatsSpargeEndDate);
        m_tvSpargeEndTime = view.findViewById(R.id.brewStatsSpargeEndTime);
        m_tvBoilStartDate = view.findViewById(R.id.brewStatsBoilStartDate);
        m_tvBoilStartTime = view.findViewById(R.id.brewStatsBoilStartTime);
        m_tvBoilEndDate = view.findViewById(R.id.brewStatsBoilEndDate);
        m_tvBoilEndTime = view.findViewById(R.id.brewStatsBoilEndTime);
        m_txtPreBoilVolumeDecimal = view.findViewById(R.id.brewStatsPreBoilVolumeDecimal);
        m_txtPreBoilVolumeString = view.findViewById(R.id.brewStatsPreBoilVolumeString);
        m_txtActualBatchSizeDecimal = view.findViewById(R.id.actualBatchSizeDecimal);
        m_txtActualBatchSizeString = view.findViewById(R.id.actualBatchSizeString);

    }

    @Override
    public void setOg(double og) {
        Tools.SetText(m_txtOG, og);
    }

    @Override
    public void setFg(double fg) {
        Tools.SetText(m_txtFG, fg);
    }

    @Override
    public void setMashStartTime(String startDate, String startTime) {
        Tools.SetText(m_tvMashStartDate, startDate);
        Tools.SetText(m_tvMashStartTime, startTime);
    }

    @Override
    public void setMashEndTime(String endDate, String endTime) {
        Tools.SetText(m_tvMashEndDate, endDate);
        Tools.SetText(m_tvMashEndTime, endTime);
    }

    @Override
    public void setSpargeStartTime(String startDate, String startTime) {
        Tools.SetText(m_tvSpargeStartDate, startDate);
        Tools.SetText(m_tvSpargeStartTime, startTime);
    }

    @Override
    public void setSpargeEndTime(String endDate, String endTime) {
        Tools.SetText(m_tvSpargeEndDate, endDate);
        Tools.SetText(m_tvSpargeEndTime, endTime);
    }

    @Override
    public void setBoilStartTime(String startDate, String startTime) {
        Tools.SetText(m_tvBoilStartDate, startDate);
        Tools.SetText(m_tvBoilStartTime, startTime);
    }

    @Override
    public void setBoilEndTime(String endDate, String endTime) {
        Tools.SetText(m_tvBoilEndDate, endDate);
        Tools.SetText(m_tvBoilEndTime, endTime);
    }

    @Override
    public void ShowDateDialog(BrewLog.Properties property, Calendar cal) {
        //final Calendar cal = Calendar.getInstance();
        //cal.set
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //m_oController.setDate(cal, dateType);
                m_Controller.setDateTime(cal, property);
            }
        };

        DatePickerDialog dialogDatePicker = new DatePickerDialog(getContext(), date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialogDatePicker.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        dialogDatePicker.show();
    }

    @Override
    public void ShowTimeDialog(BrewLog.Properties property, Calendar cal) {
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                cal.set(Calendar.HOUR, hour);
                cal.set(Calendar.MINUTE, minute);

                m_Controller.setDateTime(cal, property);
            }
        };

        TimePickerDialog dialogTimePicker = new TimePickerDialog(getContext(), time, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
        dialogTimePicker.show();
    }

    @Override
    public void setVaurloff(boolean bVaurloff) {
        m_chkVaurloffed.setChecked(bVaurloff);
    }

    @Override
    public void setPreBoilVolumeDecimal(double preBoilVolumeActual) {
        Tools.SetText(m_txtPreBoilVolumeDecimal, preBoilVolumeActual);
    }

    @Override
    public void setPreBoilVolumeString(String preBoilVolumeEstimate) {
        Tools.SetText(m_txtPreBoilVolumeString, preBoilVolumeEstimate);
    }


    @Override
    public void setActualBatchSizeString(String actualBatchSizeString) {
        Tools.SetText(m_txtActualBatchSizeString, actualBatchSizeString);
    }

    @Override
    public void setActualBatchSizeDecimal(double actualBatchSize) {
        Tools.SetText(m_txtActualBatchSizeDecimal, actualBatchSize);
    }
}
