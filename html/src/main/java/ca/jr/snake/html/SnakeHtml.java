package ca.jr.snake.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import ca.jr.snake.core.Snake;

public class SnakeHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("snake/");
    PlayN.run(new Snake(true));
  }
}
