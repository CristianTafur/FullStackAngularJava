/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import orm.JPAUtil;
import orm.Usuario;

/**
 * REST Web Service
 *
 * @author Cristian Tafur
 */
@Path("generic")

public class GenericResource extends modelo.AbstractFacade<Usuario>{ 
    
     EntityManager em;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
          super(Usuario.class);
         em = JPAUtil.getEntityManagerFactory().createEntityManager();
          System.out.println(em==null?"no conecto":"conecto");
    }

    /**
     * Retrieves representation of an instance of modelo.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> getJson() {
        //TODO return proper representation object
       return super.findAll();
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
     //mis metodos
    @GET
    @Path("/usuarios")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUsers() {
         List<Usuario> users=super.findAll();
         JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
         JsonObjectBuilder builder;
         JsonObject object;
         for (Usuario user : users) {
             System.out.println(user.getDocumento());
             builder=Json.createObjectBuilder();
             builder.add("documento",user.getDocumento());
             builder.add("nombre", user.getNombre()); 
             object=builder.build();
             arrayBuilder.add(object);
         }
         //System.out.println(""+super.findAll().toString());
        // System.out.println(""+arrayBuilder.build().toString());
        return  responder(arrayBuilder.build().toString());//metodo con permisos para otro puerto 
    }
    @GET
    @Path("/usuarios/insetar/{document}/{name}")
    @Consumes({MediaType.APPLICATION_JSON}) 
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response setUser(@PathParam("document") String document,@PathParam("name") String name){
        
        Usuario u = new Usuario();
        u.setDocumento(document);
        u.setNombre(name); 
        
        super.create(u);
        return responder(convertirAJSON(u));
    }
    @GET
    @Path("/usuarios/eliminar/{document}")
    @Consumes({MediaType.APPLICATION_JSON}) 
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response deleteUser(@PathParam("document") String document){
        
        Usuario u = new Usuario();
        u.setDocumento(document);
        u.setNombre("");
        super.remove(u); 
        return responder(convertirAJSON(u));
    }
    
    @GET
    @Path("/usuarios/editar/{document}/{name}") 
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response putUser(@PathParam("document") String document,@PathParam("name") String name){ 
        Usuario user =super.find(document);
        user.setNombre(name);
        super.edit(user); 
        return responder(convertirAJSON(user));
    }
    
    private String convertirAJSON(Usuario u){
        JsonObjectBuilder builder=Json.createObjectBuilder();
        builder.add("documento",u.getDocumento());
        builder.add("nombre", u.getNombre());  
        return builder.build().toString();
    }
    private Response responder(String obj){
        System.out.println(obj);
        return  Response.ok(obj).header("Access-Control-Allow-Origin","http://localhost:4200")
                             .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                             .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-with")
                             .build();
    }

    @Override
    protected EntityManager getEntityManager() { 
        return em; 
    }
}
