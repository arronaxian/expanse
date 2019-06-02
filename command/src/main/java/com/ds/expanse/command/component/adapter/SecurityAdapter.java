package com.ds.expanse.command.component.adapter;

import com.ds.expanse.command.component.utility.RestClient;
import com.ds.expanse.command.security.InMemoryUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityAdapter {
    public void addSecurityHeaders(String userName, RestClient restClient) {
        restClient.addHeader("Authorization",
                (String)InMemoryUserStore.getInMemoryUserStore().get("Authorization"));
    }
}
