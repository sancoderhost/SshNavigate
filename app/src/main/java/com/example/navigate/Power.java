package com.example.navigate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.Properties;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Power#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Power extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Power() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Power.
     */
    // TODO: Rename and change types and number of parameters
    public static Power newInstance(String param1, String param2) {
        Power fragment = new Power();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_power, container, false);
        Sessionlogin logininfo=Sessionlogin.getInstance();
        ImageButton hibernate=v.findViewById(R.id.hibernate_button);
       hibernate.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageButton play=new ImageButton(getContext());
                play=v.findViewById(R.id.hibernate_button);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    play.setBackgroundColor(Color.parseColor("#2A7E6464"));
               commandline cmd = new  commandline("sudo systemctl hibernate" , logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port);
                    cmd.execute();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    play.setBackgroundColor(Color.parseColor("#007E6464"));
                    // set to normal color
                }

                return true;
            }
        });


        ImageButton restart=v.findViewById(R.id.restartbutton);
        restart.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageButton play=new ImageButton(getContext());
                play=v.findViewById(R.id.restartbutton);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    play.setBackgroundColor(Color.parseColor("#2A7E6464"));
                    commandline cmd = new  commandline("systemctl reboot " , logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port);
                    cmd.execute();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    play.setBackgroundColor(Color.parseColor("#007E6464"));
                    // set to normal color
                }

                return true;
            }
        });

       ImageButton poweroff=v.findViewById(R.id.powerbutton);
       poweroff.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageButton play=new ImageButton(getContext());
                play=v.findViewById(R.id.powerbutton);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    play.setBackgroundColor(Color.parseColor("#2A7E6464"));
                    commandline cmd = new  commandline("systemctl poweroff" , logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port);
                    cmd.execute();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    play.setBackgroundColor(Color.parseColor("#007E6464"));
                    // set to normal color
                }

                return true;
            }
        });


        ImageButton signout=v.findViewById(R.id.killall_button);
        signout.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageButton play=new ImageButton(getContext());
                play=v.findViewById(R.id.killall_button);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    play.setBackgroundColor(Color.parseColor("#2A7E6464"));
                    commandline cmd = new  commandline("killall -9 -u $(whoami)" , logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port);
                    cmd.execute();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    play.setBackgroundColor(Color.parseColor("#007E6464"));
                    // set to normal color
                }

                return true;
            }
        });




/*

     hibernate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                commandline cmd = new commandline("sudo systemctl hibernate", logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port);
                cmd.execute();

            }
        });

*/



        return  v;
    }


    public void executeRemoteCommand(String username,String password,String hostname,int port, String cmd )
            throws Exception {
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




            channelssh.connect();

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




    public   class  commandline extends AsyncTask<Integer, Void, Void> {
        public  String exec,username,hostname,password ;
        int port;
        commandline(String ex,String second,String third,String fourth,int fifth)
        {
            exec=ex;
            username=second;
            hostname=third;
            password=fourth;
            port=fifth;
        }
        protected Void doInBackground(Integer... params) {
            try {
                executeRemoteCommand(username,password,hostname,port,exec);


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
}