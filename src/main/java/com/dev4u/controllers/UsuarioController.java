/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev4u.controllers;

import com.dev4u.ClausulaSQL.ClausulaWhere;
import com.dev4u.ClausulaSQL.GeneroCondicaoWhere;
import com.dev4u.ClausulaSQL.OperacaoCondicaoWhere;
import com.dev4u.ClausulaSQL.TipoCondicaoWhere;
import com.dev4u.sdream.dao.UsuarioDAO;
import com.dev4u.sdream.model.Usuario;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Washington
 */
@Path("usuario")
public class UsuarioController {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response salvar(Usuario usuario) {
        try {
            UsuarioDAO dao = new UsuarioDAO();
            if (usuario.getIdUsuario() != 0) {
                dao.inserirUsuario(usuario);
                return Response.status(Response.Status.OK).build();
            } else {
                dao.alterarUsuario(usuario);
                return Response.status(Response.Status.OK).build();
            }
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public List<Usuario> listarUsuarios() {
        try {
            UsuarioDAO dao = new UsuarioDAO();
            ClausulaWhere condicao = new ClausulaWhere();
            condicao.AdicionarCondicao(OperacaoCondicaoWhere.vazio, "nome", GeneroCondicaoWhere.contem, "", TipoCondicaoWhere.Texto);
            return dao.listarUsuarios(condicao);
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletarUsuario(@PathParam("id") long id) {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.deletarUsuario(id);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario getUsuario(@PathParam("id") long id) {
        try {
            UsuarioDAO dao = new UsuarioDAO();
            return dao.getUsuario(id);
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
