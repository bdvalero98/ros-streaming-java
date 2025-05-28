package com.rosstreaming.domain;

import java.util.Optional;

public interface RouterOSClient {
    Optional<InterfaceTraffic> getCurrentTraffic();
}
