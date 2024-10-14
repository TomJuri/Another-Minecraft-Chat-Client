package net.defekt.mc.chatclient.protocol.event;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An annotation used to mark methods as packet handlers. Primarily used in
 * {@link AnnotatedMinecraftPacketListener}
 *
 * @author Defective4
 * @see AnnotatedMinecraftPacketListener
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface PacketHandler {

    /**
     * Specify if packets should be handled before they are getting
     * sent/received.<br>
     * It gives you a chance to cancel them.<br>
     * Defaults to <code>true</code>
     *
     * @return true if packets should be handled before sending/receiving
     */
    public boolean preSend() default true;
}
