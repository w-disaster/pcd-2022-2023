package pcd.lab09.actors.basic.step1_pingpong.untyped;

import akka.actor.*;

public class RunActorSystem {
  public static void main(String[] args) throws Exception  {
	  
		final ActorSystem system = ActorSystem.create("my-actor-system");		
		final ActorRef bootActor =  system.actorOf(Props.create(BootActor.class), "boot-actor");
		bootActor.tell(new BootActor.BootMsg(), null);

  }
}
