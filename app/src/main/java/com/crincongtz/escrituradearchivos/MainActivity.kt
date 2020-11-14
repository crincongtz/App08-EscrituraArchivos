package com.crincongtz.escrituradearchivos

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.OutputStreamWriter

private const val TXT = ".txt"
private const val TAG = "CursoKotlin"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonTxt = findViewById<Button>(R.id.buttonTxt)
        buttonTxt.setOnClickListener {
            generarTxt()
        }
    }

//    private const val TXT = ".txt"
//    private const val TAG = "CursoKotlin"

    fun generarTxt() {
        val nombreDeArchivo = "ArchivoCursoKotlin"

        val contenidoParaAlmacenar = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut"

//        crearArchivoTxt(nombreDeArchivo, contenidoParaAlmacenar)
        crearArchivoTxtExterno(nombreDeArchivo, contenidoParaAlmacenar)

    }

    /***
     * Esta funcion almacena archivos en la memoria de la aplicacion
     */
    fun crearArchivoTxt(nombreDeArchivo: String, textoDeArchivo: String) {
        val directorio = this.filesDir

        val rutaDestino : String = directorio.absolutePath +
                File.separator + nombreDeArchivo + TXT
        Log.d(TAG, "El valor de la rutaDestino INTERNO es: $rutaDestino")

        val archivo = File(rutaDestino)
        archivo.setWritable(true)

        val fileWriter = FileWriter(archivo)
        fileWriter.append(textoDeArchivo)
        fileWriter.close()

        val message = "El archivo $nombreDeArchivo con el contenido: $textoDeArchivo fue creado de manera exitosa"
        showToast(message)
    }

    fun crearArchivoTxtExterno(nombreDeArchivo: String, textoDeArchivo: String) {

        val directorioRaiz = Environment.getExternalStorageDirectory().absolutePath

        val rutaDestino = directorioRaiz + File.separator + nombreDeArchivo + TXT
        Log.d(TAG, "El valor de la rutaDestino EXTERNO es: $rutaDestino")

        val archivo = File(rutaDestino)

        try {
            val fileOutputStream = FileOutputStream(archivo)

            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            outputStreamWriter.append(textoDeArchivo)
            outputStreamWriter.close()

            fileOutputStream.close()

        } catch (exception: FileNotFoundException) {
            exception.printStackTrace()
            Log.i(TAG, "** El archivo no fue encontrado")
        } catch (exception: IOException) {
            exception.printStackTrace()
        }

        // Intent Implicito
        val sendFileIntent = Intent()
        sendFileIntent.action = Intent.ACTION_SEND

        val uriFile: Uri =
                FileProvider
                        .getUriForFile(this, this.applicationContext.packageName + ".fileprovider", archivo)

        sendFileIntent.putExtra(Intent.EXTRA_STREAM, uriFile)
        sendFileIntent.setType("text/plain")
        sendFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        sendFileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        val shareIntent = Intent.createChooser(sendFileIntent, "Elige una app")
        startActivity(shareIntent)

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    

}