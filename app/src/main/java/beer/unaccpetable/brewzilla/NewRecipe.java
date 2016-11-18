package beer.unaccpetable.brewzilla;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import beer.unaccpetable.brewzilla.Adapters.HopAdapter;
import beer.unaccpetable.brewzilla.Adapters.MaltAdapter;
import beer.unaccpetable.brewzilla.Adapters.YeastAdapter;
import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.Ingredient;
import beer.unaccpetable.brewzilla.Ingredients.Malt;
import beer.unaccpetable.brewzilla.Ingredients.Yeast;
import beer.unaccpetable.brewzilla.Tools.Calculations;
import beer.unaccpetable.brewzilla.Tools.Tools;

public class NewRecipe extends AppCompatActivity {

    RecyclerView lstGrains, lstHops,lstYeasts;
    private RecyclerView.LayoutManager m_HopLayoutManager, m_YeastLayoutManager, m_MaltLayoutManager;
    private HopAdapter m_HopAdapter = new HopAdapter(R.layout.hop_list);
    private YeastAdapter m_YeastAdapter = new YeastAdapter(R.layout.yeast_list);
    private MaltAdapter m_MaltAdapter = new MaltAdapter(R.layout.hop_list);

    private Boolean bShowExtraFab = false;
    View fabGrain,fabHop, fabYeast;

    private TextView txtIBU, txtOG, txtFG, txtABV, txtSRM;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lstGrains = (RecyclerView) findViewById(R.id.listGrains);
        lstHops = (RecyclerView)findViewById(R.id.listHops);
        lstYeasts = (RecyclerView)findViewById(R.id.listYeast);

        //stats card
        txtIBU = (TextView)findViewById(R.id.txtIBUs);
        txtOG = (TextView)findViewById(R.id.txtOG);
        txtFG = (TextView)findViewById(R.id.txtFG);
        txtABV = (TextView)findViewById(R.id.txtABV);
        txtSRM = (TextView)findViewById(R.id.txtSRM);

        fabGrain = findViewById(R.id.fabGrain);
        fabGrain.setVisibility(View.INVISIBLE);
        fabHop = findViewById(R.id.fabHop);
        fabHop.setVisibility(View.INVISIBLE);
        fabYeast = findViewById(R.id.fabYeast);
        fabYeast.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SetExtraFABHideEvents();

        SetButtonClickEvents();

        SetUpHopList();
        SetUpYeastList();
        SetUpMaltList();

        RefreshStats();
    }

    private void SetUpHopList() {
        lstHops.setHasFixedSize(false);
        m_HopLayoutManager = new LinearLayoutManager(this);
        lstHops.setLayoutManager(m_HopLayoutManager);
        lstHops.setAdapter(m_HopAdapter);
        m_HopAdapter.add(new Hop("Citra", 1.5, 13.65, 20));
    }
    private void SetUpYeastList() {
        lstYeasts.setHasFixedSize(false);
        m_YeastLayoutManager = new LinearLayoutManager(this);
        lstYeasts.setLayoutManager(m_YeastLayoutManager);
        lstYeasts.setAdapter(m_YeastAdapter);
        m_YeastAdapter.add(new Yeast("1056", "Wyeast", 75));
    }
    private void SetUpMaltList() {
        lstGrains.setHasFixedSize(false);
        m_MaltLayoutManager = new LinearLayoutManager(this);
        lstGrains.setLayoutManager(m_MaltLayoutManager);
        lstGrains.setAdapter(m_MaltAdapter);
        m_MaltAdapter.add(new Malt("2 Row", 10, 37, 1));
    }

    private void SetButtonClickEvents() {
        //Main FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*AddToList(valGrains, "Test");
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                if (!bShowExtraFab ) {
                    ShowExtraFAB();
                } else {
                    HideExtraFAB();
                }
                bShowExtraFab = !bShowExtraFab;

            }
        });

        //Grain FAB
        FloatingActionButton fabGrain = (FloatingActionButton) findViewById(R.id.fabGrain);
        fabGrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddIngredient("Malt");
            }
        });

        //Hop FAB
        FloatingActionButton fabHop = (FloatingActionButton) findViewById(R.id.fabHop);
        fabHop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddIngredient("Hop");
            }
        });

        //Yeast FAB
        FloatingActionButton fabYeast = (FloatingActionButton) findViewById(R.id.fabYeast);
        fabYeast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddIngredient("Yeast");
            }
        });
    }

    private void SetExtraFABHideEvents() {/*
        View screen = (View)findViewById(R.id.coord);
        screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                if (bShowExtraFab) {
                    HideExtraFAB();
                    bShowExtraFab = false;
                }
                return true;
            }
        });

        View toolbar = (View)findViewById(R.id.nestedscrollview);
        toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (bShowExtraFab) {
                    HideExtraFAB();
                    bShowExtraFab = false;
                }
                return true;
            }
        });*/
/*
        lstGrains.setOnTouchListener(new View.OnTouchListener() {
            @Override
            @CallSuper
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //super.onTouch(arg1);

                if (bShowExtraFab) {
                    HideExtraFAB();
                    bShowExtraFab = false;
                }
                return true;
            }
        });
*/

        /*
        lstHops.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (bShowExtraFab) {
                    HideExtraFAB();
                    bShowExtraFab = false;
                }
                return true;
            }
        });
        lstYeasts.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //super.onTouch(arg0, arg1);
                if (bShowExtraFab) {
                    HideExtraFAB();
                    bShowExtraFab = false;
                }
                return true;
            }
        });*/
    }

    private void ShowExtraFAB() {
        int cx = fabGrain.getWidth() / 2;
        int cy = fabGrain.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(fabGrain, cx, cy, 0, finalRadius);
        fabGrain.setVisibility(View.VISIBLE);
        anim.start();

        anim = ViewAnimationUtils.createCircularReveal(fabHop, cx, cy, 0, finalRadius);
        fabHop.setVisibility(View.VISIBLE);
        anim.start();

        anim = ViewAnimationUtils.createCircularReveal(fabYeast, cx, cy, 0, finalRadius);
        fabYeast.setVisibility(View.VISIBLE);
        anim.start();

        //FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        //fab.setImageIcon("@android:drawable/ic_input_add");

    }

    private void HideExtraFAB() {
        int cx = fabGrain.getWidth() / 2;
        int cy = fabGrain.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(fabGrain, cx, cy, finalRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fabGrain.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();

        anim = ViewAnimationUtils.createCircularReveal(fabHop, cx, cy, finalRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fabHop.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();

        anim = ViewAnimationUtils.createCircularReveal(fabYeast, cx, cy, finalRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fabYeast.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    private void AddIngredient(final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewRecipe.this);

        if (type.equals("Hop")) {
            builder.setView(R.layout.fragment_hop_dialog);
        } else if(type.equals("Malt")) {
            builder.setView(R.layout.fragment_malt_dialog);
        } else if(type.equals("Yeast")) {
            builder.setView(R.layout.fragment_yeast_dialog);
        }

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (type.equals("Hop")) {
                    if (!AddHop(dialog)) return;
                } else if (type.equals("Malt")) {
                    if (!AddMalt(dialog)) return;
                } else if (type.equals("Yeast")) {
                    if (!AddYeast(dialog)) return;
                }
                RefreshStats();
                dialog.dismiss();
            }
        });
    }

    private boolean AddHop(Dialog d) {
        EditText name = (EditText) d.findViewById(R.id.name);
        EditText amount = (EditText) d.findViewById(R.id.amount);
        EditText aau = (EditText) d.findViewById(R.id.aau);
        EditText time = (EditText) d.findViewById(R.id.time);

        String sName = name.getText().toString();
        double dAmount = Tools.ParseDouble(amount.getText().toString());
        double dAAU = Tools.ParseDouble(aau.getText().toString());
        int iTime = Tools.ParseInt(time.getText().toString());

        if (sName.length() == 0 || dAmount == 0 || dAAU == 0) {
            CharSequence text = "Info Missing";
            Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        Hop hop = new Hop(sName, dAmount, dAAU, iTime);
        m_HopAdapter.add(hop);
        return true;
    }

    private boolean AddMalt(Dialog d) {
        EditText name = (EditText) d.findViewById(R.id.name);
        EditText weight = (EditText) d.findViewById(R.id.weight);
        EditText ppg = (EditText) d.findViewById(R.id.ppg);
        EditText color = (EditText) d.findViewById(R.id.color);


        String sName = name.getText().toString();
        double dWeight = Tools.ParseDouble(weight.getText().toString());
        double dPPG = Tools.ParseDouble(ppg.getText().toString());
        int iColor = Tools.ParseInt(color.getText().toString());

        if (sName.length() == 0 || dWeight == 0 || dPPG == 0) {
            CharSequence text = "Info Missing";
            Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        Malt malt = new Malt(sName, dWeight, dPPG, iColor);
        m_MaltAdapter.add(malt);
        return true;
    }

    private boolean AddYeast(Dialog d) {

        EditText name = (EditText) d.findViewById(R.id.name);
        EditText lab = (EditText) d.findViewById(R.id.lab);
        EditText att = (EditText) d.findViewById(R.id.attenuation);


        String sName = name.getText().toString();
        String sLab = lab.getText().toString();
        double dAtt = Tools.ParseDouble(att.getText().toString());

        if (sName.length() == 0 || sLab.length() == 0 || dAtt == 0) {
            CharSequence text = "Info Missing";
            Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        Yeast yeast = new Yeast(sName, sLab, dAtt);
        m_YeastAdapter.add(yeast);

        return true;
    }

    private void RefreshStats() {
        int dIBU = Calculations.CalculateIBU(m_HopAdapter, m_MaltAdapter);
        double dOG = Calculations.CalculateOG(m_MaltAdapter);
        double dFG = Calculations.CalculateFG(m_MaltAdapter, m_YeastAdapter);
        double dABV = Calculations.CalculateABV(m_MaltAdapter, m_YeastAdapter);
        int dSRM = Calculations.CalculateSRM(m_MaltAdapter);

        txtIBU.setText("IBUs: " + dIBU);
        txtOG.setText("OG: " + dOG);
        txtFG.setText("FG: " + dFG);
        txtABV.setText("ABV: " + dABV + "%");
        txtSRM.setText("SRM: " + dSRM);
    }
}
