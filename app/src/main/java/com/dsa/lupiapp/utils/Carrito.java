package com.dsa.lupiapp.utils;

import com.dsa.lupiapp.entity.service.DetallePedido;

import java.util.ArrayList;

public class Carrito {

    // Lista estática que almacena los detalles de los productos agregados al carrito
    private static final ArrayList<DetallePedido> detallesPedidos = new ArrayList<>();


    // Método para agregar un producto al carrito
    public static String agregarProductos(DetallePedido detallePedido) {
        int index = 0;
        boolean b = false;

        // Iterar sobre la lista de detalles de pedidos para buscar un producto similar
        for (DetallePedido dp: detallesPedidos) {
            // Verificar si el producto ya está en el carrito
            if (dp.getProducto().getId() == detallePedido.getProducto().getId()) {
                // Si el producto ya está en el carrito, reemplazarlo con el nuevo detallePedido
                detallesPedidos.set(index, detallePedido);
                b = true; // Indicar que el producto ya estaba en el carrito
            }
            index++;
        }

        // Si el producto no estaba en el carrito, agregarlo
        if (!b) {
            detallesPedidos.add(detallePedido);
            return "El producto ha sido agregado al carrito con éxito";
        }
        return ". . . .";
    }

    // Método para eliminar un producto del carrito
    public static void eliminar(final int idProducto) {
        DetallePedido dpE = null;

        // Iterar sobre la lista de detalles de pedidos para encontrar el producto a eliminar
        for (DetallePedido dp: detallesPedidos) {
            if (dp.getProducto().getId() == idProducto) {
                dpE = dp; // Guardar el detalle de pedido que coincide con el producto a eliminar
                break; // Salir del bucle después de encontrar el producto
            }
        }

        // Si se encontró un detalle de pedido para eliminar, quitarlo de la lista
        if (dpE != null) {
            detallesPedidos.remove(dpE);
            System.out.println("Se eliminó el producto del pedido");
        }
    }

    // Método para obtener la lista de detalles de pedidos
    public static ArrayList<DetallePedido> getDetallesPedidos() {
        return detallesPedidos;
    }

    // Método para limpiar el carrito (eliminar todos los productos)
    public static void limpiar() {
        detallesPedidos.clear();
    }

}
