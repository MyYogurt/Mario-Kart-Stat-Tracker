package org.moisiadis.stattrackerweb;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.moisiadis.stattrackercore.model.Driver;
import org.moisiadis.stattrackercore.model.DriverResult;
import org.moisiadis.stattrackercore.model.DriverStats;

import java.sql.SQLException;
import java.util.List;

import static org.moisiadis.stattrackerweb.Main.db;

@Path("driver")
public class DriverResource {
	@POST
	@Path("{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Driver addDriver(@PathParam("name") String name) throws SQLException {
		return db.createDriver(name);
	}

	@GET
	@Path("{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Driver getDriver(@PathParam("name") String name) throws SQLException {
		return db.getDriver(name);
	}

	@GET
	@Path("{name}/races")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DriverResult> getDriverRaces(@PathParam("name") String name) throws SQLException {
		Driver driver = db.getDriver(name);
		return db.getAllRacesOfDriver(driver.getId());
	}

	@GET
	@Path("{name}/stats")
	@Produces(MediaType.APPLICATION_JSON)
	public DriverStats getDriverStats(@PathParam("name") String name) throws SQLException {
		Driver driver = db.getDriver(name);
		return db.getDiverStats(driver.getId());
	}
}
