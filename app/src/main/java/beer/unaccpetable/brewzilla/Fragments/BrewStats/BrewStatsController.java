package beer.unaccpetable.brewzilla.Fragments.BrewStats;

import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Repositories.ITimeSource;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import beer.unaccpetable.brewzilla.Models.BrewLog;

public class BrewStatsController extends BaseLogic<BrewStatsController.View> {

    public interface IPropertyChangedListener {
        void propertyChanged(BrewLog.Properties eProperty, Object value);
    }

    private @NotNull ArrayList<IPropertyChangedListener> m_evtPropertyChanged;

    private ITimeSource m_oTimeSource;

    private @NotNull Calendar m_cMashStart;
    private @NotNull Calendar m_cMashEnd;

    public BrewStatsController(ITimeSource oTimeSource) {
        m_evtPropertyChanged = new ArrayList<>();

        m_oTimeSource = oTimeSource;

        m_cMashStart = m_oTimeSource.getCalendarInstance();
        m_cMashEnd = m_oTimeSource.getCalendarInstance();
    }

    public void fgChanged(CharSequence s) {
        double fg = Tools.ParseDouble(s.toString());
        firePropertyChanged(BrewLog.Properties.FG, fg);
    }

    public void ogChanged(CharSequence s) {
        double og = Tools.ParseDouble(s.toString());
        firePropertyChanged(BrewLog.Properties.OG, og);
    }

    public void PopulateStats(BrewLog log) {
        view.setOg(log.og);
        view.setFg(log.fg);
        ConvertAndSetMashStart(log.mashStartTime, BrewLog.Properties.MashStartTime);
        ConvertAndSetMashStart(log.mashEndTime, BrewLog.Properties.MashEndTime);
        view.setVaurloff(log.vaurloff);
    }

    private void ConvertAndSetMashStart(String sTime, BrewLog.Properties dtt) {
        Date date = parseDateFromString(sTime);
        String sDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(date);
        String sFormattedTime = new SimpleDateFormat("h:mm a", Locale.ENGLISH).format(date);

        switch (dtt) {
            case MashStartTime:
                m_cMashStart.setTime(date);
                view.setMashStartTime(sDate, sFormattedTime);
                break;
            case MashEndTime:
                m_cMashEnd.setTime(date);
                view.setMashEndTime(sDate, sFormattedTime);
                break;
        }
    }

    private Date parseDateFromString(String sDate) {
        //2019-09-28T00:00:00-05:00
        //https://stackoverflow.com/questions/7681782/simpledateformat-unparseable-date-exception
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH).parse(sDate);
        } catch (ParseException pe) {
            date = m_oTimeSource.getTodaysDate();

        }

        return date;
    }


    public void startDateDialog(BrewLog.Properties dtt) {
        Calendar c = m_oTimeSource.getCalendarInstance();

        switch (dtt) {
            case MashStartTime:
                c = m_cMashStart;
                break;
            case MashEndTime:
                c = m_cMashEnd;
                break;
        }

        view.ShowDateDialog(dtt, c);
    }

    public void startTimeDialog(BrewLog.Properties dtt) {
        Calendar c = m_oTimeSource.getCalendarInstance();

        switch (dtt) {
            case MashStartTime:
                c = m_cMashStart;
                break;
            case MashEndTime:
                c = m_cMashEnd;
                break;
        }

        view.ShowTimeDialog(dtt, c);
    }

    public void setDateTime(Calendar cal, BrewLog.Properties dtt) {
        Date dt = cal.getTime();

        String sDate = Tools.FormatDate(dt, "MM/dd/yyyy");
        String sTime = Tools.FormatDate(dt, "h:mm a");
        String sDateTime = sDate + " " + sTime;

        switch(dtt) {
            case MashStartTime:
                view.setMashStartTime(sDate, sTime);
                firePropertyChanged(BrewLog.Properties.MashStartTime, sDateTime);
                break;
            case MashEndTime:
                view.setMashEndTime(sDate, sTime);
                firePropertyChanged(BrewLog.Properties.MashEndTime, sDateTime);
                break;
        }
    }

    public void setVaurloffed(boolean bChecked) {
        firePropertyChanged(BrewLog.Properties.Vaurloff, bChecked);
    }

    public interface View {

        void setOg(double og);

        void setFg(double fg);

        void setMashStartTime(String startDate, String startTime);
        void setMashEndTime(String endDate, String endTime);

        void ShowDateDialog(BrewLog.Properties dtt, Calendar c);

        void ShowTimeDialog(BrewLog.Properties dtt, Calendar cal);

        void setVaurloff(boolean bVaurloff);
    }

    private void firePropertyChanged(BrewLog.Properties eProperty, Object value) {
        for (IPropertyChangedListener listener : m_evtPropertyChanged) {
            listener.propertyChanged(eProperty, value);
        }
    }

    public void addPropertyChangedListener(IPropertyChangedListener listener) {
        m_evtPropertyChanged.add(listener);
    }
}
