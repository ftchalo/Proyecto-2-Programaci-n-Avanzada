package clases
/*
 Clase base abstracta que representa una pieza o componente automotriz en el catálogo.

 codigoSKU Identificador único alfanumérico del repuesto (ej. OEM-101).
 
 nombre Nombre descriptivo de la pieza (ej. Pastillas de Freno).
 
 marca Nombre del fabricante o marca comercial (ej. Brembo).
 
 costoBase Valor de compra/adquisición de la pieza en bodega.
 
 stockInicial Cantidad inicial de unidades disponibles al crear el objeto.
 */
abstract class Repuesto(
    val codigoSKU: String,
    val nombre: String,
    val marca: String,
    val costoBase: Double,
    val stockInicial: Int) {
    init {
        require(codigoSKU.isNotBlank()) { "El código SKU no puede estar vacío." }
        require(nombre.isNotBlank()) { "El nombre del repuesto no puede estar vacío." }
        require(costoBase > 0.0) { "El costo base debe ser mayor a 0: $costoBase" }
        require(stockInicial >= 0) { "El stock inicial no puede ser negativo: $stockInicial" }
    }

    /* Stock disponible en la bodega. Lectura pública y modificación privada .*/
    var stock: Int = stockInicial
        private set

    /* Indica si el repuesto cuenta con unidades disponibles para la venta. */
    val enStock: Boolean
        get() = stock > 0

    /* Constructor para repuestos sin marca ni stock inicial (marca Genérica, stock 0).*/
    constructor(codigoSKU: String, nombre: String, costoBase: Double) : this(
        codigoSKU = codigoSKU,
        nombre = nombre,
        marca = "Genérica",
        costoBase = costoBase,
        stockInicial = 0
    )

    /* Actualiza el stock sumando o restando.
       return true si la operación fue exitosa, false si no había stock suficiente.
    */
    fun actualizarStock(cantidad: Int): Boolean {
        if (stock + cantidad < 0) return false
        stock += cantidad
        return true
    }

    /* Verifica si hay stock para satisfacer un pedido. */
    fun tieneStockSuficiente(cantidad: Int): Boolean = stock >= cantidad

    /* Calcula de forma polimórfica el precio final de venta al público según las reglas del tipo de repuesto.*/
    abstract fun calcularPrecioVenta(): Double

    /* Retorna una cadena con la información detallada y técnica de la pieza.*/
    abstract fun obtenerFichaTecnica(): String
}
