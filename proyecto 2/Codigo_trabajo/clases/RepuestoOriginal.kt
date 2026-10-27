

/*
 Representa una pieza de repuesto original (OEM) certificada por el fabricante del automóvil.
 
 Aplica herencia extendiendo [Repuesto] y sobrescribe el cálculo de precio con margen del 45%
 más recargo por garantía y certificación de fábrica.
 
 property garantiaMeses Período de cobertura de la garantía de fábrica en meses.
 certificadoOEM Indica si la pieza cuenta con sello homologado del fabricante.
 */

class RepuestoOriginal(
    codigoSKU: String,
    nombre: String,
    marca: String,
    costoBase: Double,
    stock: Int,
    val garantiaMeses: Int,
    val certificadoOEM: Boolean
) : Repuesto(codigoSKU, nombre, marca, costoBase, stock) {

    init {
        require(garantiaMeses >= 0) { "Los meses de garantía no pueden ser negativos: $garantiaMeses" }
    }

    /**
     * Constructor secundario que asigna por defecto 12 meses de garantía y certificado OEM habilitado.
     */
    constructor(
        codigoSKU: String,
        nombre: String,marca: String,
        costoBase: Double,stock: Int
    ) : this(
        codigoSKU = codigoSKU,
        nombre = nombre,
        marca = marca,
        costoBase = costoBase,
        stock = stock,
        garantiaMeses = 12,
        certificadoOEM = true
    )

    /**
     * Calcula el precio de venta aplicando un 45% de margen comercial sobre el costo base,
     * sumando un recargo fijo de $15.000 si posee certificado de garantía oficial.
     */
    override fun calcularPrecioVenta(): Double {
        val margen = costoBase * 1.45
        val recargoGarantia = if (certificadoOEM) 15000.0 else 0.0
        return margen + recargoGarantia
    }

    override fun obtenerFichaTecnica(): String {
        val oemTexto = if (certificadoOEM) "Con Certificación OEM" else "Sin Certificado"
        return "[$codigoSKU] $nombre ($marca) - Tipo: ORIGINAL | Garantía: $garantiaMeses meses | $oemTexto | Stock: $stock"
    }
}
