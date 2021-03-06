/*
 *  Copyright 2017 Budapest University of Technology and Economics
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package hu.bme.mit.theta.core.clock.op;

import static hu.bme.mit.theta.core.type.rattype.RatExprs.Rat;

import hu.bme.mit.theta.core.clock.constr.ClockConstr;
import hu.bme.mit.theta.core.clock.constr.ClockConstrs;
import hu.bme.mit.theta.core.decl.Decl;
import hu.bme.mit.theta.core.decl.VarDecl;
import hu.bme.mit.theta.core.stmt.AssignStmt;
import hu.bme.mit.theta.core.stmt.AssumeStmt;
import hu.bme.mit.theta.core.stmt.HavocStmt;
import hu.bme.mit.theta.core.stmt.Stmt;
import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.Type;
import hu.bme.mit.theta.core.type.abstracttype.AddExpr;
import hu.bme.mit.theta.core.type.anytype.RefExpr;
import hu.bme.mit.theta.core.type.booltype.BoolType;
import hu.bme.mit.theta.core.type.inttype.IntLitExpr;
import hu.bme.mit.theta.core.type.rattype.RatLitExpr;
import hu.bme.mit.theta.core.type.rattype.RatType;
import hu.bme.mit.theta.core.utils.StmtVisitor;
import hu.bme.mit.theta.core.utils.TypeUtils;

public final class ClockOps {

	private static final StmtToClockOpVisitor VISITOR;

	static {
		VISITOR = new StmtToClockOpVisitor();
	}

	private ClockOps() {
	}

	////

	public static ClockOp fromStmt(final Stmt stmt) {
		return stmt.accept(VISITOR, null);
	}

	////

	public static CopyOp Copy(final VarDecl<RatType> var, final VarDecl<RatType> value) {
		return new CopyOp(var, value);
	}

	public static FreeOp Free(final VarDecl<RatType> var) {
		return new FreeOp(var);
	}

	public static GuardOp Guard(final ClockConstr constr) {
		return new GuardOp(constr);
	}

	public static ResetOp Reset(final VarDecl<RatType> var, final int value) {
		return new ResetOp(var, value);
	}

	public static ShiftOp Shift(final VarDecl<RatType> var, final int offset) {
		return new ShiftOp(var, offset);
	}

	////

	private static final class StmtToClockOpVisitor implements StmtVisitor<Void, ClockOp> {

		private StmtToClockOpVisitor() {
		}

		@Override
		public <DeclType extends Type> ClockOp visit(final HavocStmt<DeclType> stmt, final Void param) {
			final VarDecl<RatType> var = TypeUtils.cast(stmt.getVarDecl(), Rat());
			return Free(var);
		}

		@Override
		public <DeclType extends Type> ClockOp visit(final AssignStmt<DeclType> stmt, final Void param) {

			final VarDecl<RatType> var = TypeUtils.cast(stmt.getVarDecl(), Rat());
			final Expr<?> expr = stmt.getExpr();

			if (expr instanceof IntLitExpr) {
				final IntLitExpr intLit = (IntLitExpr) expr;
				final int value = Math.toIntExact(intLit.getValue());
				return Reset(var, value);

			} else if (expr instanceof RefExpr) {
				final RefExpr<?> rightRef = (RefExpr<?>) expr;
				final Decl<?> rightDecl = rightRef.getDecl();
				if (rightDecl instanceof VarDecl) {
					final VarDecl<?> rightVar = (VarDecl<?>) rightDecl;
					final VarDecl<RatType> rightRatVar = TypeUtils.cast(rightVar, Rat());
					return Copy(var, rightRatVar);
				}

			} else if (expr instanceof AddExpr) {
				final RefExpr<RatType> varRef = var.getRef();
				final AddExpr<?> addExpr = (AddExpr<?>) expr;
				final Expr<?>[] ops = addExpr.getOps().toArray(new Expr<?>[0]);

				if (ops.length == 2) {
					if (ops[0].equals(varRef)) {
						if (ops[1] instanceof RatLitExpr) {
							final RatLitExpr ratLit = (RatLitExpr) ops[1];
							final int num = ratLit.getNum();
							final int denom = ratLit.getDenom();
							if (denom == 1) {
								return Shift(var, num);
							}
						}
					} else if (ops[1].equals(varRef)) {
						if (ops[0] instanceof IntLitExpr) {
							final IntLitExpr intLit = (IntLitExpr) ops[0];
							final int offset = Math.toIntExact(intLit.getValue());
							return Shift(var, offset);
						}
					}
				}
			}

			throw new IllegalArgumentException();
		}

		@Override
		public ClockOp visit(final AssumeStmt stmt, final Void param) {
			try {
				final Expr<BoolType> cond = stmt.getCond();
				final ClockConstr constr = ClockConstrs.formExpr(cond);
				return Guard(constr);

			} catch (final IllegalArgumentException e) {
				throw new IllegalArgumentException();
			}
		}

	}

}
