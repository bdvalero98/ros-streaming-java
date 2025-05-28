package com.rosstreaming.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterfaceTraffic {
    private long rxBitsPerSecond;
    private long txBitsPerSecond;
}
