package su.nightexpress.excellentcrates.animation;

import java.lang.reflect.Proxy;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.MockedStatic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Minimal sound registry for unit tests; this does not simulate client tracking. */
abstract class BukkitTestSupport {

    @BeforeAll
    @SuppressWarnings({"unchecked", "rawtypes", "removal"})
    static void initializeSounds() throws ClassNotFoundException {
        try (MockedStatic<RegistryAccess> registryAccess = mockStatic(RegistryAccess.class)) {
            RegistryAccess access = mock(RegistryAccess.class);
            registryAccess.when(RegistryAccess::registryAccess).thenReturn(access);
            org.mockito.stubbing.Answer<Registry> registries = call -> (Registry) Proxy.newProxyInstance(
                Registry.class.getClassLoader(), new Class<?>[]{Registry.class}, (proxy, method, arguments) -> {
                    if (method.getName().equals("get") || method.getName().equals("getOrThrow")) {
                        NamespacedKey key = NamespacedKey.fromString(arguments[0].toString());
                        return Proxy.newProxyInstance(Sound.class.getClassLoader(), new Class<?>[]{Sound.class},
                            (sound, soundMethod, soundArguments) -> switch (soundMethod.getName()) {
                                case "getKey", "key" -> key;
                                case "name", "toString" -> key.getKey();
                                case "hashCode" -> key.hashCode();
                                case "equals" -> sound == soundArguments[0];
                                default -> null;
                            });
                    }
                    return null;
                });
            when(access.getRegistry(any(RegistryKey.class))).thenAnswer(registries);
            when(access.getRegistry(any(Class.class))).thenAnswer(registries);
            Class.forName(Sound.class.getName());
        }
    }
}
