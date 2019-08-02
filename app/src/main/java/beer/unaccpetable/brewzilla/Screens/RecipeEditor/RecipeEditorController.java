package beer.unaccpetable.brewzilla.Screens.RecipeEditor;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import beer.unaccpetable.brewzilla.Models.Adjunct;
import beer.unaccpetable.brewzilla.Models.AdjunctAddition;
import beer.unaccpetable.brewzilla.Models.Fermentable;
import beer.unaccpetable.brewzilla.Models.FermentableAddition;
import beer.unaccpetable.brewzilla.Models.Hop;
import beer.unaccpetable.brewzilla.Models.HopAddition;
import beer.unaccpetable.brewzilla.Models.IngredientAddition;
import beer.unaccpetable.brewzilla.Models.Recipe;
import beer.unaccpetable.brewzilla.Models.RecipeParameters;
import beer.unaccpetable.brewzilla.Models.RecipeStatistics;
import beer.unaccpetable.brewzilla.Models.Responses.RecipeStatsResponse;
import beer.unaccpetable.brewzilla.Models.Style;
import beer.unaccpetable.brewzilla.Models.Yeast;
import beer.unaccpetable.brewzilla.Models.YeastAddition;
import beer.unaccpetable.brewzilla.Repositories.IRepository;
import beer.unaccpetable.brewzilla.Screens.RecipeEditor.Fragments.MashFragment;
import beer.unaccpetable.brewzilla.Tools.Calculations;

public class RecipeEditorController extends BaseLogic<RecipeEditorController.View> {

    public Recipe CurrentRecipe;

    private IActivityView m_ActivityView;
    public IMashView m_MashView;

    private @NotNull ArrayList m_alFermentables, m_alHops, m_alYeasts, m_alStyle;
    private @NotNull ArrayList<Adjunct> m_alAdjuncts;

    private IRepository m_repo;

    //used the store a copy of the recipe to make it easy to check for changes, to know if we should actually update or not
    private byte[] m_OriginalData;
    private boolean m_bDontSave;

    public RecipeEditorController(IRepository repository) {
        m_repo = repository;
        m_alAdjuncts = new ArrayList<>();
        m_alFermentables = new ArrayList();
        m_alHops = new ArrayList();
        m_alYeasts = new ArrayList();
        m_alStyle = new ArrayList();
    }

    public void LoadRecipe(String sID) {
        if (sID != null && sID.length() > 0) {
            m_repo.LoadRecipeWithAllIngredients(sID, new RepositoryCallback() {
                @Override
                public void onSuccess(String t) {
                    //TODO: Split the recipe call from the ingredient call after all. it is taking a while to load this next line and i'm guessing its because of all of the ingredients
                    RecipeEditorViewModel r = Tools.convertJsonResponseToObject(t, RecipeEditorViewModel.class);
                    CurrentRecipe = r.Recipe;
                    m_OriginalData = CurrentRecipe.BuildRestData();

                    //if (r.Styles != null)
                    //    view.PopulateStyleDropDown(r.Styles);

                    m_ActivityView.SetTitle(CurrentRecipe.name);
                    if (CurrentRecipe.style != null) {
                        view.SetStyle(CurrentRecipe.style);
                        view.SetStyleRanges(CurrentRecipe.style);
                    }
                    PopulateStats(CurrentRecipe.recipeStats);
                    view.PopulateHops(CurrentRecipe.hops);
                    view.PopulateYeasts(CurrentRecipe.yeasts);
                    view.PopulateFermentables(CurrentRecipe.fermentables);
                    view.PopulateAdjuncts(CurrentRecipe.adjuncts);
                    m_MashView.PopulateParameters(CurrentRecipe.recipeParameters);

                    /*view.PopulateHopDialog(r.Hops);
                    view.PopulateFermentableDialog(r.Fermentables);
                    view.PopulateYeastDialog(r.Yeasts);*/
                    m_alFermentables = r.Fermentables;
                    m_alHops = r.Hops;
                    m_alYeasts = r.Yeasts;
                    m_alStyle = r.Styles;

                    //view.SetRefreshing(false);
                    m_bDontSave = false;
                }

                @Override
                public void onError(VolleyError error) {
                    m_ActivityView.ShowToast(Tools.ParseVolleyError(error));
                }
            });
        } else {
            //TODO: Is this even possible anymore? I changed it so it saves the recipe before going to the screen then loads it.
            m_ActivityView.SetTitle("Create Recipe");
        }
    }

    private void PopulateStats(RecipeStatistics recipeStats) {
        view.PopulateStats(recipeStats);
        m_MashView.PopulateMashStats(recipeStats);


        int iColor = Color.parseColor(Calculations.GetSRMColor((int)recipeStats.srm));
        Color cDark = Color.valueOf(iColor);
        float fDark = 0.8f;
        int iDarkColor = Color.rgb(cDark.red() * fDark, cDark.green() * fDark, cDark.blue() * fDark);
        m_ActivityView.SetTitleSRMColor(iColor, iDarkColor);
    }

    private void FixBadData() {
        if (CurrentRecipe.recipeParameters == null)
            CurrentRecipe.recipeParameters = new RecipeParameters();

    }

    public void attachActivityView(IActivityView view) {
        m_ActivityView = view;
    }

    public void attachMashView(MashFragment mashFragment) {
        m_MashView = mashFragment;
    }


    public void SaveRecipe() {
        if (Arrays.equals(CurrentRecipe.BuildRestData(), m_OriginalData) || m_bDontSave) return;

        m_repo.SaveRecipe(CurrentRecipe.idString, CurrentRecipe, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                RecipeStatsResponse response = Tools.convertJsonResponseToObject(t, RecipeStatsResponse.class);
                if (response.Success) {
                    //comment out this line to test locking
                    CurrentRecipe.lastModifiedGuid = response.lastModifiedGuid;
                    PopulateStats(response.recipeStats);
                    CurrentRecipe.recipeStats = response.recipeStats;

                    //This should always be last so it gets the real copy of the data
                    m_OriginalData = CurrentRecipe.BuildRestData();
                }else {
                    HandleRecipeStatsResponseError(response);
                }
            }

            @Override
            public void onError(VolleyError error) {
                m_ActivityView.ShowToast("ERROR");
            }
        });
    }

    private void HandleRecipeStatsResponseError(RecipeStatsResponse response) {
        if (response.Message.contains("Recipe has been modified")) {
            m_bDontSave = true;

            m_ActivityView.ShowToast("Recipe has been modified elsewhere. Please refresh.");
            view.SetScreenReadOnly(false);

        } else {
            m_ActivityView.ShowToast(response.Message);
        }
    }

    public void setGristRatio(double dValue) {
        CurrentRecipe.recipeParameters.gristRatio = dValue;
        SaveRecipe();
    }

    public void SetInitialMashTemp(double dTemp) {
        CurrentRecipe.recipeParameters.initialMashTemp = dTemp;
        SaveRecipe();
    }

    public void SetTargetMashTemp(double dTemp) {
        CurrentRecipe.recipeParameters.targetMashTemp = dTemp;
        SaveRecipe();
    }

    public void CalcMashInfusion(String sCurrentTemp, String sTargetMashTemp, String sTotalWaterInMash, String sHLTTemp) {
        //if (bIgnoreUIChanges) return;

        /*double dCurrentTemp = Tools.ParseDouble(sCurrentTemp);
        double dTargetMashTemp = Tools.ParseDouble(sTargetMashTemp);
        double dTotalWaterInMash = Tools.ParseDouble(sTotalWaterInMash);
        double dHLTTemp = Tools.ParseDouble(sHLTTemp);*/

        boolean bAllDataValid = ! (Tools.IsEmptyString(sCurrentTemp) || Tools.IsEmptyString(sTargetMashTemp) || Tools.IsEmptyString(sTotalWaterInMash) || Tools.IsEmptyString(sHLTTemp));

        if (!bAllDataValid) return;

        m_repo.MashInfusionCalculation(CurrentRecipe.idString, sCurrentTemp, sTargetMashTemp, sTotalWaterInMash, sHLTTemp, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                //TODO: this
                m_MashView.MashInfusionShowWaterToAdd(t);
            }

            @Override
            public void onError(VolleyError error) {
                m_ActivityView.ShowToast(Tools.ParseVolleyError(error));
            }
        });
    }

    public void SetStyle(Style style) {
        CurrentRecipe.style = style;
        view.SetStyleRanges(style);
        SaveRecipe();
    }

    public void GoBack() {

        m_ActivityView.FinishActivity(CurrentRecipe.idString, CurrentRecipe.recipeStats.abv, false);
    }

    public void DeleteRecipe() {
        m_repo.DeleteRecipe(CurrentRecipe.idString, new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                m_ActivityView.FinishActivity(CurrentRecipe.idString, 0, true);
            }

            @Override
            public void onError(VolleyError error) {
                m_ActivityView.ShowToast("Error deleting recipe.");
            }
        });
    }

    public void AskDeleteRecipe() {
        m_ActivityView.PromptDeletion();
    }

    public double fermentableChanged(FermentableAddition f, String sNewWeight) {

        double dWeight = Tools.ParseDouble(sNewWeight);
        boolean bFermentableChanged = false;

        for (FermentableAddition fa : CurrentRecipe.fermentables) {
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

    public HopAddition hopChanged(HopAddition ha, String sTime, String sAmount, String sAAU, String sType) {

        int dTime = Tools.ParseInt(sTime);
        double dAmount = Tools.ParseDouble(sAmount);
        double dAAU = Tools.ParseDouble(sAAU);

        boolean bHopChanged = false;

        for (HopAddition h : CurrentRecipe.hops) {
            if (h.additionGuid.equals(ha.additionGuid)) {
                bHopChanged = h.time != dTime || h.amount != dAmount || h.hop.aau != dAAU || ! h.type.equals(sType);

                h.time = dTime;
                h.amount = dAmount;
                h.hop.aau = dAAU;
                h.type = sType;

                break;
            }
        }

        if (bHopChanged)
            RecalculateStats();

        return ha;
    }


    public @NotNull AdjunctAddition adjunctChanged(AdjunctAddition aa, String sAmount, String sAmountUnit, String sTime, String sTimeUnit) {
        double dAmount = Tools.ParseDouble(sAmount);
        int iTime = Tools.ParseInt(sTime);
        AdjunctAddition aaReturn = null;

        for (AdjunctAddition a : CurrentRecipe.adjuncts) {
            if (a.additionGuid.equals(aa.additionGuid)) {
                a.amount = dAmount;
                a.unit = sAmountUnit;
                a.time = iTime;
                a.timeUnit = sTimeUnit;
                aaReturn = a;

                break;
            }
        }

        if (aaReturn == null)
            aaReturn = new AdjunctAddition();

        return aaReturn;
    }

    private void RecalculateStats() {
        m_repo.CalculateRecipeStats(CurrentRecipe, new RepositoryCallback() {
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
                m_ActivityView.ShowToast(Tools.ParseVolleyError(error));
            }
        });
    }

    public void AddIngredient(ListableObject i, Recipe.IngredientType ingredientType) {
        if (ingredientType == null) {
            //changing style - i'm not sure if i like this or not, so we'll see
            changeStyle((Style)i);

            return;
        }

        AddIngredient(i, ingredientType, -1);
    }


    public void AddIngredient(ListableObject i, Recipe.IngredientType ingredientType, int position) {

        switch (ingredientType) {
            case Fermntable:
                FermentableAddition fa;
                if (i instanceof Fermentable)
                    fa = new FermentableAddition((Fermentable)i);
                else
                    fa = (FermentableAddition)i;

                AddIngredient(CurrentRecipe.fermentables, fa, position);
                view.AddFermentable(fa);
                break;

            case Hop:
                HopAddition ha;
                if (i instanceof Hop)
                    ha = new HopAddition((Hop)i);
                else
                    ha = (HopAddition)i;

                AddIngredient(CurrentRecipe.hops, ha, position);
                view.AddHop(ha);
                break;

            case Yeast:
                YeastAddition ya;
                if (i instanceof Yeast)
                    ya = new YeastAddition((Yeast)i);
                else
                    ya = (YeastAddition)i;
                AddIngredient(CurrentRecipe.yeasts, ya, position);
                view.AddYeast(ya);
                break;
            case Adjunct:
                AdjunctAddition aa;
                if (i instanceof Adjunct)
                    aa = new AdjunctAddition((Adjunct)i);
                else
                    aa = (AdjunctAddition)i;
                AddIngredient(CurrentRecipe.adjuncts, aa, position);
                view.AddAdjunct(aa);
                break;
        }

    }

    public void DeleteIngredient(ListableObject i, Recipe.IngredientType ingredientType) {
        IngredientAddition ia = (IngredientAddition)i;

        switch (ingredientType) {
            case Fermntable:
                DeleteIngredient(CurrentRecipe.fermentables, ia);
                break;
            case Hop:
                DeleteIngredient(CurrentRecipe.hops, ia);
                break;
            case Yeast:
                DeleteIngredient(CurrentRecipe.yeasts, ia);
                break;
            case Adjunct:
                DeleteIngredient(CurrentRecipe.adjuncts, ia);
                break;
        }
    }


    private <T> void DeleteIngredient(ArrayList<T> list, IngredientAddition o) {
        for (int i = 0; i < list.size(); i++) {
            IngredientAddition lo = (IngredientAddition)list.get(i);

            if (lo.additionGuid.equals(o.additionGuid)) {
                list.remove(i);
            }
        }

        SaveRecipe();
    }

    private <T> void AddIngredient(ArrayList<T> list, ListableObject o, int position) {

        if (position == -1) {
            list.add((T)o);
        } else {
            list.add(position, (T)o);
        }

        SaveRecipe();


    }

    public void ShowAddDialog(Recipe.IngredientType ingredientType) {
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

    public void showChangeStylePrompt() {
        view.ShowAddDialog(m_alStyle, null);
    }


    private void changeStyle(Style i) {
        CurrentRecipe.style = i;
        view.SetStyle(i);
        view.SetStyleRanges(i);
        SaveRecipe();
    }

    public void LoadIngredientLists() {
        LoadAdjuncts();
    }

    private void LoadAdjuncts() {
        m_repo.LoadIngredientList("adjunct", new RepositoryCallback() {
            @Override
            public void onSuccess(String t) {
                Adjunct[] adjuncts = Tools.convertJsonResponseToObject(t, Adjunct[].class);
                for (Adjunct a : adjuncts) {
                    m_alAdjuncts.add(a);
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public android.view.View.OnFocusChangeListener createFocusChangeListener() {
        return new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(android.view.View v, boolean hasFocus) {
                if (!hasFocus) {
                    //user left the textbox, save the recipe
                    SaveRecipe();
                }
            }
        };
    }

    public interface View {
        void PopulateStats(RecipeStatistics recipeStats);
        void PopulateHops(ArrayList<HopAddition> hops);
        void PopulateYeasts(ArrayList<YeastAddition> yeasts);
        void PopulateFermentables(ArrayList<FermentableAddition> fermentables);

        void SetStyle(Style beerStyle);
        void SetStyleRanges(Style style);
        void ShowAddDialog(ArrayList data, Recipe.IngredientType ingredientType);
        void SetScreenReadOnly(boolean bEnabled);

        void AddFermentable(FermentableAddition fa);

        void AddHop(HopAddition ha);

        void AddYeast(YeastAddition ya);
        void AddAdjunct(AdjunctAddition aa);

        void SetRefreshing(boolean bRefeshing);

        void PopulateAdjuncts(ArrayList<AdjunctAddition> adjuncts);
    }

    public interface IActivityView {
        void FinishActivity(String sIDString, double dAbv, boolean bDeleted);
        void ShowToast(String sMessage);
        void SetTitle(String sTitle);
        void PromptDeletion();
        void SetTitleSRMColor(int iColor, int iDarkColor);
    }

    public interface IMashView {
        void PopulateParameters(RecipeParameters recipeParameters);
        void MashInfusionShowWaterToAdd(String t);
        void PopulateMashStats(RecipeStatistics stats);
    }
}
