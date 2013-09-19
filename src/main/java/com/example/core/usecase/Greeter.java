package com.example.core.usecase;

import com.example.core.domain.Greeting;
import org.springframework.stereotype.Component;

@Component
public class Greeter {
    public Greeting getGreeting() {
        return new Greeting("Hello World!");
    }
}
