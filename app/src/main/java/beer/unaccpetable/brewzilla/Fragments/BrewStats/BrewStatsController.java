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

public class BrewStatsController extends BaseLogic<BrewStatsController.View> {



    public enum DateTimeType {
        Mash,
        Boil
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

    private @NotNull ArrayList<IFGChanged> m_evtFGChanged;
    private @NotNull ArrayList<IOGChanged> m_evtOGChanged;
    private @NotNull ArrayList<IMashStartTimeChanged> m_evtMashStartTimeChanged;

    private ITimeSource m_oTimeSource;

    private @NotNull Calendar m_cMashStart;

    public BrewStatsController(ITimeSource oTimeSource) {
        m_evtFGChanged = new ArrayList<>();
        m_evtOGChanged = new ArrayList<>();
        m_evtMashStartTimeChanged = new ArrayList<>();

        m_oTimeSource = oTimeSource;

        m_cMashStart = m_oTimeSource.getCalendarInstance();
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


    public void PopulateStats(double fg, double og, String mashStartTime) {
        view.setOg(og);
        view.setFg(fg);
        ConvertAndSetMashStart(mashStartTime);
    }

    private void ConvertAndSetMashStart(String sTime) {
        //2019-09-28T00:00:00-05:00
        //https://stackoverflow.com/questions/7681782/simpledateformat-unparseable-date-exception
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH).parse(sTime);
        } catch (ParseException pe) {
            date = m_oTimeSource.getTodaysDate();

        }

        m_cMashStart.setTime(date);



        view.setMashStartTime(new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(date), new SimpleDateFormat("h:mm a", Locale.ENGLISH).format(date));
    }


    public void startDateDialog(DateTimeType dtt) {
        Calendar c = m_oTimeSource.getCalendarInstance();

        switch (dtt) {
            case Mash:
                c = m_cMashStart;
        }

        view.ShowDateDialog(dtt, c);
    }

    public void startTimeDialog(DateTimeType dtt) {
        Calendar c = m_oTimeSource.getCalendarInstance();

        switch (dtt) {
            case Mash:
                c = m_cMashStart;
        }

        view.ShowTimeDialog(dtt, c);
    }

    public void setDateTime(Calendar cal, DateTimeType dtt) {
        Date dt = cal.getTime();

        String sDate = Tools.FormatDate(dt, "MM/dd/yyyy");
        String sTime = Tools.FormatDate(dt, "h:mm a");

        switch(dtt) {
            case Mash:
                view.setMashStartTime(sDate, sTime);
                fireMashStartTimeChanged(sDate + " " + sTime);
                break;
        }
    }

    /*public void setTime(Calendar cal, DateTimeType dtt) {
        Date dt = cal.getTime();

        String sDate = Tools.FormatDate(dt, "MM/dd/yyyy");
        String sTime = Tools.FormatDate(dt, "hh:mm a");

        switch (dtt) {
            case Mash:
                view.setMashStartTime(sDate, sTime);
                fireMashStartTimeChanged(sDate + " " + sTime);
                break;
        }
    }*/

    public interface View {

        void setOg(double og);

        void setFg(double fg);

        void setMashStartTime(String startDate, String startTime);

        void ShowDateDialog(DateTimeType dtt, Calendar c);

        void ShowTimeDialog(BrewStatsController.DateTimeType dtt, Calendar cal);
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
}
