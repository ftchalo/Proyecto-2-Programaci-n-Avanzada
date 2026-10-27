package clases

/*
*Punto de entrada principal de la aplicación
*
*Carga de datos iniciales desde los archivos de texto
*/

fun main(){
    val carpetaDatos = if (File("datos").exists()){
        "datos"
    }
    else if {//rellenar

    }
    else {//rellenar

    }
    val manager = TiendaManager()

    //1. Carga de repuestos
    val repuestosCargados = FileDataLoader.cargarRepuestos() //rellenar
    for (r in repuestosCargados){
        manager.agregarRepuesto(r)
    }

    //2. Carga de clientes
    val clientesCargados = FileDataLoader.cargarClientes() //rellenar
    for (c in clientesCargados) {
        manager.agregarCliente(c)
    }

    //3. Carga de ventas vinculando mapas de búsqueda en memoria
    val mapaClientes = manager.clientes.associateBy {} //rellenar
    val mapaRepuestos = manager.repuestos.associateBy {} //rellenar
    val ventasCargadas = FileDataLoader.cargarVentas{} //rellenar
    for (v in ventasCargadas) {
        manager.cargarVentaInicial(v)
    }

    println("Incialización completa con éxito!")
    println("-----------------------------------------------------")

    //comienzo del programa.
    val linkstarto = TerminalREPL(manager)
    linkstarto.inciar()
}