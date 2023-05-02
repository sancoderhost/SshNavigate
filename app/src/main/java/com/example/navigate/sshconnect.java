package com.example.navigate;

import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class sshconnect {
public  String sa;
  public void ssh_connect(String username, String password, String hostname, String exec, int port, TextView sshout) throws JSchException, IOException {

        try {
            JSch jsch = new JSch();
            Sessionlogin logininfo=Sessionlogin.getInstance();
            sshout.setMovementMethod(new ScrollingMovementMethod());

            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);

            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);

            session.connect();


            // SSH Channel
            ChannelExec channelssh = (ChannelExec)
                    session.openChannel("exec");
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //channelssh.setOutputStream(baos);

            // Execute command
            channelssh.setCommand(exec);

            //channel.setInputStream(System.in);
            channelssh.setInputStream(null);

            //channel.setOutputStream(System.out);

            //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
            //((ChannelExec)channel).setErrStream(fos);
            ((ChannelExec) channelssh).setErrStream(System.err);

            InputStream in = channelssh.getInputStream();

            channelssh.connect();

            byte[] tmp = new byte[1024];

            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    //System.out.print( new String(tmp, 0, i));
                    sa = new String(tmp, 0, i);
                    System.out.println("loop=" + sa);
                    /*runOnUiThread(new Runnable() {
                        public void run() {
                            sshout.setText(sa);
                        }
                    });
*/
                      sshout.post(new Runnable() {
                           @Override
                           public void run() {
                               sshout.setText(sa);
                           }
                       });

                }
                if (channelssh.isClosed()) {
                    if (in.available() > 0) continue;
                    System.out.println("exit-status: " + channelssh.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(10);
                } catch (Exception ee) {
                }
            }


            channelssh.disconnect();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
