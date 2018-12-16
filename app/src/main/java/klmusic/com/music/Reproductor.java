package klmusic.com.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.jar.Manifest;

public class Reproductor extends AppCompatActivity implements View.OnClickListener {

    static MediaPlayer mp;
    ArrayList<File> canciones;
    int posicion;

    Thread actualizarSeekBar;

    Uri u;

    String aux="";

    ImageButton btnPv;
    ImageButton btnNext;
    ImageButton btnPause;
    ImageButton btnList;
    TextView nombre,duracionCancion,continua;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        btnPause=findViewById(R.id.btnPause);
        btnNext=findViewById(R.id.btnnext);
        btnPv=findViewById(R.id.btnPv);
        btnList=findViewById(R.id.btnList);

        nombre=findViewById(R.id.nombre);
        duracionCancion=findViewById(R.id.tv_tiempoFinal);
        continua=findViewById(R.id.tv_tiempo);




        btnPause.setOnClickListener(this);
        btnPv.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnList.setOnClickListener(this);

        seekBar=findViewById(R.id.seekBar);

        actualizarSeekBar=new Thread(){
            @Override
            public void run() {

                int duracion=mp.getDuration();
                seekBar.setMax(duracion);


                int posicionActual=0;

                int ejecucion=0;

                boolean ban=false;
                while (posicionActual<duracion){
                    try {
                        sleep(500);
                        posicionActual=mp.getCurrentPosition();
                        seekBar.setProgress(posicionActual);
                        aux =getMRM(ejecucion);

                        continua.setText(aux.toString().trim());

                    } catch (Exception e) {
//                        e.printStackTrace();

                            continua.setText(aux);
                    }
                }
            }
        };




        if (mp!=null){
            mp.stop();
        }
        try {
            Intent i= getIntent();
            Bundle b=i.getExtras();
            canciones=(ArrayList)b.getParcelableArrayList("canciones");
            posicion=(int)b.getInt("pos",0);
            u=Uri.parse(canciones.get(posicion).toString());
            mp = MediaPlayer.create(getApplicationContext(),u);

            actualizarSeekBar.start();
            mp.start();


        }catch (Exception e){

        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }




    private String getMRM (int milisegundos){
        int seconds=(int) (milisegundos/1000)%60;
        int minutes=(int)((milisegundos/(1000*60))%60);
        int hours=(int)((milisegundos/(1000*60*60))%24);
        String aux="";

        aux=((hours<10)?"0"+hours:hours)+":"+((minutes<10)?"0"+minutes:minutes)+":"+((seconds<10)?"0"+seconds:seconds);
        return aux;
    }



    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btnPause:
            if(mp.isPlaying()){
                btnPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                mp.pause();
            }else {
                btnPause.setImageResource(R.drawable.ic_pause_black_24dp);
                mp.start();
            }
            break;

            case R.id.btnPv:
                AnteriorCancion();
                break;
            case R.id.btnnext:
                NextCancion();
                break;

            case R.id.btnList:
                startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("pos",posicion).putExtra("canciones",canciones));

        }

    }


    public void NextCancion(){
        mp.stop();

        posicion=(posicion+1)%canciones.size();
        nombre.setText(canciones.get(posicion).getName().toString());

        u=Uri.parse(canciones.get(posicion).toString());
        mp=MediaPlayer.create(getApplicationContext(),u);


        mp.start();
        seekBar.setMax(0);
        duracionCancion.setText(getMRM(mp.getDuration()));

        try{
            seekBar.setMax(mp.getDuration());

        }catch (Exception e){

        }
    }



    public void AnteriorCancion(){
        mp.stop();

        if (posicion-1<0){
            posicion=canciones.size()-1;

        }else {
            posicion=posicion-1;

        }

        nombre.setText(canciones.get(posicion).getName().toString());
        u=Uri.parse(canciones.get(posicion).toString());
        mp=MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        seekBar.setMax(0);
        duracionCancion.setText(getMRM(mp.getDuration()));
        seekBar.setMax(mp.getDuration());
    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
