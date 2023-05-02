package com.example.navigate;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
@ * create an instance of this fragment.
 */
public class Usage_view_fragment extends androidx.fragment.app.Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Usage_view_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Ssh_conection_fragment newInstance(String param1, String param2) {
        Ssh_conection_fragment fragment = new Ssh_conection_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public  int testifrun=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View v=inflater.inflate(R.layout.usage_fragment, container, false);
        TextView ramuse=v.findViewById(R.id.ramuse);
        TextView totalram=v.findViewById(R.id.ram_total);

        TextView uname=v.findViewById(R.id.uname);
        TextView osname=v.findViewById(R.id.releaseinfo);
        TextView boardname=v.findViewById(R.id.motherboard_info);
        TextView cpu_temp=v.findViewById(R.id.cpu_temp);
        TextView cpu_use=v.findViewById(R.id.cpu_use);
        TextView cpu_name=v.findViewById(R.id.cpu_name_use);
        TextView gpu_name=v.findViewById(R.id.gpu_name_use);
        TextView gpu_temp=v.findViewById(R.id.gpu_temp_use);
        TextView usb_info=v.findViewById(R.id.usb_connected);
       usb_info.setMovementMethod(new ScrollingMovementMethod());
        Sessionlogin logininfo=Sessionlogin.getInstance();


        //String username,String hostname,String password ,String cmd,int port, TextView ramuse



        Thread thread=new Thread()
        {
            @Override
            public void  run()
            {
                try {

                    while (true) {
                       // commandline ram = new commandline("free -m|grep -i mem |awk '{print $3}'", logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port, ramuse);
                        //ram.execute();

                        sshconnect(logininfo.username,logininfo.hostname,logininfo.passsword," echo $(free -m|grep -i mem |awk '{print $3}')",logininfo.port,ramuse,logininfo.ramcurrent);
                       // progress.setProgress(Integer.parseInt(logininfo.ramcurrent));


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
     /*   Runnable runnable = new Runnable() {

            @Override
            public void run() {
                ProgressBar progress = (ProgressBar) v.findViewById(R.id.Ramprogress);
                try{
                progress.setMax(8000);
                while (true) {
                    progress.post(new Runnable() {
                        @Override
                        public void run() {


                            progress.setProgress(5000);
                            SystemClock.sleep(10000);
                        }
                    });
                }
            }catch (Exception e)
                {
                    System.out.println(e.toString());
                }
            }
        };


      */

        Thread threadcpu=new Thread()
        {
            @Override
            public void  run()
            {
                try {
                    while (true) {


                        sshconnect(logininfo.username,logininfo.hostname,logininfo.passsword,"inxi -c |grep 'speed.*MHz' -o |awk -F : '{print $2}'",logininfo.port,cpu_use, logininfo.cpu_freq);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread threadtemp=new Thread()
        {
            @Override
            public void  run()
            {
                try {
                    while (true) {


                        sshconnect(logininfo.username,logininfo.hostname,logininfo.passsword,"sensors |grep CPUTIN |awk '{print $2}'",logininfo.port,cpu_temp,logininfo.cpu_temp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread threadgpu=new Thread()
        {
            @Override
            public void  run()
            {
                try {
                    while (true) {


                        sshconnect(logininfo.username,logininfo.hostname,logininfo.passsword,"sensors|grep -i 'pci adapter'  -C 1 |sed -n '3p' |awk '{print $2}'",logininfo.port,gpu_temp,logininfo.gpu_temp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread threadusb=new Thread()
        {
            @Override
            public void  run()
            {
                try {
                    while (true) {


                        sshconnect(logininfo.username,logininfo.hostname,logininfo.passsword,"usb-devices |grep -v -e 'Host Controller' -e 'Linux 5.4.0-72-generic xhci-hcd'  | grep -o -e 'Product.*'  | sed 's/Product=//g' ",logininfo.port,usb_info,logininfo.usb_info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

      /* commandline ram = new commandline("free -m|grep -i mem |awk '{print $3}'", logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port, ramuse);
        ram.execute();
    commandline ram_total = new commandline("free -m|grep -i mem |awk '{print $2}'", logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port, totalram);
    ram_total.execute();
    commandline cpu_freq = new commandline("inxi -c |grep 'speed.*MHz' -o |awk -F : '{print $2}'", logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port, cpu_use);
    cpu_freq.execute();
    commandline gputemprature = new commandline("sensors|grep -i 'pci adapter'  -C 1 |sed -n '3p' |awk '{print $2}'", logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port, gpu_temp);
    gputemprature.execute();
     commandline tempcpu = new commandline("sensors |grep CPUTIN |awk '{print $2}'", logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port, cpu_temp);
    tempcpu.execute();
        commandline usb_connected = new commandline(" usb-devices |grep -v -e 'Host Controller' -e 'Linux 5.4.0-72-generic xhci-hcd'  | grep -o -e 'Product.*'  | sed 's/Product=//g' ", logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port, usb_info);
    usb_connected.execute();

       */
        String dist = "lsb_release -a 2>/dev/null | sed -n '2p' |awk -F : '{print $2}' ";
        osname.setMovementMethod(new ScrollingMovementMethod());
        commandline osinfo = new commandline(dist, logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port, osname);
        osinfo.execute();
        uname.setMovementMethod(new ScrollingMovementMethod());
        commandline kernel = new commandline("uname -a", logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port, uname);
        kernel.execute();
    commandline board_info = new commandline("cat /sys/devices/virtual/dmi/id/board_name", logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port, boardname);
    board_info.execute();
        commandline ram_total = new commandline("free -m|grep -i mem |awk '{print $2}'", logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port, totalram);
        ram_total.execute();






    cpu_name.setText(logininfo.cpu_info);
    gpu_name.setText(logininfo.gpu_info);
   // totalram.setText(logininfo.ransize);
    if (logininfo.hostname!=null) {
        thread.start();
       //new Thread(runnable).start();
        threadcpu.start();
        threadtemp.start();
        threadgpu.start();
        threadusb.start();
    }
    return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        testifrun=1;
        System.out.println("called stop ! "+testifrun);
        //ram=null;
    }

    @Override
    public void onPause() {
        super.onPause();
        testifrun=1;
        //ram=null;
        System.out.println("called paused ! "+testifrun);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        testifrun=1;
       // ram=null;
        System.out.println("Destroyed  View ! "+testifrun);
    }

    Sessionlogin logininfo=Sessionlogin.getInstance();
    // public  int j=1;
    public void executeRemoteCommand(String username ,String password,String hostname,int port, String cmd,TextView ramuse )
            throws Exception {
        try {

            Sessionlogin logininfo=Sessionlogin.getInstance();
         /*   Button startstop=getView().findViewById(R.id.start_stop);
            startstop.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Button startstop=getView().findViewById(R.id.start_stop);
                        if (testifrun==1)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                    startstop.setText("stop");
                                    testifrun=0;
                                }
                            });


                        }
                        else
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                    startstop.setText("start");
                                    testifrun=1;
                                }
                            });

                        }

                    }
                    catch (Exception e)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(getContext().getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);
                                toast.show();


                            }
                        });

                    }
                }
            });
*/

        int j=1;
            while (j>0) {
j--;

            JSch jsch = new JSch();
            Session session = jsch.getSession(username, hostname, port);
            if (logininfo.passsword!=null) {
                session.setPassword(password);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getContext().getApplicationContext(),"PASSWORD  ENTERED  ",Toast.LENGTH_SHORT);
                        toast.show();


                    }
                });

            }
            else
            {   getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast toast = Toast.makeText(getContext().getApplicationContext(),"PRIVATE KEY SELECTED ",Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

                jsch.addIdentity(logininfo.rsa_key);
            }
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

                channelssh.setCommand(cmd);

                //channel.setInputStream(System.in);

                InputStream in = channelssh.getInputStream();


                channelssh.connect();
                byte[] tmp = new byte[1024];

                while (true) {
                    while (in.available() > 0) {
                        int i = in.read(tmp, 0, 1024);
                        if (i < 0) break;
                        //System.out.print( new String(tmp, 0, i));
                        String sa = new String(tmp, 0, i);
                        System.out.println("loop=" + sa);


                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                    ramuse.setText(sa);
                                    System.out.println("usasae"+sa);



                                }
                            });
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
                        }



                    }


                    if (channelssh.isClosed()) {
                        if (in.available() > 0) continue;
                       // System.out.println("exit-status: " + channelssh.getExitStatus());
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (Exception ee) {
                    }
                    if (testifrun==1) {
                        break;
                    }
                }


                channelssh.disconnect();
            }

        }  catch (Exception e)
        {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast toast = Toast.makeText(getContext().getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }


    }




    public   class  commandline extends AsyncTask<Integer, Void, Void> {
        public  String exec,username,hostname,password ;
        public  TextView ramuse;
        int port;
        commandline(String ex,String second,String third,String fourth,int fifth,TextView ram)
        {   ramuse=ram;
            exec=ex;
            username=second;
            hostname=third;
            password=fourth;
            port=fifth;
        }
        protected Void doInBackground(Integer... params) {
            try {
                executeRemoteCommand(username,password,hostname,port,exec,ramuse);


            } catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getContext().getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                e.printStackTrace();
            }
            return null;
        }
    }




    public  void  sshconnect(String username,String hostname,String password ,String cmd,int port, TextView ramuse,String liveusages)
    {
        try {

            Sessionlogin logininfo=Sessionlogin.getInstance();


                JSch jsch = new JSch();
                Session session = jsch.getSession(username, hostname, port);
                if (logininfo.passsword!=null) {
                    session.setPassword(password);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toast = Toast.makeText(getContext().getApplicationContext(),"PASSWORD  ENTERED  ",Toast.LENGTH_SHORT);
                            toast.show();


                        }
                    });

                }
                else
                {   getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getContext().getApplicationContext(),"PRIVATE KEY SELECTED ",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

                    jsch.addIdentity(logininfo.rsa_key);
                }
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

                channelssh.setCommand(cmd);

                //channel.setInputStream(System.in);

                InputStream in = channelssh.getInputStream();


                channelssh.connect();
                byte[] tmp = new byte[1024];

                while (true) {
                    while (in.available() > 0) {
                        int i = in.read(tmp, 0, 1024);
                        if (i < 0) break;
                        //System.out.print( new String(tmp, 0, i));
                        String sa = new String(tmp, 0, i);
                        System.out.println("loop=" + sa);
                        liveusages=sa;

                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                    ramuse.setText(sa);
                                    System.out.println("usasae"+sa);




                                }
                            });
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
                        }



                    }


                    if (channelssh.isClosed()) {
                        if (in.available() > 0) continue;
                        // System.out.println("exit-status: " + channelssh.getExitStatus());
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (Exception ee) {
                    }
                    if (testifrun==1) {
                        break;
                    }
                }


                channelssh.disconnect();


        }  catch (Exception e)
        {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast toast = Toast.makeText(getContext().getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }

    }

}