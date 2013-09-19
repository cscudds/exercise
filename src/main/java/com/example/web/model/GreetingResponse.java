package com.example.web.model;

import com.example.core.domain.Greeting;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="greeting")
public class GreetingResponse {
    private String message;

    private GreetingResponse() {}

    public GreetingResponse(Greeting greeting) {
        this.message = greeting.getGreeting();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
