package org.moisiadis.stattrackerweb;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.moisiadis.stattrackercore.model.Race;
import org.moisiadis.stattrackercore.model.Result;
import org.moisiadis.stattrackercore.model.ResultCreateRequest;

import java.sql.SQLException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import static org.moisiadis.stattrackerweb.Main.db;

@Path("race")
public class RaceResource {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newRace(@QueryParam("GrandPrix") String grandPrixName, @QueryParam("CC") String cc, List<ResultCreateRequest> results) throws SQLException {
		int grandPrixId = db.getGrandPrix(grandPrixName).getId();
		Race race = db.createRace(grandPrixId, Instant.now(), cc);

		List<Result> resultList = new LinkedList<>();

		for (ResultCreateRequest result : results) {
			int driverId = db.getDriver(result.getDriver()).getId();
			Result newResult = db.createResult(race.getID(), driverId, result.getPosition(), result.getPoints());
			resultList.add(newResult);
		}

		race.setResults(resultList);

		System.out.println(race);
		return Response.status(201).entity(race).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Race getRaceResults(@PathParam("id") int raceId) throws SQLException {
		Race race = db.getRaceResults(raceId);
		return race;
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Race> getAllRaces() throws SQLException {
		return db.getAllRacesWithResults();
	}
}
