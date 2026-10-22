// 1. CLASE CLIENTE

class Cliente( val rut: String, val nombre: String, val esTallerMecanico:Boolean, val telefono: String){
    
    fun validarRut(rut:String):Boolean{
        

        return true
    }

    
}





fun menu(){
    println("------------------------------------")
    println("------------------------------------")
    println("(1):Seleccionar entidad.")
    println("(2):Crear registro.")
    println("(3):Listar registros.")
    println("(4):Eliminar registro.")
    println("(5):Oraciones de consulta y reportes.")
    println("(0):Salir.")
    println("------------------------------------")
    println("------------------------------------\n\n")
    try {
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


//Parte (1) menú do lineas 20-()
fun SeleccionarEntidad(){
    println("------------------------------------")
    println("------------------------------------")
    println("(1):Catalogo de repuestos.")
    println("(2):Registro de clientes.")
    println("(3):Historial de ventas.")
    try {
        when (entrada){
            "1" -> CatalogoDeRepuestos()
            "2" -> RegistroDeCliente()
            "3" -> HistorialDeVentas()
        }
    }
}

fun CatalogoDeRepuestos(){

}

fun RegistroDeCliente(){

}

fun HistorialDeVentas(){

}
//FIN PRIMERA SELECCIÓN

//Inicio parte (2) menú do lineas ()-()
fun CrearRegistro(){
    //Desplegar formulario para registro
    println("Con cual registro desea proceder:")
    println("(1)")
    println("\n")
    println("")
    val repuesto: String =readln()    
    validarRegistro(repuesto)


}

fun validarRegistro(val regisro: String){

}





