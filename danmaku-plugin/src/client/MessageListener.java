package client;

public interface MessageListener {

    void onChatMessage(String msg);

    void onGiftMessage(String msg);

    void onLoginRespMessage(String message);

    void onConnected(String message);

    void onConnectionClose(String message);

    void onLoginError(String message);
}
