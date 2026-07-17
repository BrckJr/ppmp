package io.github.brckjr.ppmp.api.transactions;

import io.github.brckjr.ppmp.api.transactions.dto.TransactionDto;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TransactionApi {

  @GET
  @Path("/")
  List<TransactionDto> getAllTransactions();

  @GET
  @Path("/tx-types")
  List<TransactionType> getTransactionTypes();

  @PUT
  @APIResponse(
      responseCode = "201",
      description = "Created",
      content = @Content(schema = @Schema(implementation = TransactionDto.class))
  )
  TransactionDto createTransaction(@Valid TransactionDto newTransaction);
}
