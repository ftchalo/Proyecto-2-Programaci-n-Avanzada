package clases
import java.time.LocalDate

/*
 Representa una transacción u orden de compra procesada en la tienda.
 
 Asocia un [Cliente] con un [Repuesto], aplicando el cálculo polimórfico de precios
 y gestionando el descuento para talleres mecánicos.
 
 idVenta Identificador numérico correlativo de la transacción.
 fecha Fecha de emisión de la orden (formato DD-MM-AAAA).
 cliente Cliente que realiza la compra.
 repuesto Repuesto involucrado en la transacción.
 cantidad Número de unidades vendidas.
 completadaInicial Estado de completitud inicial (usado al cargar histórico).
 */
class Venta(
    val idVenta: Int,
    val fecha: String,
    val cliente: Cliente,
    val repuesto: Repuesto,
    val cantidad: Int,
    completadaInicial: Boolean = false
) {
    init {
        require(idVenta > 0) { "El ID de venta debe ser positivo: $idVenta" }
        require(fecha.isNotBlank()) { "La fecha no puede estar vacía." }
        require(cantidad > 0) { "La cantidad a comprar debe ser mayor a 0: $cantidad" }
    }

    /**
     * Monto total liquidado. Aplica un 10% de descuento preferencial si el cliente es taller mecánico.
     */
    val montoTotal: Double = run {
        val precioUnitario = repuesto.calcularPrecioVenta()
        val totalBruto = precioUnitario * cantidad
        if (cliente.esTallerMecanico) totalBruto * 0.90 else totalBruto
    }

    /**
     * Estado de la venta: indica si fue procesada y se rebajó el stock correctamente.
     */
    var completada: Boolean = completadaInicial
        private set

    /**
     * Constructor secundario sin estado previo (por defecto inicia no completada).
     */
    constructor(
        idVenta: Int,
        fecha: String,
        cliente: Cliente,
        repuesto: Repuesto,
        cantidad: Int
    ) : this(
        idVenta = idVenta,
        fecha = fecha,
        cliente = cliente,
        repuesto = repuesto,
        cantidad = cantidad,
        completadaInicial = false
    )

    /**
     * Constructor secundario para ventas nuevas que asigna automáticamente la fecha del sistema actual.
     */
    constructor(
        idVenta: Int,
        cliente: Cliente,
        repuesto: Repuesto,
        cantidad: Int
    ) : this(
        idVenta = idVenta,
        fecha = LocalDate.now().toString(),
        cliente = cliente,
        repuesto = repuesto,
        cantidad = cantidad,
        completadaInicial = false
    )

    /**
     * Procesa la venta validando y descontando el stock del repuesto.
     * @return true si la venta se completó con éxito, false si el stock no fue suficiente.
     */
    fun procesarVenta(): Boolean {
        if (completada) return false
        val exito = repuesto.actualizarStock(-cantidad)
        if (exito) {
            completada = true
        }
        return exito
    }

    /**
     * Anula la venta y restituye las unidades vendidas al inventario del repuesto.
     * @return true si se anuló y devolvió el stock, false si ya estaba cancelada o no completada.
     */
    fun anularVenta(): Boolean {
        if (!completada) return false
        repuesto.actualizarStock(cantidad)
        completada = false
        return true
    }
    
    fun getIdVenta(): Int = idVenta
    
    fun getCliente(): Cliente = cliente
    
    fun getRepuesto(): Repuesto = repuesto
    
    fun getCantidad(): Int = cantidad

    /**
     * Genera un comprobante de compra detallado.
     */
    fun generarComprobante(): String {
        val estado = if (completada) "COMPLETADA" else "PENDIENTE/ANULADA"
        val descTaller = if (cliente.esTallerMecanico) " (Descuento 10% Taller aplicado)" else ""
        return """
        =====================================================
        COMPROBANTE DE VENTA N° $idVenta - Fecha: $fecha
        Cliente: ${cliente.nombre} (RUT: ${cliente.rut})
        Producto: [${repuesto.codigoSKU}] ${repuesto.nombre}
        Cantidad: $cantidad unidades
        Precio unitario ref: $${"%.0f".format(repuesto.calcularPrecioVenta())}
        Total liquidado: $${"%.0f".format(montoTotal)}$descTaller
        Estado: $estado
        =====================================================
        """.trimIndent()
    }
}
