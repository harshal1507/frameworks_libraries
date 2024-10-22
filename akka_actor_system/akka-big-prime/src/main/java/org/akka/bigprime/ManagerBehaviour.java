package org.akka.bigprime;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ManagerBehaviour extends AbstractBehavior<String> {


    private ManagerBehaviour(ActorContext<String> context) {
        super(context);
    }

    public static Behavior<String> create(){
        return Behaviors.setup(ManagerBehaviour::new);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("start", () -> {
                    for (int i = 0; i < 20; i++) {
                        ActorRef<WorkerBehaviour.Command> worker = getContext().spawn(WorkerBehaviour.create(), "worker"+i);
                        // 2nd parameter an ActorRef - a pointer to the Manager
                        worker.tell(new WorkerBehaviour.Command("start", getContext().getSelf()));
                    }
                    return this;
                })
                .build();
    }
}
