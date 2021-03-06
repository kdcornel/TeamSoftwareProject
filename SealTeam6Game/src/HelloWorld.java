
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;

public class HelloWorld extends ApplicationAdapter {

	public static void main(String[] args) {
		createApplication();
	}

	private int[] platArr;
	private int[][] grid = Physics.tileGrid();
	String result;
	private String scoreboard = "Score: ";
	public Player player;
	
	public Enemy enemy1;
	public Enemy enemy2;
	public Enemy enemy3;
	public Enemy enemy4;
	public Enemy enemy5;
	public Enemy enemy6;
	public static int worldWidth = 1920;
	public static int worldHeight = 1000;
	private Music menuMusic;
	private SpriteBatch batchMain;
	private BitmapFont font;
	private Animation blackout;
	float elapsed_time = 0f;
	Physics py = new Physics();
	Levels lv = new Levels();
	int[] coinArr = lv.changeCoins();
	int pY = -20;
	int pSpeed = 1;
	Texture bkgTexture;
	TextureRegion region;
	
	Texture fireball1;
	Texture fireball2;
	Texture platform;
	Animation platformRegion;
	
	Texture heart;
	Texture broken;

	Texture coin;
	Animation coinRegion;
	//TODO remove if not needed for coin animation

	Texture bkgTexture1;
	public static TextureRegion region1;
	public static TextureRegion region2;
	public static TextureRegion region3;
	public static TextureRegion introFont;
	public static TextureRegion tutorialArrows;
	boolean init = false;
	int deathCount = 0;
	int killCount = 0;
	static LwjglApplication app;
	//private Batch batch1;

	//Boolean for JUnit tests
	public boolean testStatus = false;

	// Creates application
	public static LwjglApplicationConfiguration createApplication() {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = worldWidth;
		cfg.height = worldHeight;

		LwjglApplication app = new LwjglApplication(new HelloWorld(), cfg);
		return cfg;
	}

	@Override
	public void create() {
		batchMain = new SpriteBatch();
		font = new BitmapFont();
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Assets/Track2.wav"));
		menuMusic.setLooping(true);
		menuMusic.play();
		menuMusic.setVolume(0.05f);
		player = new Player();
		enemy1 = new Enemy(1);
		enemy2 = new Enemy(2);
		enemy3 = new Enemy(2);
		enemy4 = new Enemy(2);
		enemy5 = new Enemy(2);
		enemy6 = new Enemy(2);
		platArr = lv.changePlats();
		coinArr = lv.changeCoins();
		player.setPlats(platArr, platArr.length);
		player.setCoins(coinArr);
	}

	@Override
	public void dispose() {
		batchMain.dispose();
		font.dispose();
	}

	public void platformY() {
		if (pY == -20) {
			pSpeed = 1;
		} else if (pY == 100) {
			pSpeed = -1;
		}
		pY += pSpeed;
	}
	
	public void initialize() {
		Texture death = new Texture(Gdx.files.internal("Assets/Blackout.png"));
		Texture bkgTexture1 = new Texture(Gdx.files.internal("Assets/869.jpg"));
		Texture bkgTexture2 = new Texture(Gdx.files.internal("Assets/LowerResBkg2.jpg"));
		Texture bkgTexture3 = new Texture(Gdx.files.internal("Assets/spookyBackground.jpg"));
		Texture font1 = new Texture(Gdx.files.internal("Assets/IntroFont.png"));
		Texture arrows = new Texture(Gdx.files.internal("Assets/arrows.png"));
		
		lv.genCoins();
		region1 = new TextureRegion(bkgTexture1, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		region2 = new TextureRegion(bkgTexture2, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		region3 = new TextureRegion(bkgTexture3, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		introFont = new TextureRegion(font1, 0, 0, 1459, 91);
		tutorialArrows = new TextureRegion(arrows, 0, 0, 696, 564);
		blackout = new Animation(new TextureRegion(death), 19, 50);
		blackout.reset();
		
		// Sprite bkgSprite = new Sprite(bkgTexture);
		fireball1 = new Texture(Gdx.files.internal("Assets/fireball_0.png"));
		fireball2 = new Texture(Gdx.files.internal("Assets/fireball_1.png"));
		
		heart = new Texture(Gdx.files.internal("Assets/Heart_full.png"));
		broken = new Texture(Gdx.files.internal("Assets/Heart_empty.png"));
		platform = new Texture(Gdx.files.internal("Assets/platform2.png"));
		coin = new Texture(Gdx.files.internal("Assets/Coin.png"));
		coinRegion = new Animation(new TextureRegion(coin), 4, 35);

		//platformRegion = new Animation(new TextureRegion(platform), 36, 35);
		//TODO get coin animation working 
		
		enemy3.kill();
		enemy4.kill();
		enemy5.kill();
		enemy6.kill();
	}
	
	@Override
	public void render() {
		if (init == false) {
			initialize();
			init = true;
		}
		

		if(lv.currentScene==1){
			platform = new Texture(Gdx.files.internal("Assets/platform2.png"));
		}
		
		if(lv.currentScene==2){
			platform = new Texture(Gdx.files.internal("Assets/platform3.png"));
		}
		
		if(lv.currentScene==3){
			platform = new Texture(Gdx.files.internal("Assets/platform.png"));
		}
		
		
		
		
	
		
		

//		platArr = lv.changePlats();
//		coinArr = lv.changeCoins();

		if (player.isDead()){
			deathCount++;
			if (deathCount > 200){
				deathCount = 0;
				player.save();
				enemy1.reset();
				enemy1.eSwitch();
				enemy2.reset();
				enemy2.eSwitch();
				blackout.reset();
				enemy1.setPnts(0);
				enemy2.setPnts(0);
			}
		}
		player.cTp();
		player.getPlayerInput(py, testStatus, elapsed_time);
		player.damageImmunity();
		if (enemy1.getEvPcollision(player.x(), player.y(), player.isDead()) || enemy2.getEvPcollision(player.x(), player.y(), player.isDead())
				|| enemy3.getEvPcollision(player.x(), player.y(), player.isDead()) || enemy4.getEvPcollision(player.x(), player.y(), player.isDead())
						|| enemy5.getEvPcollision(player.x(), player.y(), player.isDead()) || enemy6.getEvPcollision(player.x(), player.y(), player.isDead())) {
			if ( !player.immune() ) {
				player.setHealth(player.health-1, true);
			}
		}
		
		if ( player.health == 0 ) {
			player.kill();
		}
		
		enemy1.getEvAcollision(Attack.curx, Attack.starty);
		if (!enemy1.pulse()){
			killCount++;
			if (killCount > 100){
				enemy1.reset();
				killCount = 0;
			}
		}
		float xyz = enemy1.getEnemyInput(player.x(), py, platArr);
		
		enemy2.getEvAcollision(Attack.curx, Attack.starty);
		if (!enemy2.pulse()){
			killCount++;
			if (killCount > 100){
				enemy2.reset();
				killCount = 0;
			}
		}
		
		float abc = enemy2.getEnemyInput(player.x(), py, platArr);
		if (Levels.currentScene == 3)
		{
			enemy3.getEvAcollision(Attack.curx, Attack.starty);
			if (!enemy3.pulse()){
				killCount++;
				if (killCount > 100){
					enemy3.reset();
					killCount = 0;
				}
			}
			enemy4.getEvAcollision(Attack.curx, Attack.starty);
			if (!enemy4.pulse()){
				killCount++;
				if (killCount > 100){
					enemy4.reset();
					killCount = 0;
				}
			}
			enemy5.getEvAcollision(Attack.curx, Attack.starty);
			if (!enemy5.pulse()){
				killCount++;
				if (killCount > 100){
					enemy5.reset();
					killCount = 0;
				}
			}
			enemy6.getEvAcollision(Attack.curx, Attack.starty);
			if (!enemy6.pulse()){
				killCount++;
				if (killCount > 100){
					enemy6.reset();
					killCount = 0;
				}
			}
		} else {
			enemy3.kill();
			enemy4.kill();
			enemy5.kill();
			enemy6.kill();
		}
		
		float bcd = enemy3.getEnemyInput(player.x(), py, platArr);
		float cde = enemy4.getEnemyInput(player.x(), py, platArr);
		float def = enemy5.getEnemyInput(player.x(), py, platArr);
		float efg = enemy6.getEnemyInput(player.x(), py, platArr);
		
		batchMain.begin();
		
		// Changes level on player location
		Player.playerX = lv.changeScene(batchMain, Player.playerX, pY, platform);
		platArr = lv.changePlats();
		coinArr = lv.changeCoins();
		player.setCoins(coinArr);
		player.setPlats(platArr, platArr.length);

		//Trying to draw coin animation here 

		if (!player.isDead()){
			for (int i = 0; i < platArr.length; i+=2){
				batchMain.draw(platform,  platArr[i]* platform.getWidth() * .25f, platArr[i+1] * platform.getHeight() * .5f, platform.getWidth() * .25f, platform.getHeight() * .5f);
			}
			

			coinRegion.update(0.5f);
			for (int i = 0; i < coinArr.length; i+=2){
				batchMain.draw(coinRegion.getFrame(), coinArr[i], coinArr[i+1]);
			}
			

			batchMain.draw(player.getTexture(), (int) player.x(), (int) player.y());
			
			for ( int i = 0; i < 5; i++) {
				if ( i == player.health-1 ) {
					batchMain.draw(heart, 10 + (50 * i), 785, heart.getWidth()*2f, heart.getWidth()*2f);
				}else if ( i < player.health-1 ) {
					batchMain.draw(heart, 10 + (50 * i), 800, heart.getWidth()*1.5f, heart.getWidth()*1.5f);
				} else {
					batchMain.draw(broken, 25 + (50 * i), 800, heart.getWidth()*1.5f, heart.getWidth()*1.5f);
				}
			}
		
		if (enemy1.pulse()) {
			if ( enemy1.getMarker() == 0 ) {
				batchMain.draw(enemy1.animate(abc), (int) enemy1.getX(), (int) enemy1.getY()-15);
			} else {
				batchMain.draw(enemy1.animate(xyz), (int) enemy1.getX(), (int) enemy1.getY()-5);
			}
		}
		if (enemy2.pulse()) {
			if ( enemy2.getMarker() == 0 ) {
				batchMain.draw(enemy2.animate(abc), (int) enemy2.getX(), (int) enemy2.getY()-15);
			} else {
				batchMain.draw(enemy2.animate(xyz), (int) enemy2.getX(), (int) enemy2.getY()-5);
			}
		}
		if (Levels.currentScene == 3)
		{
			if (enemy3.pulse()) {
				batchMain.draw(enemy3.animate(bcd), (int) enemy3.getX(), (int) enemy3.getY()-15);
			}
			if (enemy4.pulse()) {
				batchMain.draw(enemy4.animate(cde), (int) enemy4.getX(), (int) enemy4.getY()-15);
			}
			if (enemy5.pulse()) {
				batchMain.draw(enemy5.animate(def), (int) enemy5.getX(), (int) enemy5.getY()-15);
			}
			if (enemy6.pulse()) {
				batchMain.draw(enemy6.animate(efg), (int) enemy6.getX(), (int) enemy6.getY()-15);
			}
		}

	    if (Attack.attacking()) {
	        if (Attack.dir == 0) {
	            batchMain.draw(fireball2, (int) Attack.curx, (int) Attack.starty);
	        } else {
	            batchMain.draw(fireball1, (int) Attack.curx, (int) Attack.starty);
	        }
	    } 

	    } else if ( player.isDead() ){
	        if (blackout.count() < 18){
	            blackout.update(0.5f);
	            for (int i = 0; i < coinArr.length; i++){
	            	if (coinArr[i] > 2000){
	            		coinArr[i] -= 6900;
	            	}
	            }
	            
	        }
	        batchMain.draw(blackout.getFrame(), 0, 0, blackout.getFrame().getRegionWidth() * 2.7f, blackout.getFrame().getRegionHeight() * 2f);
	        result = scoreboard.concat(Integer.toString(enemy1.pnts()));
	        font.draw(batchMain, result, 950, 500);
	        lv.currentScene = 1;
		}
		
		//camera.update();
		
		batchMain.end();
	
	}

	public void drawSprite(TextureRegion region, TextureRegion textureRegion, int playerX2, int playerY2) {
	}
	
	@Override
	public void resize(int width, int height) {
	}
	
	public void setCoins(int i, int x){
		coinArr[i] = x;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}