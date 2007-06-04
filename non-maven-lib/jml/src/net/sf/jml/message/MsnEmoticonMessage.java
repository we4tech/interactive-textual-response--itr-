
package net.sf.jml.message;

import java.util.*;
import net.sf.cindy.util.ByteBufferUtils;
import net.sf.jml.protocol.outgoing.OutgoingMSG;
import net.sf.jml.util.JmlConstants;
import net.sf.jml.util.Charset;
import net.sf.jml.MsnProtocol;
import java.nio.ByteBuffer;
import net.sf.jml.*;
import net.sf.jml.message.p2p.DisplayPictureDuelManager;

public class MsnEmoticonMessage extends MsnMimeMessage {

    public static final String EMOTICON_SEPARATOR = "\t";

    private Map emoticons;

    public MsnEmoticonMessage() {
        setContentType(MessageConstants.CT_EMOTICON);
        emoticons = new HashMap(5);
    }

    public void putEmoticon(String shortcut, MsnObject emoticon) {
        emoticons.put(shortcut,emoticon);
        if (emoticon != null){
            DisplayPictureDuelManager.getDuelManager().putPicture(
                    emoticon.toString(), emoticon);
        }
    }

    protected void parseMessage(byte[] message) {
       ByteBuffer split = Charset.encode(JmlConstants.LINE_SEPARATOR
                                         + JmlConstants.LINE_SEPARATOR);
       int pos = ByteBufferUtils.indexOf(ByteBuffer.wrap(message), split);

       //header
       String header = pos == -1 ? Charset.decode(message) : Charset.decode(
               message, 0, pos);
       headers.parseString(header);

       //body
       pos += split.remaining();
       ByteBuffer body = ByteBuffer.allocate(message.length - pos);
       body.put(message, pos,
                        message.length - pos);
       body.flip();
   }

   public String toString() {
       StringBuffer ret = new StringBuffer();
       for (Iterator iter = emoticons.keySet().iterator(); iter.hasNext(); ) {
           String item = (String) iter.next();
           ret.append(item);
           ret.append(EMOTICON_SEPARATOR);
           ret.append(emoticons.get(item));
           ret.append(EMOTICON_SEPARATOR);
       }
       return super.toString() + ret.toString();
   }

}
