package org.akka;

import akka.actor.typed.ActorSystem;

public class Main {
    public static void main(String[] args) {

        // once the application is started, it can run indefinitely
        // as the actor can receive multiple messages.

        ActorSystem<String> actorSystem = ActorSystem
                .create(FirstSimpleBehaviour.create(),"FirstActorSystem");

        actorSystem.tell("say hello");
        actorSystem.tell("who are you");
        actorSystem.tell("create a child");
        actorSystem.tell("Random message");
    }
}