package uz.mirafzal.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {

	final Drop game;
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture dropImage;
	Texture bucketImage;
	Texture healthImage;
	Sound dropSound;
	Rectangle bucket;
	Rectangle health;
	Vector3 touchPos;
	Array<Rectangle> raindropsArray;
	long lastDropTime;
	int dropsCatched;
	Array<Rectangle> healthsArray;
	float speedRaindrop;
	float speedRaindropsSpawn;
	boolean firstTime;
	int getDropsCatched;
	Texture backgroundImage;
	Preferences prefs;
	int highscore;
	boolean vibrateOn;


	public GameScreen (final Drop gam) {
		this.game = gam;
		//the speed of raindrops
		speedRaindrop = 1;
		//the speed of spawning raindrops
		speedRaindropsSpawn = 1;
		firstTime = true;

		prefs = Gdx.app.getPreferences("MyPreferences");
		highscore = prefs.getInteger("highscore", 0);
		vibrateOn = prefs.getBoolean("vibrateOn",true);

		batch = new SpriteBatch();

		touchPos = new Vector3();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		//setting textures
		dropImage = new Texture("droplet.png");
		bucketImage = new Texture("bucket.png");
		healthImage = new Texture("heart.png");
		backgroundImage = new Texture("game_back.jpg");
		//initializing the drop sound
		dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
		//initializing the bucket
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;
		//creating an array of raindrops
 		raindropsArray = new Array<Rectangle>();
		//creating an array of health points
		healthsArray = new Array<Rectangle>();
		//creating the first raindrop
		spawnRainDrop();
		//creating 3 healthPoints with distance 80 points
		for (int x = 0; x < 80 * 3; x+=80){
			createHealts(x, 0);
		}
	}

	private void createHealts(int x, int y){
		health = new Rectangle();
		health.x = 800 - 64 - 5 - x;
		health.y = 480 - 64 - 5 - y;
		health.width = 64;
		health.height = 64;
		healthsArray.add(health);
	}

	private void spawnRainDrop(){
		Rectangle rainDrop = new Rectangle();
		//setting the coordinate x randomly-=
		rainDrop.x = MathUtils.random(0, 800 - 64);
		rainDrop.y = 480;
		rainDrop.width = 64;
		rainDrop.height = 64;
		raindropsArray.add(rainDrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render (float delta) {
		//clearing the screen
		Gdx.gl.glClearColor(0,0,2f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		//game.font.getData().setScale(1.8f);
		//game.font.draw(game.batch, "by @AndroidDevUz (Telegram)",425,30);
		game.font.getData().setScale(2);
		game.batch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.font.draw(game.batch, "Tomchi yig'ildi: " + dropsCatched + "\n" + "Rekord: " + highscore + "\n" +
				"Tezlik: "+(int)Math.floor(speedRaindrop)+"."+(int)Math.floor(speedRaindrop * 10) % 10 + "x", 10, 480 - 10);
		game.batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle health: healthsArray){
			game.batch.draw(healthImage, health.x, health.y);
		}
		for (Rectangle raindrop : raindropsArray){
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		game.batch.end();

		if (Gdx.input.isTouched()){
			touchPos.set(Gdx.input.getX(),Gdx.input.getY(),0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64/2;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) bucket.x = bucket.x - 600 * Gdx.graphics.getDeltaTime();

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) bucket.x = bucket.x + 600 * Gdx.graphics.getDeltaTime();

		if (bucket.x < 0 ) bucket.x = 0;

		if (bucket.x > 800 - 64) bucket.x = 800 - 64;

		if (TimeUtils.nanoTime() - lastDropTime > 1000000000 * speedRaindropsSpawn) spawnRainDrop();

		Iterator<Rectangle> iterDrops = raindropsArray.iterator();
		while (iterDrops.hasNext()){
			Rectangle raindrop = iterDrops.next();
			raindrop.y = raindrop.y - 200 * Gdx.graphics.getDeltaTime() * speedRaindrop;
			if (raindrop.y + 64 < 0) {
				iterDrops.remove();
				if (healthsArray.size > 0) {
					healthsArray.removeIndex(healthsArray.size - 1);
					if (vibrateOn) {Gdx.input.vibrate(500);}
					if (healthsArray.size == 0) {
						if(vibrateOn) {Gdx.input.vibrate(1000);}
						game.setScreen(new GameOverScreen(game, dropsCatched));
						dispose();
					}
				}
			}
			if (raindrop.overlaps(bucket)){
				dropsCatched++;
				dropSound.play();
				iterDrops.remove();

				if (speedRaindrop < 2.5){
					speedRaindropsSpawn -= 0.005000000;
					speedRaindrop += 0.01;
				}
				getDropsCatched = dropsCatched;
			}
		}

		if (dropsCatched > highscore) {
			highscore = dropsCatched;
		}

		/*if (getDropsCatched % 10 == 0 && !firstTime) {
			speedRaindrop += 0.1;
			speedRaindropsSpawn -= 0.010000000;
			getDropsCatched++;
		}

		if (dropsCatched == 10) firstTime = false;
		*/


	}

	@Override
	public void show() {

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
	public void dispose () {
		batch.dispose();
		dropSound.dispose();
		dropImage.dispose();
		bucketImage.dispose();
		backgroundImage.dispose();
	}


}
