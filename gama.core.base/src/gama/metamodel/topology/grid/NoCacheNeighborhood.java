/*******************************************************************************************************
 *
 * gama.metamodel.topology.grid.NoCacheNeighborhood.java, in plugin gama.core, is part of the source code of the
 * GAMA modeling and simulation platform (v. 1.8)
 *
 * (c) 2007-2018 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.metamodel.topology.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gama.common.interfaces.IAgent;
import gama.common.util.Collector;
import gama.runtime.exceptions.GamaRuntimeException;
import gama.runtime.scope.IScope;

public class NoCacheNeighborhood implements INeighborhood {

	/**
	 *
	 */
	private final GamaSpatialMatrix matrix;

	public NoCacheNeighborhood(final GamaSpatialMatrix gamaSpatialMatrix) {
		matrix = gamaSpatialMatrix;
	}

	@Override
	public void clear() {}

	/**
	 * Method getNeighborsIn()
	 *
	 * @see gama.metamodel.topology.grid.INeighborhood#getNeighborsIn(int, int)
	 */
	@Override
	public Set<IAgent> getNeighborsIn(final IScope scope, final int placeIndex, final int radius) {
		return computeNeighborsFrom(scope, placeIndex, 1, radius);
	}

	private Set<IAgent> computeNeighborsFrom(final IScope scope, final int placeIndex, final int begin, final int end) {
		try (final Collector.AsOrderedSet<IAgent> result = Collector.newOrderedSet()) {
			for (int i = begin; i <= end; i++) {
				for (final Integer index : matrix.usesVN ? get4NeighborsAtRadius(placeIndex, i)
						: get8NeighborsAtRadius(placeIndex, i)) {
					result.add(matrix.matrix[index].getAgent());
				}
			}
			// Addresses Issue 1071 by explicitly shuffling the result
			scope.getRandom().shuffle2(result);
			return result.items();
		}
	}

	protected List<Integer> get8NeighborsAtRadius(final int placeIndex, final int radius) {
		final int y = placeIndex / matrix.numCols;
		final int x = placeIndex - y * matrix.numCols;
		final List<Integer> v = new ArrayList<>(radius + 1 * radius + 1);
		int p;
		for (int i = 1 - radius; i < radius; i++) {
			p = matrix.getPlaceIndexAt(x + i, y - radius);
			if (p != -1) {
				v.add(p);
			}
			p = matrix.getPlaceIndexAt(x - i, y + radius);
			if (p != -1) {
				v.add(p);
			}
		}
		for (int i = -radius; i < radius + 1; i++) {
			p = matrix.getPlaceIndexAt(x - radius, y - i);
			if (p != -1) {
				v.add(p);
			}
			p = matrix.getPlaceIndexAt(x + radius, y + i);
			if (p != -1) {
				v.add(p);
			}
		}
		return v;
	}

	protected List<Integer> get4NeighborsAtRadius(final int placeIndex, final int radius) {
		final int y = placeIndex / matrix.numCols;
		final int x = placeIndex - y * matrix.numCols;

		final List<Integer> v = new ArrayList<>(radius << 2);
		int p;
		for (int i = -radius; i < radius; i++) {
			p = matrix.getPlaceIndexAt(x - i, y - Math.abs(i) + radius);
			if (p != -1) {
				v.add(p);
			}
			p = matrix.getPlaceIndexAt(x + i, y + Math.abs(i) - radius);
			if (p != -1) {
				v.add(p);
			}
		}
		return v;
	}

	/**
	 * Method isVN()
	 *
	 * @see gama.metamodel.topology.grid.INeighborhood#isVN()
	 */
	@Override
	public boolean isVN() {
		return false;
	}

	/**
	 * Method getRawNeighborsIncluding()
	 *
	 * @see gama.metamodel.topology.grid.INeighborhood#getRawNeighborsIncluding(int, int)
	 */
	@Override
	public int[] getRawNeighborsIncluding(final IScope scope, final int placeIndex, final int range) {
		throw GamaRuntimeException.warning("The diffusion of signals must rely on a neighbors cache in the grid",
				scope);
	}

	/**
	 * Method neighborsIndexOf()
	 *
	 * @see gama.metamodel.topology.grid.INeighborhood#neighborsIndexOf(int, int)
	 */
	@Override
	public int neighborsIndexOf(final IScope scope, final int placeIndex, final int n) {
		throw GamaRuntimeException.warning("The diffusion of signals must rely on a neighbors cache in the grid",
				scope);
	}

}