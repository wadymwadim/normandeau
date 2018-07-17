#include <boost/multiprecision/gmp.hpp>
#include <boost/optional.hpp>
#include <iostream>

#include "algorithms.hpp"

using Rational = boost::multiprecision::mpq_rational;
using Rectangle = geometry::Rectangle<Rational, geometry::Topology::Closed>;
using Segment = geometry::Segment<Rational, geometry::Topology::Closed>;
using PointQ = geometry::Point<Rational>;

int main() {
    const Rectangle rect{{0, 1}, {0, 1}};
    const Segment seg{{{1, 9}, {2, 3}}, {{1, 9}, {13, 18}}};

    std::cout << intersects(rect, seg) << std::endl;
}
