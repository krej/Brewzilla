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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import beer.unaccpetable.brewzilla.Adapters.HopAdapter;
import beer.unaccpetable.brewzilla.Adapters.MaltAdapter;
import beer.unaccpetable.brewzilla.Adapters.YeastAdapter;
import beer.unaccpetable.brewzilla.Ingredients.Hop;
import beer.unaccpetable.brewzilla.Ingredients.Malt;
import beer.unaccpetable.brewzilla.Ingredients.Yeast;

public class NewRecipe extends AppCompatActivity {

    RecyclerView lstGrains, lstHops,lstYeasts;
    private RecyclerView.LayoutManager m_HopLayoutManager, m_YeastLayoutManager, m_MaltLayoutManager;
    private HopAdapter m_HopAdapter = new HopAdapter(R.layout.hop_list);
    private YeastAdapter m_YeastAdapter = new YeastAdapter(R.layout.hop_list);
    private MaltAdapter m_MaltAdapter = new MaltAdapter(R.layout.hop_list);

    private Boolean bShowExtraFab = false;
    View fabGrain;
    View fabHop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lstGrains = (RecyclerView) findViewById(R.id.listGrains);
        lstHops = (RecyclerView)findViewById(R.id.listHops);
        lstYeasts = (RecyclerView)findViewById(R.id.listYeast);


        fabGrain = findViewById(R.id.fabGrain);
        fabGrain.setVisibility(View.INVISIBLE);
        fabHop = findViewById(R.id.fabHop);
        fabHop.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SetExtraFABHideEvents();

        SetButtonClickEvents();

        SetUpHopList();
        SetUpYeastList();
        SetUpMaltList();
    }

    private void SetUpHopList() {
        lstHops.setHasFixedSize(false);
        m_HopLayoutManager = new LinearLayoutManager(this);
        lstHops.setLayoutManager(m_HopLayoutManager);
        lstHops.setAdapter(m_HopAdapter);
        //m_HopAdapter.add(new Hop("Citra", 1.5, 12, 20));
    }
    private void SetUpYeastList() {
        lstYeasts.setHasFixedSize(false);
        m_YeastLayoutManager = new LinearLayoutManager(this);
        lstYeasts.setLayoutManager(m_YeastLayoutManager);
        lstYeasts.setAdapter(m_YeastAdapter);
        //m_YeastAdapter.add(new Yeast("1056", "Wyeast", 6.66));
    }
    private void SetUpMaltList() {
        lstGrains.setHasFixedSize(false);
        m_MaltLayoutManager = new LinearLayoutManager(this);
        lstGrains.setLayoutManager(m_MaltLayoutManager);
        lstGrains.setAdapter(m_MaltAdapter);
        m_MaltAdapter.add(new Malt("2 Row", 15, 5, 16));
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
                //AddToList(valGrains, "New Grain", adptGrains);

            }
        });

        //Hop FAB
        FloatingActionButton fabHop = (FloatingActionButton) findViewById(R.id.fabHop);
        fabHop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddHop();
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
    }

    private void AddHop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewRecipe.this);
        builder.setView(R.layout.fragment_hop_dialog);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                /*Dialog f = (Dialog) dialog;
                EditText name = (EditText) f.findViewById(R.id.name);
                EditText amount = (EditText) f.findViewById(R.id.amount);
                EditText aau = (EditText) f.findViewById(R.id.aau);
                EditText time = (EditText) f.findViewById(R.id.time);

                String sName = name.getText().toString();
                double dAmount = Tools.ParseDouble(amount.getText().toString());
                double dAAU = Tools.ParseDouble(aau.getText().toString());
                int iTime = Tools.ParseInt(time.getText().toString());

                if (sName.length() == 0 || dAmount == 0 || dAAU == 0) {


                    CharSequence text = "Info Missing";
                    Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }

                Hop hop = new Hop(sName, dAmount, dAAU, iTime);
                m_HopAdapter.add(hop);*/
            }
        });



        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        final AlertDialog dialog = builder.create();
        /*dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        //Dismiss once everything is OK.
                        dialog.dismiss();
                    }
                });
            }
        });*/
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Dialog f = (Dialog) dialog;
                EditText name = (EditText) dialog.findViewById(R.id.name);
                EditText amount = (EditText) dialog.findViewById(R.id.amount);
                EditText aau = (EditText) dialog.findViewById(R.id.aau);
                EditText time = (EditText) dialog.findViewById(R.id.time);

                String sName = name.getText().toString();
                double dAmount = Tools.ParseDouble(amount.getText().toString());
                double dAAU = Tools.ParseDouble(aau.getText().toString());
                int iTime = Tools.ParseInt(time.getText().toString());

                if (sName.length() == 0 || dAmount == 0 || dAAU == 0) {


                    CharSequence text = "Info Missing";
                    Toast t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }

                Hop hop = new Hop(sName, dAmount, dAAU, iTime);
                m_HopAdapter.add(hop);
                dialog.dismiss();
            }
        });
    }

}
