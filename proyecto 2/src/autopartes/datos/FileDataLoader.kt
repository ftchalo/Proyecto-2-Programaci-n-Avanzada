/*package autopartes.datos

import autopartes.factory.RepuestoFactory
import autopartes.modelo.Cliente
import autopartes.modelo.Repuesto
import autopartes.modelo.Venta
import java.io.File

/**
 * Módulo encargado de la lectura y parseo seguro de datos iniciales desde archivos de texto plano.
 *
 * Utiliza exclusivamente clases estándar del JDK (java.io.File) sin bibliotecas externas.
 */
object FileDataLoader {

    /**
     * Carga el catálogo de repuestos desde un archivo de texto delimitado por punto y coma.
     */
    fun cargarRepuestos(rutaArchivo: String): List<Repuesto> {
        val archivo = File(rutaArchivo)
        if (!archivo.exists()) {
            println("[AVISO] Archivo de repuestos no encontrado en: $rutaArchivo. Se iniciará con catálogo vacío.")
            return emptyList()
        }

        val lista = mutableListOf<Repuesto>()
        var cargados = 0
        var descartados = 0

        archivo.forEachLine { linea ->
            val numLinea = cargados + descartados + 1
            val texto = linea.trim()
            if (texto.isNotEmpty()) {
                val partes = texto.split(";")
                if (partes.size >= 8) {
                    try {
                        val tipo = partes[0].trim()
                        val sku = partes[1].trim()
                        val nombre = partes[2].trim()
                        val marca = partes[3].trim()
                        val costo = partes[4].trim().toDouble()
                        val stock = partes[5].trim().toInt()
                        val extra1 = partes[6].trim()
                        val extra2 = partes[7].trim().toBoolean()

                        val repuesto = RepuestoFactory.crearRepuesto(
                            tipo = tipo,
                            sku = sku,
                            nombre = nombre,
                            marca = marca,
                            costo = costo,
                            stock = stock,
                            extra1 = extra1,
                            extra2 = extra2
                        )
                        lista.add(repuesto)
                        cargados++
                    } catch (e: Exception) {
                        println("[ERROR ARCHIVO] Línea $numLinea descartada en repuestos: ${e.message}")
                        descartados++
                    }
                } else {
                    println("[ERROR ARCHIVO] Línea $numLinea descartada en repuestos (se esperaban 8 campos, se obtuvieron ${partes.size})")
                    descartados++
                }
            }
        }
        println("-> Repuestos cargados desde archivo: $cargados válidos ($descartados descartados)")
        return lista
    }

    /**
     * Carga el registro de clientes desde un archivo de texto.
     */
    fun cargarClientes(rutaArchivo: String): List<Cliente> {
        val archivo = File(rutaArchivo)
        if (!archivo.exists()) {
            println("[AVISO] Archivo de clientes no encontrado en: $rutaArchivo.")
            return emptyList()
        }

        val lista = mutableListOf<Cliente>()
        var cargados = 0
        var descartados = 0

        archivo.forEachLine { linea ->
            val numLinea = cargados + descartados + 1
            val texto = linea.trim()
            if (texto.isNotEmpty()) {
                val partes = texto.split(";")
                if (partes.size >= 4) {
                    try {
                        val rut = partes[0].trim()
                        val nombre = partes[1].trim()
                        val telefono = partes[2].trim()
                        val esTaller = partes[3].trim().toBoolean()

                        val cliente = Cliente(rut, nombre, telefono, esTaller)
                        lista.add(cliente)
                        cargados++
                    } catch (e: Exception) {
                        println("[ERROR ARCHIVO] Línea $numLinea descartada en clientes: ${e.message}")
                        descartados++
                    }
                } else {
                    println("[ERROR ARCHIVO] Línea $numLinea descartada en clientes (campos insuficientes)")
                    descartados++
                }
            }
        }
        println("-> Clientes cargados desde archivo: $cargados válidos ($descartados descartados)")
        return lista
    }

    /**
     * Carga el historial de ventas inicial vinculando clientes y repuestos ya presentes en memoria.
     */
    fun cargarVentas(
        rutaArchivo: String,
        clientesMap: Map<String, Cliente>,
        repuestosMap: Map<String, Repuesto>
    ): List<Venta> {
        val archivo = File(rutaArchivo)
        if (!archivo.exists()) {
            println("[AVISO] Archivo de ventas no encontrado en: $rutaArchivo.")
            return emptyList()
        }

        val lista = mutableListOf<Venta>()
        var cargados = 0
        var descartados = 0

        archivo.forEachLine { linea ->
            val numLinea = cargados + descartados + 1
            val texto = linea.trim()
            if (texto.isNotEmpty()) {
                val partes = texto.split(";")
                if (partes.size >= 7) {
                    try {
                        val idVenta = partes[0].trim().toInt()
                        val fecha = partes[1].trim()
                        val rutCliente = partes[2].trim()
                        val skuRepuesto = partes[3].trim()
                        val cantidad = partes[4].trim().toInt()
                        val completada = partes[6].trim().toBoolean()

                        val cliente = clientesMap[rutCliente]
                        val repuesto = repuestosMap[skuRepuesto]

                        if (cliente == null) {
                            println("[ERROR ARCHIVO] Línea $numLinea de venta descartada: Cliente $rutCliente no existe.")
                            descartados++
                        } else if (repuesto == null) {
                            println("[ERROR ARCHIVO] Línea $numLinea de venta descartada: Repuesto $skuRepuesto no existe.")
                            descartados++
                        } else {
                            val venta = Venta(
                                idVenta = idVenta,
                                fecha = fecha,
                                cliente = cliente,
                                repuesto = repuesto,
                                cantidad = cantidad,
                                completadaInicial = completada
                            )
                            lista.add(venta)
                            cargados++
                        }
                    } catch (e: Exception) {
                        println("[ERROR ARCHIVO] Línea $numLinea descartada en ventas: ${e.message}")
                        descartados++
                    }
                } else {
                    println("[ERROR ARCHIVO] Línea $numLinea descartada en ventas (campos insuficientes)")
                    descartados++
                }
            }
        }
        println("-> Ventas cargadas desde archivo: $cargados válidas ($descartados descartadas)")
        return lista
    }
}
 */