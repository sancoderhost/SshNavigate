package com.example.navigate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.util.Properties;


public class sshout extends AppCompatActivity {

    private int CHECK_IF_KEY=0;
    private String PRIVATE_KEY;
   private  String sa = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sshout);


        try {
            Intent intent = getIntent();


            String username = intent.getStringExtra("username");
            String hostname = intent.getStringExtra("host");
            String pass = null;
            String exec=intent.getStringExtra("commands");
            String port=intent.getStringExtra("port");
            int portno;
            if(port==null|| port.isEmpty() || port.equals("") ) {
                portno=22;

            }
            else
            {
                portno = Integer.parseInt(port);
            }
            CHECK_IF_KEY=Integer.parseInt(intent.getStringExtra("check_key"));
            if (CHECK_IF_KEY==1) {
                PRIVATE_KEY = intent.getStringExtra("keyfile");
            }
            else {
                pass=intent.getStringExtra("password");
            }
            sshout.commandline cmd = new sshout.commandline(exec, username, hostname, pass, portno,0);

            sshout.commandline cpuname=new sshout.commandline("  inxi -c |head -n 1  |awk -F : '{print $2 $3}'",username,hostname,pass,portno,1);
            sshout.commandline gpuname=new sshout.commandline("  inxi -G |sed -n '1p' ",username,hostname,pass,portno,2);
            sshout.commandline ran_size=new sshout.commandline("free -h |grep Mem  |awk '{print $2}'",username,hostname,pass,portno,3);
            cmd.execute();
            cpuname.execute();
            gpuname.execute();
            ran_size.execute();
            System.out.println("in" + exec);
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);
            toast.show();

        }







    }

public  int runonce=0;
    public void executeRemoteCommand(String username,String password,String hostname,int port, String cmd,int sixth )
            throws Exception {Sessionlogin logininfo=Sessionlogin.getInstance();
        try {


            logininfo.username=username;
            logininfo.passsword=password;
            logininfo.hostname=hostname;
            logininfo.port=port;

            TextView sshout = findViewById(R.id.sshoutput);
            sshout.setMovementMethod(new ScrollingMovementMethod());

            JSch jsch = new JSch();
            Session session = jsch.getSession(username, hostname, port);
            if (CHECK_IF_KEY==0) {
                session.setPassword(password);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(),"PASSWORD  ENTERED  ",Toast.LENGTH_SHORT);
                        toast.show();
                        logininfo.passsword=password;
                        logininfo.rsa_key=null;
                    }
                });

            }
            else
            {   runOnUiThread(new Runnable() {
                public void run() {
                    Toast toast = Toast.makeText(getApplicationContext(),"PRIVATE KEY SELECTED ",Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
                logininfo.rsa_key=PRIVATE_KEY;

                logininfo.passsword=null;
                jsch.addIdentity(PRIVATE_KEY);
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
                    runOnUiThread(new Runnable() {
                        public void run() {
                            runonce=runonce+1;
                            if (sixth==0) {
                                sshout.setText(sa);

                            }
                            else if (sixth==1){
                                logininfo.cpu_info = sa;
                                Toast toast = Toast.makeText(getApplicationContext(),"Executed Cpu_info\n"+sa,Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else if(sixth==2)
                            {
                                logininfo.gpu_info=sa;
                                Toast toast = Toast.makeText(getApplicationContext(),"Executed Gpu_info\n"+sa,Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                logininfo.ransize=sa;
                                Toast toast = Toast.makeText(getApplicationContext(),"ram size=\n"+sa,Toast.LENGTH_SHORT);
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
                    Thread.sleep(300);
                } catch (Exception ee) {
                }
            }


            channelssh.disconnect();
            if(runonce==1) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        LinearLayout ll = findViewById(R.id.sshoutput2);

                        Button btn = new Button(getApplicationContext());
                        btn.setText("Save_host");
                        btn.setBackgroundResource(R.color.purple_500);
                        btn.setTextColor(getResources().getColor(R.color.white));
                        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        ll.addView(btn);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {


                                    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put(FeedReaderContract.FeedEntry.HOST, hostname);
                                    values.put(FeedReaderContract.FeedEntry.USER, username);
                                    values.put(FeedReaderContract.FeedEntry.PORT, port);
                                    values.put(FeedReaderContract.FeedEntry.PASSWORD, password);
                                    if (CHECK_IF_KEY != 0) {
                                        values.put(FeedReaderContract.FeedEntry.KEY_LOCATION, PRIVATE_KEY);
                                    } else {
                                        values.put(FeedReaderContract.FeedEntry.KEY_LOCATION, "0");
                                    }
                                    long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
                                    if (newRowId != -1) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "row inserted =" + newRowId, Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Error Inserting ", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                    Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                                    toast.show();
                                }


                            }
                        });

                    }
                });
            }
        }catch (Exception e )
        {
            logininfo.username=null;
            logininfo.passsword=null;
            logininfo.hostname=null;
            logininfo.port=0;
            logininfo.rsa_key=null;
            System.out.println("SanbotRoot "+e);
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast toast = Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);
                    toast.show();
                    TextView sshout = findViewById(R.id.sshoutput);
                    sshout.setMovementMethod(new ScrollingMovementMethod());
                    sshout.setTextSize(25);
                    sshout.setTextColor(getResources().getColor(R.color.red));
                    sshout.setText("ERROR OCCURED!="+e.toString());

                }
            });


        }
//return  sa;
        //return baos.toString();
    }


    public class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "hosts.db";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +

                        FeedReaderContract.FeedEntry.HOST+ " TEXT PRIMARY KEY," +

                        FeedReaderContract.FeedEntry.USER+ " TEXT,"+
                        FeedReaderContract.FeedEntry.PASSWORD + " TEXT,"+
                        FeedReaderContract.FeedEntry.KEY_LOCATION+ " TEXT,"+

                        FeedReaderContract.FeedEntry.PORT+ " INTEGER)";
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

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


    public   class  commandline extends AsyncTask<Integer, Void, Void> {
        public  String exec,username,hostname,password ;
        public  int six;
        int port;
        commandline(String ex,String second,String third,String fourth,int fifth,int sixth)
        {
            exec=ex;
            username=second;
            hostname=third;
            password=fourth;
            port=fifth;
            six=sixth;
        }
        protected Void doInBackground(Integer... params) {
            try {
                executeRemoteCommand(username,password,hostname,port,exec,six);


            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                e.printStackTrace();
            }
            return null;
        }
    }


}