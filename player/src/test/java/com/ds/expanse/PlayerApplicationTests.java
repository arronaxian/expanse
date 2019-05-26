package com.ds.expanse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class PlayerApplicationTests {

    public Optional<String> getData() {
        return Optional.of("Hello, World");
    }

    @Test
    public void contextLoads() {
        

    }

}
