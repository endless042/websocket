package web;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/weball")			//중요
public class WebSocketServer {
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnMessage
	public void onMessage(String message, Session session) throws IOException{
		System.out.println(message);
		synchronized (clients) {
			/*메세지는 '이름: '추가하여 보냄
			 * 귓속말은 메세지 입력 창에 [번호] 입력하면 됨
			 * admin 창에만 번호를 보이게 하여 admin은 귓속말을 할 수 있게 한다
			 * 
			 * */
			String id=null;
			if(message.indexOf(":[")>0){
				id=message.substring(message.indexOf(":[")+2, message.indexOf("]"));
				System.out.println("id:["+id+"]");}
				String movemessage=session.getId()+":"+message;
				for(Session client : clients) {
					//자기자신에게는 보내지 않음
					
				if(!client.equals(session)) {
					if(id==null) {
						client.getBasicRemote().sendText(movemessage);}
					//귓속말 보냄
					else if(id.equals(client.getId())) {
						client.getBasicRemote().sendText(movemessage);
					}
				}
			}
		}
	}
		@OnOpen
		public void onOpen(Session session) {
			clients.add(session);
		}
		
		@OnClose
		public void onClose(Session session) {
			clients.remove(session);
		}
	}

