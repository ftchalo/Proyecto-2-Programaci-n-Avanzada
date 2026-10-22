


class RepuestoAlternativo(
    codigoSKU: String,
    nombre: String,
    marca: String,
    costoBase: Double,
    stock: Int,
    val fabricanteGenerico: String,
    val incluyeKitInstalacion: Boolean
) : Repuesto(codigoSKU, nombre, marca, costoBase, stock) {

    init {
        require(fabricanteGenerico.isNotBlank()) { "El nombre del fabricante genérico no puede estar vacío." }
    }

    /**
     * Constructor secundario para repuestos genéricos estándar sin kit de instalación incluido.
     */
    constructor(
        codigoSKU: String,
        nombre: String,
        marca: String,
        costoBase: Double,
        stock: Int
    ) : this(
        codigoSKU = codigoSKU,
        nombre = nombre,
        marca = marca,
        costoBase = costoBase,
        stock = stock,
        fabricanteGenerico = "Genérico Estándar",
        incluyeKitInstalacion = false
    )

    /**
     * Calcula el precio de venta aplicando un 25% de margen comercial sobre el costo base,
     * sumando $4.500 si incluye el kit de montaje universal.
     */
    override fun calcularPrecioVenta(): Double {
        val margen = costoBase * 1.25
        val recargoKit = if (incluyeKitInstalacion) 4_500.0 else 0.0
        return margen + recargoKit
    }

    override fun obtenerFichaTecnica(): String {
        val kitTexto = if (incluyeKitInstalacion) "Incluye Kit Universal" else "Sin Kit"
        return "[$codigoSKU] $nombre ($marca) - Tipo: ALTERNATIVO | Fabricante: $fabricanteGenerico | $kitTexto | Stock: $stock"
    }
}
