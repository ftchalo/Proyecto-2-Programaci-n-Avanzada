package clases
/**
 * Administra las colecciones en memoria y coordina la lógica comercial del sistema.
 */
class TiendaManager(
    private val repuestosIniciales: MutableList<Repuesto>,
    private val clientesIniciales: MutableList<Cliente>,
    private val ventasIniciales: MutableList<Venta>
) {
    private val repuestos: MutableList<Repuesto> = repuestosIniciales
    private val clientes: MutableList<Cliente> = clientesIniciales
    private val ventas: MutableList<Venta> = ventasIniciales

    // --- Operaciones de Repuestos ---
    fun agregarRepuesto(r: Repuesto): Boolean {
        if (buscarRepuesto(r.getCodigoSKU()) != null) {
            throw IllegalArgumentException("Ya existe un repuesto con el SKU: ${r.getCodigoSKU()}")
        }
        return repuestos.add(r)
    }

    fun buscarRepuesto(sku: String): Repuesto? = repuestos.find { it.getCodigoSKU().equals(sku, ignoreCase = true) }

    fun listarRepuestos(): List<Repuesto> = repuestos

    fun eliminarRepuesto(sku: String): Boolean {
        val rep = buscarRepuesto(sku) ?: throw NoSuchElementException("Repuesto no encontrado.")
        val enUso = ventas.any { it.getRepuesto().getCodigoSKU().equals(sku, ignoreCase = true) }
        if (enUso) {
            throw IllegalStateException("No se puede eliminar el repuesto porque tiene ventas asociadas.")
        }
        return repuestos.remove(rep)
    }

    // --- Operaciones de Clientes ---
    fun agregarCliente(c: Cliente): Boolean {
        if (buscarCliente(c.getRut()) != null) {
            throw IllegalArgumentException("Ya existe un cliente con el RUT: ${c.getRut()}")
        }
        return clientes.add(c)
    }

    fun buscarCliente(rut: String): Cliente? = clientes.find { it.getRut().equals(rut, ignoreCase = true) }

    fun listarClientes(): List<Cliente> = clientes

    fun eliminarCliente(rut: String): Boolean {
        val cli = buscarCliente(rut) ?: throw NoSuchElementException("Cliente no encontrado.")
        val enUso = ventas.any { it.getCliente().getRut().equals(rut, ignoreCase = true) }
        if (enUso) {
            throw IllegalStateException("No se puede eliminar el cliente porque tiene historial de compras.")
        }
        return clientes.remove(cli)
    }

    // --- Operaciones de Ventas ---
    fun registrarVenta(v: Venta): Boolean {
        if (ventas.any { it.getIdVenta() == v.getIdVenta() }) {
            throw IllegalArgumentException("Ya existe una venta con el ID: ${v.getIdVenta()}")
        }
        if (!v.getRepuesto().tieneStockSuficiente(v.getCantidad())) {
            throw IllegalStateException("Stock insuficiente. Disponibles: ${v.getRepuesto().getStock()}, solicitados: ${v.getCantidad()}")
        }
        v.getRepuesto().actualizarStock(-v.getCantidad())
        return ventas.add(v)
    }

    fun listarVentas(): List<Venta> = ventas
}