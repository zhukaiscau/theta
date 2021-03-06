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
package hu.bme.mit.theta.common.logging.impl;

import java.io.PrintStream;

import hu.bme.mit.theta.common.logging.Logger;

public final class ConsoleLogger extends MinLevelBasedLogger {

	private static final PrintStream CONSOLE = System.out;

	public ConsoleLogger(final int minLevel) {
		super(minLevel);
	}

	@Override
	public Logger write(final Object obj, final int level, final int padding) {
		if (level <= minLevel) {
			for (int i = 0; i < padding; ++i) {
				CONSOLE.print("|   ");
			}
			CONSOLE.print(obj);
		}
		return this;
	}

	@Override
	public Logger writeln(final int level) {
		if (level <= minLevel) {
			CONSOLE.println();
		}
		return this;
	}

	@Override
	public Logger writeHeader(final Object obj, final int level) {
		if (level <= minLevel) {
			CONSOLE.println();
			CONSOLE.println("----------" + obj + "----------");
		}
		return this;
	}

}
