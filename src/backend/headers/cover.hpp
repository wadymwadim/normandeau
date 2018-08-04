#pragma once

#include "code_sequence.hpp"
#include "general.hpp"

struct CodePair final {
    CodeSequence sequence;
    InitialAngles angles;

    explicit CodePair(const CodeSequence& code_sequence_, const InitialAngles& initial_angles_)
        : sequence{code_sequence_}, angles{initial_angles_} {}

    friend std::ostream& operator<<(std::ostream& os, const CodePair& code_pair) {
        return os << code_pair.sequence << ", " << code_pair.angles;
    }

    friend bool operator==(const CodePair& lhs, const CodePair& rhs) {
        return std::tie(lhs.sequence, lhs.angles) == std::tie(rhs.sequence, rhs.angles);
    }

    friend bool operator<(const CodePair& lhs, const CodePair& rhs) {
        return std::tie(lhs.sequence, lhs.angles) < std::tie(rhs.sequence, rhs.angles);
    }
};

struct TriplePair final {
    CodePair stable_neg;
    CodePair unstable;
    CodePair stable_pos;

    explicit TriplePair(const CodePair& stable_neg_, const CodePair& unstable_, const CodePair& stable_pos_)
        : stable_neg{stable_neg_}, unstable{unstable_}, stable_pos{stable_pos_} {}

    friend std::ostream& operator<<(std::ostream& os, const TriplePair& triple_pair) {
        return os << triple_pair.stable_neg << "; "
                  << triple_pair.unstable << "; "
                  << triple_pair.stable_pos;
    }

    friend bool operator==(const TriplePair& lhs, const TriplePair& rhs) {
        return std::tie(lhs.stable_neg, lhs.unstable, lhs.stable_pos) == std::tie(rhs.stable_neg, rhs.unstable, rhs.stable_pos);
    }

    friend bool operator<(const TriplePair& lhs, const TriplePair& rhs) {
        return std::tie(lhs.stable_neg, lhs.unstable, lhs.stable_pos) < std::tie(rhs.stable_neg, rhs.unstable, rhs.stable_pos);
    }
};

namespace cover {

struct Empty final {};

struct Single final {
    size_t index;

    explicit Single(const size_t index_)
        : index{index_} {}
};

struct Triple final {
    size_t index;

    explicit Triple(const size_t index_)
        : index{index_} {}
};

// We need to have a forward declaration here, since this is the recursive case
struct Divide;

using Cover = boost::variant<Empty, Single, Triple, Divide>;

struct Divide final {
    // TODO use std::unique_ptr<std::array<Cover, 4>>
    std::vector<Cover> quarters;

    explicit Divide(Cover&& cover0, Cover&& cover1, Cover&& cover2, Cover&& cover3)
        : quarters{cover0, cover1, cover2, cover3} {}
};
}

ClosedRectangleQ load_square(const std::string& dir);

ClosedConvexPolygonQ load_polygon(const std::string& dir);

std::vector<CodePair> load_singles(const std::string& dir);

std::vector<TriplePair> load_triples(const std::string& dir);

cover::Cover load_cover(const std::string& dir);

uint32_t load_digits(const std::string& dir);
