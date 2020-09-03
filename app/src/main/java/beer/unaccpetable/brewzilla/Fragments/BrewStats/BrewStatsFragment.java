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

    private BrewStatsController m_Controller;

    EditText m_txtOG;
    EditText m_txtFG;
    EditText m_txtMashStartTime;
    TextView m_tvMashStartDate;
    TextView m_tvMashStartTime;
    TextView m_tvMashEndDate;
    TextView m_tvMashEndTime;
    CheckBox m_chkVaurloffed;

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
        m_txtOG.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                m_Controller.ogChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        m_txtFG.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                m_Controller.fgChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

/*        m_txtMashStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    m_Controller.mashStartTimeChanged(m_txtMashStartTime.getText().toString());
                }
            }
        });*/

        m_tvMashStartDate.setOnClickListener((dtt) -> m_Controller.startDateDialog(BrewLog.Properties.MashStartTime));
        m_tvMashStartTime.setOnClickListener((dtt) -> m_Controller.startTimeDialog(BrewLog.Properties.MashStartTime));
        m_tvMashEndDate.setOnClickListener((dtt) -> m_Controller.startDateDialog(BrewLog.Properties.MashEndTime));
        m_tvMashEndTime.setOnClickListener((dtt) -> m_Controller.startTimeDialog(BrewLog.Properties.MashEndTime));
        m_chkVaurloffed.setOnCheckedChangeListener((compoundButton, bChecked) -> m_Controller.setVaurloffed(bChecked));
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
    public void ShowDateDialog(BrewLog.Properties dtt, Calendar cal) {
        //final Calendar cal = Calendar.getInstance();
        //cal.set
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //m_oController.setDate(cal, dateType);
                m_Controller.setDateTime(cal, dtt);
            }
        };

        DatePickerDialog dialogDatePicker = new DatePickerDialog(getContext(), date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialogDatePicker.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        dialogDatePicker.show();
    }

    @Override
    public void ShowTimeDialog(BrewLog.Properties dtt, Calendar cal) {
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                cal.set(Calendar.HOUR, hour);
                cal.set(Calendar.MINUTE, minute);

                m_Controller.setDateTime(cal, dtt);
            }
        };

        TimePickerDialog dialogTimePicker = new TimePickerDialog(getContext(), time, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
        dialogTimePicker.show();
    }

    @Override
    public void setVaurloff(boolean bVaurloff) {
        m_chkVaurloffed.setChecked(bVaurloff);
    }
}
