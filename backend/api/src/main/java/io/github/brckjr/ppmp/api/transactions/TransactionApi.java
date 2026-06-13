package io.github.brckjr.ppmp.api.transactions;

import io.github.brckjr.ppmp.api.transactions.dto.TransactionsDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TransactionApi {

    @GET
    @Path("/")
    TransactionsDto getTransactions();
}
