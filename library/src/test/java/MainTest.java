import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBeeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by engage on 09/07/16.
 */
public class MainTest {

    private static Logger log = LoggerFactory.getLogger(MainTest.class);

    public static void main(String args[]) throws XBeeException {

        XBeeDevice device = new XBeeDevice("/dev/ttyUSB0", 19200);
        device.open();
        device.addDataListener(new IDataReceiveListener() {
            @Override
            public void dataReceived(XBeeMessage xbeeMessage) {
                log.warn("Received packet data: " + xbeeMessage.getDataString());
            }
        });
    }

}
