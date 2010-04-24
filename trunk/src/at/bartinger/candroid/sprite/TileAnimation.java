package at.bartinger.candroid.sprite;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;


public class TileAnimation extends Sprite{

	/**
	 * @author Dominic Bartl
	 */


	public Sprite fullImage;//the source image

	private ArrayList<Bitmap> imagelist = new ArrayList<Bitmap>();//list of all images

	private int currentFrameCounter =0;//number of the current frame
	private int frameskipdelay; // time between every frame of the animation

	//number of times how often the image gets cut
	private int tileColumns;
	private int tileRows;

	//true if the animation is running
	public boolean isStarted = true;

	private long pasteTime = 0;
	private long lastTime;



	public TileAnimation(Context context, int x, int y, String assetPath, int tileColumns, int tileRows, int frameskipdelay) {
		super(context, assetPath, x, y);

		this.frameskipdelay = frameskipdelay;
		this.tileRows = tileRows;
		this.tileColumns = tileColumns;
		fullImage = new Sprite(context,assetPath, 0, 0);
		cutImage();
		this.sprite = imagelist.get(0);
		lastTime = System.currentTimeMillis();
	}


	public TileAnimation(Context context, int x, int y, String assetPath, int[] sections, int frameskipdelay) {
		super(context, assetPath, x, y);

		this.frameskipdelay = frameskipdelay;
		//this.tiles = sections.length;
		fullImage = new Sprite(context,assetPath, 0, 0);
		cutImage();
		this.sprite = imagelist.get(0);
		lastTime = System.currentTimeMillis();
	}


	public TileAnimation(Context context, int x, int y, String[] assetPaths, int frameskipdelay) {
		super(context, assetPaths[0], x, y);

		this.frameskipdelay = frameskipdelay;
		fullImage = null;
		//tiles = assetPaths.length;
		for(int i = 0; i < assetPaths.length; i++){
			imagelist.add(new Sprite(context, assetPaths[i], x, y).sprite);
		}
		this.sprite = imagelist.get(0);
		lastTime = System.currentTimeMillis();
	}


	private void cutImage(){
		int cx = 0;
		int cy = 0; 
		int stepX = fullImage.width/tileColumns;
		int stepY = fullImage.height/tileRows;
		for(int i = 1; i <= tileRows; i++){
			for(int j = 0; j < tileColumns; j++){
				if(cx+stepX <= fullImage.width){
					imagelist.add(Bitmap.createBitmap(fullImage.sprite, cx, cy, stepX, stepY));
					cx+=stepX; 
				}
			}
			cx=0;
			if(cy+stepY <= fullImage.height){
				cy+=stepY; 
			}

		}
	}

	//	private void cutImage(int [] sections){
	//		int x = 0;
	//		int y = 0; 
	//		int stepX = fullImage.width/tiles;
	//		for(int i = 1; i <= tiles; i++){
	//			if(x+stepX <= fullImage.width){
	//				imagelist.add(Bitmap.createBitmap(fullImage.sprite, x, y, stepX, fullImage.height));
	//				x+=stepX; 
	//			}
	//		}
	//	}

	@Override
	public void recycle() {
		for(Bitmap b : imagelist){
			b.recycle();
		}
		imagelist = null;
		fullImage.recycle();
		super.recycle();
	}

	public void update(){
		if(isStarted){
			long deltaTime = System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();
			pasteTime+=deltaTime;
			if(pasteTime >= frameskipdelay){
				pasteTime = 0;
				nextFrame();
			}
		}

	}

	public void nextFrame(){
		if(currentFrameCounter < imagelist.size()-1){
			currentFrameCounter++;
		}else{
			currentFrameCounter=0;
		}
		sprite = imagelist.get(currentFrameCounter);
	}

	public void start(){
		isStarted=true;
	}

	public void stop(){
		isStarted=false;
	}


}