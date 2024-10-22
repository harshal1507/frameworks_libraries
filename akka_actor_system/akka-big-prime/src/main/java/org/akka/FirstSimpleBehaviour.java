package org.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class FirstSimpleBehaviour extends AbstractBehavior<String> {

    // below 3 elements are the boilerplate code

    // constructor
    // make constructor private
    private FirstSimpleBehaviour(ActorContext<String> context) {
        super(context);
    }

    // using method reference
    public static Behavior<String> create(){
        return Behaviors.setup(FirstSimpleBehaviour::new);
    }

    // message handler, what processing do we want to do when message is received
    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()

                .onMessageEquals("say hello", () -> {
                    System.out.println("Hello");
                    return this;
                })

                .onMessageEquals("who are you", () -> {
                    System.out.println("My path is " + getContext().getSelf().path());
                    return this;
                })

                .onMessageEquals("create a child", () -> {
                    ActorRef<String> secondActor = getContext().spawn(FirstSimpleBehaviour.create(),"secondActor");
                    System.out.println("My path is " + secondActor.path());
                    return this;
                })

                .onAnyMessage(message -> {
                    System.out.println("I received the message: " + message);
                    return this;
                })
                .build();
    }
}
