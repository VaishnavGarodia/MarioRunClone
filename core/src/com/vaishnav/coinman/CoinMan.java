package com.vaishnav.coinman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int pause =0;
	int manState = 0;
	float gravity= 0.3f;
	float velocity = 0;
	int manY= 0;
	int coinCount=0;
	ArrayList<Integer> coinXs;
	ArrayList<Integer> coinYs;
	Texture coin;
	int bombCount=0;
	ArrayList<Integer> bombXs;
	ArrayList<Integer> bombYs;
	Texture bomb;
	Random random;
	ArrayList<Rectangle> BombRectangle;
	ArrayList<Rectangle> CoinRectangle;
	Rectangle ManRectangle;
	int score = 0;
	BitmapFont font;
	int gamestate = 0;
	Texture dizzyMan;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight()/2;
		coin = new Texture("coin.png");
		coinXs=new ArrayList<Integer>();
		coinYs=new ArrayList<Integer>();
		bomb = new Texture("bomb.png");
		bombXs=new ArrayList<Integer>();
		bombYs=new ArrayList<Integer>();
		random=new Random();
		CoinRectangle = new ArrayList<Rectangle>();
		BombRectangle = new ArrayList<>();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		dizzyMan = new Texture("dizzy-1.png");
		font.getData().setScale(10);
	}

	public void makeCoin(){
		float y= random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)y);
		coinXs.add(Gdx.graphics.getWidth());
	}
	public void makeBomb(){
		float y= random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)y);
		bombXs.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gamestate==0){
			//waiting to start
			batch.draw(man[manState],Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2,manY);
			if(Gdx.input.justTouched()){
				gamestate=1;
			}
		}else if(gamestate == 1){
			//game running
			if(coinCount<80){
				coinCount++;
			}else{
				coinCount = 0;
				makeCoin();
			}
			if(bombCount<160){
				bombCount++;
			}else{
				bombCount = 0;
				makeBomb();
			}
			CoinRectangle.clear();
			for(int i=0; i<coinXs.size(); i++) {
				batch.draw(coin, coinXs.get(i),coinYs.get(i));
				coinXs.set(i, coinXs.get(i)-6);
				CoinRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}
			BombRectangle.clear();
			for(int i=0; i<bombXs.size(); i++) {
				batch.draw(bomb, bombXs.get(i),bombYs.get(i));
				bombXs.set(i, bombXs.get(i)-6);
				BombRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i), bomb.getWidth(),bomb.getHeight()));
			}
			ManRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2,manY,man[manState].getWidth(),man[manState].getHeight());
			if(pause < 6){
				pause++;
			}else{
				pause=0;
				if(manState<3) {
					manState++;
				}else{
					manState=0;
				}
			}
			if(Gdx.input.justTouched()){
				velocity=-11;
			}
			velocity+=gravity;
			manY-=velocity;
			if(manY<=0){
				manY=0;
			}

			batch.draw(man[manState],Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2,manY);
			for(int i=0;i<CoinRectangle.size();i++){
				if(Intersector.overlaps(ManRectangle,CoinRectangle.get(i))){
					score++;
					CoinRectangle.remove(i);
					coinXs.remove(i);
					coinYs.remove(i);
				}
			}
			for(int i=0;i<BombRectangle.size();i++){
				if(Intersector.overlaps(ManRectangle,BombRectangle.get(i))){
					Gdx.app.log("info","Bomb touched");
					gamestate =2;
				}
			}
			font.draw(batch,String.valueOf(score),100,200);
		}else if(gamestate==2){
			//game ended
			batch.draw(dizzyMan,Gdx.graphics.getWidth()/2 - dizzyMan.getWidth()/2,manY);
			font.draw(batch,String.valueOf(score),100,200);
			if(Gdx.input.justTouched()){
				gamestate=1;
				score=0;
				coinXs.clear();
				coinYs.clear();
				coinCount=0;
				CoinRectangle.clear();
				bombXs.clear();
				bombYs.clear();
				bombCount=0;
				BombRectangle.clear();
		}
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
