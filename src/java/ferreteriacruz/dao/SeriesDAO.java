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
import ferreteriacruz.config.Conexion;
import ferreteriacruz.modelo.Series;

public class SeriesDAO {
    
    public boolean registrarSerie(Series serie) {
        String sql = "INSERT INTO series (numero_serie, id_producto, estado) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, serie.getNumeroSerie());
            ps.setInt(2, serie.getIdProducto());
            ps.setString(3, serie.getEstado());
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.err.println("Error al registrar serie en BD: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarSeriesDisponibles(int idProducto, int cantidad) {
        String sql = "DELETE FROM series WHERE id_serie IN (" +
                     "  SELECT id FROM (" +
                     "    SELECT id_serie AS id FROM series " +
                     "    WHERE id_producto = ? AND estado = 'DISPONIBLE' " +
                     "    ORDER BY id_serie DESC LIMIT ?" +
                     "  ) AS temp" +
                     ")";
                     
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setInt(1, idProducto);
            ps.setInt(2, cantidad);
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.err.println("Error al eliminar series excedentes: " + e.getMessage());
            return false;
        }
    }

    public java.util.List<Series> listarSeriesDisponibles(int idProducto) {
        java.util.List<Series> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM series WHERE id_producto = ? AND estado = 'DISPONIBLE'";
        
        try (java.sql.Connection con = ferreteriacruz.config.Conexion.getInstancia().getConexion();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setInt(1, idProducto);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Series s = new Series();
                    s.setIdSerie(rs.getInt("id_serie"));
                    s.setNumeroSerie(rs.getString("numero_serie"));
                    s.setEstado(rs.getString("estado"));
                    s.setIdProducto(rs.getInt("id_producto"));
                    lista.add(s);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar series: " + e.getMessage());
        }
        return lista;
    }
    
}