package net.defekt.mc.chatclient.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.defekt.mc.chatclient.protocol.packets.PacketFactory;

/**
 * Enum with all supported protocol versions and names
 * 
 * @see MinecraftClient
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public enum ProtocolNumber {
    V1_19_2(760, "1.19.2"), V1_19(759, "1.19"), V1_18_2(758, "1.18.2"), V1_18_1(757, "1.18.1"), V1_17_1(756, "1.17.1"),
    V1_17(755, "1.17"), V1_16_5(754, "1.16.5"), V1_16_3(753, "1.16.3"),
//	V1_16_2(736, "1.16.2"),
//	V1_16_1(736, "1.16.1"),
//	V1_16(735, "1.16"),
    V1_15_2(578, "1.15.2"), V1_15_1(575, "1.15.1"), V1_15(573, "1.15"), V1_14_4(498, "1.14.4"), V1_14_3(490, "1.14.3"),
    V1_14_2(485, "1.14.2"), V1_14_1(480, "1.14.1"), V1_14(477, "1.14"),
//	V1_13_2(404, "1.13.2")
    V1_13_1(401, "1.13.1"), V1_13(393, "1.13"), V1_12_2(340, "1.12.2"), V1_12_1(338, "1.12.1"), V1_12(335, "1.12"),
    V1_11_2(316, "1.11.2"), V1_11(315, "1.11"), V1_10_X(210, "1.10"), V1_9_4(110, "1.9.4"), V1_9_2(109, "1.9.2"),
    V1_9_1(108, "1.9.1"), V1_9(107, "1.9"), V1_8(47, "1.8");

    // Commented out versions do not work properly, sorry...

    /**
     * Protocol number represented by this enum
     */
    public final int protocol;

    /**
     * Version name of this enum
     */
    public final String name;

    private ProtocolNumber(final int protocol, final String name) {
        this.protocol = protocol;
        this.name = name;
    }

    /**
     * Get protocol number associated with this enum
     * 
     * @return protocol number
     */
    public int getProtocol() {
        return protocol;
    }

    /**
     * Get version name of this enum
     * 
     * @return version name
     */
    public String getName() {
        return name;
    }

    /**
     * Get enum representation of this version name
     * 
     * @param name human readable version name ("x.x.x")
     * @return enum representing this version or last supported version
     */
    public static ProtocolEntry getForName(final String name) {
        for (final ProtocolEntry num : ProtocolNumber.getValues())
            if (num.name.equals(name)) return num;
        return ProtocolNumber.getValues()[0];
    }

    public static ProtocolEntry getForNumber(final int protocol) {
        for (final ProtocolEntry num : ProtocolNumber.getValues())
            if (num.protocol == protocol) return num;
        return ProtocolNumber.getValues()[ProtocolNumber.values().length - 1];
    }

    public static ProtocolEntry[] getValues() {
        List<ProtocolEntry> num = new ArrayList<>();
        List<ProtocolNumber> values = new ArrayList<>();
        Collections.addAll(values, values());
        for (Map.Entry<Integer, String> up : PacketFactory.getUserVersions().entrySet()) {
            boolean add = true;
            for (ProtocolNumber nm : values) {
                if (up.getKey().equals(nm.protocol)) {
                    add = false;
                    break;
                }
            }
            if (!add) break;
            ProtocolEntry pe = new ProtocolEntry(up.getKey(), up.getValue());
            num.add(pe);
        }
        for (ProtocolNumber nm : values) {
            num.add(new ProtocolEntry(nm.protocol, nm.name));
        }

        return num.toArray(new ProtocolEntry[0]);
    }
}
