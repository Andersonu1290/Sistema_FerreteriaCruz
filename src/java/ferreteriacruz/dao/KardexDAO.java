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
import ferreteriacruz.modelo.MovimientoKardex;

public class KardexDAO {

    public List<MovimientoKardex> listarHistorialKardex() {
        List<MovimientoKardex> lista = new ArrayList<>();
        
        String sql = "SELECT k.fecha, k.tipo_movimiento, p.nombre AS producto, k.cantidad, k.motivo, u.username " +
                     "FROM kardex_movimientos k " +
                     "INNER JOIN productos p ON k.id_producto = p.id_producto " +
                     "INNER JOIN usuarios u ON k.id_usuario = u.id_usuario " +
                     "ORDER BY k.fecha DESC";
                     
        try {
            Connection con = Conexion.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                MovimientoKardex mov = new MovimientoKardex();
                mov.setFecha(rs.getTimestamp("fecha"));
                mov.setTipoMovimiento(rs.getString("tipo_movimiento"));
                mov.setNombreProducto(rs.getString("producto"));
                mov.setCantidad(rs.getInt("cantidad"));
                mov.setMotivo(rs.getString("motivo"));
                mov.setNombreUsuario(rs.getString("username"));
                lista.add(mov);
            }
        } catch (Exception e) {
            System.err.println("Error en KardexDAO: " + e.getMessage());
        }
        return lista;
    }
}