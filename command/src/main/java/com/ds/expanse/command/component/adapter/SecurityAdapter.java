package com.ds.expanse.command.component.adapter;

import com.ds.expanse.command.component.utility.RestClient;
import com.ds.expanse.command.service.InMemoryUserStore;
import org.springframework.stereotype.Component;

@Component
public class SecurityAdapter {
    public void addSecurity(RestClient restClient) {
        restClient.addHeader("Authorization",
                (String)InMemoryUserStore.getInMemoryUserStore().get("authorization"));
    }

    public String getUserName() {
        return (String)InMemoryUserStore.getInMemoryUserStore().get("username");
    }

}
