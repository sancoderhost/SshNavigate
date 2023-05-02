package com.example.navigate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.util.Properties;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ssh_conection_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Play_fragment extends androidx.fragment.app.Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Play_fragment() {
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
        View v=inflater.inflate(R.layout.play_buttons_fragment, container, false);
        Button execute=v.findViewById(R.id.execute);
        Sessionlogin logininfo=Sessionlogin.getInstance();

      execute.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              try {
                  EditText command=getView().findViewById(R.id.runcommand);

                  String exec=command.getText().toString();
                  commandline cmd = new commandline(exec, logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port);
                  cmd.execute();
              }
              catch (Exception e)
              {
                  Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                  toast.show();
              }
          }
      });

                    ImageButton play=new ImageButton(getContext());
       play=v.findViewById(R.id.play_song);

       play.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageButton play=new ImageButton(getContext());
                play=v.findViewById(R.id.play_song);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    play.setBackgroundColor(Color.parseColor("#2A7E6464"));
                     commandline cmd = new commandline("export DISPLAY=:0 ; vlc /home/sanbot/Desktop/songs/" , logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port);
                cmd.execute();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    play.setBackgroundColor(Color.parseColor("#007E6464"));
                    // set to normal color
                }

                return true;
            }
        });



       /* play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton play=v.findViewById(R.id.play_song);
              //  play.

               commandline cmd = new commandline("export DISPLAY=:0 ; vlc /home/sanbot/Desktop/songs/" , logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port);
                cmd.execute();

            }
        });
*/



        ImageButton vm=new ImageButton(getContext());
        vm=v.findViewById(R.id.windows);


        vm.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageButton play=new ImageButton(getContext());
                play=v.findViewById(R.id.windows);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    play.setBackgroundColor(Color.parseColor("#2A7E6464"));
                    commandline cmd = new commandline("export DISPLAY=:0 ; sudo virsh start win10; remote-viewer spice://localhost:5900" , logininfo.username, logininfo.hostname, logininfo.passsword, logininfo.port);
                    cmd.execute();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    play.setBackgroundColor(Color.parseColor("#007E6464"));
                    // set to normal color
                }

                return true;
            }
        });




        // Inflate the layout for this fragment


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
            InputStream in = channelssh.getInputStream();



            byte[] tmp = new byte[1024];

            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    //System.out.print( new String(tmp, 0, i));
                    String sa = new String(tmp, 0, i);
                    System.out.println("loop=" + sa);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {


                            TextView sshout=getView().findViewById(R.id.ssh_console);
                            sshout.setMovementMethod(new ScrollingMovementMethod());
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
                    Thread.sleep(300);
                } catch (Exception ee) {
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