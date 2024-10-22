package org.akka.bigprime;

import akka.actor.typed.ActorSystem;

public class MainExecutor {
    public static void main(String[] args) {
        ActorSystem<String> bigPrimes = ActorSystem.create(ManagerBehaviour.create(), "manager");
        bigPrimes.tell("start");
    }
}
