 * Representa a un cliente del mesón de venta (persona particular o taller mecánico).

    * rut Rol Único Tributario o identificador oficial del cliente.
    * nombre Nombre de la persona o razón social del taller.
    * telefono Teléfono de contacto.
    * esTallerMecanico Indica si es un taller mecánico registrado con convenio.
    */
class Cliente(
    val rut: String,
    val nombre: String,
    val telefono: String,
    val esTallerMecanico: Boolean
) {
    init {
        require(rut.isNotBlank()) { "El RUT del cliente no puede estar vacío." }
        require(nombre.isNotBlank()) { "El nombre del cliente no puede estar vacío." }
        require(telefono.isNotBlank()) { "El teléfono no puede estar vacío." }
    }

    /**
     * Constructor secundario para clientes particulares por defecto (no taller).
     */
    constructor(rut: String, nombre: String, telefono: String) : this(
        rut = rut,
        nombre = nombre,
        telefono = telefono,
        esTallerMecanico = false
    )

    /**
     * Verifica que el RUT tenga formato básico válido con guión.
     */
    fun validarRut(): Boolean {
        val limpio = rut.trim().replace(".", "")
        return limpio.contains("-") && limpio.length in 8..10
    }

    /**
     * Retorna una representación textual formateada de los datos del cliente.
     */
    fun obtenerFicha(): String {
        val tipo = if (esTallerMecanico) "Taller Mecánico (Convenio)" else "Cliente Particular"
        return "RUT: $rut | Nombre: $nombre | Tel: $telefono | Tipo: $tipo"
    }
}



class cliente 
    val rut: String
    val nombre: String
    val telefono: String
    val esTallerMecanico: Boolean   

    constructor(rut: String, nombre: String, telefono: String) : this( /*constructor en caso que no venga declarado si es taller mecanico (valor default falso) */
        rut = rut,
        nombre = nombre,
        telefono = telefono,
        esTallerMecanico = false
    )

    
    val limpio = rut.replace(".", "").replace("-", "").uppercase()
    val cuerpo = limpio.dropLast(1)
    val digito = limpio.last().toString()

    var suma = 0
    var factor = 2

    for (numero in cuerpo.reversed()) {
        suma += numero.toString().toInt() * factor
        factor++

        if (factor > 7) {
            factor = 2
        }
    }

    val resultado = 11 - (suma % 11)

    var verificador = resultado.toString()

    if (resultado == 11) {
        verificador = "0"
    } else if (resultado == 10) {
        verificador = "K"
    }
