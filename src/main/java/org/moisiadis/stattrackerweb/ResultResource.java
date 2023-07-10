package org.moisiadis.stattrackerweb;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.moisiadis.stattrackercore.model.CreateResultRequest;
import org.moisiadis.stattrackercore.model.Result;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.moisiadis.stattrackerweb.Main.db;

@Path("result")
public class ResultResource {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public List<Result> newResult(List<CreateResultRequest> requests) throws SQLException {
		List<Result> results = new LinkedList<>();
		for (CreateResultRequest request : requests) {
			int driverId = db.getDriver(request.getDriver()).getId();
			results.add(db.createResult(request.getRaceId(), driverId, request.getPosition(), request.getPoints()));
		}
		return results;
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Result getResult(@PathParam("id") int id) throws SQLException {
		return db.getResult(id);
	}

	@DELETE
	@Path("{id}")
	public Response deleteResult(@PathParam("id") int id) throws SQLException {
		db.deleteResult(id);

		return Response.status(200).build();
	}
}
