package clases
/**
 * Administra las colecciones en memoria y coordina la lógica comercial del sistema.
 */
class TiendaManager(
    private val repuestosIniciales: MutableList<Repuesto> = mutableListOf(),
    private val clientesIniciales: MutableList<Cliente> = mutableListOf(),
    private val ventasIniciales: MutableList<Venta> = mutableListOf()
) {
    private val repuestos: MutableList<Repuesto> = repuestosIniciales
    private val clientes: MutableList<Cliente> = clientesIniciales
    private val ventas: MutableList<Venta> = ventasIniciales

    // --- Operaciones de Repuestos ---
    fun agregarRepuesto(r: Repuesto): Boolean {
        if (buscarRepuesto(r.codigoSKU) != null) {
            throw IllegalArgumentException("Ya existe un repuesto con el SKU: ${r.codigoSKU}")
        }
        return repuestos.add(r)
    }

    fun buscarRepuesto(sku: String): Repuesto? = 
        repuestos.find { it.codigoSKU.equals(sku, ignoreCase = true) }

    fun eliminarRepuesto(sku: String): Boolean {
        val rep = buscarRepuesto(sku) ?: throw NoSuchElementException("Repuesto no encontrado.")
        val enUso = ventas.any { it.repuesto.codigoSKU.equals(sku, ignoreCase = true) }
        if (enUso) {
            throw IllegalStateException("No se puede eliminar el repuesto porque tiene ventas asociadas.")
        }
        return repuestos.remove(rep)
    }

    fun listarRepuestos(): List<Repuesto> = repuestos
    

    // --- Operaciones de Clientes ---
    fun agregarCliente(c: Cliente): Boolean {
        if (buscarCliente(c.rut) != null) {
            throw IllegalArgumentException("Ya existe un cliente con el RUT: ${c.rut}")
        }
        return clientes.add(c)
    }

    fun buscarCliente(rut: String): Cliente? = 
        clientes.find { it.rut.equals(rut, ignoreCase = true) }

    fun eliminarCliente(rut: String): Boolean {
        val cli = buscarCliente(rut) ?: throw NoSuchElementException("Cliente no encontrado.")
        val enUso = ventas.any { it.cliente.rut.equals(rut, ignoreCase = true) }
        if (enUso) {
            throw IllegalStateException("No se puede eliminar el cliente porque tiene historial de compras.")
        }
        return clientes.remove(cli)
    }

    fun listarClientes(): List<Cliente> = clientes
    
    // --- Operaciones de Ventas ---

    fun registrarVenta(v: Venta): Boolean {
        if (ventas.any { it.idVenta == v.idVenta }) {
            throw IllegalArgumentException("Ya existe una venta con el ID: ${v.idVenta}")
        }
        if (!v.repuesto.tieneStockSuficiente(v.cantidad)) {
            throw IllegalStateException("Stock insuficiente. Disponibles: ${v.repuesto.stock}, solicitados: ${v.cantidad}")
        }
        v.repuesto.actualizarStock(-v.cantidad)
        return ventas.add(v)
    }

    fun cargarVentaInicial(v: Venta): Boolean {
        if (ventas.none { it.idVenta == v.idVenta }) {
            return ventas.add(v)
        }
        return false
    }

    /**
     * Busca una venta por su ID numérico correlativo.
     */
    fun buscarVenta(idVenta: Int): Venta? = ventas.find { it.idVenta == idVenta }

    /**
     * 2. Anula la venta: restituye el stock al repuesto y remueve la transacción.
     * Retorna true si se anuló con éxito, o false si la venta no existía o no estaba completada.
     */
    fun anularVenta(idVenta: Int): Boolean {
        val venta = buscarVenta(idVenta) ?: return false
        val fueAnulada = venta.anularVenta() // Llama al método de Venta.kt que restaura el stock
        if (fueAnulada) {
            ventas.remove(venta)
        }
        return fueAnulada
    }

    fun listarVentas(): List<Venta> = ventas
}