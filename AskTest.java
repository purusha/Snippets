import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.Future;

import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Identify;
import akka.actor.Props;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;

public class AskTest {

  public static void main(String[] args) throws Exception{
    ActorSystem sys = ActorSystem.apply("test");
    sys.actorOf(Props.create(TestActor.class), "mytest");

    ActorSelection sel = sys.actorSelection("/user/mytest");

    Timeout t = new Timeout(5, TimeUnit.SECONDS);
    AskableActorSelection asker = new AskableActorSelection(sel);
    Future<Object> fut = asker.ask(new Identify(1), t);
    ActorIdentity ident = (ActorIdentity)Await.result(fut, t.duration());
    ActorRef ref = ident.getRef();
    System.out.println(ref == null);
  }
}
