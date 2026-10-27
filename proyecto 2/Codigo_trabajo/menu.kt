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
            when (entrada) {
                "1" -> SeleccionarEntidad()
                "2" -> CrearRegistro()
                "3" -> ListarRegistros()
                "4" -> EliminarRegistro()
                "5" -> OperacionesEspeciales()
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

        }
    }
        
    //Mostrar menú de opciones
    private fun MostrarMenu(){
        println("---------------------------------------------------------------")
        println("POR FAVOR DIGITE UN NUMERO PARA SELECCIONAR UNA OPCIÓN DEL MENÚ")
        println("---------------------------------------------------------------")
        println("(1):Seleccionar entidad.")
        println("(2):Crear registro.")
        println("(3):Listar registros.")
        println("(4):Eliminar registro.")
        println("(5):Oraciones de consulta y reportes.")
        println("(0):Salir.")
        println("---------------------------------------------------------------")
        println("---------------------------------------------------------------\n\n")
    }

    // (1). Seleccionar entidad
    private fun seleccionarEntidad(){
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
                entidadActual = "Venta"
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
            else -> println("[ERROR] Entidad no válida.")
        }
    }
    // (2.1). Crear repuesto
    fun crearRepuestoTerminal() {
        println("Ingrese el código SKU del repuesto:")
        val codigoSKU = readlnOrNull()?.trim() ?: ""
        println("Ingrese el nombre del repuesto:")
        val nombre = readlnOrNull()?.trim() ?: ""
        println("Ingrese la marca del repuesto:")
        val marca = readlnOrNull()?.trim() ?: ""
        println("Ingrese el stock del repuesto:")
        val stock = readlnOrNull()?.toIntOrNull() ?: 0
        println("Ingrese el precio de compra del repuesto:")
        val precioCompra = readlnOrNull()?.toDoubleOrNull() ?: 0.0
        println("Ingrese la descripción de la ficha técnica del repuesto:")
        val descripcionFichaTecnica = readlnOrNull()?.trim() ?: ""

        val repuesto = Repuesto(codigoSKU, nombre, marca, stock, precioCompra, descripcionFichaTecnica)
        manager.agregarRepuesto(repuesto)
        println("Repuesto agregado exitosamente.")
    }
    // (2.2). Crear cliente
    fun crearClienteTerminal() {
        println("Ingrese el nombre del cliente:")
        val nombre = readlnOrNull()?.trim() ?: ""
        println("Ingrese el correo electrónico del cliente:")
        val correoElectronico = readlnOrNull()?.trim() ?: ""
        println("Ingrese el número de teléfono del cliente:")
        val numeroTelefono = readlnOrNull()?.trim() ?: ""

        val cliente = Cliente(nombre, correoElectronico, numeroTelefono)
        manager.agregarCliente(cliente)
        println("Cliente agregado exitosamente.")
    }
    // (2.3). Crear venta
    fun crearVentaTerminal() {
        println("Ingrese el código SKU del repuesto vendido:")
        val codigoSKU = readlnOrNull()?.trim() ?: ""
        println("Ingrese la cantidad vendida:")
        val cantidadVendida = readlnOrNull()?.toIntOrNull() ?: 0
        println("Ingrese el nombre del cliente:")
        val nombreCliente = readlnOrNull()?.trim() ?: ""

        val venta = Venta(codigoSKU, cantidadVendida, nombreCliente)
        manager.agregarVenta(venta)
        println("Venta registrada exitosamente.")
    }
    //(3) Listar Registros
    private fun ListarRegistros() {
        when(entidadActual){
            "REPUESTO" -> {
                println("--- Catalogo de repuestos ---")
                val repuesto = manager.listarRepuestos()
                if(repuestos.isEmpty()){ /*si el contendio está vacio devuelve un true*/
                    println("No hay repuestos en el catalogo") 
                    return
                }
                for (r in listarRepuestos){
                    val pVenta = {r.calcularPrecioVenta()}
                    println($r.codigoSKU)
                    r.codigoSKU,
                    r.nombre,
                    r.marca,
                    r.stock,
                    pVenta,
                    r.obtenerFichaTecnica()
                }
            }
            "CLIENTE" ->{
                println("--- Lista de clientes ---")
                val clientes = manager.ListarClientes()
                if(clientes.isEmpty()){
                    println("No hay clientes registrados")
                    return
                }
                for (c in clientes){
                    println(c.nombre)
                    println(c.correoElectronico)
                    println(c.numeroTelefono)
                }
            }
            "VENTA" ->{
                println("--- Lista de ventas ---")
                val ventas = manager.listarVentas()
                if(ventas.isEmpty()){
                    println("No hay ventas registradas")
                    return
                }
                for (v in ventas){
                    println(v.codigoSKU)
                    println(v.cantidadVendida)
                    println(v.nombreCliente)
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
                    println("[ERROR] No se encontró un repuesto con el código SKU proporcionado.")
                }
            }
            "CLIENTE" -> {
                println("Ingrese el nombre del cliente a eliminar:")
                val nombreCliente = readlnOrNull()?.trim() ?: ""
                if (manager.eliminarCliente(nombreCliente)) {
                    println("Cliente eliminado exitosamente.")
                } else {
                    println("[ERROR] No se encontró un cliente con el nombre proporcionado.")
                }
            }
            "VENTA" -> {
                println("Ingrese el código SKU de la venta a eliminar:")
                val codigoSKU = readlnOrNull()?.trim() ?: ""
                if (manager.eliminarVenta(codigoSKU)) {
                    println("Venta eliminada exitosamente.")
                } else {
                    println("[ERROR] No se encontró una venta con el código SKU proporcionado.")
                }
            }
            else -> println("[ERROR] Entidad no válida.")
        }
    }

    //(5) Operaciones especiales











}