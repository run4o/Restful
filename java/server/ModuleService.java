package server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


/*
* Response to Feedback:
* I will not merge the client and server into a single project this time.
 */


/**
 * REST Web Service for a Module resource.
 *
 * @author Martin Trifonov (martin.trifonov98@gmail.com)
 */
@Singleton
@Path("/module")
public class ModuleService {

    @Context
    private UriInfo context;
    /**
     * Hash map holding all data.
     */
    private static HashMap<String, Module> data = new HashMap<>();
    /**
     * ReentrantReadWriteLock fair lock. Fair policy is set to true, and long
     * waiting operations will get priority.
     */
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    /**
     * Creates a new instance of GenericResource
     */
    public ModuleService() {
    }

    /**
     * Returns all modules. Can also filter for active/inactive.
     *
     * @param filterInput status filter, true = only active, false = only
     * discontinued, anything else = all.
     * @return JsonArray holding modules.
     */
    @GET
    @Produces("application/json")
    @Consumes("text/plain")
    public JsonArray getModules(@DefaultValue("all") @QueryParam("filter") String filterInput) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        boolean check = false;
        boolean filter = false;
        if (filterInput.equalsIgnoreCase("true") || filterInput.equalsIgnoreCase("false")) {
            check = true;
            filter = Boolean.valueOf(filterInput);
        }
        lock.readLock().lock();
        try {
            for (Map.Entry<String, Module> elem : data.entrySet()) {
                if (elem != null) {
                    if (check) {
                        if (elem.getValue().isActive() == filter) {
                            arrayBuilder.add(Json.createObjectBuilder().
                                    add("name", elem.getValue().getName()).
                                    add("Subject", elem.getValue().getSubject()).
                                    add("Level", elem.getValue().getLevel()).
                                    add("Active", elem.getValue().isActive()));
                        }
                    } else {
                        arrayBuilder.add(Json.createObjectBuilder().
                                add("name", elem.getValue().getName()).
                                add("Subject", elem.getValue().getSubject()).
                                add("Level", elem.getValue().getLevel()).
                                add("Active", elem.getValue().isActive()));

                    }
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return arrayBuilder.build();
    }

    /**
     * Returns all modules in a subject. Can also filter for active/inactive.
     *
     * @param subject Subject filter.
     * @param filterInput status filter, true = only active, false = only
     * discontinued, anything else = all.
     * @return JsonArray holding modules.
     */
    @GET
    @Path("/{subject}")
    @Consumes("text/plain")
    @Produces("application/json")
    public JsonArray getModulesBySubject(@PathParam("subject") String subject, @DefaultValue("all") @QueryParam("filter") String filterInput) {

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        boolean check = false;
        boolean filter = false;
        if (filterInput.equalsIgnoreCase("true") || filterInput.equalsIgnoreCase("false")) {
            check = true;
            filter = Boolean.valueOf(filterInput);
        }
        lock.readLock().lock();
        try {
            for (Map.Entry<String, Module> elem : data.entrySet()) {
                if (elem != null) {
                    if (elem.getValue().getSubject().equals(subject)) {
                        if (check) {
                            if (elem.getValue().isActive() == filter) {
                                arrayBuilder.add(Json.createObjectBuilder().
                                        add("name", elem.getValue().getName()).
                                        add("Subject", elem.getValue().getSubject()).
                                        add("Level", elem.getValue().getLevel()).
                                        add("Active", elem.getValue().isActive()));
                            }
                        } else {
                            arrayBuilder.add(Json.createObjectBuilder().
                                    add("name", elem.getValue().getName()).
                                    add("Subject", elem.getValue().getSubject()).
                                    add("Level", elem.getValue().getLevel()).
                                    add("Active", elem.getValue().isActive()));

                        }
                    }
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return arrayBuilder.build();
    }

    /**
     *
     * Returns all modules filtered by subject and level . Can also filter for
     * active/inactive.
     *
     * @param subject subject filter.
     * @param level level filter.
     * @param filterInput status filter, true = only active, false = only
     * discontinued, anything else = all.
     * @return JsonArray holding modules
     */
    @GET
    @Path("/{subject}/{level}")
    @Consumes("text/plain")
    @Produces("application/json")
    public JsonArray getModulesBySubjectAndLevel(@PathParam("subject") String subject, @PathParam("level") String level, @DefaultValue("all") @QueryParam("filter") String filterInput) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        boolean check = false;
        boolean filter = false;
        if (filterInput.equalsIgnoreCase("true") || filterInput.equalsIgnoreCase("false")) {
            check = true;
            filter = Boolean.valueOf(filterInput);
        }
        lock.readLock().lock();
        try {
            for (Map.Entry<String, Module> elem : data.entrySet()) {
                if (elem != null) {
                    if (elem.getValue().getSubject().equals(subject)
                            && elem.getValue().getLevel().equals(level)) {
                        if (check) {
                            if (elem.getValue().isActive() == filter) {
                                arrayBuilder.add(Json.createObjectBuilder().
                                        add("name", elem.getValue().getName()).
                                        add("Subject", elem.getValue().getSubject()).
                                        add("Level", elem.getValue().getLevel()).
                                        add("Active", elem.getValue().isActive()));
                            }
                        } else {
                            arrayBuilder.add(Json.createObjectBuilder().
                                    add("name", elem.getValue().getName()).
                                    add("Subject", elem.getValue().getSubject()).
                                    add("Level", elem.getValue().getLevel()).
                                    add("Active", elem.getValue().isActive()));

                        }
                    }
                }
            }

            return arrayBuilder.build();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Add module to database.
     *
     * @param module JsonObject holding Module.
     * @return HTTP Status 201 (Created) or HTTP Status 409 (Conflict)
     */
    @POST
    @Consumes("application/json")
    public Response addModule(JsonObject module) {
        String subject = module.getJsonString("subject").getString();
        String name = module.getJsonString("name").getString();
        String level = module.getJsonString("level").getString();

        Module newModule = new Module(subject, level, name);
        lock.writeLock().lock();
        try {
            if (!data.containsKey(name)) {
                data.putIfAbsent(name, newModule);
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.CONFLICT).build();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Removes module from database.
     *
     * @param subject module's subject
     * @param level module's level
     * @param name module's name
     * @return HTTP Status 204 (Deleted) or HTTP Status 404 (NotFound)
     */
    @DELETE
    @Path("/{subject}/{level}/{name}")
    public Response deleteModule(@PathParam("subject") String subject, @PathParam("level") String level, @PathParam("name") String name) {

        lock.writeLock().lock();
        try {
            if (data.containsKey(name)) {
                data.remove(name);
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Changes Status of a module
     *
     * @param subject module's subject
     * @param level module's level
     * @param name module's name
     * @param status new status.
     * @return HTTP Status 200 (OK) or HTTP Status 404 (NotFound)
     */
    @PUT
    @Path("/{subject}/{level}/{name}")
    @Consumes("text/plain")
    public Response changeState(@PathParam("subject") String subject, @PathParam("level") String level, @PathParam("name") String name, String status) {

        //matches input to state, if input doesnt match, matches to false
        boolean active = Boolean.valueOf(status);
        lock.writeLock().lock();
        try {
            if (data.containsKey(name)) {
                data.get(name).setActive(active);
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

}
