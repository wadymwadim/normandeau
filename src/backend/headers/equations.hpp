#pragma once

#include "cover.hpp"

template <template <typename> class Trig>
Coeff64 gradient_bound(const LinComMapZ<Trig<LinComArrZ<XY>>>& eq) {

    Coeff64 sum = 0;

    for (const auto& kv : eq) {

        const auto coeff = kv.second;

        const auto& arg = kv.first.arg;
        const auto x_coeff = arg.coeff(XY::X);
        const auto y_coeff = arg.coeff(XY::Y);

        sum += math::abs(coeff) * (math::abs(x_coeff) + math::abs(y_coeff));
    }

    return sum;
}

template <template <typename> class Trig>
EqVec<Trig> map_to_vec(const EqMap<Trig>& eq) {

    EqVec<Trig> vec{};
    for (const auto& kv : eq) {

        const auto x_coeff = kv.first.arg.coeff(XY::X);
        const auto y_coeff = kv.first.arg.coeff(XY::Y);

        const math::LinComArr<XY, Coeff16> arg{boost::numeric_cast<Coeff16>(x_coeff),
                                               boost::numeric_cast<Coeff16>(y_coeff)};
        const Trig<math::LinComArr<XY, Coeff16>> trig{arg};

        vec.coeffs.emplace_back(trig, boost::numeric_cast<Coeff16>(kv.second));
    }

    return vec;
}

struct CodeInfo final {

    std::vector<PointQ> points;
    std::set<EqMap<Sin>> sin_equations;
    std::set<EqMap<Cos>> cos_equations;

    // TODO move?
    explicit CodeInfo(const std::vector<PointQ>& points_,
                      const std::set<EqMap<Sin>>& sin_equations_,
                      const std::set<EqMap<Cos>>& cos_equations_)
        : points{points_},
          sin_equations{sin_equations_},
          cos_equations{cos_equations_} {}
};

struct StableInfo {

    OpenConvexPolygonQ polygon;
    std::vector<std::tuple<EqVec<Sin>, Coeff64, Coeff64>> sines;
    std::vector<std::tuple<EqVec<Cos>, Coeff64, Coeff64>> cosines;

    explicit StableInfo(const CodeInfo& code_info)
        : polygon{code_info.points} {

        for (const auto& equation : code_info.sin_equations) {
            const auto gbounds = gradient_bounds(equation);
            sines.emplace_back(map_to_vec(equation), gbounds.first, gbounds.second);
        }

        for (const auto& equation : code_info.cos_equations) {
            const auto gbounds = gradient_bounds(equation);
            cosines.emplace_back(map_to_vec(equation), gbounds.first, gbounds.second);
        }
    }
};

struct UnstableInfo {

    OpenSegmentQ segment;
    std::vector<std::tuple<EqVec<Sin>, Coeff64, Coeff64>> sines;
    std::vector<std::tuple<EqVec<Cos>, Coeff64, Coeff64>> cosines;

    explicit UnstableInfo(const CodeInfo& code_info)
        : segment{code_info.points.at(0), code_info.points.at(1)} {

        for (const auto& equation : code_info.sin_equations) {
            const auto gbounds = gradient_bounds(equation);
            sines.emplace_back(map_to_vec(equation), gbounds.first, gbounds.second);
        }

        for (const auto& equation : code_info.cos_equations) {
            const auto gbounds = gradient_bounds(equation);
            cosines.emplace_back(map_to_vec(equation), gbounds.first, gbounds.second);
        }
    }
};

struct TripleInfo {

    StableInfo stable_neg_info;
    UnstableInfo unstable_info;
    StableInfo stable_pos_info;

    explicit TripleInfo(const StableInfo& stable_neg_info_,
                        const UnstableInfo& unstable_info_,
                        const StableInfo& stable_pos_info_)
        : stable_neg_info{stable_neg_info_},
          unstable_info{unstable_info_},
          stable_pos_info{stable_pos_info_} {}
};

CodeInfo calculate_code_info(const CodePair& code_pair);

std::map<size_t, std::pair<CodePair, StableInfo>> load_single_infos(const std::vector<CodePair>& singles);
std::map<size_t, std::pair<TriplePair, TripleInfo>> load_triple_infos(const std::vector<TriplePair>& triples);
