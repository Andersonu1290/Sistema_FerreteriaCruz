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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ferreteriacruz.config.Conexion;
import ferreteriacruz.modelo.SerieHardware;

public class MermaDAO {

    public List<SerieHardware> listarSeries(String estadoFiltro) {
        List<SerieHardware> lista = new ArrayList<>();
        String sql = "SELECT s.*, p.nombre, p.codigo_SKU " +
                     "FROM series_hardware s " +
                     "INNER JOIN productos p ON s.id_producto = p.id_producto " +
                     "WHERE s.estado = ?";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estadoFiltro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SerieHardware s = new SerieHardware();
                    s.setIdSerie(rs.getInt("id_serie"));
                    s.setNumeroSerie(rs.getString("numero_serie"));
                    s.setIdProducto(rs.getInt("id_producto"));
                    s.setEstado(rs.getString("estado"));
                    s.setNombreProducto(rs.getString("nombre"));
                    s.setCodigoSKU(rs.getString("codigo_SKU"));
                    lista.add(s);
                }
            }
        } catch (Exception e) { System.err.println("Error listar series: " + e.getMessage()); }
        return lista;
    }

    public boolean procesarMerma(String nroSerie, String motivo, int idUsuario) {
        boolean exito = false;
        Connection con = null;
        try {
            con = Conexion.getInstancia().getConexion();
            con.setAutoCommit(false);

            int idProducto = -1;
            String sqlCheck = "SELECT id_producto FROM series_hardware WHERE numero_serie = ? AND estado = 'DISPONIBLE'";
            try(PreparedStatement psCheck = con.prepareStatement(sqlCheck)){
                psCheck.setString(1, nroSerie);
                ResultSet rs = psCheck.executeQuery();
                if(rs.next()) idProducto = rs.getInt("id_producto");
                else throw new Exception("La serie no está disponible.");
            }

            String sqlUpd = "UPDATE series_hardware SET estado = 'MERMA' WHERE numero_serie = ?";
            try(PreparedStatement psUpd = con.prepareStatement(sqlUpd)){
                psUpd.setString(1, nroSerie);
                psUpd.executeUpdate();
            }

            String sqlStock = "UPDATE productos SET stock_actual = stock_actual - 1 WHERE id_producto = ?";
            try(PreparedStatement psStock = con.prepareStatement(sqlStock)){
                psStock.setInt(1, idProducto);
                psStock.executeUpdate();
            }

            String sqlKardex = "INSERT INTO kardex_movimientos (id_producto, tipo_movimiento, cantidad, motivo, id_usuario) VALUES (?, 'MERMA', 1, ?, ?)";
            try(PreparedStatement psKardex = con.prepareStatement(sqlKardex)){
                psKardex.setInt(1, idProducto);
                psKardex.setString(2, "MERMA S/N: " + nroSerie + " | Motivo: " + motivo);
                psKardex.setInt(3, idUsuario);
                psKardex.executeUpdate();
            }
            
            con.commit();
            exito = true;
        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) {}
            System.err.println("Error procesar merma: " + e.getMessage());
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (SQLException ex) {}
        }
        return exito;
    }
}