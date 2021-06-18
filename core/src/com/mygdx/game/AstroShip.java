package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

public class AstroShip extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	float gravity = 0.5f;
	float velocity = 0;
	BitmapFont font;
	int gameState = 0;
	int score=0;

	Texture plane;
	float planeY =0;
	Rectangle planeShapeX;
	Rectangle planeShapeY;

	Random random;
	ArrayList<Integer> coinX = new ArrayList<Integer>();
	ArrayList<Integer> coinY = new ArrayList<Integer>();
	ArrayList<Integer> asteroidX = new ArrayList<Integer>();
	ArrayList<Integer> asteroidY = new ArrayList<Integer>();
	ArrayList<Integer> meteorX = new ArrayList<Integer>();
	ArrayList<Integer> meteorY = new ArrayList<Integer>();
	ArrayList<Rectangle> coinShape = new ArrayList<Rectangle>();
	ArrayList<Rectangle> asteroidShape = new ArrayList<Rectangle>();
	ArrayList<Rectangle> meteorShape = new ArrayList<Rectangle>();

	Texture coin;
	Texture asteroid;
	Texture meteor;
	Texture blast;
	int coinCount;
	int asteroidCount;
	int meteorCount;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");


		planeY = Gdx.graphics.getHeight();
		coin = new Texture("coin.png");
		asteroid = new Texture("asteroid.png");
		meteor = new Texture("meteor.png");
		blast = new Texture("blast.png");

		random = new Random();
		font = new BitmapFont();
		font.setColor(Color.YELLOW);
		font.getData().setScale(8);

		plane = new Texture("ap.png");
	}

	public void coins(){
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		coinY.add((int) height);
		coinX.add(Gdx.graphics.getWidth());

	}
	public void asteroids(){
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		asteroidY.add((int)height);
		asteroidX.add(Gdx.graphics.getWidth());
	}
	public void meteors(){
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		meteorY.add((int)height);
		meteorX.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState==1){
			if(coinCount<100){
				coinCount++;
			}else{
				coinCount=0;
				coins();
			}

			coinShape.clear();
			for(int i = 0; i< coinX.size(); i++){
				batch.draw(coin,coinX.get(i), coinY.get(i));
				coinX.set(i, coinX.get(i)-4);
				coinShape.add(new Rectangle(coinX.get(i), coinY.get(i), coin.getWidth(), coin.getHeight()));
			}

			if(asteroidCount<200){
				asteroidCount++;
			}else{
				asteroidCount=0;
				asteroids();
			}

			asteroidShape.clear();
			for(int i = 0; i< asteroidX.size(); i++){
				batch.draw(asteroid,asteroidX.get(i), asteroidY.get(i));
				asteroidX.set(i, asteroidX.get(i)-8);
				asteroidShape.add(new Rectangle(asteroidX.get(i), asteroidY.get(i), asteroid.getWidth(), asteroid.getHeight()));

			}

			if(meteorCount<300){
				meteorCount++;
			}else{
				meteorCount=0;
				meteors();
			}

			meteorShape.clear();
			for(int i = 0; i< meteorX.size(); i++){
				batch.draw(meteor,meteorX.get(i), meteorY.get(i));
				meteorX.set(i, meteorX.get(i)-10);
				meteorShape.add(new Rectangle(meteorX.get(i), meteorY.get(i), meteor.getWidth(), meteor.getHeight()));

			}
			if (Gdx.input.justTouched()) {
				velocity = -10;
			}

			velocity = velocity+gravity;
			planeY = planeY - velocity;

			if(planeY<=0){
				gameState=2;
			}

		}else if(gameState==0){
			if(Gdx.input.justTouched())
				gameState=1;
		}else if(gameState==2){
			velocity=0;

			if(Gdx.input.justTouched()){
				planeY = Gdx.graphics.getHeight()/2;
				gameState=1;
				score = 0;
				coinShape.clear();
				coinX.clear();
				coinY.clear();
				coinCount = 0;
				asteroidShape.clear();
				asteroidX.clear();
				asteroidY.clear();
				asteroidCount = 0;
				meteorShape.clear();
				meteorX.clear();
				meteorY.clear();
				meteorCount = 0;
			}
		}

		batch.draw(plane, Gdx.graphics.getWidth()/2-plane.getWidth(), planeY);
		if(gameState==2){
			batch.draw(blast, Gdx.graphics.getWidth()/2-plane.getWidth(), planeY);
		}



		planeShapeX = new Rectangle(Gdx.graphics.getWidth()/2- plane.getWidth(), (int) planeY+plane.getHeight()/2, plane.getWidth(), 10);

		for(int i =0; i< coinShape.size(); i++ ){
			if(Intersector.overlaps(planeShapeX, coinShape.get(i))){
				score++;
				coinShape.remove(i);
				coinX.remove(i);
				coinY.remove(i);
			}
		}
		for(int i =0; i< asteroidShape.size(); i++ ){
			if(Intersector.overlaps(planeShapeX, asteroidShape.get(i))){
				asteroidShape.remove(i);
				asteroidX.remove(i);
				asteroidY.remove(i);
				gameState=2;
			}
		}

		for(int i =0; i< meteorShape.size(); i++ ){
			if(Intersector.overlaps(planeShapeX, meteorShape.get(i))){
				meteorShape.remove(i);
				meteorX.remove(i);
				meteorY.remove(i);
				gameState=2;
			}
		}


		planeShapeY = new Rectangle(Gdx.graphics.getWidth()/2- plane.getWidth()/2, (int) planeY, 12, plane.getHeight());

		for(int i =0; i< coinShape.size(); i++ ){
			if(Intersector.overlaps(planeShapeY, coinShape.get(i))){
				score++;
				coinShape.remove(i);
				coinX.remove(i);
				coinY.remove(i);
			}
		}
		for(int i =0; i< asteroidShape.size(); i++ ){
			if(Intersector.overlaps(planeShapeY, asteroidShape.get(i))){
				asteroidShape.remove(i);
				asteroidX.remove(i);
				asteroidY.remove(i);
				gameState=2;
			}
		}

		for(int i =0; i< meteorShape.size(); i++ ){
			if(Intersector.overlaps(planeShapeY, meteorShape.get(i))){
				meteorShape.remove(i);
				meteorX.remove(i);
				meteorY.remove(i);
				gameState=2;
			}
		}

		font.draw(batch, String.valueOf(score),100,200);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();

	}
}
