package clases

import java.io.File

/**
 * Módulo encargado de la lectura y escritura seguro de datos iniciales
 * desde archivos de texto plano (.txt).
 *
 * Se define como 'object' para permitir su invocación estática (ej. FileDataLoader.cargarRepuestos(...)).
 */
object FileDataLoader {

    /**
     * Carga el catálogo de repuestos desde el archivo txt especificado.
     * Formato esperado: tipo;sku;nombre;marca;costoBase;stock;extra1;extra2
     */
    fun cargarRepuestos(rutaArchivo: String): MutableList<Repuesto> {
        val lista = mutableListOf<Repuesto>()
        val archivo = File(rutaArchivo)

        if (!archivo.exists()) {
            println("[AVISO] No se encontró el archivo de repuestos en: $rutaArchivo")
            return lista
        }

        archivo.forEachLine { linea ->
            try {
                if (linea.isNotBlank()) {
                    val partes = linea.split(";")
                    if (partes.size >= 8) {
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
                    } else {
                        println("[AVISO] Línea de repuesto descartada (campos insuficientes): $linea")
                    }
                }
            } catch (e: Exception) {
                println("[AVISO] Línea de repuesto descartada por formato incorrecto: ${e.message}")
            }
        }
        return lista
    }

    /**
     * Carga el registro de clientes desde el archivo txt especificado.
     * Formato esperado: rut;nombre;telefono;esTallerMecanico
     */
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
                    if (partes.size >= 4) {
                        val rut = partes[0].trim()
                        val nombre = partes[1].trim()
                        val telefono = partes[2].trim()
                        val esTaller = partes[3].trim().toBoolean()

                        val cliente = Cliente(rut, nombre, telefono, esTaller)
                        lista.add(cliente)
                    } else {
                        println("[AVISO] Línea de cliente descartada (campos insuficientes): $linea")
                    }
                }
            } catch (e: Exception) {
                println("[AVISO] Línea de cliente descartada por formato incorrecto: ${e.message}")
            }
        }
        return lista
    }

    /**
     * Carga el historial de ventas inicial vinculando las entidades Cliente y Repuesto
     * a partir de mapas de búsqueda en memoria (usado en main.kt).
     * Formato esperado: idVenta;fecha;rutCliente;skuRepuesto;cantidad;montoTotal;completada
     */
    fun cargarVentas(
        rutaArchivo: String,
        mapaClientes: Map<String, Cliente>,
        mapaRepuestos: Map<String, Repuesto>
    ): MutableList<Venta> {
        val lista = mutableListOf<Venta>()
        val archivo = File(rutaArchivo)

        if (!archivo.exists()) {
            println("[AVISO] No se encontró el archivo de ventas en: $rutaArchivo")
            return lista
        }

        archivo.forEachLine { linea ->
            try {
                if (linea.isNotBlank()) {
                    val partes = linea.split(";")
                    if (partes.size >= 7) {
                        val idVenta = partes[0].trim().toInt()
                        val fecha = partes[1].trim()
                        val rutCliente = partes[2].trim()
                        val skuRepuesto = partes[3].trim()
                        val cantidad = partes[4].trim().toInt()
                        // partes[5] es el montoTotal que calcula Venta automáticamente
                        val completada = partes[6].trim().toBoolean()

                        val cliente = mapaClientes[rutCliente]
                        val repuesto = mapaRepuestos[skuRepuesto]

                        if (cliente == null) {
                            println("[AVISO] Venta $idVenta descartada: El cliente con RUT '$rutCliente' no existe.")
                        } else if (repuesto == null) {
                            println("[AVISO] Venta $idVenta descartada: El repuesto con SKU '$skuRepuesto' no existe.")
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
                        }
                    } else {
                        println("[AVISO] Línea de venta descartada (campos insuficientes): $linea")
                    }
                }
            } catch (e: Exception) {
                println("[AVISO] Línea de venta descartada por formato incorrecto: ${e.message}")
            }
        }
        return lista
    }

    /**
     * Sobrecarga auxiliar por si prefieres pasar listas directamente en lugar de mapas:
     */
    fun cargarVentas(
        rutaArchivo: String,
        clientes: List<Cliente>,
        repuestos: List<Repuesto>
    ): MutableList<Venta> {
        val mapaClientes = clientes.associateBy { it.rut }
        val mapaRepuestos = repuestos.associateBy { it.codigoSKU }
        return cargarVentas(rutaArchivo, mapaClientes, mapaRepuestos)
    }
}