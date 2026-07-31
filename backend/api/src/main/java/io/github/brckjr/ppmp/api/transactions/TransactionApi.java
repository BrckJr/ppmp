package io.github.brckjr.ppmp.api.transactions;

import io.github.brckjr.ppmp.api.transactions.dto.TransactionDto;
import io.github.brckjr.ppmp.api.transactions.dto.TransactionMetricsDto;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Transactions", description = "Operations related to portfolio transactions and metrics")
public interface TransactionApi {

  @GET
  @Path("/")
  @Operation(summary = "Get transaction history", description = "Retrieves transactions with optional filtering and pagination")
  @APIResponse(responseCode = "200", description = "Success")
  List<TransactionDto> getAllTransactions(
    @QueryParam("type") @Parameter(description = "Filter by transaction type") TransactionType type,
    @QueryParam("limit") @DefaultValue("50") @Parameter(description = "Page size limit") int limit,
    @QueryParam("offset") @DefaultValue("0") @Parameter(description = "Pagination offset") int offset
  );

  @GET
  @Path("/{id}")
  @Operation(summary = "Get single transaction", description = "Retrieves full details for a specific transaction")
  @APIResponse(responseCode = "200", description = "Found", content = @Content(schema = @Schema(implementation = TransactionDto.class)))
  @APIResponse(responseCode = "404", description = "Transaction not found")
  TransactionDto getTransactionById(@PathParam("id") UUID id);

  @GET
  @Path("/metrics")
  @Operation(summary = "Get transaction metrics", description = "Computes aggregated top-level KPIs for dashboard display")
  @APIResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = TransactionMetricsDto.class)))
  TransactionMetricsDto getTransactionMetrics(
    @QueryParam("period") @DefaultValue("ytd") @Parameter(description = "Timeframe for metrics: 'ytd', '1y', 'all'") String period
  );
  
  @POST
  @Path("")
  @Operation(summary = "Create transaction", description = "Records a new transaction or cash activity")
  @APIResponse(
    responseCode = "201",
    description = "Created successfully",
    content = @Content(schema = @Schema(implementation = TransactionDto.class))
  )
  @APIResponse(responseCode = "400", description = "Invalid payload")
  TransactionDto createTransaction(@Valid TransactionDto newTransaction);

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete transaction", description = "Deletes an existing transaction record")
  @APIResponse(responseCode = "204", description = "Deleted successfully")
  @APIResponse(responseCode = "404", description = "Transaction not found")
  void deleteTransaction(@PathParam("id") UUID id);

}