/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev4u.sdream.dao;

import com.dev4u.ClausulaSQL.ClausulaWhere;
import com.dev4u.sdream.conexao.ConexaoJDBC;
import com.dev4u.sdream.conexao.ConexaoMySQL;
import com.dev4u.sdream.enumerado.usuario.Tipo;
import com.dev4u.sdream.model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SQL
 */
public class UsuarioDAO {

    private final ConexaoJDBC conexao;

    public UsuarioDAO() throws ClassNotFoundException, SQLException {
        this.conexao = new ConexaoMySQL();
    }

    public Usuario inserirUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO USUARIO (NOME,EMAIL,SENHA,TIPO,DATA_CADASTRO) VALUES (?,?,?,?,SYSDATE())";
        try {

            PreparedStatement ps = this.conexao.getConnection().prepareStatement(sql);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getTipo().toString());

            ps.executeQuery();
            this.conexao.commit();
            usuario.setIdUsuario(getLastId());
            return usuario;
        } catch (SQLException e) {
            this.conexao.rollback();
            throw new RuntimeException(e.getMessage());
        }

    }

    public Usuario alterarUsuario(Usuario usuario) {
        String sql = "UPDATE USUARIO SET NOME = ?,EMAIL = ?,SENHA = ?, TIPO = ? WHERE ID_USUARIO = ?";
        try {

            PreparedStatement ps = this.conexao.getConnection().prepareStatement(sql);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getTipo().toString());
            ps.setLong(5, usuario.getIdUsuario());
            ps.executeQuery();
            this.conexao.commit();
            return usuario;
        } catch (SQLException e) {
            this.conexao.rollback();
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Usuario> listarUsuarios(ClausulaWhere sClausula) throws SQLException {
        String sql = " SELECT * FROM USUARIO " + sClausula.montarsClausula();
        try {
            PreparedStatement ps = this.conexao.getConnection().prepareStatement(sql);
            List<Usuario> usuarios = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setTipo(Tipo.valueOf(rs.getString("tipo")));
                usuarios.add(u);
            }

            return usuarios;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Usuario getUsuario(long id) throws SQLException {
        String sql = " SELECT * FROM USUARIO WHERE ID_USUARIO = " + id;
        try {
            PreparedStatement ps = this.conexao.getConnection().prepareStatement(sql);
            Usuario u = new Usuario();
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setTipo(Tipo.valueOf(rs.getString("tipo")));
            }
            return u;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean deletarUsuario(long id) throws SQLException {
        String sql = "delete from usuario where id_usuario = " + id;
        try {
            PreparedStatement ps = this.conexao.getConnection().prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeQuery();
            this.conexao.commit();
            return true;
        } catch (SQLException e) {
            this.conexao.rollback();            
            throw e;
        }
    }

    public long getLastId() throws SQLException {

        String sql = "SELECT MAX(ID_USUARIO) id FROM USUARIO";
        try {
            PreparedStatement ps = this.conexao.getConnection().prepareStatement(sql);
            long id;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return id = rs.getLong("id");
            } else {
                return 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
