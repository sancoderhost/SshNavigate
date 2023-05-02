package com.example.navigate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ssh_conection_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ssh_conection_fragment extends androidx.fragment.app.Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Ssh_conection_fragment() {
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

        return inflater.inflate(R.layout.ssh_fragment, container, false);

    }
    private  int CHECK_IF_KEY=0;
    private  String OUTPUTPATH;
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
    super.onActivityCreated(savedInstanceState);

        final Button pickfile = getView().findViewById(R.id.rsa_key);

        pickfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pickfile();


            }
        });

        final Button sshconnect = getView().findViewById(R.id.sshbutton);
        sshconnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {


                    EditText commands = getView().findViewById(R.id.commands);
                    EditText password =getView().findViewById(R.id.getpassword);
                    EditText user = getView().findViewById(R.id.getusername);
                    EditText host = getView().findViewById(R.id.gethostname);
                    EditText port = getView().findViewById(R.id.getport);
                    String username = user.getText().toString();
                    String hostname = host.getText().toString();
                    String pass = password.getText().toString();
                    String exec = commands.getText().toString();

                    String portno = port.getText().toString();
                    Intent myIntent = new Intent(getActivity().getApplicationContext(), sshout.class);


                    myIntent.putExtra("host", hostname);
                    myIntent.putExtra("username", username);

                    myIntent.putExtra("port", portno);
                    myIntent.putExtra("password", pass);
                    myIntent.putExtra("commands", exec);
                    if(CHECK_IF_KEY==1)
                    {
                        myIntent.putExtra("check_key","1");
                        myIntent.putExtra("keyfile",OUTPUTPATH);

                    }
                    else
                    {
                        myIntent.putExtra("check_key","0");
                    }
                    startActivity(myIntent);
                    CHECK_IF_KEY=0;

                    //String username="sdaasd";
                    //String hostname="192.168.43.202";

                    //   sshout.setText(cmd.output);

                } catch (Exception e) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                    toast.show();

                }


            }
        });

    }

    int PICKFILE_RESULT_CODE = 1;

    public void pickfile() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }
    private void copy(File source, File destination) throws IOException {

        FileChannel in = new FileInputStream(source).getChannel();
        FileChannel out = new FileOutputStream(destination).getChannel();

        try {
            in.transferTo(0, in.size(), out);
        } catch(Exception e ){
            System.out.println("Errocopy"+e.toString());
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
            toast.show();
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            CHECK_IF_KEY=1;
            Uri content_describer = data.getData();
            InputStream in = null;
            OutputStream out = null;

            try {
                in = getActivity().getContentResolver().openInputStream(content_describer);
                String content=content_describer.getLastPathSegment();
                String[] srcarr=content.split(":");
                // String src=content.replace("filesprimary:","");
                String src=srcarr[srcarr.length-1];
               OUTPUTPATH=getActivity().getApplication().getFilesDir()+"/"+src;
                System.out.println(OUTPUTPATH);
                out = new FileOutputStream(new File(String.valueOf(OUTPUTPATH)));
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "File Choosed= "+content, Toast.LENGTH_SHORT);
                toast.show();

                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                toast.show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                if (out != null){
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
            //String src = content_describer.getPath();
            // File source = new File(src);
            //  Log.d("src is ", source.toString());


            //String filename = content_describer.getLastPathSegment();

             /* Log.d("FileName is ", filename);
              File destination = new File(getApplication().getFilesDir(), filename);
              try {
                  copy(source,destination);
              } catch (IOException e) {
                  e.printStackTrace();
              }
              Log.d("Destination is ", destination.toString());
*/
        }


    }
}