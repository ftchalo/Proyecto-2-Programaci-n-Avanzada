package autopartes.servicio

import autopartes.modelo.Cliente
import autopartes.modelo.Repuesto
import autopartes.modelo.Venta

/**
 * Servicio central que gestiona en memoria el estado del catálogo, clientes y transacciones.
 *
 * Aplica el principio de encapsulamiento utilizando listas mutables privadas
 * y exponiendo vistas de solo lectura hacia el exterior.
 */
class TiendaManager {

    private val _repuestos: MutableList<Repuesto> = mutableListOf()
    val repuestos: List<Repuesto>
        get() = _repuestos.toList()

    private val _clientes: MutableList<Cliente> = mutableListOf()
    val clientes: List<Cliente>
        get() = _clientes.toList()

    private val _ventas: MutableList<Venta> = mutableListOf()
    val ventas: List<Venta>
        get() = _ventas.toList()

    // -------------------------------------------------------------
    // GESTIÓN DE REPUESTOS
    // -------------------------------------------------------------

    fun agregarRepuesto(repuesto: Repuesto): Boolean {
        if (_repuestos.any { it.codigoSKU.equals(repuesto.codigoSKU, ignoreCase = true) }) {
            return false // SKU duplicado
        }
        _repuestos.add(repuesto)
        return true
    }

    fun buscarRepuesto(sku: String): Repuesto? {
        return _repuestos.find { it.codigoSKU.equals(sku.trim(), ignoreCase = true) }
    }

    fun eliminarRepuesto(sku: String): Boolean {
        val repuesto = buscarRepuesto(sku) ?: return false
        // Validación de integridad: no eliminar si tiene ventas registradas
        val tieneVentas = _ventas.any { it.repuesto.codigoSKU.equals(sku, ignoreCase = true) }
        if (tieneVentas) {
            throw IllegalStateException("No se puede eliminar el repuesto '$sku' porque tiene ventas históricas asociadas.")
        }
        return _repuestos.remove(repuesto)
    }

    fun repuestosStockCritico(limite: Int = 5): List<Repuesto> {
        return _repuestos.filter { it.stock <= limite }
    }

    // -------------------------------------------------------------
    // GESTIÓN DE CLIENTES
    // -------------------------------------------------------------

    fun agregarCliente(cliente: Cliente): Boolean {
        if (_clientes.any { it.rut.equals(cliente.rut.trim(), ignoreCase = true) }) {
            return false // RUT duplicado
        }
        _clientes.add(cliente)
        return true
    }

    fun buscarCliente(rut: String): Cliente? {
        val limpio = rut.trim().replace(".", "")
        return _clientes.find { it.rut.replace(".", "").equals(limpio, ignoreCase = true) }
    }

    fun eliminarCliente(rut: String): Boolean {
        val cliente = buscarCliente(rut) ?: return false
        // Validación de integridad referencial
        val tieneVentas = _ventas.any { it.cliente.rut.equals(cliente.rut, ignoreCase = true) }
        if (tieneVentas) {
            throw IllegalStateException("No se puede eliminar el cliente '${cliente.rut}' porque registra compras en el historial.")
        }
        return _clientes.remove(cliente)
    }

    // -------------------------------------------------------------
    // GESTIÓN DE VENTAS
    // -------------------------------------------------------------

    /**
     * Registra una venta nueva realizada en tiempo real por el vendedor, descontando stock.
     */
    fun registrarVenta(venta: Venta): Boolean {
        if (_ventas.any { it.idVenta == venta.idVenta }) {
            throw IllegalArgumentException("Ya existe una venta con el ID ${venta.idVenta}.")
        }
        val exitoStock = venta.procesarVenta()
        if (!exitoStock) {
            throw IllegalStateException("Stock insuficiente del repuesto '${venta.repuesto.codigoSKU}' para concretar la venta.")
        }
        _ventas.add(venta)
        return true
    }

    /**
     * Carga una venta histórica proveniente de archivo inicial sin volver a descontar stock.
     */
    fun cargarVentaInicial(venta: Venta) {
        if (_ventas.none { it.idVenta == venta.idVenta }) {
            _ventas.add(venta)
        }
    }

    fun buscarVenta(idVenta: Int): Venta? {
        return _ventas.find { it.idVenta == idVenta }
    }

    fun anularVenta(idVenta: Int): Boolean {
        val venta = buscarVenta(idVenta) ?: return false
        venta.anularVenta()
        return _ventas.remove(venta)
    }

    fun ventasPorCliente(rut: String): List<Venta> {
        val cliente = buscarCliente(rut) ?: return emptyList()
        return _ventas.filter { it.cliente.rut.equals(cliente.rut, ignoreCase = true) }
    }

    fun totalRecaudado(): Double {
        return _ventas.filter { it.completada }.sumOf { it.montoTotal }
    }
}
