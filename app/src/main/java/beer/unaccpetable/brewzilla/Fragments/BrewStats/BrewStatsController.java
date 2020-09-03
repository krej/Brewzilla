package beer.unaccpetable.brewzilla.Fragments.BrewStats;

import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Repositories.ITimeSource;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import beer.unaccpetable.brewzilla.Models.BrewLog;

public class BrewStatsController extends BaseLogic<BrewStatsController.View> {

    public enum DateTimeType {
        MashStart,
        MashEnd,
        BoilStart,
        BoilEnd
    }

    public interface IFGChanged {
        void fgChanged(double fg);
    }

    public interface IOGChanged {
        void ogChanged(double og);
    }

    public interface IMashStartTimeChanged {
        void mashStartTimeChanged(String startTime);
    }

    public interface IMashEndTimeChanged {
        void mashEndTimeChanged(String endTime);
    }

    public interface IGenericListener {
        void propertyChanged(String sProperty, Object value);
    }

    private @NotNull ArrayList<IFGChanged> m_evtFGChanged;
    private @NotNull ArrayList<IOGChanged> m_evtOGChanged;
    private @NotNull ArrayList<IMashStartTimeChanged> m_evtMashStartTimeChanged;
    private @NotNull ArrayList<IMashEndTimeChanged> m_evtMashEndTimeChanged;
    private @NotNull ArrayList<IGenericListener> m_evtPropertyChanged;

    private ITimeSource m_oTimeSource;

    private @NotNull Calendar m_cMashStart;
    private @NotNull Calendar m_cMashEnd;

    public BrewStatsController(ITimeSource oTimeSource) {
        m_evtFGChanged = new ArrayList<>();
        m_evtOGChanged = new ArrayList<>();
        m_evtMashStartTimeChanged = new ArrayList<>();
        m_evtMashEndTimeChanged = new ArrayList<>();
        m_evtPropertyChanged = new ArrayList<>();

        m_oTimeSource = oTimeSource;

        m_cMashStart = m_oTimeSource.getCalendarInstance();
        m_cMashEnd = m_oTimeSource.getCalendarInstance();
    }

    public void fgChanged(CharSequence s) {
        double fg = Tools.ParseDouble(s.toString());
        fireFGChangedEvent(fg);
    }

    public void ogChanged(CharSequence s) {
        double og = Tools.ParseDouble(s.toString());
        fireOGChangedEvent(og);
    }

    public void mashStartTimeChanged(String startTime) {
        fireMashStartTimeChanged(startTime);
    }


    public void PopulateStats(BrewLog log) { //double fg, double og, String mashStartTime) {
        view.setOg(log.og);
        view.setFg(log.fg);
        ConvertAndSetMashStart(log.mashStartTime, DateTimeType.MashStart);
        ConvertAndSetMashStart(log.mashEndTime, DateTimeType.MashEnd);
        view.setVaurloff(log.vaurloff);
    }

    private void ConvertAndSetMashStart(String sTime, DateTimeType dtt) {
        Date date = parseDateFromString(sTime);

        switch (dtt) {
            case MashStart:
                m_cMashStart.setTime(date);
                view.setMashStartTime(new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(date), new SimpleDateFormat("h:mm a", Locale.ENGLISH).format(date));
                break;
            case MashEnd:
                m_cMashEnd.setTime(date);
                view.setMashEndTime(new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(date), new SimpleDateFormat("h:mm a", Locale.ENGLISH).format(date));
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


    public void startDateDialog(DateTimeType dtt) {
        Calendar c = m_oTimeSource.getCalendarInstance();

        switch (dtt) {
            case MashStart:
                c = m_cMashStart;
                break;
            case MashEnd:
                c = m_cMashEnd;
                break;
        }

        view.ShowDateDialog(dtt, c);
    }

    public void startTimeDialog(DateTimeType dtt) {
        Calendar c = m_oTimeSource.getCalendarInstance();

        switch (dtt) {
            case MashStart:
                c = m_cMashStart;
                break;
            case MashEnd:
                c = m_cMashEnd;
                break;
        }

        view.ShowTimeDialog(dtt, c);
    }

    public void setDateTime(Calendar cal, DateTimeType dtt) {
        Date dt = cal.getTime();

        String sDate = Tools.FormatDate(dt, "MM/dd/yyyy");
        String sTime = Tools.FormatDate(dt, "h:mm a");

        switch(dtt) {
            case MashStart:
                view.setMashStartTime(sDate, sTime);
                fireMashStartTimeChanged(sDate + " " + sTime);
                break;
            case MashEnd:
                view.setMashEndTime(sDate, sTime);
                fireMashEndTimeChanged(sDate + " " + sTime);
                break;
        }
    }

    public void setVaurloffed(boolean bChecked) {
        firePropertyChanged("Vaurloff", bChecked);
    }

    public interface View {

        void setOg(double og);

        void setFg(double fg);

        void setMashStartTime(String startDate, String startTime);
        void setMashEndTime(String endDate, String endTime);

        void ShowDateDialog(DateTimeType dtt, Calendar c);

        void ShowTimeDialog(BrewStatsController.DateTimeType dtt, Calendar cal);

        void setVaurloff(boolean bVaurloff);
    }

    public void addFGChangedListener(IFGChanged listener) {
        m_evtFGChanged.add(listener);
    }

    private void fireFGChangedEvent(double fg) {
        for (IFGChanged listener : m_evtFGChanged) {
            listener.fgChanged(fg);
        }
    }

    public void addOGChangedListener(IOGChanged listener) {
        m_evtOGChanged.add(listener);
    }

    private void fireOGChangedEvent(double og) {
        for (IOGChanged listener : m_evtOGChanged) {
            listener.ogChanged(og);
        }
    }

    public void addMashStartTimeChangedListener(IMashStartTimeChanged listener) {
        m_evtMashStartTimeChanged.add(listener);
    }

    private void fireMashStartTimeChanged(String startTime) {
        for (IMashStartTimeChanged listener : m_evtMashStartTimeChanged) {
            listener.mashStartTimeChanged(startTime);
        }
    }

    private void fireMashEndTimeChanged(String startTime) {
        for (IMashEndTimeChanged listener : m_evtMashEndTimeChanged) {
            listener.mashEndTimeChanged(startTime);
        }
    }

    public void addMashEndTimeChangedListener(IMashEndTimeChanged listener) {
        m_evtMashEndTimeChanged.add(listener);
    }

    private void firePropertyChanged(String sProperty, Object value) {
        for (IGenericListener listener : m_evtPropertyChanged) {
            listener.propertyChanged(sProperty, value);
        }
    }

    public void addPropertyChangedListener(IGenericListener listener) {
        m_evtPropertyChanged.add(listener);
    }
}
