package com.antutu.ABenchMark;

import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
public class AndroidShell {
    public String runShell(final String command) {
        final String[] errors = {""};
        // Чтобы не вис интерфейс, запускаем в другом потоке
        /*new Thread(new Runnable() {
            public void run() {
                OutputStream out = null;
                InputStream in = null;
                try {
                    // Отправляем скрипт в рантайм процесс
                    Process child = Runtime.getRuntime().exec("sh");
                    DataOutputStream stdin = new DataOutputStream(child.getOutputStream());

                    stdin.writeBytes(command+"\n");
                    stdin.flush();
                    stdin.writeBytes("exit\n");
                    stdin.flush();
                    BufferedReader stdout = new BufferedReader(new InputStreamReader(child.getInputStream()));
                    BufferedReader stderr=new BufferedReader(new InputStreamReader(child.getErrorStream()));
                    while(true){
                        String s=stderr.readLine();
                        if(s==null){
                            break;
                        }
                        errors[0] +=s;
                        Log.v("err",s);
                    }
                    while(true){
                        String s=stdout.readLine();
                        if(s==null){
                            break;
                        }
                        errors[0] +=s;
                        Log.v("Script out",s);
                    }
                    child.destroy();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();*/
        OutputStream out = null;
        InputStream in = null;
        try {
            // Отправляем скрипт в рантайм процесс
            Process child = Runtime.getRuntime().exec(new String[] {"system/bin/sh" });
            DataOutputStream stdin = new DataOutputStream(child.getOutputStream());

            stdin.writeBytes(command+"\n");
            stdin.flush();
            stdin.writeBytes("exit\n");
            stdin.flush();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(child.getInputStream()));
            BufferedReader stderr=new BufferedReader(new InputStreamReader(child.getErrorStream()));
            while(true){
                String s=stderr.readLine();
                if(s==null){
                    break;
                }
                errors[0] +=s+"\n";
                Log.v("err",s);
            }
            while(true){
                String s=stdout.readLine();
                if(s==null){
                    break;
                }
                errors[0] +=s+"\n";
                Log.v("Script out",s);
            }
            child.destroy();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Log.v("str", errors[0]);
        return errors[0];
    }
}