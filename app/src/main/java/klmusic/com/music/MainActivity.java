package klmusic.com.music;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    String[ ] items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lv= findViewById(R.id.lv_playlist);
        final ArrayList<File> canciones=EncontrarCanciones(Environment.getExternalStorageDirectory());//aqui hacemos referencia al directorio raiz

        items = new String[canciones.size()];
        for (int i = 0 ; i<canciones.size();i++ ){
            items[i]= canciones.get(i).getName().toString().replace(".m4a","").replace(".mp3","").toLowerCase();

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.canciones, R.id.textView, items);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),Reproductor.class).putExtra("pos",position).putExtra("canciones",canciones));
            }
        });
    }


    //Aqui estamos buscando todas las canciones que estan almacenadas en el dispositivo, comenzando desde el directorio raíz.

    public ArrayList<File> EncontrarCanciones(File root){
        ArrayList<File> canciones= new ArrayList<File>();
        File[] archivos=root.listFiles();
        for (File lista : archivos){
            if (lista.isDirectory() && !lista.isHidden()){
                canciones.addAll(EncontrarCanciones(lista));

            }else{
                if (lista.getName().toString().endsWith(".m4a")|| lista.getName().toString().endsWith(".mp3") ){
                    canciones.add(lista);
                }
            }
        }
        return canciones;
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        return super.onCreateOptionsMenu(menu);
//
//        getMenuInflater().inflate(R.id.menu_main.menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id =item.getItemId();
//
//        if (id==R.id.action_settings){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//
//    }
}
