package ca.jr.snake.core;

import static playn.core.PlayN.*;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Game;
import playn.core.ImageLayer;
import playn.core.Keyboard;
import playn.core.Mouse;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import playn.core.Mouse.WheelEvent;
import playn.core.Pointer;
import pythagoras.f.Vector;

public class Snake implements Game {
	private ca.jr.snake.core.Game game;
	private int speed = 120;
	private boolean showController;
	private CanvasImage controller;
	
	private int controllerX;
	private int controllerY;
	
	private int controlerWidth = 120;
	private int controlerHeight = 120;
	
	private boolean refresh = true;
	Vector vector = new Vector();

	public Snake(boolean showController) {
		this.showController = showController;
	}
	
	@Override
	public void init() {
		graphics().setSize(graphics().screenWidth() - 5, graphics().screenHeight() - 5);
		
		int maxW = graphics().width();
		
		if(showController) {
			maxW -= controlerWidth + 60;
			
			controllerX = graphics().width() - (controlerWidth / 2 + 30);
			controllerY = graphics().height() - (controlerHeight / 2 + 50);
			
			controller = graphics().createImage(controlerWidth, controlerHeight);
			
			ImageLayer layer = graphics().createImageLayer(controller);
			layer.setTranslation(controllerX - controlerWidth / 2, controllerY - controlerHeight / 2);
			
			graphics().rootLayer().add(layer);
			
			mouse().setListener(new Mouse.Listener() {
				
				boolean pressed = false;
				
				@Override
				public void onMouseWheelScroll(WheelEvent event) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onMouseUp(ButtonEvent event) {
					if(event.button() == Mouse.BUTTON_LEFT) {
						pressed = false;
						vector.set(0, 0);
						refresh = true;
					}
				}
				
				@Override
				public void onMouseMove(MotionEvent event) {
					if(pressed)
						setMoveVector((int)event.x(), (int)event.y());
				}
				
				@Override
				public void onMouseDown(ButtonEvent event) {
					if(event.button() == Mouse.BUTTON_LEFT) {
						pressed = true;
						setMoveVector((int)event.x(), (int)event.y());
					}
				}
			});
			
			pointer().setListener(new Pointer.Listener() {
				
				@Override
				public void onPointerStart(playn.core.Pointer.Event event) {
					setMoveVector((int)event.x(), (int)event.y());
				}
				
				@Override
				public void onPointerEnd(playn.core.Pointer.Event event) {
					vector.set(0, 0);
					refresh = true;
				}
				
				@Override
				public void onPointerDrag(playn.core.Pointer.Event event) {
					setMoveVector((int)event.x(), (int)event.y());
				}
			});
		}
		
		game = new ca.jr.snake.core.Game(30, 20, maxW, graphics().height(), graphics().rootLayer());
		
		keyboard().setListener(new Keyboard.Listener() {
			
			@Override
			public void onKeyUp(Event event) {
			}
			
			@Override
			public void onKeyTyped(TypedEvent event) {
			}
			
			@Override
			public void onKeyDown(Event event) {
				switch(event.key()) {
				case RIGHT:
					game.setDirection(0);
					break;
				case DOWN:
					game.setDirection(1);
					break;
				case LEFT:
					game.setDirection(2);
					break;
				case UP:
					game.setDirection(3);
					break;
				default:
					break;
				}
			}
		});
	}
	
	public void setMoveVector(int x, int y) {
		vector.set(x - controllerX, y - controllerY);
		if(vector.length() > 15)
			vector.setLength(15);
		
		int direction = (int)Math.round(vector.angle() / (Math.PI / 2));
		if(direction < 0)
			direction += 4;
		
		game.setDirection(direction);
		refresh = true;
	}

	@Override
	public void paint(float alpha) {
		if(refresh && showController) {
			Canvas c = controller.canvas();
			c.clear();
			
			c.setStrokeWidth(4);
			c.setStrokeColor(Color.rgb(255, 255, 255));
			c.strokeCircle(controlerWidth / 2, controlerHeight / 2, controlerWidth / 2 - 2);
			
			c.setStrokeWidth(6);
			c.setStrokeColor(Color.rgb(120, 120, 120));
			c.setFillColor(Color.rgb(170, 170, 170));
			c.fillCircle(controlerWidth / 2 + vector.x, controlerHeight / 2 + vector.y, controlerWidth / 2 - 20);
			c.strokeCircle(controlerWidth / 2 + vector.x, controlerHeight / 2 + vector.y, controlerWidth / 2 - 20);
			
			refresh = false;
		}
		
		game.paint();
	}

	@Override
	public void update(float delta) {
		game.update();
	}

	@Override
	public int updateRate() {
		return speed;
	}
}
