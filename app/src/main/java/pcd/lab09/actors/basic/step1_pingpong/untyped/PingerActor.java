package pcd.lab09.actors.basic.step1_pingpong.untyped;

import akka.actor.*;
import pcd.lab09.actors.basic.step1_pingpong.untyped.PingerPongerProtocol.*;

public class PingerActor extends AbstractActor {

	public Receive createReceive() {
		return receiveBuilder()
				.match(BootMsg.class, this::onBootMsg)
				.match(PongMsg.class, this::onPongMsg)
	            .build();
	}

	private void onPongMsg(PongMsg msg) {
		log("got pong " + msg.count + " => ping " + (msg.count + 1));
		msg.ponger.tell(new PingMsg(msg.count + 1, this.getSelf()), this.getSelf());
	}

	private void onBootMsg(BootMsg msg) {
		log("booting.");
		msg.ponger.tell(new PingMsg(0, this.getSelf()), this.getSelf());
	}
	
	private void log(String msg) {
		System.out.println("[CounterUserActor] " + msg);
	}
	
}
