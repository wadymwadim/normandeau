#include <iostream>

#include "equations.hpp"
#include "parse.hpp"
#include "wrapper.hpp"

static std::string serialize(const std::vector<PointQ>& points) {

    std::ostringstream oss{};

    bool first = true;
    for (const auto& point : points) {

        if (!first) {
            oss << '\n';
        }

        oss << point.x << ' ' << point.y;

        first = false;
    }

    return oss.str();
}

template <template <typename> class Trig>
void serialize(std::ostringstream& oss, const LinComMapZ<Trig<LinComArrZ<XY>>>& equation) {

    bool first = true;
    for (const auto& kv : equation) {
        // order is trig_coeff, x_coeff, y_coeff
        const auto trig_coeff = kv.second;
        const auto x_coeff = kv.first.arg.coeff(XY::X);
        const auto y_coeff = kv.first.arg.coeff(XY::Y);

        if (!first) {
            oss << ' ';
        }

        oss << trig_coeff << ' ' << x_coeff << ' ' << y_coeff;

        first = false;
    }
}

template <template <typename> class Trig>
std::string serialize(const std::set<LinComMapZ<Trig<LinComArrZ<XY>>>>& eqs) {

    std::ostringstream oss{};

    bool first = true;
    for (const auto& eq : eqs) {

        if (!first) {
            oss << '\n';
        }

        serialize(oss, eq);

        first = false;
    }

    return oss.str();
}

static char* to_cstr(const std::string& str) {

    // + 1 for the nul character
    char* const c_str = new char[str.size() + 1];
    strcpy(c_str, str.c_str());

    return c_str;
}

static void copy_to_c_code_info(const CodeInfo& code_info, CCodeInfo* const c_code_info) {

    c_code_info->points = to_cstr(serialize(code_info.points));
    c_code_info->sin_equations = to_cstr(serialize(code_info.sin_equations));
    c_code_info->cos_equations = to_cstr(serialize(code_info.cos_equations));
}

// 0 means failure
// 1 means success
int32_t load_code_info(const char* const code_sequence_ptr,
                       const char* const initial_angles_ptr,
                       CCodeInfo* const c_code_info) {

    try {
        const auto code_sequence = parse_code_sequence(code_sequence_ptr);
        const auto initial_angles = parse_initial_angles(initial_angles_ptr);

        const auto code_info = calculate_code_info(CodePair{code_sequence, initial_angles});

        copy_to_c_code_info(code_info, c_code_info);

        return 1;

    } catch (const std::exception& except) {
        std::cerr << except.what() << std::endl;

        return 0;
    }
}

void cleanup_code_info(const CCodeInfo* const c_code_info) {
    delete[] c_code_info->points;
    delete[] c_code_info->sin_equations;
    delete[] c_code_info->cos_equations;
}
