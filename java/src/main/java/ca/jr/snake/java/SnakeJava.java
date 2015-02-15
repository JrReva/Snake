package ca.jr.snake.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import ca.jr.snake.core.Snake;

public class SnakeJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("ca/jr/snake/resources");
    PlayN.run(new Snake(true));
  }
}
