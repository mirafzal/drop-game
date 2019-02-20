package uz.mirafzal.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

/**
 * Created by admin on 28.06.17.
 */

public class GameOverScreen implements Screen{

    final Drop game;
    OrthographicCamera camera;
    final int score;
    Texture backgroundImage;
    Preferences prefs;

    public GameOverScreen(Drop game, int score) {
        this.game = game;
        this.score = score;
        //catching back button
        Gdx.input.setCatchBackKey(true);

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode) {
                //Are you sure you want to exit? dialog
                if (keycode == Input.Keys.BACK) {
                    showDialog();
                }

                return true;
            }
        });

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        backgroundImage = new Texture("game_back.jpg");
        prefs = Gdx.app.getPreferences("MyPreferences");

        if (score > prefs.getInteger("highscore")) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }
    }

    private void showDialog(){
        
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        game.batch.begin();
        game.batch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.font.draw(game.batch, "O'yin tugadi...\n\n" +
                "Tabriklaymiz! Siz "+score+" ta tomchi yig'dingiz!\n\n" +
                "Yana bir urinib ko'ring!", 800 / 2 - 250, 480 / 2 + 100);
        game.font.getData().setScale(1.8f);
        //game.font.draw(game.batch, "sinus.uz",425 + 275,30);
        game.batch.end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            game.setScreen(new GameScreen(game));
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