package net.defekt.minecraft.auth.microsoft;

@SuppressWarnings("javadoc")
public interface TokenCallback {
    public void authed(TokenResponse resp);

    public void errored(TokenErrorResponse resp);

    public void exception(Exception ex);
}