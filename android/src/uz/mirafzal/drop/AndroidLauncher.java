package uz.mirafzal.drop;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//turning off accelerometer and compass as we do not use them
		config.useAccelerometer = false;
		config.useCompass = false;
		//starting class Drop.java
		initialize(new Drop(), config);
	}
}
