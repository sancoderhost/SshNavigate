package com.example.navigate;

class Sessionlogin
{
    // static variable single_instance of type Singleton
    private static Sessionlogin sessionlogin = null;

    // variable of type String

    public  String username,hostname,passsword,rsa_key,cpu_info,gpu_info,ransize ,cpu_temp,cpu_freq,ramcurrent,gpu_temp,usb_info;

    public  int port;


    // private constructor restricted to this class itself
    private Sessionlogin()
    {
        this.cpu_temp=null;
        this.ramcurrent=null;
        this.cpu_freq=null;
        this.username=null;
        this.hostname=null;
        this.passsword=null;
        this.port=22;
        this.rsa_key=null;
        this.cpu_info=null;
        this.gpu_info=null;
        this.ransize=null;
        this.gpu_temp=null;
        this.usb_info=null;
    }

    // static method to create instance of Singleton class
    public static Sessionlogin getInstance()
    {
        if (sessionlogin == null)
            sessionlogin = new Sessionlogin();

        return sessionlogin;
    }
}
