package clases

import java.io.File

/*
 * Punto de entrada principal de la aplicación
 * 
 * Carga de datos iniciales desde los archivos de texto
 */

fun main() {
    // Busca la carpeta de datos segun la ubicacion desde donde se ejecute la terminal
    val carpetaDatos = if (File("datos").exists()) {
        "datos"
    } else if (File("../datos").exists()) {
        "../datos"
    } else if (File("proyecto 2/datos").exists()) {
        "proyecto 2/datos"
    } else {
        "datos"
    }

    println("Cargando datos desde: '$carpetaDatos'...")
    val manager = TiendaManager()

    // 1. Carga de repuestos
    val repuestosCargados = FileDataLoader.cargarRepuestos("$carpetaDatos/repuestos.txt")
    for (r in repuestosCargados) {
        manager.agregarRepuesto(r)
    }

    // 2. Carga de clientes
    val clientesCargados = FileDataLoader.cargarClientes("$carpetaDatos/clientes.txt")
    for (c in clientesCargados) {
        manager.agregarCliente(c)
    }

    // 3. Carga de ventas vinculando mapas de busqueda en memoria
    val mapaClientes = manager.listarClientes().associateBy { it.rut }
    val mapaRepuestos = manager.listarRepuestos().associateBy { it.codigoSKU }
    val ventasCargadas = FileDataLoader.cargarVentas("$carpetaDatos/ventas.txt", mapaClientes, mapaRepuestos)
    for (v in ventasCargadas) {
        manager.cargarVentaInicial(v)
    }

    println("¡Inicializacion completa con exito!")
    println("-----------------------------------------------------")

    // Comienzo del programa
    val inicializacion = Terminal(manager)
    inicializacion.iniciar()
}