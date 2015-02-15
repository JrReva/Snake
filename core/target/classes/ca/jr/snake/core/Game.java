package ca.jr.snake.core;

import java.util.LinkedList;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Font.Style;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.TextFormat;
import pythagoras.i.Point;

public class Game {

	private int scoreCounter;
	private int score;
	private int highScore = 0;
	
	private int borderColor = Color.rgb(255, 255, 255);
	private int snakeColor = Color.rgb(0, 0, 255);
	private int candyColor = Color.rgb(255, 0, 0);
	
	private int toGenerate;

	private int tileSize;
	
	private int width;
	private int height;

	private int nbTileHorizontal;
	private int nbTileVertical;
	
	private int borderSize = 4;
	
	private boolean refresh = true;

	private CanvasImage canvas;
	private CanvasImage pointsCanvas;

	private LinkedList<Point> candies = new LinkedList<Point>();
	private LinkedList<Point> snake = new LinkedList<Point>();

	private int direction; // 0 = Right 1 = Down 2 = Left 3 = Up
	private TextFormat format;

	public Game(int nbTileHorizontal, int nbTileVertical, int width, int height, GroupLayer groupLayer) {
		this.nbTileHorizontal = nbTileHorizontal;
		this.nbTileVertical = nbTileVertical;

		tileSize = Math.min((width - borderSize * 2) / nbTileHorizontal, (height - 40 - borderSize * 2) / nbTileVertical);
		
		this.width = tileSize * nbTileHorizontal + borderSize * 2;
		this.height = tileSize * nbTileVertical + borderSize * 2;

		canvas = PlayN.graphics().createImage(this.width, this.height);
		pointsCanvas = PlayN.graphics().createImage(this.width, 30);
		
		ImageLayer layer = PlayN.graphics().createImageLayer();
		layer.setImage(canvas);
		layer.setTranslation((width - this.width) / 2, (height - this.height + 30) / 2);
		groupLayer.add(layer);
		
		
		layer = PlayN.graphics().createImageLayer();
		layer.setImage(pointsCanvas);
		layer.setTranslation((width - this.width) / 2, 5);
		groupLayer.add(layer);
		
		format = new TextFormat().withFont(PlayN.graphics().createFont("Arial", Style.BOLD, 20));
		
		start();
	}
	
	public void start() {
		refresh = true;
		
		toGenerate = 3;
		
		score = 0;
		scoreCounter = 100;
		direction = 0;
		
		snake.clear();
		candies.clear();
		
		snake.add(new Point(0, 0));
		generateCandy();
	}

	public void generateCandy() {
		Point point = null;

		do {
			point = new Point((int) (PlayN.random() * nbTileHorizontal),
					(int) (PlayN.random() * nbTileVertical));
			
			if(snake.contains(point) || snake.contains(point))
				point = null;
		} while (point == null);

		candies.add(point);
	}

	public void update() {
		Point current = snake.getFirst().clone();
		
		switch (direction) {
		case 0:
			current.x += 1;
			break;
		case 1:
			current.y += 1;
			break;
		case 2:
			current.x -= 1;
			break;
		case 3:
			current.y -= 1;
			break;
		}
		
		if(current.x < 0 || current.y < 0 || current.x >= nbTileHorizontal || current.y >= nbTileVertical || snake.contains(current)) {
			gameover();
			return;
		}
		
		if(candies.contains(current)) {
			candyGet(current);
		}
		
		snake.addFirst(current);
		
		if(toGenerate > 0) {
			--toGenerate;
		} else {
			snake.removeLast();
		}
		
		refresh = true;
		--scoreCounter;
	}
	
	public void paint() {
		if(refresh) {
			Canvas canvas = pointsCanvas.canvas();
			canvas.clear();
			
			canvas.setFillColor(borderColor);
			
			canvas.fillText(PlayN.graphics().layoutText("Score: " + score, format), 0, 0);
			canvas.fillText(PlayN.graphics().layoutText("HighScore: "+highScore, format), 160, 0);
			
			canvas = this.canvas.canvas();
			canvas.clear();
			
			canvas.setStrokeColor(borderColor);
			canvas.setStrokeWidth(borderSize);
			
			canvas.strokeRect(0, 0, width, height);
	
			canvas.setFillColor(candyColor);
			
			for(Point p : candies) {
				canvas.fillRect(tileSize * p.x + borderSize, tileSize * p.y + borderSize, tileSize, tileSize);
			}
			
			canvas.setFillColor(snakeColor);
			
			for(Point p : snake) {
				canvas.fillRect(tileSize * p.x + borderSize, tileSize * p.y + borderSize, tileSize, tileSize);
			}
			
			refresh = false;
		}
	}
	
	public void candyGet(Point point) {
		candies.remove(point);
		generateCandy();
		
		toGenerate += 3;
		
		score += Math.max(scoreCounter * snake.size(), 10);
		scoreCounter = 100;
		
		if(score > highScore)
			highScore = score;
	}

	private void gameover() {
		start();
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
}
