package io.github.brckjr.ppmp.api.instrument;

import io.github.brckjr.ppmp.api.instrument.dto.InstrumentDto;
import io.github.brckjr.ppmp.api.instrument.dto.InstrumentPriceDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Path("/instruments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Instruments", description = "Known instruments and their price history")
public interface InstrumentApi {

  // TODO: Restrict the access to this API as only the web crawler shall be able to upload and update instruments

  @GET
  @Path("")
  @Operation(summary = "List instruments", description = "Retrieves known instruments with optional search, type filter and pagination")
  @APIResponse(responseCode = "200", description = "Success")
  List<InstrumentDto> getAllInstruments(
    @QueryParam("q") @Parameter(description = "Case-insensitive search in ticker, name and ISIN") String query,
    @QueryParam("type") @Parameter(description = "Filter by instrument type") String type,
    @QueryParam("limit") @DefaultValue("50") @Parameter(description = "Page size limit") int limit,
    @QueryParam("offset") @DefaultValue("0") @Parameter(description = "Pagination offset") int offset
                                       );

  @GET
  @Path("/{id}")
  @Operation(summary = "Get single instrument", description = "Retrieves a specific instrument")
  @APIResponse(responseCode = "200", description = "Found", content = @Content(schema = @Schema(implementation = InstrumentDto.class)))
  @APIResponse(responseCode = "404", description = "Instrument not found")
  InstrumentDto getInstrumentById(@PathParam("id") UUID id);

  @POST
  @Path("")
  @Operation(summary = "Create instrument", description = "Registers a new instrument that transactions can refer to")
  @APIResponse(responseCode = "201", description = "Created successfully", content = @Content(schema = @Schema(implementation = InstrumentDto.class)))
  @APIResponse(responseCode = "400", description = "Invalid payload or ticker / ISIN already exists")
  InstrumentDto createInstrument(@Valid InstrumentDto newInstrument);

  @GET
  @Path("/{id}/prices")
  @Operation(summary = "Get price history", description = "Retrieves the stored prices of an instrument, oldest first")
  @APIResponse(responseCode = "200", description = "Success")
  @APIResponse(responseCode = "400", description = "Invalid date range")
  @APIResponse(responseCode = "404", description = "Instrument not found")
  List<InstrumentPriceDto> getInstrumentPrices(
    @PathParam("id") UUID id,
    @QueryParam("from") @Parameter(description = "Inclusive start date (ISO-8601)") LocalDate from,
    @QueryParam("to") @Parameter(description = "Inclusive end date (ISO-8601)") LocalDate to
                                              );

  @GET
  @Path("/{id}/prices/latest")
  @Operation(summary = "Get latest price", description = "Retrieves the most recent stored price of an instrument")
  @APIResponse(responseCode = "200", description = "Found", content = @Content(schema = @Schema(implementation = InstrumentPriceDto.class)))
  @APIResponse(responseCode = "404", description = "Instrument or price not found")
  InstrumentPriceDto getLatestInstrumentPrice(@PathParam("id") UUID id);
}
