package uz.mirafzal.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

/**
 * Created by admin on 18.03.18.
 */

public class MyTextInputListener implements Input.TextInputListener {

    Preferences prefs;

    public MyTextInputListener(){
        prefs = Gdx.app.getPreferences("MyPreferences");
    }

    @Override
    public void input(String text) {
        prefs.putString("name", text);
        prefs.flush();
    }

    @Override
    public void canceled() {
        prefs.putBoolean("firstTime", true);
        prefs.flush();
    }
}
