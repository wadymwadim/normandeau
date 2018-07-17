#include <iostream>

#include <tbb/parallel_invoke.h>

#include "evaluator.hpp"
#include "progress.hpp"
#include "verify.hpp"

template <template <typename> class Trig>
bool equations_positive(const std::vector<std::pair<EqVec<Trig>, Coeff64>>& eqs, const PointQ& center, const Rational& radius, Evaluator& eval) {

    for (const auto& p : eqs) {

        const auto pos = eval.is_positive(p.first, p.second, center, radius);

        if (!pos) {
            std::cout << "failure: not positive" << std::endl;
            std::cout << "f(x, y) = " << p.first << std::endl;
            std::cout << "bound = " << p.second << std::endl;
            std::cout << "radius = " << radius << std::endl;
            std::cout << "center = " << center << std::endl;
            return false;
        }
    }

    return true;
}

// the first is if the square was
static bool covers_square(const StableInfo& info, const ClosedRectangleQ& square, const uint32_t bits) {

    if (!geometry::subset(square, info.polygon)) {
        std::cerr << "failure: square is not a subset of polygon" << std::endl;
        return false;
    }

    Evaluator eval{bits};

    const auto center = square.center();
    // It's a square, so we can use width or height
    const Rational radius = square.width() / 2;

    if (!equations_positive(info.sines, center, radius, eval)) {
        std::cerr << "not all sines positive" << std::endl;
        return false;
    }

    if (!equations_positive(info.cosines, center, radius, eval)) {
        std::cerr << "not all cosines positive" << std::endl;
        return false;
    }

    return true;
}

#if 0
static bool triple_intersection(const TriplePair& triple_pair, const TripleInfo& info, const ClosedRectangleQ& square) {

    const auto line = triple_pair.unstable.sequence.constraint();

    // TODO also do periodic check. That's important too

    const std::array<PointQ, 4> corners{{square.lower_left(),
                                         square.lower_right(),
                                         square.upper_right(),
                                         square.upper_left()}};

    const std::array<int, 4> signs{}

    std::vector<PointQ> positives{}; // 0-4 points
    std::vector<PointQ> zeros{};     // 0, 1, or 2 points
    std::vector<PointQ> negatives{}; // 0-4 points

    for (const auto& corner : corners) {
        const auto sign = line_sign_at_point(line, corner);

        // More case analysis here
        if (sign > 0) {
            positives.push_back(corner);
        } else if (sign == 0) {
            zeros.push_back(corner)
        } else if (sign < 0) {
            negatives.push_back(corner);
        } else {
            throw std::runtime_error("big problem");
        }
    }

    // all positives must be inside stable_pos polygon
    // zeros must be within the segment
    // ditto for negatives
    // zeros must be in closure of polygons

    // then we find the radius's and midpoints that we use in the later calculations
}

static bool covers_square(const TripleInfo& info, const ClosedRectangleQ& square, const uint32_t bits) {

    if (!triple_geometry(info, square)) {
        return false;
    }

    // TODO: really we have the intersection of the line with the square, and then check if that
    // is a subset of the segment
    // Likewise we have the > 0 intersection with the square, then check that that is a subset
    // of the bounding polygon
    // And ditto for the < 0 intersection.
    // Need < 0 corners, intersection points, and > 0 corners. We can do all those at once.

    Evaluator eval{bits};

    if (midpoint) {

        // TODO we could also use a smaller square in this case too
        // TODO we also need to replace the intersects with a different check, one that
        // checks if the region we are looking at is a subset of the polygon
        const auto stable_pos_works = //intersects(square, info.stable_pos_info.polygon) &&
            all_positive(info.stable_pos_info.sines, center, radius, eval) &&
            all_positive(info.stable_pos_info.cosines, center, radius, eval);

        const auto unstable_works =
            all_positive(info.unstable_info.sines, *midpoint, radius, eval) &&
            all_positive(info.unstable_info.cosines, *midpoint, radius, eval);

        // We should do a check. If the square intersects the bounding polygon of either of the regions,
        // then we need to check that region. If it does not, then we don't need to check. This deals
        // with subtle issues like when the square is right on the side of a bounding polygon, but
        // strictly speaking don't intersect, since the bounding polygon is open.
        // We will leave it like this for now.

        // Normally, we need the square to be a subset of the bounding polygon. Is that a problem here?

        // And a smaller square here too
        const auto stable_neg_works = //intersects(square, info.stable_neg_info.polygon) &&
            all_positive(info.stable_neg_info.sines, center, radius, eval) &&
            all_positive(info.stable_neg_info.cosines, center, radius, eval);

        return stable_pos_works && unstable_works && stable_neg_works;
    }

    return false;

    // TODO the square must be contained within the corner polygon, which we haven't looked at yet
    // TODO also look at the corner polygon for the other ones as well

    if (!special_intersect(square, info.unstable_info.segment)) {
        std::cerr << "square does not intersect line segment" << std::endl;
        return false;
    }

    const auto inter = intersection(square, info.unstable_info.segment);
    const auto midpoint_rat = inter->midpoint();

    const geometry::Point<VarInterval> midpoint{VarInterval{midpoint_rat.x} * half_pi, VarInterval{midpoint_rat.y} * half_pi};

    // Stable Pos
    if (!all_equations_positive(info.stable_pos_info.sines, center, radius)) {
        std::cerr << "not all sines positive" << std::endl;
        return false;
    }

    if (!all_equations_positive(info.stable_pos_info.cosines, center, radius)) {
        std::cerr << "not all cosines positive" << std::endl;
        return false;
    }

    // Unstable
    if (!all_equations_positive(info.unstable_info.sines, midpoint, radius)) {
        std::cerr << "not all sines positive" << std::endl;
        return false;
    }

    if (!all_equations_positive(info.unstable_info.cosines, midpoint, radius)) {
        std::cerr << "not all cosines positive" << std::endl;
        return false;
    }

    // Stable Neg
    if (!all_equations_positive(info.stable_neg_info.sines, center, radius)) {
        std::cerr << "not all sines positive" << std::endl;
        return false;
    }

    if (!all_equations_positive(info.stable_neg_info.cosines, center, radius)) {
        std::cerr << "not all cosines positive" << std::endl;
        return false;
    }

    return true;
}

#endif

struct CountLeaves final : public boost::static_visitor<uint64_t> {

    uint64_t operator()(const cover::Empty) const {
        return 1;
    }

    uint64_t operator()(const cover::Single&) const {
        return 1;
    }

    uint64_t operator()(const cover::Triple&) const {
        return 1;
    }

    uint64_t operator()(const cover::Divide& divide) const {

        uint64_t sum = 0;
        for (const auto& quarter : divide.quarters) {
            sum += boost::apply_visitor(*this, quarter);
        }

        return sum;
    }
};

class CoverVerifier final : public boost::static_visitor<bool> {

  private:
    const ClosedRectangleQ& square;
    const ClosedConvexPolygonQ& polygon;
    const std::map<size_t, std::pair<CodePair, StableInfo>>& single_infos;
    const std::map<size_t, std::pair<TriplePair, TripleInfo>>& triple_infos;
    const uint32_t bits;
    Progress& progress;

  public:
    explicit CoverVerifier(const ClosedRectangleQ& square_, const ClosedConvexPolygonQ& polygon_,
                           const std::map<size_t, std::pair<CodePair, StableInfo>>& single_infos_,
                           const std::map<size_t, std::pair<TriplePair, TripleInfo>>& triple_infos_,
                           const uint32_t bits_,
                           Progress& progress_)
        : square{square_},
          polygon{polygon_},
          single_infos{single_infos_},
          triple_infos{triple_infos_},
          bits{bits_},
          progress{progress_} {}

    bool operator()(const cover::Empty) const {

        const auto inter = geometry::intersects(square, polygon);

        if (inter) {
            std::cout << "Failure: empty intersects polygon" << std::endl;
        }

        ++progress;

        return !inter;
    }

    bool operator()(const cover::Single& single) const {

        const auto stable_info = single_infos.at(single.index).second;

        const auto cover = covers_square(stable_info, square, bits);

        ++progress;

        return cover;
    }

    bool operator()(const cover::Triple&) const {

        ++progress;

        return true;
#if 0
        const auto triple_info = triple_infos.at(triple.index).second;

        if (covers_square(triple_info, square, bits)) {
            return true;
        } else {
            return false;
        }
#endif
    }

    bool operator()(const cover::Divide& divide) const {

        const auto quarter_squares = subdivide(square);
        const auto& quarter_covers = divide.quarters;

        // These values get overwritten anyway
        bool covered0 = false;
        bool covered1 = false;
        bool covered2 = false;
        bool covered3 = false;

        const auto l0 = [&] {
            const CoverVerifier verifier{quarter_squares.at(0), polygon, single_infos, triple_infos, bits, progress};
            covered0 = boost::apply_visitor(verifier, quarter_covers.at(0));
        };

        const auto l1 = [&] {
            const CoverVerifier verifier{quarter_squares.at(1), polygon, single_infos, triple_infos, bits, progress};
            covered1 = boost::apply_visitor(verifier, quarter_covers.at(1));
        };

        const auto l2 = [&] {
            const CoverVerifier verifier{quarter_squares.at(2), polygon, single_infos, triple_infos, bits, progress};
            covered2 = boost::apply_visitor(verifier, quarter_covers.at(2));
        };

        const auto l3 = [&] {
            const CoverVerifier verifier{quarter_squares.at(3), polygon, single_infos, triple_infos, bits, progress};
            covered3 = boost::apply_visitor(verifier, quarter_covers.at(3));
        };

        tbb::parallel_invoke(l0, l1, l2, l3);

        return covered0 && covered1 && covered2 && covered3;
    }
};

// Convert the givin amount of digits to a roughly equivalent amount of bits
static uint32_t digits_to_bits(const uint32_t digits) {
    // This is the function that boost uses, and so we'll keep it for now

    // log2(10) ~ 1000/301

    return (digits * 1000) / 301 + ((digits * 1000) % 301 ? 2 : 1);
}

bool verify_cover(const ClosedRectangleQ& square, const ClosedConvexPolygonQ& polygon,
                  const std::map<size_t, std::pair<CodePair, StableInfo>>& single_infos,
                  const std::map<size_t, std::pair<TriplePair, TripleInfo>>& triple_infos,
                  const cover::Cover& cover,
                  const uint32_t digits) {

    if (!geometry::subset(polygon, square)) {
        std::cout << "Error: the polygon is not a subset of the cover square" << std::endl;
        return false;
    }

    const auto bits = digits_to_bits(digits);

    const auto leaves = boost::apply_visitor(CountLeaves{}, cover);

    Progress progress{"Checking cover: ", leaves};

    const CoverVerifier verifier{square, polygon, single_infos, triple_infos, bits, progress};

    const auto covered = boost::apply_visitor(verifier, cover);

    return covered;
}
