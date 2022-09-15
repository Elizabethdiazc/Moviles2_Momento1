package com.example.concesionario_jueves;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class FacturaActivity extends AppCompatActivity {

    EditText jetCodigoFactura, jetFecha, jetPlaca, jetMarca, jetModelo, jetValor;
    CheckBox jcbActivo;
    Button jbtnGuardar, jbtnConsultar, jbtnAnular, jbtnCancelar, jbtnRegresar;
    String placa, codFactura, fecha;
    long resp;
    ClsSqLite admin=new ClsSqLite(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);
        getSupportActionBar().hide();

        jetCodigoFactura=findViewById(R.id.etCodigoFactura);
        jetFecha=findViewById(R.id.etFecha);
        jetPlaca=findViewById(R.id.etPlaca);
        jetMarca=findViewById(R.id.etMarca);
        jetModelo=findViewById(R.id.etModelo);
        jetValor=findViewById(R.id.etValor);
        jcbActivo=findViewById(R.id.cbActivo);

        jbtnGuardar=findViewById(R.id.btnGuardar);
        jbtnConsultar=findViewById(R.id.btnConsultar);
        jbtnAnular=findViewById(R.id.btnAnular);
        jbtnCancelar=findViewById(R.id.btnCancelar);
        jbtnRegresar=findViewById(R.id.btnRegresar);
    }


    public void Buscar(View view){
        placa=jetPlaca.getText().toString();
        if (placa.isEmpty()){
            Toast.makeText(this, "Placa es requerida para busqueda", Toast.LENGTH_SHORT).show();
            jetPlaca.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TbLAutomovil where placa='" + placa + "'",null);
            if (fila.moveToNext()){
                jetMarca.setText(fila.getString(1));
                jetModelo.setText(fila.getString(2));
                jetValor.setText(fila.getString(3));
            }
            else{
                Toast.makeText(this, "Vehiculo no registrado", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    public void Guardar(View view){
        codFactura=jetCodigoFactura.getText().toString();
        fecha=jetFecha.getText().toString();
        placa=jetPlaca.getText().toString();

        if (placa.isEmpty() || codFactura.isEmpty()
                || fecha.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetCodigoFactura.requestFocus();
        }else{
            SQLiteDatabase db=admin.getWritableDatabase();

            Cursor fila=db.rawQuery("select * from TbLAutomovil where placa='" + placa + "' AND estado = 'si'",null);

            if (fila.getCount() > 0){
                ContentValues registro=new ContentValues();
                registro.put("placa",placa);
                registro.put("cod_factura",codFactura);
                registro.put("fecha",fecha);

                resp=db.insert("TblFactura",null,registro);

                if (resp > 0){
                    Toast.makeText(this, "Se vendio con exito", Toast.LENGTH_SHORT).show();

                    jetCodigoFactura.setText("");
                    jetFecha.setText("");
                    jetPlaca.setText("");
                    jetMarca.setText("");
                    jetModelo.setText("");
                    jetValor.setText("");

                    ContentValues inactivarCarro=new ContentValues();
                    inactivarCarro.put("estado", "no");
                    db.update("TblAutomovil", inactivarCarro, "placa = '"+placa+"'", null);
                }
            } else{
                Toast.makeText(this, "No se pudo vender el carrito", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    public void Consultar(View view){
        codFactura=jetCodigoFactura.getText().toString();
        if (codFactura.isEmpty()){
            Toast.makeText(this, "Placa es requerida para busqueda", Toast.LENGTH_SHORT).show();
            jetCodigoFactura.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblFactura where cod_factura='" + codFactura + "'",null);

            if (fila.moveToNext()){
                jetFecha.setText(fila.getString(1));
                jetPlaca.setText(fila.getString(2));
                if (fila.getString(3).equals("si"))
                    jcbActivo.setChecked(true);
                else
                    jcbActivo.setChecked(false);
            }
            db.close();
        }
    }

    public void Anular(View view){
        SQLiteDatabase db=admin.getWritableDatabase();
        ContentValues anularFactura=new ContentValues();
        anularFactura.put("estado","no");
        resp=db.update("TblFactura", anularFactura,"cod_factura='"+ codFactura +"'",null);
        if (resp > 0){
            Toast.makeText(this, "Factura anulada", Toast.LENGTH_SHORT).show();
            jcbActivo.setChecked(false);
        }
        else
            Toast.makeText(this, "Error al anular", Toast.LENGTH_SHORT).show();
        db.close();
    }


    public void Cancelar(View view){
        jetCodigoFactura.setText("");
        jetFecha.setText("");
        jetPlaca.setText("");
        jetMarca.setText("");
        jetModelo.setText("");
        jetValor.setText("");
    }

    public void Regresar(View view){
        Intent intMain=new Intent(this,MainActivity.class);
        startActivity(intMain);
    }
}