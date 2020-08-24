package in.neolog.minikanren.goal;

import in.neolog.minikanren.SubstMap;
import in.neolog.minikanren.stream.LazyStream;

public interface Goal {

    LazyStream<SubstMap> with(SubstMap map);

}
