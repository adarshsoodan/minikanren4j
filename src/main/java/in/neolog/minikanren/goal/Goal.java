/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.goal;

import in.neolog.minikanren.SubstMap;
import in.neolog.minikanren.stream.LazyStream;

public interface Goal {

    LazyStream<SubstMap> with(SubstMap map);

}
