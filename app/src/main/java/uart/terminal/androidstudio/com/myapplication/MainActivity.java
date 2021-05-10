package uart.terminal.androidstudio.com.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    TextView txtOut;
    UsbSerialPort port;
    MQTTService mqttService;
    private static final String ACTION_USB_PERMISSION = "com.android.recipes.USB_PERMISSION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtOut = findViewById(R.id.txtOut);
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);

        mqttService = new MQTTService( this);
        mqttService.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) { }
            @Override
            public void connectionLost( Throwable cause){

            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String data_to_microbit = message. toString();
                port.write(data_to_microbit.getBytes(),1000);
            }
            @Override
            public void deliveryComplete( IMqttDeliveryToken token)
            {

            }
        });

        }
    private void sendDataMQTT(String data){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);
        byte[] b = data.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);
        Log.d("ABC","Publish:"+ msg);
        try {
            mqttService.mqttAndroidClient.publish("[Your subscriptiontopic]", msg);
            } catch ( MqttException e){

        }
    }

}