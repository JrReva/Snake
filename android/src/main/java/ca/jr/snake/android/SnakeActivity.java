package ca.jr.snake.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import ca.jr.snake.core.Snake;

public class SnakeActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("ca/jr/snake/resources");
    PlayN.run(new Snake(true));
  }
}
