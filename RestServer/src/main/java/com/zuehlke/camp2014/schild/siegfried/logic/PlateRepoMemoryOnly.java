package com.zuehlke.camp2014.schild.siegfried.logic;

import java.util.List;

import org.elasticsearch.common.collect.Lists;

import com.zuehlke.camp2014.schild.siegfried.domain.Plate;

public class PlateRepoMemoryOnly implements PlateRepo {

	List<Plate> plates = Lists.newArrayList();
	
	{
		plates.add(new Plate("42", Lists.<String>newArrayList() ));
		plates.add(new Plate("43", Lists.<String>newArrayList() ));
	}
	
	@Override
	public List<Plate> getPlates() {
		return plates;
	}

}
