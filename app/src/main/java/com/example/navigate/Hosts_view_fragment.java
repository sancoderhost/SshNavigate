package com.example.navigate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ssh_conection_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Hosts_view_fragment extends androidx.fragment.app.Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Hosts_view_fragment() {
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.host_fragment, container, false);
        LinearLayout ll = view.findViewById(R.id.host_layout);

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] projection = {

                FeedReaderContract.FeedEntry.USER,
                FeedReaderContract.FeedEntry.HOST,
                FeedReaderContract.FeedEntry.PASSWORD,
                FeedReaderContract.FeedEntry.PORT,
                FeedReaderContract.FeedEntry.KEY_LOCATION
        };
        String selection = FeedReaderContract.FeedEntry.HOST+ " = ?";
        String[] selectionArgs = { "" };
        String sortOrder =
                FeedReaderContract.FeedEntry.PORT+ " DESC";
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        while(cursor.moveToNext()) {
            //long itemId = cursor.getLong(
            //      cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_NAME));

          /*  itemIds.add(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_ID)));
            names.add(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_NAME)));
            desgisnation.add(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_DESI)));
            phno.add(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_PH)));
            System.out.println(
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_ID))+"|"+
                            cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_NAME))+"|"+
                            cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_DESI))+"|"+
                            cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_PH))+"\n");
*/

                /*   Out=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_ID))+"|"+
                           cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_NAME))+"|"+
                           cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_DESI))+"|"+
                           cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.EMP_PH))+"\n";
*/

            Button btn = new Button(getContext().getApplicationContext());
            String username,hostname,password,key_file;
            int port;
            username=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.USER));
            hostname=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.HOST));
            password=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.PASSWORD));
            key_file=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.KEY_LOCATION));
            port=Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.PORT)));
            btn.setText( hostname);

          /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
                   LinearLayout.LayoutParams.WRAP_CONTENT
            );
              params.setMargins(100, 100, 100, 100);
            btn.setLayoutParams(params);
           */

            btn.setBackgroundResource(R.color.purple_500);
            btn.setTextColor(getResources().getColor(R.color.white));
            setMargins(btn, 0 ,10, 10, 10);
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.addView(btn);
           btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Sessionlogin logininfo=Sessionlogin.getInstance();

                    logininfo.username=username;
                    logininfo.passsword=password;
                    logininfo.rsa_key=key_file;
                    logininfo.hostname=hostname;
                   logininfo.port=port;

                   commandline cmd=new commandline("inxi -c |head -n 1  |awk -F : '{print $2 $3}'", logininfo.username,logininfo.hostname, logininfo.passsword, port,0);
                   cmd.execute();
                   commandline  gpucmd=new commandline(" inxi -G |sed -n '1p'", logininfo.username,logininfo.hostname, logininfo.passsword, port,1);
                   gpucmd.execute();
                   commandline  ramsize=new commandline("free -m |grep Mem  |awk '{print $2}'", logininfo.username,logininfo.hostname, logininfo.passsword, port,2);
                   ramsize.execute();

                   getActivity().runOnUiThread(new Runnable() {
                       public void run() {
                           Toast toast = Toast.makeText(getContext().getApplicationContext(),"SELECTED=>"+hostname,Toast.LENGTH_SHORT);
                           toast.show();
                       }
                   });

               }
           });


        }
        cursor.close();



        return view;
    }
    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }


    public class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "hosts.db";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + sshout.FeedReaderContract.FeedEntry.TABLE_NAME + " (" +

                        sshout.FeedReaderContract.FeedEntry.HOST+ " TEXT PRIMARY KEY," +

                        sshout.FeedReaderContract.FeedEntry.USER+ " TEXT,"+
                        sshout.FeedReaderContract.FeedEntry.PASSWORD + " TEXT,"+
                        sshout.FeedReaderContract.FeedEntry.KEY_LOCATION+ " TEXT,"+

                        sshout.FeedReaderContract.FeedEntry.PORT+ " INTEGER)";
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + sshout.FeedReaderContract.FeedEntry.TABLE_NAME;

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }


    public final class FeedReaderContract {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private FeedReaderContract() {}

        /* Inner class that defines the table contents */
        public  class FeedEntry implements BaseColumns {

            public static final String TABLE_NAME = "hosts";
            public static final String USER= "username";
            public static final String PASSWORD = "passwords";
            public static final String PORT= "port";
            public  static  final  String HOST="hostname";
            public static final String KEY_LOCATION= "private_key";


        }
    }

    public void executeRemoteCommand(String username,String password,String hostname,int port, String cmd ,int six)
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
            channelssh.setInputStream(null);

            //channel.setOutputStream(System.out);

            //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
            //((ChannelExec)channel).setErrStream(fos);
            ((ChannelExec) channelssh).setErrStream(System.err);
            InputStream in = channelssh.getInputStream();

            //channel.setInputStream(System.in);




            channelssh.connect();
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

                                if (six==0) {
                                    logininfo.cpu_info = sa;
                                    Toast toast = Toast.makeText(getContext().getApplicationContext(),"Executed Cpu_info=\n"+sa,Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                else if(six==1)
                                {
                                    logininfo.gpu_info=sa;
                                    Toast toast = Toast.makeText(getContext().getApplicationContext(),"Executed Gpu_info\n"+sa,Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                else {
                                    logininfo.ransize=sa;
                                    Toast toast = Toast.makeText(getContext().getApplicationContext(),"ram_size=\n"+sa,Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                        }
                    });


                }



                if (channelssh.isClosed()) {
                    if (in.available() > 0) continue;
                    System.out.println("exit-status: " + channelssh.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(50);
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
        int port,sixth;
        commandline(String ex,String second,String third,String fourth,int fifth,int six)
        {
            exec=ex;
            username=second;
            hostname=third;
            password=fourth;
            port=fifth;
            sixth=six;
        }
        protected Void doInBackground(Integer... params) {
            try {
                executeRemoteCommand(username,password,hostname,port,exec,sixth);


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