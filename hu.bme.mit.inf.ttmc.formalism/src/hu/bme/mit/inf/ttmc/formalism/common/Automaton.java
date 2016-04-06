package hu.bme.mit.inf.ttmc.formalism.common;

import java.util.Collection;

public interface Automaton {

	public Loc getInitLoc();

	public Collection<? extends Loc> getLocs();

	public Collection<? extends Edge> getEdges();

}
