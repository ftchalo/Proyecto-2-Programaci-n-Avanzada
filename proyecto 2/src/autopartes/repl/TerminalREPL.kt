package autopartes.repl

import autopartes.factory.RepuestoFactory
import autopartes.modelo.Cliente
import autopartes.modelo.Venta
import autopartes.servicio.TiendaManager

/**
 * Interfaz interactiva de consola (Read-Eval-Print Loop).
 *
 * Implementa un menú continuo robusto a entradas erróneas mediante bloques try-catch.
 */
class TerminalREPL(private val manager: TiendaManager) {

    private var entidadActual: String = "REPUESTO" // REPUESTO, CLIENTE, VENTA

    /**
     * Bucle principal interactivo.
     */
    fun iniciar() {
        println("=============================================================")
        println("       BIENVENIDO A AUTOPARTESPRO - GESTIÓN COMERCIAL        ")
        println("=============================================================")

        var ejecutando = true
        while (ejecutando) {
            mostrarMenuPrincipal()
            print("Seleccione una opción: ")
            val entrada = readlnOrNull()?.trim() ?: ""

            try {
                when (entrada) {
                    "1" -> seleccionarEntidad()
                    "2" -> crearRegistro()
                    "3" -> listarRegistros()
                    "4" -> eliminarRegistro()
                    "5" -> operacionesEspeciales()
                    "0" -> {
                        print("¿Está seguro de que desea salir del sistema? (S/N): ")
                        val confirmacion = readlnOrNull()?.trim()?.uppercase() ?: "N"
                        if (confirmacion == "S") {
                            ejecutando = false
                            println("\n¡Gracias por utilizar AutopartesPro! Cierre de sesión exitoso.")
                        }
                    }
                    else -> {
                        println("[ERROR] Opción no válida. Ingrese un número entre 0 y 5.")
                    }
                }
            } catch (e: Exception) {
                println("[ERROR INESPERADO] Ocurrió un fallo en la operación: ${e.message}")
            }
            println()
        }
    }

    private fun mostrarMenuPrincipal() {
        println("-------------------------------------------------------------")
        println("MENÚ PRINCIPAL | Entidad activa actual: [$entidadActual]")
        println("-------------------------------------------------------------")
        println("1. Seleccionar entidad de trabajo (Repuestos / Clientes / Ventas)")
        println("2. Crear nuevo registro en [$entidadActual]")
        println("3. Listar todos los registros de [$entidadActual]")
        println("4. Eliminar un registro de [$entidadActual]")
        println("5. Consultas y reportes avanzados")
        println("0. Salir del sistema")
        println("-------------------------------------------------------------")
    }

    // -------------------------------------------------------------
    // 1. SELECCIONAR ENTIDAD
    // -------------------------------------------------------------
    private fun seleccionarEntidad() {
        println("\n--- SELECCIONAR ENTIDAD DE TRABAJO ---")
        println("1. Repuestos (Inventario de piezas)")
        println("2. Clientes (Particulares y Talleres)")
        println("3. Ventas (Órdenes comerciales)")
        print("Elija la entidad [1-3]: ")
        when (readlnOrNull()?.trim()) {
            "1" -> {
                entidadActual = "REPUESTO"
                println("-> Entidad activa cambiada a: REPUESTOS")
            }
            "2" -> {
                entidadActual = "CLIENTE"
                println("-> Entidad activa cambiada a: CLIENTES")
            }
            "3" -> {
                entidadActual = "VENTA"
                println("-> Entidad activa cambiada a: VENTAS")
            }
            else -> println("[ERROR] Opción inválida. Se mantiene la entidad actual: $entidadActual")
        }
    }

    // -------------------------------------------------------------
    // 2. CREAR REGISTRO
    // -------------------------------------------------------------
    private fun crearRegistro() {
        when (entidadActual) {
            "REPUESTO" -> crearRepuestoInteractivo()
            "CLIENTE" -> crearClienteInteractivo()
            "VENTA" -> crearVentaInteractiva()
        }
    }

    private fun crearRepuestoInteractivo() {
        println("\n--- ALTA DE NUEVO REPUESTO ---")
        val sku = leerTexto("Ingrese código SKU (ej. OEM-301): ")
        if (manager.buscarRepuesto(sku) != null) {
            println("[ERROR] Ya existe un repuesto registrado con el SKU '$sku'. Operación cancelada.")
            return
        }
        val nombre = leerTexto("Ingrese nombre del repuesto: ")
        val marca = leerTexto("Ingrese marca: ")
        val costo = leerDouble("Ingrese costo base ($): ")
        val stock = leerEntero("Ingrese unidades en stock inicial: ")

        println("Seleccione el tipo de repuesto:")
        println("1. Original OEM (Garantía extendida y certificación de fábrica)")
        println("2. Alternativo / Genérico (Económico, opción de kit universal)")
        val tipoOpcion = leerEntero("Opción [1-2]: ")

        val tipoStr: String
        val extra1: String
        val extra2: Boolean

        if (tipoOpcion == 1) {
            tipoStr = "ORIGINAL"
            val meses = leerEntero("Ingrese meses de garantía de fábrica: ")
            extra1 = meses.toString()
            extra2 = leerBoolean("¿Cuenta con certificado OEM de fábrica? (S/N): ")
        } else {
            tipoStr = "ALTERNATIVO"
            extra1 = leerTexto("Ingrese fabricante genérico: ")
            extra2 = leerBoolean("¿Incluye kit de instalación universal? (S/N): ")
        }

        try {
            val nuevo = RepuestoFactory.crearRepuesto(
                tipo = tipoStr,
                sku = sku,
                nombre = nombre,
                marca = marca,
                costo = costo,
                stock = stock,
                extra1 = extra1,
                extra2 = extra2
            )
            val exito = manager.agregarRepuesto(nuevo)
            if (exito) {
                println("[ÉXITO] Repuesto registrado correctamente.")
                println("Precio de venta al público calculado: $${"%.0f".format(nuevo.calcularPrecioVenta())}")
            } else {
                println("[ERROR] No fue posible agregar el repuesto al inventario.")
            }
        } catch (e: Exception) {
            println("[ERROR] No se pudo crear el repuesto: ${e.message}")
        }
    }

    private fun crearClienteInteractivo() {
        println("\n--- REGISTRO DE NUEVO CLIENTE ---")
        val rut = leerTexto("Ingrese RUT del cliente (ej. 19.876.543-2): ")
        if (manager.buscarCliente(rut) != null) {
            println("[ERROR] Ya existe un cliente registrado con ese RUT. Operación cancelada.")
            return
        }
        val nombre = leerTexto("Ingrese nombre o razón social: ")
        val telefono = leerTexto("Ingrese teléfono de contacto: ")
        val esTaller = leerBoolean("¿Es un taller mecánico con convenio? (S/N): ")

        try {
            val cliente = Cliente(rut, nombre, telefono, esTaller)
            if (!cliente.validarRut()) {
                println("[AVISO] El RUT ingresado no parece tener formato chileno estándar (XX.XXX.XXX-X). Se registrará de todas formas.")
            }
            val exito = manager.agregarCliente(cliente)
            if (exito) {
                println("[ÉXITO] Cliente registrado correctamente.")
            } else {
                println("[ERROR] No fue posible registrar al cliente.")
            }
        } catch (e: Exception) {
            println("[ERROR] Error al crear cliente: ${e.message}")
        }
    }

    private fun crearVentaInteractiva() {
        println("\n--- REGISTRAR NUEVA ORDEN DE VENTA ---")
        val idVenta = leerEntero("Ingrese número correlativo de venta (ej. 1010): ")
        if (manager.buscarVenta(idVenta) != null) {
            println("[ERROR] Ya existe una venta registrada con el ID $idVenta.")
            return
        }
        val rutCliente = leerTexto("Ingrese RUT del cliente comprador: ")
        val cliente = manager.buscarCliente(rutCliente)
        if (cliente == null) {
            println("[ERROR] Cliente no encontrado. Registre al cliente antes de procesar la venta.")
            return
        }

        val sku = leerTexto("Ingrese código SKU del repuesto a vender: ")
        val repuesto = manager.buscarRepuesto(sku)
        if (repuesto == null) {
            println("[ERROR] Repuesto con SKU '$sku' no encontrado en catálogo.")
            return
        }

        println("Pieza seleccionada: ${repuesto.nombre} | Stock actual: ${repuesto.stock}")
        val precioUnitario = repuesto.calcularPrecioVenta()
        println("Precio de venta unitario: $${"%.0f".format(precioUnitario)}")

        val cantidad = leerEntero("Ingrese cantidad de unidades a vender: ")
        if (!repuesto.tieneStockSuficiente(cantidad)) {
            println("[ERROR] Stock insuficiente. Solo hay ${repuesto.stock} unidades disponibles.")
            return
        }

        try {
            val venta = Venta(idVenta, cliente, repuesto, cantidad)
            val exito = manager.registrarVenta(venta)
            if (exito) {
                println("[ÉXITO] Venta procesada y stock rebajado correctamente.")
                println(venta.generarComprobante())
            } else {
                println("[ERROR] No se pudo procesar la venta.")
            }
        } catch (e: Exception) {
            println("[ERROR AL PROCESAR VENTA] ${e.message}")
        }
    }

    // -------------------------------------------------------------
    // 3. LISTAR REGISTROS
    // -------------------------------------------------------------
    private fun listarRegistros() {
        when (entidadActual) {
            "REPUESTO" -> {
                println("\n--- CATÁLOGO DE REPUESTOS EN BODEGA ---")
                val repuestos = manager.repuestos
                if (repuestos.isEmpty()) {
                    println("(No hay repuestos registrados en el catálogo)")
                    return
                }
                println("%-10s | %-30s | %-12s | %-6s | %-12s | %s".format("SKU", "NOMBRE", "MARCA", "STOCK", "P. VENTA", "DETALLE"))
                println("-".repeat(95))
                for (r in repuestos) {
                    val pVenta = "$${"%.0f".format(r.calcularPrecioVenta())}"
                    println("%-10s | %-30s | %-12s | %-6d | %-12s | %s".format(
                        r.codigoSKU,
                        r.nombre.take(30),
                        r.marca.take(12),
                        r.stock,
                        pVenta,
                        r.obtenerFichaTecnica()
                    ))
                }
            }
            "CLIENTE" -> {
                println("\n--- BASE DE CLIENTES REGISTRADOS ---")
                val clientes = manager.clientes
                if (clientes.isEmpty()) {
                    println("(No hay clientes registrados)")
                    return
                }
                for (c in clientes) {
                    println(c.obtenerFicha())
                }
            }
            "VENTA" -> {
                println("\n--- HISTORIAL DE VENTAS REALIZADAS ---")
                val ventas = manager.ventas
                if (ventas.isEmpty()) {
                    println("(No hay ventas registradas en el historial)")
                    return
                }
                println("%-6s | %-12s | %-14s | %-10s | %-5s | %-12s | %s".format(
                    "ID", "FECHA", "RUT CLIENTE", "SKU REP", "CANT", "TOTAL", "ESTADO"
                ))
                println("-".repeat(85))
                for (v in ventas) {
                    val totalStr = "$${"%.0f".format(v.montoTotal)}"
                    val estado = if (v.completada) "COMPLETADA" else "CANCELADA"
                    println("%-6d | %-12s | %-14s | %-10s | %-5d | %-12s | %s".format(
                        v.idVenta,
                        v.fecha,
                        v.cliente.rut,
                        v.repuesto.codigoSKU,
                        v.cantidad,
                        totalStr,
                        estado
                    ))
                }
            }
        }
    }

    // -------------------------------------------------------------
    // 4. ELIMINAR REGISTRO
    // -------------------------------------------------------------
    private fun eliminarRegistro() {
        when (entidadActual) {
            "REPUESTO" -> {
                println("\n--- ELIMINAR REPUESTO DEL CATÁLOGO ---")
                val sku = leerTexto("Ingrese el código SKU de la pieza a eliminar: ")
                val repuesto = manager.buscarRepuesto(sku)
                if (repuesto == null) {
                    println("[ERROR] No se encontró ningún repuesto con SKU '$sku'.")
                    return
                }
                println("Pieza encontrada: ${repuesto.nombre} (${repuesto.marca}) - Stock: ${repuesto.stock}")
                val confirmar = leerBoolean("¿Confirma la eliminación permanente de este repuesto? (S/N): ")
                if (confirmar) {
                    try {
                        val eliminado = manager.eliminarRepuesto(sku)
                        if (eliminado) {
                            println("[ÉXITO] Repuesto eliminado satisfactoriamente.")
                        } else {
                            println("[ERROR] No se pudo eliminar el repuesto.")
                        }
                    } catch (e: IllegalStateException) {
                        println("[ERROR DE INTEGRIDAD] ${e.message}")
                    }
                } else {
                    println("Operación de eliminación cancelada por el usuario.")
                }
            }
            "CLIENTE" -> {
                println("\n--- ELIMINAR CLIENTE DE LA BASE DE DATOS ---")
                val rut = leerTexto("Ingrese el RUT del cliente a eliminar: ")
                val cliente = manager.buscarCliente(rut)
                if (cliente == null) {
                    println("[ERROR] No se encontró ningún cliente con RUT '$rut'.")
                    return
                }
                println("Cliente encontrado: ${cliente.nombre} (${cliente.rut})")
                val confirmar = leerBoolean("¿Confirma la eliminación de este cliente? (S/N): ")
                if (confirmar) {
                    try {
                        val eliminado = manager.eliminarCliente(rut)
                        if (eliminado) {
                            println("[ÉXITO] Cliente eliminado correctamente.")
                        } else {
                            println("[ERROR] No se pudo eliminar el cliente.")
                        }
                    } catch (e: IllegalStateException) {
                        println("[ERROR DE INTEGRIDAD] ${e.message}")
                    }
                } else {
                    println("Operación cancelada.")
                }
            }
            "VENTA" -> {
                println("\n--- ANULAR Y ELIMINAR VENTA ---")
                val idVenta = leerEntero("Ingrese el ID de la venta a anular: ")
                val venta = manager.buscarVenta(idVenta)
                if (venta == null) {
                    println("[ERROR] No se encontró ninguna venta con ID $idVenta.")
                    return
                }
                println("Venta encontrada: Venta #$idVenta | Total: $${"%.0f".format(venta.montoTotal)} | Pieza: ${venta.repuesto.nombre} (${venta.cantidad} u)")
                val confirmar = leerBoolean("¿Confirma la anulación y restitución de stock? (S/N): ")
                if (confirmar) {
                    val anulada = manager.anularVenta(idVenta)
                    if (anulada) {
                        println("[ÉXITO] Venta anulada y stock devuelto a bodega.")
                    } else {
                        println("[ERROR] No fue posible anular la venta.")
                    }
                } else {
                    println("Operación cancelada.")
                }
            }
        }
    }

    // -------------------------------------------------------------
    // 5. CONSULTAS Y REPORTES
    // -------------------------------------------------------------
    private fun operacionesEspeciales() {
        println("\n--- REPORTES Y CONSULTAS AVANZADAS ---")
        println("1. Consultar repuestos en stock crítico (<= 5 unidades)")
        println("2. Consultar historial de compras por cliente")
        println("3. Resumen financiero total recaudado")
        print("Seleccione una opción: ")

        when (readlnOrNull()?.trim()) {
            "1" -> {
                val criticos = manager.repuestosStockCritico(5)
                println("\n=== ALERTA DE STOCK CRÍTICO ===")
                if (criticos.isEmpty()) {
                    println("Todos los repuestos cuentan con stock saludable (> 5 unidades).")
                } else {
                    for (r in criticos) {
                        println("[CRÍTICO] ${r.codigoSKU}: ${r.nombre} -> Quedan ${r.stock} unidades!")
                    }
                }
            }
            "2" -> {
                val rut = leerTexto("Ingrese RUT del cliente: ")
                val ventasCliente = manager.ventasPorCliente(rut)
                if (ventasCliente.isEmpty()) {
                    println("El cliente con RUT '$rut' no registra compras históricas.")
                } else {
                    println("\nCompras realizadas por cliente con RUT $rut:")
                    for (v in ventasCliente) {
                        println("  Venta #${v.idVenta} (${v.fecha}): ${v.cantidad}x ${v.repuesto.nombre} = $${"%.0f".format(v.montoTotal)}")
                    }
                }
            }
            "3" -> {
                val total = manager.totalRecaudado()
                println("\n=== RESUMEN FINANCIERO DE VENTAS ===")
                println("Total facturado en órdenes completadas: $${"%.0f".format(total)}")
                println("Cantidad de órdenes registradas: ${manager.ventas.size}")
            }
            else -> println("[ERROR] Opción no válida.")
        }
    }

    // -------------------------------------------------------------
    // HELPERS ROBUSTOS DE LECTURA CON VALIDACIÓN Y TRY-CATCH
    // -------------------------------------------------------------

    private fun leerTexto(prompt: String): String {
        while (true) {
            print(prompt)
            val entrada = readlnOrNull()?.trim() ?: ""
            if (entrada.isNotEmpty()) return entrada
            println("[ERROR] El campo no puede estar vacío. Intente de nuevo.")
        }
    }

    private fun leerEntero(prompt: String): Int {
        while (true) {
            print(prompt)
            val entrada = readlnOrNull()?.trim() ?: ""
            try {
                return entrada.toInt()
            } catch (e: NumberFormatException) {
                println("[ERROR] Entrada inválida. Debe ingresar un número entero (ej. 12). Intente de nuevo.")
            }
        }
    }

    private fun leerDouble(prompt: String): Double {
        while (true) {
            print(prompt)
            val entrada = readlnOrNull()?.trim() ?: ""
            try {
                val num = entrada.toDouble()
                if (num > 0.0) return num
                println("[ERROR] El valor debe ser un número positivo.")
            } catch (e: NumberFormatException) {
                println("[ERROR] Entrada inválida. Debe ingresar un valor numérico (ej. 25000.0). Intente de nuevo.")
            }
        }
    }

    private fun leerBoolean(prompt: String): Boolean {
        while (true) {
            print(prompt)
            val entrada = readlnOrNull()?.trim()?.uppercase() ?: ""
            when (entrada) {
                "S", "SI", "SÍ", "TRUE", "1" -> return true
                "N", "NO", "FALSE", "0" -> return false
                else -> println("[ERROR] Ingrese 'S' para Sí o 'N' para No.")
            }
        }
    }
}
