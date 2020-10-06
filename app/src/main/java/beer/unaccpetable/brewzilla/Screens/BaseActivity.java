package beer.unaccpetable.brewzilla.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unacceptable.unacceptablelibrary.Tools.Network;
import com.unacceptable.unacceptablelibrary.Tools.Preferences;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import beer.unaccpetable.brewzilla.Screens.MainScreen.MainScreen;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InitialAppSetup();
    }


    private boolean InitialAppSetup() {
        Network.getInstance(getApplicationContext());
        Preferences.getInstance(getApplicationContext(), "beernet");

        if (!Preferences.ServerSettingExists()) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            Tools.ShowToast(getApplicationContext(), "Select a server to connect to.", Toast.LENGTH_LONG);

            return false;
        }

        if (!Tools.LoginTokenExists(this, MainScreen.class)) return false;
        return true;
    }
}
