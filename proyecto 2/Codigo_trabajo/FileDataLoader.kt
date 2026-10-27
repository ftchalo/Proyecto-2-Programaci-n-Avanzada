package autopartes.datos

import autopartes.factory.RespuestoFactory
import autopartes.modelo.Cliente 
import autopartes.modelo.Respuesto
import autopartes.modelo.Venta
import java.io.File 


class FileDataLoader(val factory: RespuestosFactory) {

    fun cargarRepuestos(rutaArchivo: String): mutableList<repuestos>{
        val lista = mutableListOf<Respuesto>()
        val archivo = File(rutaArchivo)

        if (!archivo.exists()){
            println("[AVISO] No se encontro el archivo de repuestos en $rutaArchivo")
        }
        archivo.forEachLine{ linea -> 
            try {
                if(linea.isNotBlank()){
                    val partes = linea.split(";")

                    if(partes.size >=8){
                        val tipo = partes[0]
                        val sku = partes [1]
                        val nombre = partes[2]
                        val marca = partes[3]
                        val costo = partes[4].toDouble()
                        val stock = partes[5].toInt()
                        val extra1 = partes[6]
                        val extra2 = partes[7].toBoolean()

                        val repuesto = factory.crearRepuesto(tipo, sku, nombre, marca, costo, stock, extra1, extra2)
                        lista.add(repuesto)
                    }

                }
            } catch(e: Exception){
                println("[AVISO] Linea de repuesto descartada por formato incorrecto: ${e.message}")
            }
        
        }
        return lista
    }

    fun cargarClientes(rutaArchivo: String): MutableList<Cliente> {
        val lista = mutableListOf<Cliente>()
        val archivo = File(rutaArchivo)
        
        if (!archivo.exists()) {
            println("[AVISO] No se encontró el archivo de clientes en: $rutaArchivo")
            return lista
        }

        archivo.forEachLine { linea ->
            try {
                if (linea.isNotBlank()) {
                    val partes = linea.split(";")
                    // Formato esperado en el txt: rut;nombre;telefono;esTallerMecanico
                    if (partes.size >= 4) {
                        val rut = partes[0]
                        val nombre = partes[1]
                        val telefono = partes[2]
                        val esTaller = partes[3].toBoolean()
                        
                        val cliente = Cliente(rut, nombre, telefono, esTaller)
                        lista.add(cliente)
                    }
                }
            } catch (e: Exception) {
                println("[AVISO] Línea de cliente descartada por formato incorrecto: ${e.message}")
            }
        }
        return lista
    }

    fun cargarVentas(rutaArchivo: String, clientes: List<Cliente>, repuestos: List<Repuesto>): MutableList<Venta> {
        val lista = mutableListOf<Venta>()
        val archivo = File(rutaArchivo)
    }


    
}