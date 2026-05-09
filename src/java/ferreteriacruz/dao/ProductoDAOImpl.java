/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ferreteriacruz.dao;

/**
 *
 * @author Anderson
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import ferreteriacruz.config.Conexion;
import ferreteriacruz.modelo.Producto;

public class ProductoDAOImpl implements IProductoDAO {

    @Override
    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try {
            Connection con = Conexion.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setCodigoSKU(rs.getString("codigo_SKU"));
                p.setNombre(rs.getString("nombre"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setStockActual(rs.getInt("stock_actual"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setPrecio(rs.getDouble("precio"));
                p.setImagen(rs.getBytes("imagen"));
                lista.add(p);
            }
        } catch (Exception e) { System.err.println("Error listar: " + e.getMessage()); }
        return lista;
    }

    @Override
    public boolean registrarProducto(Producto p) {
        String sql = "INSERT INTO productos (codigo_SKU, nombre, id_categoria, stock_actual, stock_minimo, precio, imagen) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = Conexion.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, p.getCodigoSKU());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getIdCategoria());
            ps.setInt(4, p.getStockActual());
            ps.setInt(5, p.getStockMinimo());
            ps.setDouble(6, p.getPrecio());
            ps.setBytes(7, p.getImagen());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { System.err.println("Error registrar: " + e.getMessage()); return false; }
    }

    @Override
    public boolean actualizarProducto(Producto p) {
        String sql;
        boolean actualizaImagen = (p.getImagen() != null && p.getImagen().length > 0);
        
        if(actualizaImagen) {
            sql = "UPDATE productos SET codigo_SKU=?, nombre=?, id_categoria=?, stock_actual=?, stock_minimo=?, precio=?, imagen=? WHERE id_producto=?";
        } else {
            sql = "UPDATE productos SET codigo_SKU=?, nombre=?, id_categoria=?, stock_actual=?, stock_minimo=?, precio=? WHERE id_producto=?";
        }
        
        try {
            Connection con = Conexion.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, p.getCodigoSKU());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getIdCategoria());
            ps.setInt(4, p.getStockActual());
            ps.setInt(5, p.getStockMinimo());
            ps.setDouble(6, p.getPrecio());
            
            if(actualizaImagen) {
                ps.setBytes(7, p.getImagen());
                ps.setInt(8, p.getIdProducto());
            } else {
                ps.setInt(7, p.getIdProducto());
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) { System.err.println("Error actualizar: " + e.getMessage()); return false; }
    }

    @Override
    public boolean eliminarProducto(int id) {
        Connection con = null;
        try {
            con = Conexion.getInstancia().getConexion();
            // Desactivar autocommit para usar transacciones
            con.setAutoCommit(false); 

            // 1. Eliminar dependencias en series_hardware
            String sqlSeries = "DELETE FROM series_hardware WHERE id_producto=?";
            PreparedStatement psSeries = con.prepareStatement(sqlSeries);
            psSeries.setInt(1, id);
            psSeries.executeUpdate();

            // 2. Eliminar dependencias en kardex_movimientos
            String sqlKardex = "DELETE FROM kardex_movimientos WHERE id_producto=?";
            PreparedStatement psKardex = con.prepareStatement(sqlKardex);
            psKardex.setInt(1, id);
            psKardex.executeUpdate();

            // 3. Finalmente, eliminar el producto principal
            String sqlProducto = "DELETE FROM productos WHERE id_producto=?";
            PreparedStatement psProducto = con.prepareStatement(sqlProducto);
            psProducto.setInt(1, id);
            int filasAfectadas = psProducto.executeUpdate();

            // Confirmar todos los cambios si no hubo errores
            con.commit(); 
            return filasAfectadas > 0;
            
        } catch (Exception e) { 
            // Si hay error, deshacer los borrados
            if(con != null) {
                try { con.rollback(); } catch(Exception ex) { System.err.println("Error rollback: " + ex.getMessage()); }
            }
            System.err.println("Error al eliminar en cascada: " + e.getMessage()); 
            return false; 
        } finally {
            // Restaurar el comportamiento normal de la conexión
            if(con != null) {
                try { con.setAutoCommit(true); } catch(Exception ex) {} 
            }
        }
    }

    @Override
    public Producto buscarPorId(int id) {
        Producto p = new Producto();
        String sql = "SELECT * FROM productos WHERE id_producto=?";
        try {
            Connection con = Conexion.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p.setIdProducto(rs.getInt("id_producto"));
                p.setCodigoSKU(rs.getString("codigo_SKU"));
                p.setNombre(rs.getString("nombre"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setStockActual(rs.getInt("stock_actual"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setPrecio(rs.getDouble("precio"));
                p.setImagen(rs.getBytes("imagen"));
            }
        } catch (Exception e) { System.err.println("Error buscar: " + e.getMessage()); }
        return p;
    }
    
    @Override
    public Producto buscarPorSKU(String sku) {
        Producto p = null;
        String sql = "SELECT * FROM productos WHERE codigo_SKU=?";
        try {
            Connection con = Conexion.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sku);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setCodigoSKU(rs.getString("codigo_SKU"));
                p.setNombre(rs.getString("nombre"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setStockActual(rs.getInt("stock_actual"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setPrecio(rs.getDouble("precio"));
                p.setImagen(rs.getBytes("imagen"));
            }
        } catch (Exception e) { 
            System.err.println("Error buscarPorSKU: " + e.getMessage()); 
        }
        return p;
    }
}