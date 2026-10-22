package autopartes.repl

import autopartes.datos.FileDataLoader
import autopartes.servicio.TiendaManager
import java.io.File

/**
 * Punto de entrada principal de la aplicación AutopartesPro.
 *
 * Carga los datos iniciales desde los archivos de texto plano y lanza el ciclo REPL.
 */
fun main() {
    println("=============================================================")
    println("           INICIALIZANDO SISTEMA AUTOPARTESPRO               ")
    println("  INF-223 Programación Avanzada — UCM Laboratorio Unidad 2    ")
    println("=============================================================")

    // Resolver ruta de la carpeta de datos (soporta ejecución desde raíz o subcarpeta)
    val carpetaDatos = if (File("datos").exists()) {
        "datos"
    } else if (File("Unidad 2/proyecto 2/datos").exists()) {
        "Unidad 2/proyecto 2/datos"
    } else {
        "datos"
    }

    println("Cargando información inicial desde directorio: '$carpetaDatos/'...")
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

    // 3. Carga de ventas vinculando mapas de búsqueda en memoria
    val mapaClientes = manager.clientes.associateBy { it.rut }
    val mapaRepuestos = manager.repuestos.associateBy { it.codigoSKU }
    val ventasCargadas = FileDataLoader.cargarVentas("$carpetaDatos/ventas.txt", mapaClientes, mapaRepuestos)
    for (v in ventasCargadas) {
        manager.cargarVentaInicial(v)
    }

    println("¡Inicialización completada con éxito!")
    println("-------------------------------------------------------------")

    // Lanzar interfaz interactiva por consola
    val repl = TerminalREPL(manager)
    repl.iniciar()
}
