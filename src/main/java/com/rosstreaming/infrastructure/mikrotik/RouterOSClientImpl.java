package com.rosstreaming.infrastructure.mikrotik;

import com.rosstreaming.config.RouterOSProperties;
import com.rosstreaming.domain.InterfaceTraffic;
import com.rosstreaming.domain.RouterOSClient;
import lombok.RequiredArgsConstructor;
import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.MikrotikApiException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouterOSClientImpl implements RouterOSClient {

    private final RouterOSProperties properties;

    @Override
    public Optional<InterfaceTraffic> getCurrentTraffic() {
        try (ApiConnection connection = ApiConnection.connect(properties.getHost())) {
            connection.login(properties.getUsername(), properties.getPassword());

            String command = String.format("/interface/monitor-traffic interface=%s once", properties.getInterfaceName());
            var result = connection.execute(command);

            if (!result.isEmpty()) {
                var data = result.get(0);
                long rx = Long.parseLong(data.get("rx-bits-per-second"));
                long tx = Long.parseLong(data.get("tx-bits-per-second"));

                return Optional.of(new InterfaceTraffic(rx, tx));
            }
        } catch (MikrotikApiException | NumberFormatException exception) {
            System.err.println("Error al obtener trafico de Mikrotik: " + exception.getMessage());
        }
        return Optional.empty();
    }
}
