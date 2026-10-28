package clases

/*
*Menú interactivo por consola
*
*Este archivo implementa una interfaz con entradas mediante teclado.
*/

class Terminal(private val manager: TiendaManager) {
    private var entidadActual: String = "REPUESTO" //Respuesto, CLIENTE o Venta


    //Comienzo del bucle interactivo (menú)
    fun iniciar(){
        println("//----------------------------------------------//")
        println("          - BIENVENIDO A AUTOPARTESPRO -          ")
        println("//----------------------------------------------//")

        var ejecutando = true
        while(ejecutando) {
            MostrarMenu()
            val entrada = readlnOrNull()?.trim() ?: ""
            when (entrada) {
                "1" -> SeleccionarEntidad()
                "2" -> CrearRegistro()
                "3" -> ListarRegistros()
                "4" -> EliminarRegistro()
                "0" -> {
                    print("¿Esta seguro de que desea salir del sistema? (S/N): ")
                    val confirmacion = readlnOrNull()?.trim()?.uppercase() ?: "N"
                    if (confirmacion == "S") {
                        ejecutando = false
                        println("\n¡Gracias por utilizar AutopartesPro! Cierre de sesion exitoso.")
                    }
                }
                else -> {
                println("[ERROR] Opcion no valida. Ingrese un numero entre 0 y 4.")
                }
            }

        }
    }
        
    //Mostrar menú de opciones
    private fun MostrarMenu(){
        println("---------------------------------------------------------------")
        println("POR FAVOR DIGITE UN NUMERO PARA SELECCIONAR UNA OPCIÓN DEL MENÚ")
        println("---------------------------------------------------------------")
        println("(1): Seleccionar entidad.")
        println("(2): Crear registro.")
        println("(3): Listar registros.")
        println("(4): Eliminar registro.")
        println("(0): Salir.")
        println("---------------------------------------------------------------")
        println("---------------------------------------------------------------\n\n")
    }

    // (1). Seleccionar entidad
    private fun SeleccionarEntidad(){
        println("\n--- SELECCIONAR ENTIDAD DE TRANAJO ---")
        println("(1). Respuestos (Inventario)")
        println("(2). CLIENTEs ")
        println("(3). Ventas")
        println("(4). Escoja [1-3]")
        when (readlnOrNull()?.trim()) {
            "1" -> {
                entidadActual = "REPUESTO"
            println("Entidad selecionada: REPUESTO")
            }
            "2" -> {
                entidadActual = "CLIENTE"
            println("Entidad selecionada: CLIENTE")
            }
            "3" -> {
                entidadActual = "VENTA"
            println("Entidad selecionada: Venta")
            }
            else -> println("[Error] Opcion invalida. Se mantendra la entidad actual: $entidadActual")
        }
    }

    // (2). Crear registro
    fun crearRegistro() {
        println("\n---CREAR REGISTRO---")
        when (entidadActual) {
            "REPUESTO" -> crearRepuestoTerminal()
            "CLIENTE" -> crearClienteTerminal()
            "VENTA" -> crearVentaTerminal()
            else -> println("[ERROR] Entidad no valida.")
        }
    }
    // (2.1). Crear repuesto
    fun crearRepuestoTerminal() {
        println("Ingrese el codigo SKU del repuesto:")
        val codigoSKU = readlnOrNull()?.trim() ?: ""
        println("Ingrese el nombre del repuesto:")
        val nombre = readlnOrNull()?.trim() ?: ""
        println("Ingrese la marca del repuesto:")
        val marca = readlnOrNull()?.trim() ?: ""
        println("Ingrese el stock del repuesto:")
        val stock = readlnOrNull()?.toIntOrNull() ?: 0
        println("Ingrese el precio de compra del repuesto:")
        val costoBase = readlnOrNull()?.toDoubleOrNull() ?: 0.0
        println("Ingrese la descripción de la ficha tecnica del repuesto:")
        val descripcionFichaTecnica = readlnOrNull()?.trim() ?: ""

        println("Seleccione tipo: (1) Original OEM  (2) Alternativo Genérico:")
        val tipo = readlnOrNull()?.trim() ?: "1"
        val repuesto: Repuesto = if (tipo == "1") {
            print("Meses de garantía: ")
            val garantia = readlnOrNull()?.toIntOrNull() ?: 12
            print("¿Tiene certificado OEM? (S/N): ")
            val oem = readlnOrNull()?.trim()?.uppercase() == "S"
            RepuestoOriginal(codigoSKU, nombre, marca, costoBase, stock, garantia, oem)
        } else {
            print("Fabricante generico: ")
            val fab = readlnOrNull()?.trim() ?: "Genérico"
            print("¿Incluye kit de instalacion? (S/N): ")
            val kit = readlnOrNull()?.trim()?.uppercase() == "S"
            RepuestoAlternativo(codigoSKU, nombre, marca, costoBase, stock, fab, kit)
        }
        manager.agregarRepuesto(repuesto)
        println("Repuesto agregado exitosamente.")
    }

    // (2.2). Crear cliente
    fun crearClienteTerminal() {
        print("Ingrese RUT del cliente: ")
        val rut = readlnOrNull()?.trim() ?: ""
        
        print("Ingrese nombre: ")
        val nombre = readlnOrNull()?.trim() ?: ""
        
        print("Ingrese telefono: ")
        val telefono = readlnOrNull()?.trim() ?: ""
        
        print("¿Es taller mecánico con convenio? (S/N): ")
        val esTaller = readlnOrNull()?.trim()?.uppercase() == "S"
        
        val cliente = Cliente(rut, nombre, telefono, esTaller)
        manager.agregarCliente(cliente)
        println("Cliente agregado exitosamente.")
    }
    // (2.3). Crear venta
    fun crearVentaTerminal() {
        print("Ingrese ID  de la venta (ej. 1010): ")
        val idVenta = readlnOrNull()?.toIntOrNull() ?: 0

        print("Ingrese RUT del cliente comprador: ")
        val rutCliente = readlnOrNull()?.trim() ?: ""
        val cliente = manager.buscarCliente(rutCliente)
        if (cliente == null) {
            println("[ERROR] El cliente no existe. Registrelo primero en la opcion (2).")
            return
        }

        print("Ingrese codigo SKU del repuesto vendido: ")
        val codigoSKU = readlnOrNull()?.trim() ?: ""
        val repuesto = manager.buscarRepuesto(codigoSKU)
        if (repuesto == null) {
            println("[ERROR] El repuesto no existe en el catalogo.")
            return
        }

        print("Ingrese cantidad de piezas a comprar: ")
        val cantidad = readlnOrNull()?.toIntOrNull() ?: 1

        val venta = Venta(idVenta, cliente, repuesto, cantidad)
        try {
            manager.registrarVenta(venta)
            println("Venta registrada exitosamente.")
        } catch (e: Exception) {
            println("[ERROR AL REGISTRAR VENTA] ${e.message}")
        }
    }
    //(3) Listar Registros
    private fun ListarRegistros() {
        when(entidadActual){
            "REPUESTO" -> {
                println("--- Catalogo de repuestos ---")
                val listaRepuestos = manager.listarRepuestos()
                if (listaRepuestos.isEmpty()) {
                    println("No hay repuestos en el catalogo.")
                    return
                }
                for (r in listaRepuestos) {
                    println(r.obtenerFichaTecnica() + " | Precio Venta: $${"%.0f".format(r.calcularPrecioVenta())}")
                }
            }
            "CLIENTE" -> {
                println("--- Lista de clientes ---")
                val clientes = manager.listarClientes()
                if (clientes.isEmpty()) {
                    println("No hay clientes registrados.")
                    return
                }
                for (c in clientes) {
                    println(c.obtenerFicha())
                }
            }
            "VENTA" -> {
                println("--- Lista de ventas ---")
                val ventas = manager.listarVentas()
                if (ventas.isEmpty()) {
                    println("No hay ventas registradas.")
                    return
                }
                for (v in ventas) {
                    println("Venta #${v.idVenta} | Cliente: ${v.cliente.nombre} | Pieza: [${v.repuesto.codigoSKU}] ${v.repuesto.nombre} | Cant: ${v.cantidad} | Total: $${"%.0f".format(v.montoTotal)}")
                }
            }
            else -> println("[ERROR] Entidad no válida.")
        }
    }
    
    //(4) Eliminar registro
    private fun EliminarRegistro() {
        println("\n---ELIMINAR REGISTRO---")
        when (entidadActual) {
            "REPUESTO" -> {
                println("Ingrese el código SKU del repuesto a eliminar:")
                val codigoSKU = readlnOrNull()?.trim() ?: ""
                if (manager.eliminarRepuesto(codigoSKU)) {
                    println("Repuesto eliminado exitosamente.")
                } else {
                    println("[ERROR] No se encontro un repuesto con el codigo SKU proporcionado.")
                }
            }
            "CLIENTE" -> {
                print("Ingrese el RUT del cliente a eliminar: ")
                val rut = readlnOrNull()?.trim() ?: ""
                try {
                    if (manager.eliminarCliente(rut)) {
                        println("Cliente eliminado exitosamente.")
                    } else {
                        println("[ERROR] No se encontró ningún cliente con ese RUT.")
                    }
                } catch (e: Exception) {
                    println("[ERROR DE INTEGRIDAD] ${e.message}")
                }
            }
            "VENTA" -> {
                print("Ingrese el ID de la venta a anular: ")
                val idVenta = readlnOrNull()?.toIntOrNull()
                if (idVenta == null) {
                    println("[ERROR] El ID de la venta debe ser un numero entero valido (ej. 1001).")
                    return
                }
                
                if (manager.anularVenta(idVenta)) {
                    println("Venta anulada exitosamente y stock devuelto al inventario.")
                } else {
                    println("[ERROR] No se encontro ninguna venta completada con el ID $idVenta.")
                }
            }
            else -> println("[ERROR] Entidad no valida.")
        }
    }
}