/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brandts.jazzbot.socket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Teddy
 */
@ServerEndpoint("/infosocket")
public class infoSocket {

    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(final Session session) {
        // add the new session to the set
        clients.add(session);
        System.out.println("session: " + session.getId());
    }

    @OnClose
    public void onClose(final Session session) {
        // remove the session from the set
        clients.remove(session);
    }

    
    
    //Code 1 == new track
    //Code 2 == track puased
    //Code 3 == track started
    public static void broadcastReload() {
        //Check so only members get the broadcast
        String message = "{\"message\": \"" + "Update cover and queue\", \"code\": " + "1 }";

        for (Session session : clients) {
            if (session.isOpen()) {

                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException ex) {
                    Logger.getLogger(infoSocket.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
    
}
