package io.github.brckjr.ppmp.app;

import io.github.brckjr.ppmp.api.BaseApi;
import io.github.brckjr.ppmp.domain.model.BaseModel;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.UUID;

public abstract class BaseApp<D extends BaseModel> implements BaseApi<D> {

    protected BaseService<D> service;

    public BaseApp(BaseService<D> service) {
        this.service = service;
    }

    public BaseApp() {
        // required for proxying
    }

    @Override
    @GET
    @Path("/all")
    public List<D> findAll() {
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    public D findById(@PathParam("id") UUID id) {
        return service.findById(id);
    }

    @Override
    @POST
    public D create(D model) {
        return service.create(model);
    }

    @Override
    @PUT
    @Path("/{id}")
    public D update(@PathParam("id") UUID uuid, D model) {
        return service.update(uuid, model);
    }

    @Override
    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") UUID id) {
        service.deleteById(id);
    }

    @Override
    @GET
    @Path("/count")
    public long count() {
        return service.count();
    }
}

