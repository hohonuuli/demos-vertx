package chapter2.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * HelloVerticle
 */
public class HelloVerticle extends AbstractVerticle {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private long counter = 1;

  @Override
  public void start() {
    vertx.setPeriodic(5000, id -> log.info("tick"));
    vertx.createHttpServer().requestHandler(req -> {
      log.info("Request #{} from {}", counter++, req.remoteAddress().host());
      req.response().end("Hello!");
    }).listen(9090);
    log.info("Open http://localhost:8080");
  }

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new HelloVerticle());
  }

}
