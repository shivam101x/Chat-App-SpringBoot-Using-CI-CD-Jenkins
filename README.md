# Chat-App-SpringBoot
This project is built using Spring Boot and WebSockets.

Java SpringBoot- web chatting real time project..   



What is Websocket?

WebSocket is a computer communications protocol, providing full-duplex communication channels over a single TCP connection. The WebSocket protocol was standardized by the IETF as RFC 6455 in 2011. The current API specification allowing web applications to use this protocol is known as WebSockets.


![Handshake (HTTP Upgrade)](https://user-images.githubusercontent.com/67068290/226824586-66a18682-0f4d-446d-977d-a23ed8f6ff6f.png)


￼
Once a websocket connection is established between a client and a server, both can exchange information until the connection is closed by any of the parties.
This is the main reasion which websocket is preferred over the HTTP protocol when building a chat-like communication service that operates at high frequencies with low latency.

What is STOMP?

Simple (or Streaming) Text Oriented Message Protocol (STOMP), formerly known as TTMP, is a simple text-based protocol, designed for working with message-oriented middleware (MOM). It provides an interoperable wire format that allows STOMP clients to talk with any message broker supporting the protocol.
Since websocket is just a communication protocol, it doesn’t know how to send a message to a particular user. STOMP is basically a messaging protocol which is useful for these functionalities.

Setting up the application
Our application will have the following configuration which can be set using Spring Initializr :
* Java version : 11
* Type : Maven Project
* Dependencies : Web-socket
* Spring Boot version : 3.0.4

![spring initializr](https://user-images.githubusercontent.com/67068290/226824651-db4676ac-bbe2-4d3e-b295-ed3437e34477.png)


￼

Project structure


![EXPLORER](https://user-images.githubusercontent.com/67068290/226824693-9f39c704-3ae8-4d7c-8a8d-e29a6eaba9a1.png)




￼




Configuring WebSocket
Configuring our websocket endpoint and message broker is fairly simple.
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
* @EnableWebSocketMessageBroker annotation is used to enable our WebSocket server.
* WebSocketMessageBrokerConfigurer interface is used to provide implementation for some of its methods to configure the websocket connection.
* registerStompEndpoints method is used to register a websocket endpoint that the clients will use to connect to the server.
* configureMessageBroker method is used to configure our message broker which will be used to route messages from one client to another.
SockJS is also being used to enable fallback options for browsers that don’t support websocket.

Creating a Chat Model
Our chat model is the message payload which will be exchanged between the client side and server side of the application.
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;

    public enum MessageType {
        *CHAT*, *LEAVE*, *JOIN
    *}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}

Creating our Chat Controller
Our controller will be responsible for handling all message methods present in our chat application which will basically receive messages from one client and then broadcast it to others.
@Controller
public class ChatController {

    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }
}
The use of /app as a destination point is because of our websocket configuration file which says that all messages will be routed to these handling methods annotated with @MessageMapping.

Creating a front-end UI
![7270381 gif](https://user-images.githubusercontent.com/67068290/226824749-27d2d985-d66e-4973-a0d5-3a339c8366a0.png)

Creating a front-end UI
￼

Our UI is a simple cardbox built using HTML and CSS that runs some JS functions to send and receive messages.
* index.html is a HTML file which contains some basic structure a Sock.js to enable fallback options to those that can’t run JS on their browsers and a STOMP library to serve as a message broker.
* main.css is a CSS file that styles our HTML.
* main.js is a Javascript file which connects the websocket endpoint to send and receive messages. It also displays and format the messages on the screen.

End result

![Screenshot 2023-03-22 at 12 14 53 PM](https://user-images.githubusercontent.com/67068290/226824855-8f14e64f-72eb-4401-9694-2037a295fd4f.png)
![Screenshot 2023-03-22 at 11 53 13 AM](https://user-images.githubusercontent.com/67068290/226824862-3cefb006-b0e8-48d7-b4f2-53f7e9059c81.png)
![Screenshot 2023-03-22 at 11 53 06 AM](https://user-images.githubusercontent.com/67068290/226824866-70ed30d8-8d92-492c-8197-4b9364aafc04.png)
![Screenshot 2023-03-22 at 11 53 01 AM](https://user-images.githubusercontent.com/67068290/226824870-d8b9b608-b2b7-4ffb-b759-f75616b6262c.png)





https://user-images.githubusercontent.com/67068290/226826346-a50754f4-5909-4346-b7d2-0f31173a2f82.mp4


