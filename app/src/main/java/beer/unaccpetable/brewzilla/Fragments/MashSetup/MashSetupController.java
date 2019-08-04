package beer.unaccpetable.brewzilla.Fragments.MashSetup;

import android.widget.ArrayAdapter;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.RecipeParameters;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Repositories.IRepository;

public class MashSetupController extends BaseLogic<MashSetupController.View> {

    public interface IRequestRecipeEvent {
        Recipe RequestRecipe();
    }

    public interface IGristRatioChangedEvent {
        void GristRatioChanged(double value);
    }

    public interface IInitialMashTempChangedEvent {
        void InitialMashTempChanged(double value);
    }

    public interface ITargetMashTempChanged {
        void TargetMashTempChanged(double value);
    }

    private IRequestRecipeEvent m_evtRequestRecipeEvent;

    private IRepository m_repo;

    private @NotNull ArrayList<IGristRatioChangedEvent> m_evtGristRatioChangedEvent;
    private @NotNull ArrayList<IInitialMashTempChangedEvent> m_evtInitialMashTempChangedEvent;
    private @NotNull ArrayList<ITargetMashTempChanged> m_evtTargetMashTempChangedEvent;

    private RecipeParameters m_Parameters;

    public MashSetupController(IRepository repo) {
        super();

        m_repo = repo;

        m_evtGristRatioChangedEvent = new ArrayList<>();
        m_evtInitialMashTempChangedEvent = new ArrayList<>();
        m_evtTargetMashTempChangedEvent = new ArrayList<>();
    }

    public void PopulateParameters(RecipeParameters recipeParameters) {
        m_Parameters = recipeParameters;
        view.PopulateParameters(recipeParameters);
    }

    public void PopulateMashStats(RecipeStatistics recipeStats) {
        view.PopulateMashStats(recipeStats);
    }

    void CalcMashInfusion(String sCurrentTemp, String sTargetMashTemp, String sTotalWaterInMash, String sHLTTemp) {

        /*double dCurrentTemp = Tools.ParseDouble(sCurrentTemp);
        double dTargetMashTemp = Tools.ParseDouble(sTargetMashTemp);
        double dTotalWaterInMash = Tools.ParseDouble(sTotalWaterInMash);
        double dHLTTemp = Tools.ParseDouble(sHLTTemp);*/

        boolean bAllDataValid = ! (Tools.IsEmptyString(sCurrentTemp) || Tools.IsEmptyString(sTargetMashTemp) || Tools.IsEmptyString(sTotalWaterInMash) || Tools.IsEmptyString(sHLTTemp));

        if (!bAllDataValid || m_evtRequestRecipeEvent == null) return;

        Recipe recipe = m_evtRequestRecipeEvent.RequestRecipe();

        m_repo.MashInfusionCalculation(recipe, sCurrentTemp, sTargetMashTemp, sTotalWaterInMash, sHLTTemp, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                view.MashInfusionShowWaterToAdd(t);
            }

            @Override
            public void onError(VolleyError error) {
                fireShowMessageEvent(Tools.ParseVolleyError(error));
            }
        });
    }

    void setGristRatio(double dValue) {
        if (m_Parameters.gristRatio == dValue) return;

        fireGristRatioChangedEvent(dValue);
        //CurrentRecipe.recipeParameters.gristRatio = dValue;
        //SaveRecipe();
    }

    void SetInitialMashTemp(double dTemp) {
        if (m_Parameters.initialMashTemp == dTemp) return;

        fireInitialMashTempChangedEvent(dTemp);
        //CurrentRecipe.recipeParameters.initialMashTemp = dTemp;
        //SaveRecipe();
    }

    void SetTargetMashTemp(double dTemp) {
        if (m_Parameters.targetMashTemp != dTemp)
            fireTargetMashTempChangedEvent(dTemp);
        //CurrentRecipe.recipeParameters.targetMashTemp = dTemp;
        //SaveRecipe();
    }


    public interface View {
        void PopulateParameters(RecipeParameters recipeParameters);
        void MashInfusionShowWaterToAdd(String t);
        void PopulateMashStats(RecipeStatistics stats);
    }

    public void setRecipeRequestListener(IRequestRecipeEvent listener) {
        m_evtRequestRecipeEvent = listener;
    }

    public void addGristRatioChangedEventListener(IGristRatioChangedEvent listener) {
        m_evtGristRatioChangedEvent.add(listener);
    }

    private void fireGristRatioChangedEvent(double value) {
        for (IGristRatioChangedEvent listener : m_evtGristRatioChangedEvent) {
            listener.GristRatioChanged(value);
        }
    }

    public void addInitialMashTempChangedListener(IInitialMashTempChangedEvent listener) {
        m_evtInitialMashTempChangedEvent.add(listener);
    }

    private void fireInitialMashTempChangedEvent(double value) {
        for (IInitialMashTempChangedEvent listener : m_evtInitialMashTempChangedEvent) {
            listener.InitialMashTempChanged(value);
        }
    }

    public void addTargetMashTempChangedListener(ITargetMashTempChanged listener) {
        m_evtTargetMashTempChangedEvent.add(listener);
    }

    private void fireTargetMashTempChangedEvent(double value) {
        for (ITargetMashTempChanged listener : m_evtTargetMashTempChangedEvent) {
            listener.TargetMashTempChanged(value);
        }
    }
}
