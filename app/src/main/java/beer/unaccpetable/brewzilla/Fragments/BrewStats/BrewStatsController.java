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

    private @NotNull Calendar m_cMashStart, m_cMashEnd;
    private @NotNull Calendar m_cSpargeStart, m_cSpargeEnd;
    private @NotNull Calendar m_cBoilStart, m_cBoilEnd;

    public BrewStatsController(ITimeSource oTimeSource) {
        m_evtPropertyChanged = new ArrayList<>();

        m_oTimeSource = oTimeSource;

        m_cMashStart = m_oTimeSource.getCalendarInstance();
        m_cMashEnd = m_oTimeSource.getCalendarInstance();
        m_cSpargeStart = m_oTimeSource.getCalendarInstance();
        m_cSpargeEnd = m_oTimeSource.getCalendarInstance();
        m_cBoilStart = m_oTimeSource.getCalendarInstance();
        m_cBoilEnd = m_oTimeSource.getCalendarInstance();
    }

    public void PopulateStats(BrewLog log) {
        view.setOg(log.og);
        view.setFg(log.fg);
        ConvertAndSetDateTimes(log.mashStartTime, BrewLog.Properties.MashStartTime);
        ConvertAndSetDateTimes(log.mashEndTime, BrewLog.Properties.MashEndTime);
        view.setVaurloff(log.vaurloff);
        ConvertAndSetDateTimes(log.spargeStartTime, BrewLog.Properties.SpargeStartTime);
        ConvertAndSetDateTimes(log.spargeEndTime, BrewLog.Properties.SpargeEndTime);
        ConvertAndSetDateTimes(log.boilStartTime, BrewLog.Properties.BoilStartTime);
        ConvertAndSetDateTimes(log.boilEndTime, BrewLog.Properties.BoilEndTime);
        view.setPreBoilVolumeString(log.preBoilVolumeEstimate);
        view.setPreBoilVolumeDecimal(log.preBoilVolumeActual);
        view.setActualBatchSizeString(log.actualBatchSizeString);
        view.setActualBatchSizeDecimal(log.actualBatchSize);
    }

    private void ConvertAndSetDateTimes(String sTime, BrewLog.Properties dtt) {
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
            case SpargeStartTime:
                m_cSpargeStart.setTime(date);
                view.setSpargeStartTime(sDate, sFormattedTime);
                break;
            case SpargeEndTime:
                m_cSpargeEnd.setTime(date);
                view.setSpargeEndTime(sDate, sFormattedTime);
                break;
            case BoilStartTime:
                m_cBoilStart.setTime(date);
                view.setBoilStartTime(sDate, sFormattedTime);
                break;
            case BoilEndTime:
                m_cBoilEnd.setTime(date);
                view.setBoilEndTime(sDate, sFormattedTime);
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


    public void startDateDialog(BrewLog.Properties p) {
        Calendar c = getCalendar(p);

        view.ShowDateDialog(p, c);
    }

    public void startTimeDialog(BrewLog.Properties p) {
        Calendar c = getCalendar(p);

        view.ShowTimeDialog(p, c);
    }

    private Calendar getCalendar(BrewLog.Properties p) {
        switch (p) {
            case MashStartTime:
                return m_cMashStart;
            case MashEndTime:
                return m_cMashEnd;
            case SpargeStartTime:
                return m_cSpargeStart;
            case SpargeEndTime:
                return m_cSpargeEnd;
            case BoilStartTime:
                return m_cBoilStart;
            case BoilEndTime:
                return m_cBoilEnd;
        }

        return m_oTimeSource.getCalendarInstance();
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
            case SpargeStartTime:
                view.setSpargeStartTime(sDate, sTime);
                firePropertyChanged(BrewLog.Properties.SpargeStartTime, sDateTime);
                break;
            case SpargeEndTime:
                view.setSpargeEndTime(sDate, sTime);
                firePropertyChanged(BrewLog.Properties.SpargeEndTime, sDateTime);
                break;
            case BoilStartTime:
                view.setBoilStartTime(sDate, sTime);
                firePropertyChanged(BrewLog.Properties.BoilStartTime, sDateTime);
                break;
            case BoilEndTime:
                view.setBoilEndTime(sDate, sTime);
                firePropertyChanged(BrewLog.Properties.BoilEndTime, sDateTime);
                break;
        }
    }

    public void propertyChanged(BrewLog.Properties property, Object value) {
        switch (property) {
            //Numbers, things that are coming over as a CharSequence(from a text box) and need to be converted to numbers first.
            case FG:
            case OG:
            case PreBoilVolumeActual:
            case ActualBatchSize:
                double d = Tools.ParseDouble(((CharSequence)value).toString());
                firePropertyChanged(property, d);
                break;

                //Strings, Bools, etc. Things that don't need any extra conversion
            case Vaurloff:
            case PreBoilVolumeEstimate:
            case ActualBatchSizeString:
                firePropertyChanged(property, value);
                break;

        }
    }

    public interface View {

        void setOg(double og);

        void setFg(double fg);

        void setMashStartTime(String startDate, String startTime);
        void setMashEndTime(String endDate, String endTime);
        void setSpargeStartTime(String startDate, String startTime);
        void setSpargeEndTime(String endDate, String endTime);
        void setBoilStartTime(String startDate, String startTime);
        void setBoilEndTime(String endDate, String endTime);

        void ShowDateDialog(BrewLog.Properties dtt, Calendar c);

        void ShowTimeDialog(BrewLog.Properties dtt, Calendar cal);

        void setVaurloff(boolean bVaurloff);

        void setPreBoilVolumeDecimal(double preBoilVolumeActual);

        void setPreBoilVolumeString(String preBoilVolumeEstimate);

        void setActualBatchSizeString(String actualBatchSizeString);

        void setActualBatchSizeDecimal(double actualBatchSize);
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
