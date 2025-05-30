package com.rosstreaming.application;

import com.rosstreaming.domain.InterfaceTraffic;
import com.rosstreaming.domain.RouterOSClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetInterfaceTrafficUseCase {

    private final RouterOSClient routerOSClient;

    public Optional<InterfaceTraffic> execute() {
        return routerOSClient.getCurrentTraffic();
    }
}
