package com.antutu.ABenchMark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.antutu.ABenchMark.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'ABenchMark' library on application startup.
    static {
        System.loadLibrary("ABenchMark");
    }
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String cmd= "export LOG_PATH=\""+this.getFilesDir()+"/usr/log.txt\"\n"+"echo \"Starting xserver\"\n" +
                "echo \"Loading exported settings from the app\"\n"+
                "source \""+this.getFilesDir()+"/usr/mobox/exported-settings.sh\"\n"+"if [ -f \""+this.getFilesDir()+"/usr/wineprefix/1/mobox-settings/selected-preset\" ]; then\n"+"APP_SELECTED_PRESET=\"$(cat "+this.getFilesDir()+"/usr/wineprefix/1/mobox-settings/selected-preset)\"\n" +
                "\n" +
                "    if [ ! \"1\" = \"\" ] && [ ! \"1\" = \"global\" ]; then\n" +
                "        echo \"Switching to 1 environment preset\"\n" +
                "        source \""+this.getFilesDir()+"/usr/mobox/exported-settings-1.sh\"\n" +
                "    fi\n" +
                "    unset APP_SELECTED_PRESET\n" +
                "fi\n" +
                "\n" +
                "export APP_SELECTED_WINE=\"$(cat "+this.getFilesDir()+"/usr/wineprefix/1/used-wine)\"\n" +
                "\n" +
                "\n" +
                "export PREFIX=\""+this.getFilesDir()+"/usr\"\n" +
                "export HOME=\""+this.getFilesDir()+"/home\"\n" +
                "export TMPDIR=\"$PREFIX/tmp\"\n" +
                "export GLIBC_PREFIX=\"$PREFIX/glibc\"\n" +
                "export SHELL=\"$(which sh)\"\n" +
                "export PATH=\"$PATH:$GLIBC_PREFIX/bin\"\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "export FONTCONFIG_PATH=\"$GLIBC_PREFIX/etc/fonts\"\n" +
                "export VK_ICD_FILENAMES=\"$GLIBC_PREFIX/etc/vulkan/icd.d/freedreno_icd.aarch64.json\"\n" +
                "export VK_DRIVER_FILES=\"$VK_ICD_FILENAMES\"\n" +
                "export DXVK_CONFIG_FILE=\"$GLIBC_PREFIX/share/dxvk.conf\"\n" +
                "\n" +
                "\n" +
                "export WINE_PATH=\"$PREFIX/wine/$APP_SELECTED_WINE\"\n" +
                "export WINEPREFIX=\"$PREFIX/wineprefix/$APP_SELECTED_CONTAINER\"\n" +
                "export BOX64_LD_LIBRARY_PATH=\"$GLIBC_PREFIX/lib/x86_64-linux-gnu:$WINE_PATH/lib/wine/x86_64-unix\"\n" +
                "export BOX64_RCFILE=\"$GLIBC_PREFIX/etc/box64.box64rc\"\n" +
                "\n" +
                "##export BOX64_CPUNAME=$(lscpu | grep \"Model name:\" | sed -r 's/Model name:\\ {1,}//g' | cut -d $'\\n' -f 1)\n" +
                "\n" +
                "export GALLIUM_DRIVER=zink\n" +
                "export MESA_LOADER_DRIVER_OVERRIDE=zink\n" +
                "export LIBGL_DRIVERS_PATH=\"$GLIBC_PREFIX/lib/dri\"\n" +
                "\n" +
                "mkdir -p \"$GLIBC_PREFIX/bin\"\n" +
                "mkdir -p \"$GLIBC_PREFIX/share\"\n" +
                "mkdir -p \"$GLIBC_PREFIX/etc\"\n" +
                "mkdir -p \"$GLIBC_PREFIX/lib/x86_64-linux-gnu\"\n" +
                "mkdir -p \"$HOME\"\n" +
                "cd \"$HOME\"\n" +
                "mkdir -p \"$TMPDIR\"\n" +
                "mkdir -p \"$PREFIX/wine\"\n" +
                "mkdir -p \"$PREFIX/wineprefix\"\n" +
                "\n" +
                "if [ -z \"$APP_TARGET_EXECUTABLE\" ]; then\n" +
                "    export APP_TARGET_EXECUTABLE=\"$PREFIX/mobox/apps/tfm.exe\"\n" +
                "    export APP_TARGET_EXECUTABLE=\"$PREFIX/mobox/apps/explorer.exe\"\n" +
                "fi\n" +
                "\n" +
                "\n" +
                "echo \"Target executable: $APP_TARGET_EXECUTABLE\"\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "if [ -d \"$TMPDIR\" ]; then\n" +
                "    rm -rf \"$TMPDIR/\"*\n" +
                "fi\n" +
                "\n" +
                "if [ ! -e \"$GLIBC_PREFIX/etc/passwd\" ]; then\n" +
                "    echo \"Generating glibc passwd user\"\n" +
                "    x=$(/system/bin/id -u)\n" +
                "    echo \"u0_a${x:2}::$x:$x::$HOME:$SHELL\">\"$GLIBC_PREFIX/etc/passwd\"\n" +
                "    unset x\n" +
                "fi\n" +
                "\n" +
                "mkdir -p \"$LOG_PATH/trace\"\n" +
                "rm -rf \"$LOG_PATH/trace/\"*\n" +
                "\n" +
                "ln -sf \"$WINE_PATH/bin/wine\" \"$GLIBC_PREFIX/bin/wine\"\n" +
                "ln -sf \"$WINE_PATH/bin/wineserver\" \"$GLIBC_PREFIX/bin/wineserver\"\n" +
                "\n" +
                "echo \"Starting pulse server\"\n" +
                "\n" +
                "chmod +x \"$PREFIX/mobox/pulse/pulseaudio\"\n" +
                "LD_LIBRARY_PATH=$PREFIX/mobox/pulse \\\n" +
                " TMPDIR=$PREFIX/tmp \\\n" +
                " $PREFIX/mobox/pulse/pulseaudio \\\n" +
                "  --disable-shm=true \\\n" +
                "  --system=false \\\n" +
                "  --daemonize=false \\\n" +
                "  --use-pid-file=false \\\n" +
                "  -n --file=\"$PREFIX/mobox/pulse/default.pa\" \\\n" +
                "  --exit-idle-time=-1 --fail=false \\\n" +
                "   >\"$LOG_PATH/pulseaudio.txt\" 2>&1 &\n" +
                "\n" +
                "chmod +x \"$GLIBC_PREFIX/bin/box64\"\n" +
                "\n" +
                "echo \"Starting main executable\"\n" +
                "\n" +
                "touch \"$PREFIX/mobox/startup-complete\"\n" +
                "\n" +
                "box64 wine explorer /desktop=shell,$RESOLUTION \\\n" +
                " \"$APP_TARGET_EXECUTABLE\" \\\n" +
                "  >\"$LOG_PATH/main.txt\" 2>&1\n" +
                "\n" +
                "box64 wineserver -k\n";
        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
        Log.e("print", String.valueOf(this.getFilesDir()));
        LinearLayout ll=findViewById(R.id.scr);
        TextView tx=new TextView(this);
        tx.setText("new: "+new AndroidShell().runShell(cmd));
        ll.addView(tx);

    }
    void execFile(File f){
        List str=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                str= Files.readAllLines(Paths.get(this.getFilesDir().getAbsolutePath()+"/"+f.getName()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String cmd="";
        for(Object st:str){
            String shell=st.toString();
            cmd+=shell;
        }
        new AndroidShell().runShell(cmd);
    }

    /**
     * A native method that is implemented by the 'ABenchMark' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public void executeCMD(View w){

        EditText etx=(EditText) findViewById(R.id.sshell);
        //new AndroidShell().runShell(etx.getText().toString());
        LinearLayout ll=findViewById(R.id.scr);
        TextView tx=new TextView(this);
        tx.setText("new: "+new AndroidShell().runShell(etx.getText().toString()));
        ll.addView(tx);
    }
    public void launch(View w){
        String cmd= "chmod 777 -R "+this.getFilesDir()+"/usr/glibc/lib;" +
                "chmod 777 -R "+this.getFilesDir()+"/usr/wine;" +
                "export LOG_PATH=\""+this.getFilesDir()+"/usr/log.txt\"\n"+"echo \"Starting xserver\"\n" +
                "echo \"Loading exported settings from the app\"\n"+
                "source \""+this.getFilesDir()+"/usr/mobox/exported-settings.sh\"\n"+"if [ -f \""+this.getFilesDir()+"/usr/wineprefix/1/mobox-settings/selected-preset\" ]; then\n"+"APP_SELECTED_PRESET=\"$(cat "+this.getFilesDir()+"/usr/wineprefix/1/mobox-settings/selected-preset)\"\n" +
                "\n" +
                "    if [ ! \"1\" = \"\" ] && [ ! \"1\" = \"global\" ]; then\n" +
                "        echo \"Switching to 1 environment preset\"\n" +
                "        source \""+this.getFilesDir()+"/usr/mobox/exported-settings-1.sh\"\n" +
                "    fi\n" +
                "    unset APP_SELECTED_PRESET\n" +
                "fi\n" +
                "\n" +
                "export APP_SELECTED_WINE=\"$(cat "+this.getFilesDir()+"/usr/wineprefix/1/used-wine)\"\n" +
                "\n" +
                "\n" +
                "export PREFIX=\""+this.getFilesDir()+"/usr\"\n" +
                "export HOME=\""+this.getFilesDir()+"/home\"\n" +
                "export TMPDIR=\"$PREFIX/tmp\"\n" +
                "export GLIBC_PREFIX=\"$PREFIX/glibc\"\n" +
                "export SHELL=\"$(which sh)\"\n" +
                "export PATH=\"$PATH:$GLIBC_PREFIX/bin\"\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "export FONTCONFIG_PATH=\"$GLIBC_PREFIX/etc/fonts\"\n" +
                "export VK_ICD_FILENAMES=\"$GLIBC_PREFIX/etc/vulkan/icd.d/freedreno_icd.aarch64.json\"\n" +
                "export VK_DRIVER_FILES=\"$VK_ICD_FILENAMES\"\n" +
                "export DXVK_CONFIG_FILE=\"$GLIBC_PREFIX/share/dxvk.conf\"\n" +
                "\n" +
                "\n" +
                "export WINE_PATH=\"$PREFIX/wine/$APP_SELECTED_WINE\"\n" +
                "export WINEPREFIX=\"$PREFIX/wineprefix/$APP_SELECTED_CONTAINER\"\n" +
                "export BOX64_LD_LIBRARY_PATH=\"$GLIBC_PREFIX/lib/x86_64-linux-gnu:$WINE_PATH/lib/wine/x86_64-unix\"\n" +
                "export BOX64_RCFILE=\"$GLIBC_PREFIX/etc/box64.box64rc\"\n" +
                "\n" +
                "##export BOX64_CPUNAME=$(lscpu | grep \"Model name:\" | sed -r 's/Model name:\\ {1,}//g' | cut -d $'\\n' -f 1)\n" +
                "\n" +
                "export GALLIUM_DRIVER=zink\n" +
                "export MESA_LOADER_DRIVER_OVERRIDE=zink\n" +
                "export LIBGL_DRIVERS_PATH=\"$GLIBC_PREFIX/lib/dri\"\n" +
                "\n" +
                "mkdir -p \"$GLIBC_PREFIX/bin\"\n" +
                "mkdir -p \"$GLIBC_PREFIX/share\"\n" +
                "mkdir -p \"$GLIBC_PREFIX/etc\"\n" +
                "mkdir -p \"$GLIBC_PREFIX/lib/x86_64-linux-gnu\"\n" +
                "mkdir -p \"$HOME\"\n" +
                "cd \"$HOME\"\n" +
                "mkdir -p \"$TMPDIR\"\n" +
                "mkdir -p \"$PREFIX/wine\"\n" +
                "mkdir -p \"$PREFIX/wineprefix\"\n" +
                "\n" +
                "if [ -z \"$APP_TARGET_EXECUTABLE\" ]; then\n" +
                "    export APP_TARGET_EXECUTABLE=\"$PREFIX/mobox/apps/tfm.exe\"\n" +
                "    export APP_TARGET_EXECUTABLE=\"$PREFIX/mobox/apps/explorer.exe\"\n" +
                "fi\n" +
                "\n" +
                "\n" +
                "echo \"Target executable: $APP_TARGET_EXECUTABLE\"\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "if [ -d \"$TMPDIR\" ]; then\n" +
                "    rm -rf \"$TMPDIR/\"*\n" +
                "fi\n" +
                "\n" +
                "if [ ! -e \"$GLIBC_PREFIX/etc/passwd\" ]; then\n" +
                "    echo \"Generating glibc passwd user\"\n" +
                "    x=$(/system/bin/id -u)\n" +
                "    echo \"u0_a${x:2}::$x:$x::$HOME:$SHELL\">\"$GLIBC_PREFIX/etc/passwd\"\n" +
                "    unset x\n" +
                "fi\n" +
                "\n" +
                "mkdir -p \"$LOG_PATH/trace\"\n" +
                "rm -rf \"$LOG_PATH/trace/\"*\n" +
                "\n" +
                "ln -sf \"$WINE_PATH/bin/wine\" \"$GLIBC_PREFIX/bin/wine\"\n" +
                "ln -sf \"$WINE_PATH/bin/wineserver\" \"$GLIBC_PREFIX/bin/wineserver\"\n" +
                "\n" +
                "echo \"Starting pulse server\"\n" +
                "\n" +
                "chmod +x \"$PREFIX/mobox/pulse/pulseaudio\"\n" +
                "LD_LIBRARY_PATH=$PREFIX/mobox/pulse \\\n" +
                " TMPDIR=$PREFIX/tmp \\\n" +
                " $PREFIX/mobox/pulse/pulseaudio \\\n" +
                "  --disable-shm=true \\\n" +
                "  --system=false \\\n" +
                "  --daemonize=false \\\n" +
                "  --use-pid-file=false \\\n" +
                "  -n --file=\"$PREFIX/mobox/pulse/default.pa\" \\\n" +
                "  --exit-idle-time=-1 --fail=false \\\n" +
                "   >\"$LOG_PATH/pulseaudio.txt\" 2>&1 &\n" +
                "\n" +
                "chmod +x \"$GLIBC_PREFIX/bin/box64\"\n" +
                "\n" +
                "echo \"Starting main executable\"\n" +
                "\n" +
                "touch \"$PREFIX/mobox/startup-complete\"\n" +
                "\n" +
                "box64 wine explorer /desktop=shell,$RESOLUTION \\\n" +
                " \"$APP_TARGET_EXECUTABLE\" \\\n" +
                "  >\"$LOG_PATH/main.txt\" 2>&1\n" +
                "\n" +
                "box64 wineserver -k\n";
        LinearLayout ll=findViewById(R.id.scr);
        TextView tx=new TextView(this);
        tx.setText("new: "+new AndroidShell().runShell(cmd));
        ll.addView(tx);
    }

}