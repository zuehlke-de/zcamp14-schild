package com.zuehlke.camp2014.schild.siegfried;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.zuehlke.camp2014.schild.siegfried.domain.Plate;

@Path("/plates")
public class PlatesService {
	
	public static List<Plate> plates = Lists.newArrayList(
			new Plate("42", Lists.<String>newArrayList()),
			new Plate("43", Lists.<String>newArrayList())
			);
	
	public static Plate get(final String plateId) {
		return Iterables.tryFind(plates, new Predicate<Plate>() {
			@Override
			public boolean apply(final Plate input) {
				return input.getPlateId().equals(plateId);
			}
			
		}).orNull();
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Plate> getAll() {
		return Lists.<Plate>newArrayList(plates);
	}
}
