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
package hu.bme.mit.theta.formalism.xta;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Streams.zip;
import static java.util.stream.Collectors.joining;

import java.util.List;

import com.google.common.collect.ImmutableList;

import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.Type;

public final class Sync {

	public enum Kind {
		EMIT, RECV
	}

	private final Label label;
	private final List<Expr<?>> args;
	private final Kind kind;

	private Sync(final Label label, final List<? extends Expr<?>> args, final Kind kind) {
		checkNotNull(label);
		checkNotNull(args);
		checkNotNull(kind);
		final List<Type> types = label.getParamTypes();
		checkArgument(args.size() == types.size());
		checkArgument(zip(args.stream(), types.stream(), (a, t) -> a.getType() == t).allMatch(p -> p));
		this.label = label;
		this.args = ImmutableList.copyOf(args);
		this.kind = kind;
	}

	public static Sync emit(final Label label, final List<? extends Expr<?>> args) {
		return new Sync(label, args, Kind.EMIT);
	}

	public static Sync recv(final Label label, final List<? extends Expr<?>> args) {
		return new Sync(label, args, Kind.RECV);
	}

	public Label getLabel() {
		return label;
	}

	public List<Expr<?>> getArgs() {
		return args;
	}

	public Kind getKind() {
		return kind;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}

	@Override
	public boolean equals(final Object obj) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(label.getName());
		if (!args.isEmpty()) {
			sb.append('(');
			sb.append(args.stream().map(Object::toString).collect(joining(", ")));
			sb.append(')');
		}
		if (kind == Kind.EMIT) {
			sb.append('!');
		} else {
			sb.append('?');
		}
		return sb.toString();

	}

}
