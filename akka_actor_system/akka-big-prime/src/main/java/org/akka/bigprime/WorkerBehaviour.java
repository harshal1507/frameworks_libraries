package org.akka.bigprime;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

public class WorkerBehaviour extends AbstractBehavior<WorkerBehaviour.Command> {

    // static class so that we can use it in ManagerBehaviour class
    public static class Command implements Serializable {

        private static final long serialVersionUID = 1;
        private String message;
        private ActorRef<String> sender;

        // these objects should be immutable so that we have parameterised constructor and only getter methods
        public Command(String message, ActorRef<String> sender) {
            this.message = message;
            this.sender = sender;
        }

        public String getMessage() {
            return message;
        }

        public ActorRef<String> getSender() {
            return sender;
        }
    }

    private WorkerBehaviour(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
        return Behaviors.setup(WorkerBehaviour::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()

                .onAnyMessage(command -> {
                    if(command.getMessage().equals("start")){
                        BigInteger prime = new BigInteger(2000, new Random());
                        System.out.println(prime.nextProbablePrime());
                        System.out.println("prime calculated");
                    }
                    return this;
                })
                .build();
    }
}
