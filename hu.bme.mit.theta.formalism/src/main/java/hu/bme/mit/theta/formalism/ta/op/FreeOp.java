package hu.bme.mit.theta.formalism.ta.op;

import hu.bme.mit.theta.core.stmt.HavocStmt;
import hu.bme.mit.theta.core.type.RatType;
import hu.bme.mit.theta.formalism.common.decl.ClockDecl;

public interface FreeOp extends ClockOp {

	public ClockDecl getClock();

	@Override
	public HavocStmt<RatType> toStmt();

}