package uz.mirafzal.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by admin on 28.06.17.
 */

public class MainMenuScreen implements Screen {

    final Drop game;
    OrthographicCamera camera;
    Music backgroundMusic;
    Texture backgroundImage;

    MyTextInputListener listener;
    Preferences prefs;

    public MainMenuScreen(Drop game, boolean playMusic) {
        this.game = game;

        // setting camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        //setting background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("monody.mp3"));
        //setting background image
        backgroundImage = new Texture("game_back.jpg");
        //checking if music turned on in settings
        if (playMusic) {
            //playing music
            backgroundMusic.play();
            //setting looping
            backgroundMusic.setLooping(true);
        }

        prefs = Gdx.app.getPreferences("MyPreferences");

        if (prefs.getBoolean("firstTime", true)){
            listener = new MyTextInputListener();
            prefs.putBoolean("firstTime", false);
            prefs.flush();
            Gdx.input.getTextInput(listener, "Ismingizni yozing", "Player", "Ism");
        }

    }

    @Override
    public void show() {

    }


    //The main loop of the game
    @Override
    public void render(float delta) {
        //clearing the screen
        Gdx.gl.glClearColor(0, 0, 2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        //setting text size
        game.font.getData().setScale(1.8f);
        game.batch.setProjectionMatrix(camera.combined);
        //the start of drawing
        game.batch.begin();
        //drawing background image
        game.batch.draw(backgroundImage, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //showing the instruction
        game.font.draw(
                game.batch,
                "Salom " + prefs.getString("name") + "! Barcha tomchilarni tering!\n\n" +
                "Sizning 3 ta joningiz bor. Ular tugasa yutqazasiz.\n\n" +
                "O'yinni boshlash uchun ekranga bosing!" ,
                800 / 2 - 250, 480 / 2 + 75);
        //advertisement
        //game.font.draw(game.batch, "sinus.uz",425 + 275,30);
        //the end of drawing
        game.batch.end();

        //checking if screen touched
        if(Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            //starting game(launching GameScreen.java)
            game.setScreen(new GameScreen(game));
            //deleting needless resources
            dispose();
        }


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundImage.dispose();
    }
}
