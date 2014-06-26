package com.zuehlke.camp2014.schild.siegfried.logic;

import joptsimple.internal.Strings;
import junit.framework.TestCase;

import org.elasticsearch.common.collect.Lists;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.zuehlke.camp2014.schild.siegfried.MoveService;
import com.zuehlke.camp2014.schild.siegfried.UpdatesService;
import com.zuehlke.camp2014.schild.siegfried.domain.Move;
import com.zuehlke.camp2014.schild.siegfried.domain.Update;

public class LogicTest extends TestCase {

	private MoveService moveService;
	private UpdatesService updatesService;
	
	@Test
	public void testMoveForUserWithoutPlate()  {
		// given
		assertTrue("there are no pending updates", updatesService.getAll().isEmpty());
		
		// when
		final Move move = new Move(
				"", "heinrich", "42"
				);
		moveService.put(move);
		
		// then
		assertEquals("there is one update in pending", 1, updatesService.getPending().size());
		assertEquals("there is one update in all", 1, updatesService.getAll().size());
		
	}

	@Test
	public void testMoveForUserWithPlate() {
		// given
		UpdatesLogic.updates.add(new Update("fixUpdate", "42", Lists.newArrayList("heinrich"), "pending"));
		assertEquals("there is one update pending", 1, updatesService.getAll().size());
		
		// when
		// heinrich moves to 43
		final Move move = new Move(
				"", "heinrich", "43"
				);
		
		moveService.put(move);
		
		// then
		assertEquals("there are two updates in pending", 2, updatesService.getPending().size());
		assertEquals("there are two updates in all", 2, updatesService.getAll().size());
		final Update newUpdate = updatesService.getPending().iterator().next();
		assertTrue("there is an update fixUpdate for plate 42", Collections2.filter(updatesService.getPending(), new Predicate<Update>() {
			@Override
			public boolean apply(Update input) {
				return 
						input.getUpdateId().equals("fixUpdate")
						&& input.getPlateId().equals("42");
			}
		}).size() > 0);
		assertTrue("there is an update fixUpdate for plate 42", Collections2.filter(updatesService.getPending(), new Predicate<Update>() {
			@Override
			public boolean apply(Update input) {
				return input.getPlateId().equals("43") && !Strings.isNullOrEmpty(input.getUpdateId());
			}
		}).size() > 0);
		
		assertNotSame("the update is not the old one", "fixUpdate", newUpdate.getUpdateId());
	}
	
}
