package clases

object RepuestoFactory {
    fun crearRepuesto(
        tipo: String,
        sku: String,
        nombre: String,
        marca: String,
        costo: Double,
        stock: Int,
        extra1: String,
        extra2: Boolean
    ): Repuesto {
        return when (tipo.uppercase().trim()) {
            "ORIGINAL" -> RepuestoOriginal(
                codigoSKU = sku,
                nombre = nombre,
                marca = marca,
                costoBase = costo,
                stock = stock,
                garantiaMeses = extra1.toIntOrNull() ?: 12,
                certificadoOEM = extra2
            )
            "ALTERNATIVO" -> RepuestoAlternativo(
                codigoSKU = sku,
                nombre = nombre,
                marca = marca,
                costoBase = costo,
                stock = stock,
                fabricanteGenerico = extra1.ifBlank { "Genérico Estándar" },
                incluyeKitInstalacion = extra2
            )
            else -> throw IllegalArgumentException("Tipo de repuesto desconocido: '$tipo'.")
        }
    }
}