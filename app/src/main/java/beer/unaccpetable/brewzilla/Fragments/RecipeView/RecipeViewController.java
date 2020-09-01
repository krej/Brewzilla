package beer.unaccpetable.brewzilla.Fragments.RecipeView;
import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import beer.unaccpetable.brewzilla.Models.Adjunct;
import beer.unaccpetable.brewzilla.Models.AdjunctAddition;
import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.FermentableAddition;
import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.Models.HopAddition;
import beer.unaccpetable.brewzilla.Models.IngredientAddition;
import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Responses.RecipeStatsResponse;
import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.Models.YeastAddition;
import beer.unaccpetable.brewzilla.Repositories.IRepository;

public class RecipeViewController extends BaseLogic<RecipeViewController.View> {



    public interface ISaveRecipeEvent {
        void SaveRecipe(Recipe r);
    }

    public interface IStatsChangedEvent {
        void StatsChanged(RecipeStatistics stats);
    }

    public interface IErrorOccurredEvent {
        void ErrorOccurred(boolean bRecipeModifiedElsewhere);
    }

    private @NotNull ArrayList<ISaveRecipeEvent> m_evtSaveRecipeListener;
    private @NotNull ArrayList<IStatsChangedEvent> m_evtStatsChangedListener;
    private @NotNull ArrayList<IErrorOccurredEvent> m_evtErrorOccurredListener;

    private IRepository m_repo;
    private Recipe m_Recipe;

    private @NotNull ArrayList<Fermentable> m_alFermentables;
    private @NotNull ArrayList<Yeast> m_alYeasts;
    private @NotNull ArrayList<Adjunct> m_alAdjuncts;
    private @NotNull ArrayList<Hop> m_alHops;
    private @NotNull ArrayList<Style> m_alStyle;


    //used the store a copy of the recipe to make it easy to check for changes, to know if we should actually update or not
    private byte[] m_OriginalData;

    private boolean m_bDontSave;

    public RecipeViewController(IRepository repository) {
        super();

        m_repo = repository;

        m_alAdjuncts = new ArrayList<>();
        m_alFermentables = new ArrayList<>();
        m_alHops = new ArrayList<>();
        m_alYeasts = new ArrayList<>();
        m_alStyle = new ArrayList<>();

        m_evtSaveRecipeListener = new ArrayList<>();
        m_evtStatsChangedListener = new ArrayList<>();
        m_evtErrorOccurredListener = new ArrayList<>();

        LoadAllIngredientLists();
    }

    public void SetRecipe(Recipe r) {
        m_Recipe = r;
        PopulateScreen(r);
        m_OriginalData = m_Recipe.BuildRestData();
    }

    public void activityCreated() {
        if (m_Recipe != null)
            PopulateScreen(m_Recipe);
    }

    private void PopulateScreen(Recipe r) {
        m_OriginalData = r.BuildRestData();

        if (r.style != null) {
            view.SetStyle(r.style);
            view.SetStyleRanges(r.style);
        }
        PopulateStats(r.recipeStats);
        view.PopulateHops(r.hops);
        view.PopulateYeasts(r.yeasts);
        view.PopulateFermentables(r.fermentables);
        view.PopulateAdjuncts(r.adjuncts);

        //view.SetRefreshing(false);
        m_bDontSave = false;
    }

    double fermentableChanged(FermentableAddition f, String sNewWeight) {

        double dWeight = Tools.ParseDouble(sNewWeight);
        boolean bFermentableChanged = false;

        for (FermentableAddition fa : m_Recipe.fermentables) {
            if (fa.additionGuid.equals(f.additionGuid)) {
                if (fa.weight != dWeight) {
                    bFermentableChanged = true;
                }
                fa.weight = dWeight;
                break;
            }
        }

        if (bFermentableChanged) {
            RecalculateStats();
        }

        return dWeight;
    }

    HopAddition hopChanged(HopAddition ha, String sTime, String sAmount, String sAAU, String sType) {

        int dTime = Tools.ParseInt(sTime);
        double dAmount = Tools.ParseDouble(sAmount);
        double dAAU = Tools.ParseDouble(sAAU);

        boolean bHopChanged = false;
        boolean bTypeChanged = false;

        for (HopAddition h : m_Recipe.hops) {
            if (h.additionGuid.equals(ha.additionGuid)) {
                bHopChanged = h.time != dTime || h.amount != dAmount || h.hop.aau != dAAU || ! h.type.equals(sType);

                h.time = dTime;
                h.amount = dAmount;
                h.hop.aau = dAAU;

                if (!h.type.equals(sType))
                    bTypeChanged = true;
                h.type = sType;

                break;
            }
        }

        if (bHopChanged)
            RecalculateStats();

        if (bTypeChanged)
            SaveRecipeIfChanged();

        return ha;
    }


    @NotNull AdjunctAddition adjunctChanged(AdjunctAddition aa, String sAmount, String sAmountUnit, String sTime, String sTimeUnit, String sType) {
        double dAmount = Tools.ParseDouble(sAmount);
        int iTime = Tools.ParseInt(sTime);
        AdjunctAddition aaReturn = null;

        for (AdjunctAddition a : m_Recipe.adjuncts) {
            if (a.additionGuid.equals(aa.additionGuid)) {
                a.amount = dAmount;
                a.unit = sAmountUnit;
                a.time = iTime;
                a.timeUnit = sTimeUnit;
                a.type = sType;
                aaReturn = a;

                break;
            }
        }

        if (aaReturn == null)
            aaReturn = new AdjunctAddition();

        return aaReturn;
    }

    public void RecalculateStats() {
        m_repo.CalculateRecipeStats(m_Recipe, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                RecipeStatsResponse r = Tools.convertJsonResponseToObject(t, RecipeStatsResponse.class);
                if (r.Success) {
                    PopulateStats(r.recipeStats);
                } else {
                    HandleRecipeStatsResponseError(r);
                }
            }

            @Override
            public void onError(VolleyError error) {
                fireShowMessageEvent(Tools.ParseVolleyError(error));
            }
        });
    }

    private void HandleRecipeStatsResponseError(RecipeStatsResponse response) {
        if (response.Message.contains("Recipe has been modified")) {
            fireErrorOccurredEvent(true);
            /*m_bDontSave = true;

            m_ActivityView.ShowToast("Recipe has been modified elsewhere. Please refresh.");
            view.SetScreenReadOnly(false);
*/
        } else {
            fireShowMessageEvent(response.Message);
        }
    }

    private void PopulateStats(RecipeStatistics recipeStats) {
        view.PopulateStats(recipeStats);
        fireStatsChangedEvent(recipeStats);
    }

    android.view.View.OnFocusChangeListener createFocusChangeListener() {
        return (view, hasFocus) -> {
            if (!hasFocus) SaveRecipeIfChanged();
        };

    }

    private void SaveRecipeIfChanged() {
        //lastModifiedGuid needs to be set after saving!
        //RecipeEditorController currently sets it
        //m_OriginalData also needs to be set! It gets set after the save is completed by RecipeEditorController
        if (Arrays.equals(m_Recipe.BuildRestData(), m_OriginalData) || m_bDontSave) return;

        fireSaveRecipeEvent();
    }

    void AddIngredient(ListableObject i, Recipe.IngredientType ingredientType) {
        if (ingredientType == null) {
            //changing style - i'm not sure if i like this or not, so we'll see
            changeStyle((Style)i);

            return;
        }

        AddIngredient(i, ingredientType, -1);
    }
    private void changeStyle(Style i) {
        m_Recipe.style = i;
        view.SetStyle(i);
        view.SetStyleRanges(i);
        fireSaveRecipeEvent();
    }

    void AddIngredient(ListableObject i, Recipe.IngredientType ingredientType, int position) {

        switch (ingredientType) {
            case Fermntable:
                FermentableAddition fa;
                if (i instanceof Fermentable)
                    fa = new FermentableAddition((Fermentable)i);
                else
                    fa = (FermentableAddition)i;

                AddIngredient(m_Recipe.fermentables, fa, position);
                view.AddFermentable(fa); //TODO: When clicking 'undo' it isn't putting it back in its normal spot in the UI because i'm not passing position here
                break;

            case Hop:
                HopAddition ha;
                if (i instanceof Hop)
                    ha = new HopAddition((Hop)i);
                else
                    ha = (HopAddition)i;

                AddIngredient(m_Recipe.hops, ha, position);
                view.AddHop(ha);
                break;

            case Yeast:
                YeastAddition ya;
                if (i instanceof Yeast)
                    ya = new YeastAddition((Yeast)i);
                else
                    ya = (YeastAddition)i;
                AddIngredient(m_Recipe.yeasts, ya, position);
                view.AddYeast(ya);
                break;
            case Adjunct:
                AdjunctAddition aa;
                if (i instanceof Adjunct)
                    aa = new AdjunctAddition((Adjunct)i);
                else
                    aa = (AdjunctAddition)i;
                AddIngredient(m_Recipe.adjuncts, aa, position);
                view.AddAdjunct(aa);
                break;
        }

    }

    void DeleteIngredient(ListableObject i, Recipe.IngredientType ingredientType) {
        IngredientAddition ia = (IngredientAddition)i;

        switch (ingredientType) {
            case Fermntable:
                DeleteIngredient(m_Recipe.fermentables, ia);
                break;
            case Hop:
                DeleteIngredient(m_Recipe.hops, ia);
                break;
            case Yeast:
                DeleteIngredient(m_Recipe.yeasts, ia);
                break;
            case Adjunct:
                DeleteIngredient(m_Recipe.adjuncts, ia);
                break;
        }
    }


    private <T> void DeleteIngredient(ArrayList<T> list, IngredientAddition o) {
        for (int i = 0; i < list.size(); i++) {
            IngredientAddition lo = (IngredientAddition)list.get(i);

            if (lo.additionGuid.equals(o.additionGuid)) {
                list.remove(i);
                break;
            }
        }

        SaveRecipeIfChanged();
    }

    private <T> void AddIngredient(ArrayList<T> list, ListableObject o, int position) {

        if (position == -1) {
            list.add((T)o);
        } else {
            list.add(position, (T)o);
        }

        SaveRecipeIfChanged();

    }

    void ShowAddDialog(Recipe.IngredientType ingredientType) {
        switch (ingredientType) {
            case Fermntable:
                view.ShowAddDialog(m_alFermentables, ingredientType);
                break;
            case Hop:
                view.ShowAddDialog(m_alHops, ingredientType);
                break;
            case Yeast:
                view.ShowAddDialog(m_alYeasts, ingredientType);
                break;
            case Adjunct:
                view.ShowAddDialog(m_alAdjuncts, ingredientType);
                break;
        }
    }

    private void LoadAllIngredientLists() {
        LoadIngredientList(Recipe.IngredientType.Hop);
        LoadIngredientList(Recipe.IngredientType.Yeast);
        LoadIngredientList(Recipe.IngredientType.Fermntable);
        LoadIngredientList(Recipe.IngredientType.Adjunct);
        LoadStyles();
    }

    private void LoadIngredientList(Recipe.IngredientType type) {
        m_repo.LoadCollection(type.toString(), new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                StoreIngredientList(t, type);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void LoadStyles() {
        m_repo.LoadCollection("style", new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                Style[] styles = Tools.convertJsonResponseToObject(t, Style[].class);
                Collections.addAll(m_alStyle, styles);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void StoreIngredientList(String t, Recipe.IngredientType type) {
        switch (type) {
            case Adjunct:
                Adjunct[] adjuncts = Tools.convertJsonResponseToObject(t, Adjunct[].class);
                Collections.addAll(m_alAdjuncts, adjuncts);
                break;
            case Hop:
                Hop[] hops = Tools.convertJsonResponseToObject(t, Hop[].class);
                Collections.addAll(m_alHops, hops);
                break;
            case Fermntable:
                Fermentable[] fermentables = Tools.convertJsonResponseToObject(t, Fermentable[].class);
                Collections.addAll(m_alFermentables, fermentables);
                break;
            case Yeast:
                Yeast[] yeasts = Tools.convertJsonResponseToObject(t, Yeast[].class);
                Collections.addAll(m_alYeasts, yeasts);
                break;
        }

    }

    public Recipe getRecipe() {
        return m_Recipe;
    }


    public void setReadOnly(boolean bReadOnly) {
        view.SetScreenReadOnly(bReadOnly);
    }

    public void showStylePrompt() {
        view.ShowAddDialog(m_alStyle, null);
    }

    public void saveComplete() {
        m_OriginalData = m_Recipe.BuildRestData();
    }

    public void afterOnCreateView() {
        if (m_Recipe != null)
            PopulateScreen(m_Recipe);
    }


    public void refreshLayout() {
        view.RefreshStatsLayout();
    }

    /**
     * View interface
     */
    public interface View {

        void PopulateStats(RecipeStatistics recipeStats);
        void PopulateHops(ArrayList<HopAddition> hops);
        void PopulateYeasts(ArrayList<YeastAddition> yeasts);
        void PopulateFermentables(ArrayList<FermentableAddition> fermentables);
        void PopulateAdjuncts(ArrayList<AdjunctAddition> adjuncts);

        void SetStyle(Style beerStyle);
        void SetStyleRanges(Style style);
        void ShowAddDialog(ArrayList data, Recipe.IngredientType ingredientType);
        void SetScreenReadOnly(boolean bEnabled);


        void AddFermentable(FermentableAddition fa);

        void AddHop(HopAddition ha);

        void AddYeast(YeastAddition ya);
        void AddAdjunct(AdjunctAddition aa);

        void RefreshStatsLayout();
    }

    /**
     * Event helper methods
     */
    public void addSaveRecipeEventListener(ISaveRecipeEvent listener) {
        m_evtSaveRecipeListener.add(listener);
    }

    private void fireSaveRecipeEvent() {
        for (ISaveRecipeEvent listener : m_evtSaveRecipeListener) {
            listener.SaveRecipe(m_Recipe);
        }
    }

    public void addStatsChangedEventListener(IStatsChangedEvent listener) {
        m_evtStatsChangedListener.add(listener);
    }

    private void fireStatsChangedEvent(RecipeStatistics stats) {
        for (IStatsChangedEvent listener: m_evtStatsChangedListener) {
            listener.StatsChanged(stats);
        }
    }

    public void addErrorOccurredListener(IErrorOccurredEvent listener) {
        m_evtErrorOccurredListener.add(listener);
    }

    private void fireErrorOccurredEvent(boolean bRecipeModifiedElsewhere) {
        for (IErrorOccurredEvent listener : m_evtErrorOccurredListener) {
            listener.ErrorOccurred(bRecipeModifiedElsewhere);
        }
    }
}
