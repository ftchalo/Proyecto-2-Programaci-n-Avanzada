package autopartes.factory

import autopartes.modelo.Repuesto
import autopartes.modelo.RepuestoAlternativo
import autopartes.modelo.RepuestoOriginal

/**
 * Fábrica que implementa el patrón de diseño creacional Factory Method.
 *
 * Encapsula la lógica de instanciación de piezas originales o alternativas,
 * evitando que la capa de lectura de datos (archivos planos) o la interfaz REPL
 * se acoplen a las subclases concretas.
 */
object RepuestoFactory {

    /**
     * Crea y retorna una instancia concreta de [Repuesto] según el tipo especificado.
     *
     * @param tipo Identificador del tipo de repuesto ("ORIGINAL" o "ALTERNATIVO").
     * @param sku Código SKU único.
     * @param nombre Nombre descriptivo de la pieza.
     * @param marca Marca o fabricante comercial.
     * @param costo Costo base de adquisición.
     * @param stock Unidades disponibles.
     * @param extra1 Meses de garantía (para Original) o Nombre del fabricante genérico (para Alternativo).
     * @param extra2 Certificado OEM (para Original) o Incluye Kit de instalación (para Alternativo).
     * @throws IllegalArgumentException Si el tipo no coincide con "ORIGINAL" ni "ALTERNATIVO".
     */
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
            "ORIGINAL" -> {
                val meses = extra1.toIntOrNull() ?: 12
                RepuestoOriginal(
                    codigoSKU = sku,
                    nombre = nombre,
                    marca = marca,
                    costoBase = costo,
                    stock = stock,
                    garantiaMeses = meses,
                    certificadoOEM = extra2
                )
            }
            "ALTERNATIVO" -> {
                RepuestoAlternativo(
                    codigoSKU = sku,
                    nombre = nombre,
                    marca = marca,
                    costoBase = costo,
                    stock = stock,
                    fabricanteGenerico = extra1.ifBlank { "Genérico Estándar" },
                    incluyeKitInstalacion = extra2
                )
            }
            else -> throw IllegalArgumentException("Tipo de repuesto desconocido: '$tipo'. Se esperaba 'ORIGINAL' o 'ALTERNATIVO'.")
        }
    }
}
