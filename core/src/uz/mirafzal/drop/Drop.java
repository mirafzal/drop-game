package uz.mirafzal.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by admin on 28.06.17.
 */

public class Drop extends Game {

    SpriteBatch batch;
    BitmapFont font;
    //Music monodyMusic;

    @Override
    public void create() {
        //initializing variables
        batch = new SpriteBatch();
        font = new BitmapFont();
        //setting background music
        //monodyMusic = Gdx.audio.newMusic(Gdx.files.internal("monody.mp3"));
        //starting class MainMenuScreen.java
        this.setScreen(new MainMenuScreen(this, true));

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        //disposing
        super.dispose();
        batch.dispose();
        font.dispose();
    }
}
