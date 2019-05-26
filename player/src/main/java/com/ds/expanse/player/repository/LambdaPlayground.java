package com.ds.expanse.player.repository;

import java.util.Optional;

public class LambdaPlayground {
    public static void main(String ... args) {
        new LambdaPlayground().test();

        LambdaPlayground lp = new LambdaPlayground();
        lp.printSomething("Hello, World!");
        lp.printSomething(100);

        lp.printManySomething(100, "Hello, World!");
    }

    public void test1() {
        Optional<String> optName = Optional.of("Gene");

        optName.map(n->Optional.empty()).orElse(Optional.of(getName()));
    }

    public void test() {
//        Optional<String> optName = Optional.empty();
        Optional<String> optName = Optional.of("Gene");

        String name = optName
                .map(n->"NotGene")
        .orElse(getName());

        System.out.println(name);
    }

    public String getName() {
        return "Hi";
    }

    public <T> T printSomething(T entry) {
        System.out.println(entry.getClass().getName() + " => " + entry.toString());

        return entry;
    }

    public <T,S extends String> T printManySomething(T entry1, S entry2) {
        System.out.println(entry1.getClass().getName() + " => " + entry1.toString());
        System.out.println(entry2.getClass().getName() + " => " + entry2.toString());

        return entry1;
    }




}
